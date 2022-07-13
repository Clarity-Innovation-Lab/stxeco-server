package eco.stx.edao.stacks.service.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias(value = "AppMapContract")
@Document
public class AppMapContract {

	@Id private String id;
	private List<Application> applications;
	private String administrator;
	private String adminContractAddress;
	private String adminContractName;
	private long appCounter;
	
	public void addApplication(Application application) {
		if (this.applications == null) {
			this.applications = new ArrayList<>();
		}
		applications.add(application);
	}
	
	public List<Application> getApplications() {
		return this.applications;
	}
}
