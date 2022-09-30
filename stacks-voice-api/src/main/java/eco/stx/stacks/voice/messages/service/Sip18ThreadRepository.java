package eco.stx.stacks.voice.messages.service;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import eco.stx.stacks.voice.messages.api.model.Sip18Thread;

@Repository
public interface Sip18ThreadRepository extends MongoRepository<Sip18Thread, String> {

    public Sip18Thread findByThread_id(String thread_id);
    
    public List<Sip18Thread> findByAuthor(String author);
    
    @Query(value = "{ 'replies.reply_id' : ?#{[0]} }")
    public Sip18Thread findByReply_id(String reply_id);

    @Query(value = "{ 'replies.author' : ?#{[0]} }")
    public List<Sip18Thread> findByReplyAuthor(String reply_id);

}
