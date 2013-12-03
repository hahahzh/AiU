package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Table(name = "everygametype")
@Entity
public class EveryGameType extends Model {

	@Required
	public String everygametype;
	
	public String toString(){
		return everygametype;
	}
}