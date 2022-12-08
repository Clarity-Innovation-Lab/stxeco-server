package eco.stx.edao.twoone.method2;

import java.util.List;
import java.util.Map;

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
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias(value = "Method2VoteCount")
public class Method2VoteCount {

	private long countFor;
	private long countForIncludingDuplicates;
	private long countAgainst;
	private long countAgainstIncludingDuplicates;
	private int numbTransactions;
	private int numbVotesFor;
	private int numbVotesAgainst;
	private int numbUniqueVotesIncludingDuplicates;
	private long lockedAtHeight;
	private long earlyVotes;
	private Map<String, List<TwoOneEvent>> votes;

}
