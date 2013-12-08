package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Table(name="news")
@Entity
public class New extends ThirdModel {
	public Blob icon;
	@Required
	public String describe_aiu; // 简介
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REFRESH)
	public NewType newtype; // 简介
	
	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REFRESH)
	public Game game;
	
	@Required
	public Blob picture1;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt1; // 详细内容
	
	public Blob picture2;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt2; // 详细内容
	
	public Blob picture3;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt3; // 详细内容
	
	public Blob picture4;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt4; // 详细内容
	
	public Blob picture5;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt5; // 详细内容
	
	public Blob picture6;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt6; // 详细内容
	
	public Blob picture7;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt7; // 详细内容
	
	public Blob picture8;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt8; // 详细内容
	
	public Blob picture9;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt9; // 详细内容
	
	public Blob picture10;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt10; // 详细内容
	
	public String toString(){
		return title;
	}
}