package eco.stx.edao.twoone.method2;

import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface TwoOneEventsService
{
	public void getTwoOneVotes(String principal, int until) throws JsonMappingException, JsonProcessingException;
	public void getLockedBalance(Pageable pageable, int until, String voteAddress) throws JsonMappingException, JsonProcessingException, InterruptedException;
	public Method2VoteCount votingInfo(String balanceKey);
	StxBalance fetchStacksBalance(String principal, long until);
}
