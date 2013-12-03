package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Blob;

@Table(name="games")
@Entity
public class Game extends ThirdModel {
	public Blob icon;
	@Required
	public Integer type; // 下载类型
	
	@Index(name = "idx_games_star")
	public Integer star; // 热度
	
	@URL
	public String bbsurl;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.ALL)
	public GameType gtype;
	
	public String exp;
	
	@Required
	public Blob picture1;
	
	public Blob picture2;
	
	public Blob picture3;
	
	public Blob picture4;
	
	public Blob picture5;
	
	public Blob picture6;
	
	public Blob picture7;
	
	public Blob picture8;
	
	public Blob picture9;
	
	public Blob picture10;
	
	@Required
	@Column(columnDefinition="TEXT")
	@MaxSize(1000)
	public String txt; // 详细内容
	
	public String toString(){
		return title;
	}
}