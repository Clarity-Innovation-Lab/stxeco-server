package eco.stx.edao.github.api.model;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias(value = "GitHubPullRequestUrls")
public class GitHubPullRequestUrls {

	protected String diffUrl;
	protected String url;
	protected String patchUrl;
	/**
	 * @return the diffUrl
	 */
	public String getDiffUrl() {
		return diffUrl;
	}
	/**
	 * @param diffUrl the diffUrl to set
	 */
	public void setDiffUrl(String diffUrl) {
		this.diffUrl = diffUrl;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the patchUrl
	 */
	public String getPatchUrl() {
		return patchUrl;
	}
	/**
	 * @param patchUrl the patchUrl to set
	 */
	public void setPatchUrl(String patchUrl) {
		this.patchUrl = patchUrl;
	}
}
