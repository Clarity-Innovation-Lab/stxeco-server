package eco.stx.stacks.voice.messages.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eco.stx.stacks.voice.messages.api.model.Sip18Reply;
import eco.stx.stacks.voice.messages.api.model.Sip18Thread;
import eco.stx.stacks.voice.messages.service.Sip18ThreadRepository;


@RestController
public class Sip18ThreadController {
	
	@Autowired private Sip18ThreadRepository sip18ThreadRepository;

	@GetMapping(value = "/v1/threads")
	public List<Sip18Thread> threads() throws IOException {
		return sip18ThreadRepository.findAll();
	}
	
	@GetMapping(value = "/v1/threads/{thread_id}")
	public Optional<Sip18Thread> thread(@PathVariable String thread_id) throws IOException {
		return sip18ThreadRepository.findById(thread_id);
	}
	
	@GetMapping(value = "/v1/threads-by-author/{author}")
	public List<Sip18Thread> threadsByAuthor(@PathVariable String author) throws IOException {
		return sip18ThreadRepository.findByAuthor(author);
	}
	
	@GetMapping(value = "/v1/threads-by-replier/{author}")
	public List<Sip18Thread> threadsByReplyAuthor(@PathVariable String author) throws IOException {
		return sip18ThreadRepository.findByReplyAuthor(author);
	}
	
	@PostMapping(value = "/v1/thread")
	public Sip18Thread postNewThread(@RequestBody Sip18Thread thread) throws IOException {
		sip18ThreadRepository.insert(thread);
		return sip18ThreadRepository.findByThread_id(thread.getThread_id());
	}
	
	@PostMapping(value = "/v1/thread/{thread_id}")
	public Sip18Thread postReply(@PathVariable String thread_id, @RequestBody Sip18Reply reply) throws IOException {
		Sip18Thread thread = sip18ThreadRepository.findByThread_id(thread_id);
		thread.getReplies().add(reply);
		sip18ThreadRepository.save(thread);
		return thread;
	}
	
	@DeleteMapping(value = "/v1/thread/{thread_id}")
	public Sip18Thread deleteReply(@PathVariable String thread_id, @RequestBody Sip18Reply reply) throws IOException {
		Sip18Thread thread = sip18ThreadRepository.findByThread_id(thread_id);
		thread.getReplies().add(reply);
		sip18ThreadRepository.save(thread);
		return thread;
	}
	
}
