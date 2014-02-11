package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import controllers.CRUD.Hidden;
import play.data.validation.Match;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Table(name = "adlist")
@Entity
public class Carousel extends Model {

	@Hidden
	@Required
	public Long ad_id;
	
	@Required
	public int mtype;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.REFRESH)
	public CarouselType ct;

	public String toString(){
		String name = null;
		if(2 == ct.id){
	  		EveryGame l = EveryGame.findById(ad_id);
	  		name = l.title;
	  	  } else if(3 == ct.id){
	  		Game l = Game.findById(ad_id);
	  		name = l.title;
	  	  }else if(4 == ct.id){
	  		NativeNew l = NativeNew.findById(ad_id);
	  		name = l.title;
	  	  }else if(5 == ct.id){
	  		Pack l = Pack.findById(ad_id);
	  		name = l.title;
	  	  }
		return name;
	}
}