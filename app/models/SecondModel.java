package models;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Model;

@MappedSuperclass
public class SecondModel extends BaseModel {

	public String res; // 来源
	
	@Required
	public Long hit;// 点击量
}