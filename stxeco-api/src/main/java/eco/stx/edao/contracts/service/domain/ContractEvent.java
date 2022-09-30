package eco.stx.edao.contracts.service.domain;

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
@TypeAlias(value = "ContractEvent")
@Document
public class ContractEvent {

	@Id private String id;
    private Integer event_index;
    private String event_type;
    private ContractLog contract_log;
    private VoteEvent voteEvent;
}
