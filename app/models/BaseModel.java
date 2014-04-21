package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Index;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.JPABase;
import play.db.jpa.Model;
import controllers.CRUD.Hidden;

@MappedSuperclass
public class BaseModel extends Model {
	
	@Required
	@MaxSize(50)
	@MinSize(2)
	@Index(name = "idx_title")
	public String title;
	
	@Index(name = "idx_data")
	@Hidden
	public Long data = new Date().getTime();

	
	@Required
	// 1 IOS 2 Android 3 WP
	public int mtype;
	

}