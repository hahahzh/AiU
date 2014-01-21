package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import controllers.CRUD.Hidden;
import play.data.validation.Match;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Table(name = "game_adlist")
@Entity
public class GameCarousel extends Model {

	@Required
	// 1 IOS 2 Android 3 WP
	public int mtype;
	
	@Hidden
	public Long ad_id;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.REFRESH)
	public CarouselType ct;
	
	@Hidden
	public Long ad_id2;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.REFRESH)
	public CarouselType ct2;
	
	@Hidden
	public Long ad_id3;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.REFRESH)
	public CarouselType ct3;
	
	@Hidden
	public Long ad_id4;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.REFRESH)
	public CarouselType ct4;
	
	@Hidden
	public Long ad_id5;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.REFRESH)
	public CarouselType ct5;
	
	@Hidden
	public Long ad_id6;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.REFRESH)
	public CarouselType ct6;
	
	@Hidden
	public Long ad_id7;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.REFRESH)
	public CarouselType ct7;
	
	@Hidden
	public Long ad_id8;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.REFRESH)
	public CarouselType ct8;
	
	@Required
	@OneToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.REFRESH)
	public Game game;

	public String toString(){
		if(this.mtype == 1) return game.title+" Android版"; else return game.title+" IOS版";
	}
}