package models;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Table(name = "g_d_count")
@Entity
public class GameDownloadCount extends Model {

	@OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REFRESH)
	public Game g;
	
	public long gcount;
}