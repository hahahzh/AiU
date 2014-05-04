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
	@MaxSize(2000)
	public String txt1; // 详细内容
	
	public String toString(){
		return title;
	}
}