package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;


import play.Play;
import play.data.validation.Email;
import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Phone;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Blob;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Table(name = "customer")
@Entity
public class User extends Model {

	@MaxSize(5)
	public String cid;

	public Long vid_id;

	public String mac;

	public int os;

	public String type;

	@Required
	@Phone
	@Unique
	@Index(name = "idx_m_number")
	public String m_number;

	@Required
	@MaxSize(15)
	@MinSize(6)
	@Match(value = "^\\w*$", message = "Not a valid username")
	public String nickname;

	@Required
	@MaxSize(15)
	@MinSize(5)
	@Password
	public String psd;

	public String imei;

	public Long exp;
	
	public Long lv_id;

	public Long data = new Date().getTime();

	public Byte gender;
	
	public Blob portrait;
	
	public String serialNumber;
    
    // ~~~~~~~~~~~~ 
    
    public User(String m, String password, String name) {
        this.m_number = m;
        this.psd = password;//Codec.hexMD5(password);
        this.nickname = name;
    }
    
    public User(Long id, String m, String password, String name) {
    	this.id = id;
        this.m_number = m;
        this.psd = password;//Codec.hexMD5(password);
        this.nickname = name;
    }
    
    // ~~~~~~~~~~~~ 
    
    public boolean checkPassword(String password) {
        //return passwordHash.equals(Codec.hexMD5(password));
    	return psd.equals(password);
    }

    public boolean isAdmin() {
        return m_number.equals(Play.configuration.getProperty("forum.adminEmail", ""));
    }
    
// ~~~~~~~~~~~~ 
    
    public List<Post> getRecentsPosts() {
        return Post.find("postedBy = ? order by postedAt", this).fetch(1, 10);
    }

    public Long getPostsCount() {
        return Post.count("postedBy_id=?", this.id);
    }

    public Long getTopicsCount() {
        //return Post.count("select count(distinct t) from Topic t, Post p, User u where p.postedBy_id = ? and p.topic_id = t.id", this.id);
    	return Post.count("select count(distinct forum_topic.id) from forum_topic , forum_post where forum_post.postedBy_id = ? and forum_post.topic_id = forum_topic.id", this.id);
    }
    
    // ~~~~~~~~~~~~ 
    
    public static User findByEmail(String email) {
        return find("email", email).first();
    }

    public static User findByRegistrationUUID(String uuid) {
        return find("needConfirmation", uuid).first();
    }

    public static List<User> findAll(int page, int pageSize) {
        return User.all().fetch(page, pageSize);
    }

    public static boolean isEmailAvailable(String email) {
        return findByEmail(email) == null;
    }
    
}

