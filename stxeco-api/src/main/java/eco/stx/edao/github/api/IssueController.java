package eco.stx.edao.github.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import eco.stx.edao.github.api.model.GitHubIssue;
import eco.stx.edao.github.service.JsonIssueRepository;


@RestController
public class IssueController {
	
	@Autowired private JsonIssueRepository jsonIssueRepository;
	@Autowired private IssueWatcher issueWatcher;

	@GetMapping(value = "/v2/gh-refresh")
	public boolean processProposals() throws IOException {
		issueWatcher.processProposals();
		return true;
	}
	
	@GetMapping(value = "/v2/gh-spis-readme")
	public ResponseEntity sipsWiki(HttpServletRequest request) throws IOException {
		//return issueWatcher.repo.getReadme().getDownloadUrl();
		String pathname = request.getParameter("pathname");
		GHRepository repo = issueWatcher.getGHRepository();
		InputStreamResource inputStreamResource = null;
		if (pathname == null || pathname.equals("readme")) {
			inputStreamResource = new InputStreamResource(repo.getReadme().read());
		} else {
			GHContent content = repo.getFileContent(pathname);
			// InputStream input = new URL("http://www.somewebsite.com/a.txt").openStream();
			inputStreamResource = new InputStreamResource(content.read());
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentLength(repo.getReadme().getSize());
		return new ResponseEntity(inputStreamResource, headers, HttpStatus.OK);
	}
	
	@GetMapping(value = "/v2/gh-issues")
	public List<GitHubIssue> issues() throws IOException {
		return jsonIssueRepository.findAll();
	}
	
	@GetMapping(value = "/v2/gh-pull-requests")
	public List<GitHubIssue> pulls() throws IOException {
		return jsonIssueRepository.findAll();
	}
	
}
