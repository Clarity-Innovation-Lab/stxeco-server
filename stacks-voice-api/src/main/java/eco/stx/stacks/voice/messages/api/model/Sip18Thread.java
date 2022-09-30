package eco.stx.stacks.voice.messages.api.model;

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
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias(value = "Sip18Thread")
@Document
public class Sip18Thread {
	@Id private String thread_id;
	private List<Sip18Reply> replies;
	private String author;
	private String subject;
	private String body;
	private Long timestamp;
	private String signature;
}
