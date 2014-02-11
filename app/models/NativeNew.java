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
import check.PicCompres;

@Table(name="nativenews")
@Entity
public class NativeNew extends New {

	public String toString(){
		return title;
	}
}