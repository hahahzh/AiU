package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import check.PicCompres;
import play.data.validation.CheckWith;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import utils.CompressPic;

@Table(name="gamevideo")
@Entity
public class GameVideo extends ThirdModel {

	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REFRESH)
	public Game game;

	public Blob video1;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt1; // 详细内容
	
	public Blob video2;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt2; // 详细内容
	
	public Blob video3;

	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt3; // 详细内容
	
	public Blob video4;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt4; // 详细内容
	
	public Blob video5;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt5; // 详细内容
	
	public Blob video6;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt6; // 详细内容
	
	public Blob video7;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt7; // 详细内容
	
	public Blob video8;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt8; // 详细内容
	
	public Blob video9;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt9; // 详细内容
	
	public Blob video10;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt10; // 详细内容
	
	public String toString(){
		return title;
	}
}