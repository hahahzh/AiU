package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Match;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Table(name = "leveltype")
@Entity
public class LevelType extends Model {

	
	@Required
	@Unique(message = "名称不能重复")
	public String level_name;

	public String toString() {
		return level_name;
	}
}