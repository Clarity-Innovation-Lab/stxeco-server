package eco.stx.edao.eco.daoProperties.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import eco.stx.edao.eco.daoProperties.service.DaoPropertyRepository;
import eco.stx.edao.eco.daoProperties.service.domain.DaoProperty;


@RestController
public class DaoPropertiesController {

	@Autowired private DaoPropertyRepository daoPropertyRepository;
	@Autowired private DaoPropertiesWatcher daoPropertiesWatcher;

	@GetMapping(value = "/v2/process-dao-properties")
	public void process() throws JsonProcessingException {
		daoPropertiesWatcher.process();
	}

	@GetMapping(value = "/v2/dao-properties")
	public List<DaoProperty> fetch() {
		List<DaoProperty> dps = daoPropertyRepository.findAll();
		return dps;
	}
}
