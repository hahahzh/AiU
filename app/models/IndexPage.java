package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.CheckWith;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import utils.CompressPic;
import check.PicCompres;

@Table(name = "IndexPage")
@Entity
public class IndexPage extends Model {

//	@URL
//	public String indexpage_url;
	
	@Required
	@CheckWith(PicCompres.class)
	public Blob pic;

	public Blob getPic() {
		return pic;
	}

	public void setPic(Blob pic) {
		if(pic != null){
			CompressPic.compressPic(pic.getFile().getPath(), pic.getFile().getPath());
		}
		this.pic = pic;
	}
	
	public String toString() {
		return pic.getFile().getName();
	}
	
}