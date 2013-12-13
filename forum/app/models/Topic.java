package models;

import javax.persistence.*;

import java.util.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
@Table(name = "forum_topic")
public class Topic extends Model {

    @Required
    public String subject;
    
    public Integer views = 0;
    
    @ManyToOne
    public Forum forum;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "topic")
    public List<Post> posts;
    
    // ~~~~~~~~~~~~ 
    
    public Topic(Forum forum, Long by, String subject, String content) {
        this.forum = forum;
        this.subject = subject;
        create();
        new Post(this, by, content);
    }
    
    // ~~~~~~~~~~~~ 
    public Post reply(Long by, String content) {
        return new Post(this, by, content);
    }
    
    // ~~~~~~~~~~~~ 
    
    public List<Post> getPosts(int page, int pageSize) {
        return Post.find("topic", this).fetch(page, pageSize);
    }

    public Long getPostsCount() {
        return Post.count("topic", this);
    }

    public Long getVoicesCount() {
    	List l = JPA.em().createNativeQuery("select count(distinct 1) from customer u, forum_topic t, forum_post p where p.postedBy_id = u.id and p.topic_id = t.id").getResultList();
    	if(l.size() < 0)return 0L;
        return Long.parseLong(l.get(0).toString());
    }

    public Post getLastPost() {
        return Post.find("topic = ? order by postedAt desc", this).first();
    }
    
}