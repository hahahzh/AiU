package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@MappedSuperclass
public class ThirdModel extends SecondModel {
	
	public String author;
	
	@play.data.validation.URL
	public String URL;
	
}