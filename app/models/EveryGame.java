package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import check.PicCompres;
import play.data.validation.CheckWith;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import utils.CompressPic;

@Table(name="everygame")
@Entity
public class EveryGame extends ThirdModel {

	@Required
	public Integer type; // 游戏类型
	
	@Index(name = "idx_everygame_star")
	public Integer star; // 热度
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REFRESH)
	public EveryGameType everygametype;
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REFRESH)
	public Game game;
	
	@CheckWith(PicCompres.class)
	public Blob picture;
	
	public Blob getPicture() {
		return picture;
	}

	public void setPicture(Blob picture) {
		CompressPic.compressPic(picture.getFile().getPath(), picture.getFile().getPath());
		this.picture = picture;
	}

	@Required
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt; // 详细内容
	
	public String toString(){
		return title;
	}
}