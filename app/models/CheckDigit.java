package models;


import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import play.db.jpa.Model;

@Table(name = "checkdigit")
@Entity
public class CheckDigit extends Model {

	@Index(name = "checkdigit_d")
	public int d;
	public long updatetime;
}