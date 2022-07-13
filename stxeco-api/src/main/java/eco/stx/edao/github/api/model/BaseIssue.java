package eco.stx.edao.github.api.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public abstract class BaseIssue {

	@Id private long id;
	protected String repository;
	protected GitHubUser assignee;
	protected GitHubUser[] assignees;
	protected String body;
	protected String closedAt;
	protected Long updatedAt;
	protected Long createdAt;
	protected GitHubUser closedBy;
	protected List<GitHubIssueComment> comments;
	protected String htmlUrl;
	protected String apiUrl;
	protected List<GitHubLabel>	labels;
	protected boolean locked;
	protected boolean pullRequest;
	//protected GHMilestone milestone;
	protected int number;
	protected GitHubPullRequestUrls pullRequestUrls;
	protected String state;
	protected String title;
	protected GitHubUser user;
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the repository
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}

	/**
	 * @return the assignee
	 */
	public GitHubUser getAssignee() {
		return assignee;
	}

	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(GitHubUser assignee) {
		this.assignee = assignee;
	}

	/**
	 * @return the assignees
	 */
	public GitHubUser[] getAssignees() {
		return assignees;
	}

	/**
	 * @param assignees the assignees to set
	 */
	public void setAssignees(GitHubUser[] assignees) {
		this.assignees = assignees;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the closedAt
	 */
	public String getClosedAt() {
		return closedAt;
	}

	/**
	 * @param closedAt the closedAt to set
	 */
	public void setClosedAt(String closedAt) {
		this.closedAt = closedAt;
	}

	/**
	 * @return the updatedAt
	 */
	public Long getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the createdAt
	 */
	public Long getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the closedBy
	 */
	public GitHubUser getClosedBy() {
		return closedBy;
	}

	/**
	 * @param closedBy the closedBy to set
	 */
	public void setClosedBy(GitHubUser closedBy) {
		this.closedBy = closedBy;
	}

	/**
	 * @return the comments
	 */
	public List<GitHubIssueComment> getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<GitHubIssueComment> comments) {
		this.comments = comments;
	}

	/**
	 * @return the htmlUrl
	 */
	public String getHtmlUrl() {
		return htmlUrl;
	}

	/**
	 * @param htmlUrl the htmlUrl to set
	 */
	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	/**
	 * @return the url
	 */
	public String getApiUrl() {
		return apiUrl;
	}

	/**
	 * @param url the url to set
	 */
	public void setApiUrl(String url) {
		this.apiUrl = url;
	}

	/**
	 * @return the labels
	 */
	public List<GitHubLabel> getLabels() {
		return labels;
	}

	/**
	 * @param labels the labels to set
	 */
	public void setLabels(List<GitHubLabel> labels) {
		this.labels = labels;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return the pullRequestUrls
	 */
	public GitHubPullRequestUrls getPullRequestUrls() {
		return pullRequestUrls;
	}

	/**
	 * @param pullRequestUrls the pullRequestUrls to set
	 */
	public void setPullRequestUrls(GitHubPullRequestUrls pullRequestUrls) {
		this.pullRequestUrls = pullRequestUrls;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the user
	 */
	public GitHubUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(GitHubUser user) {
		this.user = user;
	}

	/**
	 * @return the pullRequest
	 */
	public boolean isPullRequest() {
		return pullRequest;
	}

	/**
	 * @param pullRequest the pullRequest to set
	 */
	public void setPullRequest(boolean pullRequest) {
		this.pullRequest = pullRequest;
	}
}
