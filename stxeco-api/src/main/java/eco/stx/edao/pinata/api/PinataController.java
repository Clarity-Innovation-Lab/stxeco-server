package eco.stx.edao.pinata.api;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eco.stx.edao.pinata.api.model.PinataFile;
import eco.stx.edao.pinata.service.PinataHelper;
import pinata.Pinata;
import pinata.PinataException;
import pinata.PinataResponse;


@RestController
public class PinataController {
	
    private static final Logger logger = LogManager.getLogger(PinataController.class);
	@Autowired private PinataHelper pinataHelper;

	@PostMapping(value = "/v2/ipfs/pin-file")
	public String processProposals(@RequestBody PinataFile pinataFile) throws IOException {
		PinataResponse pr = null;
		Pinata p = pinataHelper.getPinata();
		try {
			pr = p.pinFileToIpfs(pinataFile.getFile(), pinataFile.getMetaData());
		} catch (PinataException | IOException e) {
			logger.error("Pinning error", e);
		}
		return pr.getBody();
	}
}
