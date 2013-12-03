package models;


import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Table(name = "ttt")
@Entity
public class TTT extends Model {

	@Required
//	@Match(value = "^\\w*$", message = "Not a valid username")
	public Blob ttt1;

	public String toString() {
		return ttt1.toString();
	}
}