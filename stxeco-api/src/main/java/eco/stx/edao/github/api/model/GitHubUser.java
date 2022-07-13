package eco.stx.edao.github.api.model;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias(value = "GitHubUser")
public class GitHubUser {

	protected String avatar_url;
	protected String bio;
	protected String blog;
	protected String company;
	protected String email;
	protected int followers;
	protected int following;
	protected boolean hireable;
	protected String htmlUrl;
	protected String location;
	protected String login;
	protected String name;
	protected int public_gists;
	protected int public_repos;
	protected boolean site_admin;
	protected Integer total_private_repos;
	protected String twitter_username;
	protected String type;
	/**
	 * @return the avatar_url
	 */
	public String getAvatar_url() {
		return avatar_url;
	}
	/**
	 * @param avatar_url the avatar_url to set
	 */
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	/**
	 * @return the bio
	 */
	public String getBio() {
		return bio;
	}
	/**
	 * @param bio the bio to set
	 */
	public void setBio(String bio) {
		this.bio = bio;
	}
	/**
	 * @return the blog
	 */
	public String getBlog() {
		return blog;
	}
	/**
	 * @param blog the blog to set
	 */
	public void setBlog(String blog) {
		this.blog = blog;
	}
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the followers
	 */
	public int getFollowers() {
		return followers;
	}
	/**
	 * @param followers the followers to set
	 */
	public void setFollowers(int followers) {
		this.followers = followers;
	}
	/**
	 * @return the following
	 */
	public int getFollowing() {
		return following;
	}
	/**
	 * @param following the following to set
	 */
	public void setFollowing(int following) {
		this.following = following;
	}
	/**
	 * @return the hireable
	 */
	public boolean isHireable() {
		return hireable;
	}
	/**
	 * @param hireable the hireable to set
	 */
	public void setHireable(boolean hireable) {
		this.hireable = hireable;
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
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the public_gists
	 */
	public int getPublic_gists() {
		return public_gists;
	}
	/**
	 * @param public_gists the public_gists to set
	 */
	public void setPublic_gists(int public_gists) {
		this.public_gists = public_gists;
	}
	/**
	 * @return the public_repos
	 */
	public int getPublic_repos() {
		return public_repos;
	}
	/**
	 * @param public_repos the public_repos to set
	 */
	public void setPublic_repos(int public_repos) {
		this.public_repos = public_repos;
	}
	/**
	 * @return the site_admin
	 */
	public boolean isSite_admin() {
		return site_admin;
	}
	/**
	 * @param site_admin the site_admin to set
	 */
	public void setSite_admin(boolean site_admin) {
		this.site_admin = site_admin;
	}
	/**
	 * @return the total_private_repos
	 */
	public Integer getTotal_private_repos() {
		return total_private_repos;
	}
	/**
	 * @param total_private_repos the total_private_repos to set
	 */
	public void setTotal_private_repos(Integer total_private_repos) {
		this.total_private_repos = total_private_repos;
	}
	/**
	 * @return the twitter_username
	 */
	public String getTwitter_username() {
		return twitter_username;
	}
	/**
	 * @param twitter_username the twitter_username to set
	 */
	public void setTwitter_username(String twitter_username) {
		this.twitter_username = twitter_username;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
