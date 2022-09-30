package eco.stx.stacks.voice.messages.api;

import org.springframework.data.annotation.TypeAlias;

import eco.stx.stacks.voice.messages.api.model.Sip18Reply;
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
@TypeAlias(value = "CryptoHelper")
public class CryptoHelper {

	public boolean verifyCanDeleteReply(Sip18Reply reply, String signature) {
		if (reply.getSignature() == null || reply.getAuthor() == null) return false;
		const author = typeof message.author === "string" ? hexToBytes(message.author) : message.author;
		const sig = typeof signature === "string" ? hexToBytes(signature) : signature;
		const is_reply = typeof (message as any).reply_id !== "undefined";
		return verify_structured_data_signature(domainCV, delete_action_tuple(is_reply ? (message as any).reply_id : message.thread_id, is_reply), author, sig);

	}
}
