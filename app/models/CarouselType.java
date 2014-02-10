package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Required;
import play.data.validation.URL;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Table(name = "carouseltype")
@Entity
public class CarouselType extends Model {

	@Required
	@Unique
	public String type;
	
	public String toString(){
		return type;
	}
}