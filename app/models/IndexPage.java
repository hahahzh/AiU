package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Table(name = "IndexPage")
@Entity
public class IndexPage extends Model {

//	@URL
//	public String indexpage_url;
	
	@Required
	public Blob pic;
}