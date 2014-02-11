package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Blob;

@Table(name="packs")
@Entity
public class Pack extends BaseModel {
	public Blob icon;
	
	@Required
	public Integer type; // 游戏类型
	
	@Index(name = "idx_packs_star")
	public Integer star; // 热度
	
	@Required
	@MaxSize(200)
	public String describe_aiu; // 简介
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REFRESH)
	public Game game;
	
	@Required
	public int ranking; // 自动排序 0 其他排序 1234
	
	@Required
	public Date remaining;
	
	public String toString(){
		return title;
	}
}
