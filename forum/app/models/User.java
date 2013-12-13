package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.Play;
import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
public class User extends Model {

    @Email
    @Required
    public String email;
    
    @Required
    public String passwordHash;
    
    @Required
    public String name;
    
    
    // ~~~~~~~~~~~~ 
    
    public User(String email, String password, String name) {
        this.email = email;
        this.passwordHash = password;//Codec.hexMD5(password);
        this.name = name;
    }
    
    public User(Long id, String email, String password, String name) {
    	this.id = id;
        this.email = email;
        this.passwordHash = password;//Codec.hexMD5(password);
        this.name = name;
    }
    
    // ~~~~~~~~~~~~ 
    
    public boolean checkPassword(String password) {
        //return passwordHash.equals(Codec.hexMD5(password));
    	return passwordHash.equals(password);
    }

    public boolean isAdmin() {
        return email.equals(Play.configuration.getProperty("forum.adminEmail", ""));
    }
    
    // ~~~~~~~~~~~~ 
    
    public List<Post> getRecentsPosts() {
        return Post.find("postedBy_id = ? order by postedAt", this.id).fetch(1, 10);
    }

    public Long getPostsCount() {
        return Post.count("postedBy_id=?", this.id);
    }

    public Long getTopicsCount() {
    	List l = JPA.em().createNativeQuery("select count(distinct 1) from customer u, forum_topic t, forum_post p where p.postedBy_id = u.id and p.topic_id = t.id").getResultList();
    	if(l.size() < 0)return 0L;
        return Long.parseLong(l.get(0).toString());
    }
    
    public static User findByMobile(String m) {
    	List l = JPA.em().createNativeQuery("select m_number, nickname, psd, id from customer where m_number='"+ m +"'").getResultList();
    	if(l.size()<1)return null;
    	Object[] u = (Object[])l.get(0);
        return setUser(u);
    }

    public static List<User> findAll(int page, int pageSize) {
    	List l = JPA.em().createNativeQuery("select m_number, nickname, psd, id from customer limit "+page+","+pageSize).getResultList();
    	List<User> ll = new ArrayList<User>();
    	for(Object u: l){
    		Object[] uu = (Object[])u;
    		ll.add(setUser(uu));
    	}
        return ll;
    }

    public static boolean isEmailAvailable(String email) {
        return findByMobile(email) == null;
    }
    public static User findById(Long id){
      	List l = JPA.em().createNativeQuery("select m_number, nickname, psd, id from customer where id="+ id).getResultList();
    	if(l.size()<1)return null;
    	Object[] u = (Object[])l.get(0);
        return setUser(u);
    }
    public static User setUser(Object[] u){
    	return new User(Long.parseLong(u[3].toString()), u[0].toString(),u[2].toString(),u[1].toString());
    }
    
}

