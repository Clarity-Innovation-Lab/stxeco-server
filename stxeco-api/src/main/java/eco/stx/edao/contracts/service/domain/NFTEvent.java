package eco.stx.edao.contracts.service.domain;

import java.util.Map;

import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@TypeAlias(value = "NFTEvent")
public class NFTEvent {

    @JsonProperty("asset_identifier") private String assetIdentifier;
    private Map<String, String> bnsName;
    private String recipient;
    private String sender;
    private HexValueBean value;
    private String tx_id;
	private Long block_height;
}
