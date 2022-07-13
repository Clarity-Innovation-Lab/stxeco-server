package eco.stx.edao.eco.extensions.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import eco.stx.edao.eco.extensions.service.ExtensionRepository;
import eco.stx.edao.eco.extensions.service.domain.Extension;


@RestController
public class ExtensionController {

	@Autowired private ExtensionRepository extensionRepository;
	@Autowired private ExtensionWatcher extensionWatcher;

	@GetMapping(value = "/v2/process-extensions")
	public void extensionRead() throws JsonProcessingException {
		extensionWatcher.processExtensions();
	}

	@GetMapping(value = "/v2/process-extension-data")
	public void extensionReadData() throws JsonProcessingException {
		extensionWatcher.processExtensionData();
	}

	@GetMapping(value = "/v2/process-extension-data/{contractId}")
	public void extensionReadData(@PathVariable String contractId) throws JsonProcessingException {
		extensionWatcher.processExtensionData(contractId);
	}

	@GetMapping(value = "/v2/extensions")
	public List<Extension> fetch() {
		return extensionRepository.findAll();
	}

	@PostMapping(value = "/v2/extensions")
	public Extension save(@RequestBody Extension extension) {
		if (extension.getId() == null) {
			return extensionRepository.insert(extension);
		}
		return extensionRepository.save(extension);
	}
}
