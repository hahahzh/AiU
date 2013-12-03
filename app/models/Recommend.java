package models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Phone;
import play.data.validation.Required;
import play.db.jpa.Model;

@Table(name = "recommend")
@Entity
public class Recommend extends Model {

	@Required
	// 1 IOS 2 Android 3 WP
	public int mtype;
	
	@Required
	public int ranking;
	
	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REFRESH)
	public Game game;

	public String toString() {
		return "游戏名称:"+ game.title+" 排名:" + ranking;
	}
}