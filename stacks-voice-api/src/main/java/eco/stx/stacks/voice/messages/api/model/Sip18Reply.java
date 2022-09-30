package eco.stx.stacks.voice.messages.api.model;

import org.springframework.data.annotation.TypeAlias;

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
@TypeAlias(value = "Sip18Reply")
public class Sip18Reply {
	
	private String reply_id;
	private String thread_id;
	private String author;
	private String subject;
	private String body;
	private Long timestamp;
	private String signature;
}
