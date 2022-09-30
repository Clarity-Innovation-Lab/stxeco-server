package eco.stx.edao.eco.proposals.api;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eco.stx.edao.eco.api.model.Contract;
import eco.stx.edao.eco.api.model.ContractSource;
import eco.stx.edao.eco.proposals.api.model.ProposalContracts;
import eco.stx.edao.eco.proposals.api.model.ProposalTrait;
import eco.stx.edao.eco.proposals.service.ProposalRepository;
import eco.stx.edao.eco.proposals.service.ProposerRepository;
import eco.stx.edao.eco.proposals.service.domain.Proposal;
import eco.stx.edao.eco.proposals.service.domain.ProposalData;
import eco.stx.edao.eco.proposals.service.domain.Proposer;
import eco.stx.edao.eco.proposals.service.domain.Status;
import eco.stx.edao.eco.proposals.service.domain.clarity.CTypeValue;
import eco.stx.edao.eco.proposals.service.domain.clarity.TypeValue;
import eco.stx.edao.stacks.ApiFetchConfig;
import eco.stx.edao.stacks.ApiHelper;
import eco.stx.edao.stacks.PostData;
import eco.stx.edao.stacks.ReadResult;
import eco.stx.edao.stacks.model.transactions.TransactionFromApiBean;

@Configuration
@EnableScheduling
public class ProposalWatcher {

	private static final Logger logger = LogManager.getLogger(ProposalWatcher.class);
	@Autowired
	private ApiHelper apiHelper;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private ProposerRepository proposerRepository;
	@Autowired
	private ProposalRepository proposalRepository;
	@Value("${stacks.dao.deployer}")
	String deployer;
	private static String snapshotVoting = "ede007-snapshot-proposal-voting-v2";
	private static String proposalVoting = "ede001-proposal-voting";
	private static String fundedSubmission = "ede008-funded-proposal-submission-v2";
	private static String emergencyExecute = "ede004-emergency-execute";

	// @Scheduled(fixedDelay=3600000)
	public void processProposalsByTrait() throws JsonProcessingException {
		ApiFetchConfig path = new ApiFetchConfig("GET", "/extended/v1/contract/by_trait?trait_abi={trait}", null);
		String json = apiHelper.fetchFromApi(path, ProposalTrait.trait);
		ProposalContracts contracts = (ProposalContracts) mapper.readValue(json,
				new TypeReference<ProposalContracts>() {
				});
		for (Contract pc : contracts.getResults()) {
			if (allowed(pc.getContractId())) {
				Proposal p = merge(pc);
				proposalRepository.save(p);
			}
		}
	}

	@Scheduled(fixedDelay = 180000) // every 3 mins
	public void processProposalsFromDB() throws JsonProcessingException {
		List<Proposal> proposals = proposalRepository.findAll();
		for (Proposal proposal : proposals) {
			processProposalFromDB(proposal);
		}
	}

	@Scheduled(fixedDelay = 360000) // every 6 mins
	public void processVoteCounts() throws JsonProcessingException {
		List<Proposal> proposals = proposalRepository.findAll();
		for (Proposal proposal : proposals) {
			processProposalFromDB(proposal);
		}
	}

	public void processProposal(String contractId) throws JsonProcessingException {
		Proposal proposal = proposalRepository.findByContractId(contractId);
		processProposalFromDB(proposal);
	}

	private void processProposalFromDB(Proposal proposal) throws JsonProcessingException {
		ApiFetchConfig path = null;
		try {
			if (proposal.getContract() != null) {
				String txStatus = proposal.getContract().getTxStatus();
				if (txStatus != null && (txStatus.equals("success") || txStatus.equals("failed"))) {
					return;
				}
			}
			String contractAddress = proposal.getContractId().split("\\.")[0];
			String contractName = proposal.getContractId().split("\\.")[1];
			path = new ApiFetchConfig("GET",
					"/v2/contracts/source/" + contractAddress + "/" + contractName + "?proof=0", null);
			String json = apiHelper.fetchFromApi(path);
			logger.info("Found json at path1: " + path.getPath() + "\n---\n" + json);
			ContractSource contract = (ContractSource) mapper.readValue(json, new TypeReference<ContractSource>() {
			});
			Contract c = new Contract();
			c.setBlockHeight(contract.getPublish_height());
			c.setContractId(proposal.getContractId());
			c.setSourceCode(contract.getSource());
			c.setTxId(proposal.getDeployTxId());
			c.setTxStatus("success");
			proposal.setContract(c);
			proposalRepository.save(proposal);
		} catch (Exception e) {
			try {
				logger.error("Error reading contract from path=" + path.getPath(), e);
				TransactionFromApiBean tx = apiHelper.fetchTransaction(proposal.getDeployTxId());
				Contract contract = proposal.getContract();
				if (contract == null)
					contract = new Contract();
				if (tx.getTx_status().equals("pending")) {
					contract.setTxStatus("pending");
				} else if (tx.getTx_status().equals("success")) {
					contract.setTxStatus("success");
				} else {
					contract.setTxStatus("failed");
				}
				proposal.setContract(contract);
				proposalRepository.save(proposal);
			} catch (Exception e1) {
				logger.error("Unable to read contract from path=" + path.getPath(), e1);
			}
		}
	}

	@Scheduled(fixedDelay = 120000)
	public void processProposalData() throws JsonProcessingException {
		List<Proposal> props = proposalRepository.findAll();
		for (Proposal p : props) {
			try {
				if (p.getContract().getTxStatus().equals("success")) {
					checkProposalStatus(p);
				}
			} catch (Exception e) {
				logger.error("Error fetching proposal data: " + e.getMessage());
			}
		}
	}

	@Scheduled(fixedDelay = 3600000) // hourly
	public void processSubmissions() throws JsonProcessingException {
		List<Proposal> proposals = proposalRepository.findByStatus("deploying");
		checkTransactionStatus(proposals, "deployed");
		proposals = proposalRepository.findByStatus("submitting");
		checkTransactionStatus(proposals, "submitted");
	}

	private boolean allowed(String contractId) {
		boolean allowed = true;
		String contractAddress = contractId.split("\\.")[0];
		String contractName = contractId.split("\\.")[1];
		if (contractName.equals("executor-dao")) {
			allowed = false;
		}
		Proposer p = proposerRepository.findByStxAddress(contractAddress);
		if (p == null) {
			allowed = false;
		}
		return allowed;
	}

	private void checkTransactionStatus(List<Proposal> proposals, String newStatus) throws JsonProcessingException {
		if (proposals == null || proposals.isEmpty())
			return;
		for (Proposal proposal : proposals) {
			String txId = proposal.getDeployTxId();
			if (newStatus.equals("submitted"))
				txId = proposal.getSubmitTxId();
			if (txId == null) {
				proposal.setStatus(new Status("draft", null, null));
			} else {
				try {
					TransactionFromApiBean tx = apiHelper.fetchTransaction(txId);
					if (tx.getTx_status().equals("pending")) {
						// do nothing
					} else if (tx.getTx_status().equals("success")) {
						proposal.setStatus(new Status(newStatus, null, null));
					} else {
						proposal.setStatus(new Status("draft", null, null));
					}
				} catch (Exception e) {
					// devnet txs come and go - shouldnt happen on mainnet
					proposal.setStatus(new Status("draft", null, null));
				}
			}
			proposalRepository.save(proposal);
		}
	}

	public void processProposalData(String contractId) throws JsonProcessingException {
		Proposal p = proposalRepository.findByContractId(contractId);
		try {
			checkProposalStatus(p);
		} catch (Exception e) {
			logger.error("Error fetching proposal data: ", e);
		}
	}

	private Proposal merge(Contract pc) {
		Proposal prop = proposalRepository.findByContractId(pc.getContractId());
		if (prop == null) {
			prop = new Proposal();
			prop.setContractId(pc.getContractId());
			prop.setTitle(pc.getContractId().split("\\.")[1]);
			prop.setProposer(pc.getContractId().split("\\.")[0]);
			prop.setStatus(new Status("deployed", null, null));
		}
		if (prop.getStatus().equals(new Status("draft", null, null)))
			prop.setStatus(new Status("deployed", null, null));
		prop.setContract(pc);
		return prop;
	}

	/**
	 * Checks for funded and threshold proposal submission and also for emergency
	 * execution
	 * 
	 * @param p
	 * @throws JsonProcessingException
	 */
	@Async
	private void checkProposalStatus(Proposal p) throws JsonProcessingException {
		try {
			if (!p.getContract().getTxStatus().equals("success"))
				return;
			ProposalData pd = null;
			Long funding = checkProposalFunding(p);
			if (funding != null) {
				// if (funding != null && funding > 0) {
				p.setFunding(funding.intValue() / 1000000);
				pd = checkProposalSubmission(snapshotVoting, p);
				saveProposal(snapshotVoting, p, pd);
			} else {
				p.setFunding(-1);
				pd = checkProposalSubmission(proposalVoting, p);
				if (pd != null)
					saveProposal(proposalVoting, p, pd);
			}
			Long signals = checkEmergencyExecute(p);
			if (signals != null && signals > 0) {
				p.setEmergencySignals(signals.intValue());
				saveProposal(emergencyExecute, p, pd);
			}
		} catch (Exception e) {
			logger.error("checkProposalStatus: ", e);
			throw e;
		}
	}

	private void saveProposal(String votingContract, Proposal p, ProposalData pd) throws JsonProcessingException {
		p.setVotingContract(votingContract);
		p.setProposalData(pd);
		checkProposalExecution(p);
		proposalRepository.save(p);
	}

	private ProposalData checkProposalSubmission(String votingContract, Proposal p) throws JsonProcessingException {
		try {
			if (!p.getContract().getTxStatus().equals("success"))
				return null;
			String contractAddress = deployer;
			String contractName = votingContract;
			String functionName = "get-proposal-data";
			String param = "/contract-principal/" + p.getContractId().split("\\.")[0] + "/"
					+ p.getContractId().split("\\.")[1];
			String arg0 = apiHelper.cvConversion(param);
			logger.info("Extension argument: " + arg0);

			PostData postd = new PostData();
			postd.setArguments(new String[] { arg0 });
			postd.setSender(contractAddress);
			String path = "/v2/contracts/call-read/" + contractAddress + "/" + contractName + "/" + functionName;
			ApiFetchConfig principal = new ApiFetchConfig("POST", path, postd);
			String json = apiHelper.fetchFromApi(principal);
			if (json.indexOf(":false") > -1)
				return null;
			ProposalData pd = deserialise(functionName, contractAddress, json);
			return pd;
		} catch (Exception e) {
			logger.error("checkProposalSubmission: ", e);
			return null;
		}
	}

	private Long checkProposalFunding(Proposal p) throws JsonProcessingException {
		try {
			if (!p.getContract().getTxStatus().equals("success"))
				return null;
			String contractAddress = deployer;
			String contractName = fundedSubmission;
			String functionName = "get-proposal-funding";
			String param = "/contract-principal/" + p.getContractId().split("\\.")[0] + "/"
					+ p.getContractId().split("\\.")[1];
			String arg0 = apiHelper.cvConversion(param);
			logger.info("checkProposalFunding: argument: " + arg0);

			PostData postd = new PostData();
			postd.setArguments(new String[] { arg0 });
			postd.setSender(contractAddress);
			String path = "/v2/contracts/call-read/" + contractAddress + "/" + contractName + "/" + functionName;
			ApiFetchConfig principal = new ApiFetchConfig("POST", path, postd);
			String json = apiHelper.fetchFromApi(principal);
			Long funding = deserialiseExecution(functionName, contractAddress, json);
			return funding;
		} catch (Exception e) {
			logger.error("checkProposalFunding: ", e);
			return null;
		}
	}

	private Long checkEmergencyExecute(Proposal p) throws JsonProcessingException {
		try {
			if (!p.getContract().getTxStatus().equals("success"))
				return null;
			String contractAddress = deployer;
			String contractName = emergencyExecute;
			String functionName = "get-signals";
			String param = "/contract-principal/" + p.getContractId().split("\\.")[0] + "/"
					+ p.getContractId().split("\\.")[1];
			String arg0 = apiHelper.cvConversion(param);
			logger.info("Extension argument: " + arg0);

			PostData postd = new PostData();
			postd.setArguments(new String[] { arg0 });
			postd.setSender(contractAddress);
			String path = "/v2/contracts/call-read/" + contractAddress + "/" + contractName + "/" + functionName;
			ApiFetchConfig principal = new ApiFetchConfig("POST", path, postd);
			String json = apiHelper.fetchFromApi(principal);
			Long signals = deserialiseExecution(functionName, contractAddress, json);
			return signals;
		} catch (Exception e) {
			logger.error("checkEmergencyExecute: ", e);
			return 0L;
		}
	}

	private void checkProposalExecution(Proposal p) throws JsonProcessingException {
		try {
			if (!p.getContract().getTxStatus().equals("success"))
				return;
			String contractAddress = p.getContractId().split("\\.")[0];
			String contractName = "executor-dao"; // p.getContractId().split("\\.")[1];
			String functionName = "executed-at";
			String param = "/contract-principal/" + contractAddress + "/" + p.getContractId().split("\\.")[1];
			String arg0 = apiHelper.cvConversion(param);
			logger.info("Extension argument: " + arg0);

			PostData postd = new PostData();
			postd.setArguments(new String[] { arg0 });
			postd.setSender(contractAddress);
			String path = "/v2/contracts/call-read/" + contractAddress + "/" + contractName + "/" + functionName;
			ApiFetchConfig principal = new ApiFetchConfig("POST", path, postd);
			String json = apiHelper.fetchFromApi(principal);
			Long blockHeight = deserialiseExecution(functionName, contractAddress, json);
			if (blockHeight != null)
				p.setExecutedAt(blockHeight.intValue());
			proposalRepository.save(p);
		} catch (Exception e) {
			logger.error("checkProposalExecution: ", e);
			return;
		}
	}

	private ProposalData deserialise(String functionName, String contractId, String json)
			throws JsonMappingException, JsonProcessingException {
		try {
			ReadResult contractRead = (ReadResult) mapper.readValue(json, new TypeReference<ReadResult>() {
			});
			if (contractRead.getResult() == null)
				return null;
			String param = "/to-json/" + contractRead.getResult();
			json = apiHelper.cvConversion(param);
			TypeValue typeValue = (TypeValue) mapper.readValue(json, new TypeReference<TypeValue>() {
			});
			if (typeValue.getValue() == null)
				return null;
			return ProposalData.fromClarity(typeValue.getValue().getValue());
		} catch (Exception e) {
			logger.error("deserialise: ", e);
			return null;
		}
	}

	private Long deserialiseExecution(String functionName, String contractId, String json)
			throws JsonMappingException, JsonProcessingException {
		try {
			ReadResult contractRead = (ReadResult) mapper.readValue(json, new TypeReference<ReadResult>() {
			});
			if (contractRead.getResult() == null)
				return null;
			String param = "/to-json/" + contractRead.getResult();
			json = apiHelper.cvConversion(param);
			CTypeValue typeValue = (CTypeValue) mapper.readValue(json, new TypeReference<CTypeValue>() {
			});
			if (functionName.equals("get-proposal-funding") || functionName.equals("get-signals")) {
				return Long.parseLong(String.valueOf(typeValue.getValue()));
			}
			LinkedHashMap<String, Object> val = (LinkedHashMap) typeValue.getValue();
			// typeValue = (CTypeValue)mapper.readValue((String)typeValue.getValue(), new
			// TypeReference<CTypeValue>() {});
			// if (json.indexOf("some") > -1 || json.indexOf("true") > -1) return true;
			if (val == null)
				return null;
			Object o = val.get("value");
			return Long.parseLong(String.valueOf(o));
		} catch (Exception e) {
			logger.error("deserialiseExecution: ", e);
			return null;
		}
	}

}
