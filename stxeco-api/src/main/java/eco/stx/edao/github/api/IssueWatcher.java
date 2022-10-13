package eco.stx.edao.github.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import eco.stx.edao.github.api.model.BaseIssue;
import eco.stx.edao.github.api.model.GitHubIssue;
import eco.stx.edao.github.api.model.GitHubIssueComment;
import eco.stx.edao.github.api.model.GitHubLabel;
import eco.stx.edao.github.api.model.GitHubPullRequestUrls;
import eco.stx.edao.github.api.model.GitHubUser;
import eco.stx.edao.github.service.GitHubHelper;
import eco.stx.edao.github.service.JsonIssueRepository;

@Configuration
@EnableScheduling
public class IssueWatcher {

    private static final Logger logger = LogManager.getLogger(IssueWatcher.class);
	@Autowired private GitHubHelper gitHubHelper;
	private GHRepository gHRepository;
	@Autowired private JsonIssueRepository jsonIssueRepository;
	private static final String REPO = "stacksgov/sips";

	@Scheduled(fixedDelay=3600000)
	public void processProposals() throws IOException {
		try {
			GitHub gitHub = gitHubHelper.setup();
			this.gHRepository = gitHub.getRepository(REPO);
			filterIssues(gHRepository.getIssues(GHIssueState.OPEN));
		} catch (Exception e) {
			logger.error("Unble to connect to github", e);
		}
		// filterPulls(repo.getPullRequests(GHIssueState.OPEN));
	}
	
	public GHRepository getGHRepository() {
		return this.gHRepository;
	}
	
//	private void filterPulls(List<GHPullRequest> issues) throws BeansException, IOException {
//		GitHubPullRequest jsonIssue = new GitHubPullRequest();
//		for (GHIssue issue : issues) {
//			jsonIssue = (GitHubPullRequest)convertJsonIssue(jsonIssue, issue);
//			jsonPullRequestRepository.save(jsonIssue);
//		}
//	}

	private void filterIssues(List<GHIssue> issues) throws BeansException, IOException {
		GitHubIssue jsonIssue = new GitHubIssue();
		for (GHIssue issue : issues) {
			jsonIssue = (GitHubIssue)convertJsonIssue(jsonIssue, issue);
			jsonIssueRepository.save(jsonIssue);
		}
	}
	
	private BaseIssue convertJsonIssue(BaseIssue jsonIssue, GHIssue issue) throws BeansException, IOException {
		jsonIssue.setRepository(REPO);
		BeanUtils.copyProperties(issue, jsonIssue);
		jsonIssue.setId(issue.getId());
		jsonIssue.setPullRequest(issue.isPullRequest());
		if (issue.getPullRequest() != null) {
			GitHubPullRequestUrls ghpr = new GitHubPullRequestUrls();
			BeanUtils.copyProperties(issue.getPullRequest(), ghpr);
			ghpr.setDiffUrl(issue.getPullRequest().getDiffUrl().toString());
			ghpr.setUrl(issue.getPullRequest().getUrl().toString());
			ghpr.setPatchUrl(issue.getPullRequest().getPatchUrl().toString());
			jsonIssue.setPullRequestUrls(ghpr);
		}
		if (issue.getComments() != null) {
			List<GitHubIssueComment> comments = new ArrayList<GitHubIssueComment>();
			for (GHIssueComment gic : issue.getComments()) {
				GitHubIssueComment obj = new GitHubIssueComment();
				BeanUtils.copyProperties(gic, obj);
				obj.setUser(convertGHUser(gic.getUser()));
				obj.setHtmlUrl(gic.getHtmlUrl().toString());
				obj.setAuthorAssociation(gic.getAuthorAssociation().name());;
				obj.setCreatedAt(gic.getCreatedAt().getTime());;
				comments.add(obj);
			}
			jsonIssue.setComments(comments);
		}
		if (issue.getLabels() != null && issue.getLabels().size() > 0) {
			List<GitHubLabel> labels = new ArrayList<GitHubLabel>();
			for (GHLabel label : issue.getLabels()) {
				GitHubLabel obj = new GitHubLabel();
				BeanUtils.copyProperties(label, obj);
				labels.add(obj);
			}
			jsonIssue.setLabels(labels);
		}
		jsonIssue.setClosedBy(convertGHUser(issue.getClosedBy()));
		jsonIssue.setAssignee(convertGHUser(issue.getAssignee()));
		jsonIssue.setUser(convertGHUser(issue.getUser()));
		jsonIssue.setState(issue.getState().name());
		jsonIssue.setHtmlUrl(issue.getHtmlUrl().toString());
		jsonIssue.setApiUrl(issue.getUrl().toString());
		jsonIssue.setUpdatedAt(issue.getUpdatedAt().getTime());
		jsonIssue.setCreatedAt(issue.getCreatedAt().getTime());
		return jsonIssue;
	}
	
	private GitHubUser convertGHUser(GHUser user) {
		if (user == null) return null;
		GitHubUser obj = new GitHubUser();
		BeanUtils.copyProperties(user, obj);
		return obj;
	}
	

}
