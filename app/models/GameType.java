package models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Phone;
import play.data.validation.Required;
import play.db.jpa.Model;

@Table(name = "gametype")
@Entity
public class GameType extends Model {

	@Required
//	@Match(value = "^\\w*$", message = "Not a valid username")
	public String gametype_name;

	public String toString() {
		return gametype_name;
	}
}