package eco.stx.edao.stacks.model.stxbuffer.types;

import org.springframework.data.annotation.TypeAlias;

import eco.stx.edao.stacks.service.domain.metadata.Sip016NFTMetaData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias(value = "CacheMetaDataResult")
public class CacheMetaDataResult {

	private String opcode;
	private Sip016NFTMetaData nFTMetaData;

}
