package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.CheckWith;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import utils.CompressPic;

@Table(name="firmnews")
@Entity
public class FirmNew extends New implements Comparable<FirmNew>{

	@ManyToOne(fetch=FetchType.LAZY,cascade=javax.persistence.CascadeType.REFRESH)
	public Game game;

	public String toString(){
		return title+"--"+game.title;
	}

	@Override
	public int compareTo(FirmNew o) {
		
		return ((Integer)super.id.intValue()).compareTo(o.id.intValue());
	}
}