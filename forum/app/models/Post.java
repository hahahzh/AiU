package models;

import javax.persistence.*;

import org.hibernate.annotations.ForeignKey;

import java.util.*;

import play.data.binding.*;

import play.db.jpa.*;

@Entity
@Table(name = "forum_post")
public class Post extends Model {

    public String content;
    
    @As("yyyy-MM-dd")
    public Date postedAt;
    
    @ForeignKey(name = "FK_postedBy")
    @JoinTable(name = "customer", inverseJoinColumns = @JoinColumn(name = "id"))
    public User postedBy;
    
    @ManyToOne
    public Topic topic;
    
    // ~~~~~~~~~~~~ 
    
    public Post(Topic topic, User postedBy, String content) {
        this.topic = topic;
        this.postedBy = postedBy;
        this.content = content;
        this.postedAt = new Date();
        create();
    }
    
}