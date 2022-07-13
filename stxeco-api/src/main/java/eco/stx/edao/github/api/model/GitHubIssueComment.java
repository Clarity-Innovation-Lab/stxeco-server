package eco.stx.edao.github.api.model;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias(value = "GitHubIssueComment")
public class GitHubIssueComment {

    private Long createdAt;
    private String body;
    private String gravatar_id;
    private String htmlUrl;
    private String authorAssociation;
    private GitHubUser user;
    
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
	 * @return the gravatar_id
	 */
	public String getGravatar_id() {
		return gravatar_id;
	}
	/**
	 * @param gravatar_id the gravatar_id to set
	 */
	public void setGravatar_id(String gravatar_id) {
		this.gravatar_id = gravatar_id;
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
	 * @return the authorAssociation
	 */
	public String getAuthorAssociation() {
		return authorAssociation;
	}
	/**
	 * @param authorAssociation the authorAssociation to set
	 */
	public void setAuthorAssociation(String authorAssociation) {
		this.authorAssociation = authorAssociation;
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
}
