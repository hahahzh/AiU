package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Table(name="packs")
@Entity
public class Pack extends BaseModel {
	public Blob icon;
	@Required
	public Integer type; // 游戏类型
	
	@Index(name = "idx_packs_star")
	public Integer star; // 热度
	
	@Required
	public String pkey; // 礼包代码
	
	@Required
	public Integer allnum;
	
	@Required
	public String describe_aiu; // 简介
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REFRESH)
	public Game game;
	
	public String toString(){
		return title;
	}
}