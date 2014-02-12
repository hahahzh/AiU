package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import play.data.validation.CheckWith;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Blob;
import utils.CompressPic;
import check.PicCompres;

@Table(name="games")
@Entity
public class Game extends ThirdModel {
	
	public Blob icon;
	
	public Blob rankingicon;
	
	@Required
	public Integer type; // 下载类型
	
	@Index(name = "idx_games_star")
	public Integer star; // 热度
	
	@URL
	public String bbsurl;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.ALL)
	public GameType gtype;
	
	public String exp;
	
	@Required
	public String size;
	
	@Required
	public String version;
	
	@Required
	@CheckWith(PicCompres.class)
	public Blob picture1;
	
	@CheckWith(PicCompres.class)
	public Blob picture2;
	
	@CheckWith(PicCompres.class)
	public Blob picture3;
	
	@CheckWith(PicCompres.class)
	public Blob picture4;
	
	@CheckWith(PicCompres.class)
	public Blob picture5;
	
	@CheckWith(PicCompres.class)
	public Blob picture6;
	
	@CheckWith(PicCompres.class)
	public Blob picture7;
	
	@CheckWith(PicCompres.class)
	public Blob picture8;
	
	@CheckWith(PicCompres.class)
	public Blob picture9;
	
	@CheckWith(PicCompres.class)
	public Blob picture10;
	
	public Blob getPicture1() {
		return picture1;
	}

	public void setPicture1(Blob picture1) {
		if(picture1 != null){
			CompressPic.compressPic(picture1.getFile().getPath(), picture1.getFile().getPath());
		}
		this.picture1 = picture1;
	}

	public Blob getPicture2() {
		return picture2;
	}

	public void setPicture2(Blob picture2) {
		if(picture2 != null){
			CompressPic.compressPic(picture2.getFile().getPath(), picture2.getFile().getPath());	
		}
		this.picture2 = picture2;
	}

	public Blob getPicture3() {
		return picture3;
	}

	public void setPicture3(Blob picture3) {
		if(picture3 != null){
			CompressPic.compressPic(picture3.getFile().getPath(), picture3.getFile().getPath());
		}
		this.picture3 = picture3;
	}

	public Blob getPicture4() {
		return picture4;
	}

	public void setPicture4(Blob picture4) {
		if(picture4 != null){
			CompressPic.compressPic(picture4.getFile().getPath(), picture4.getFile().getPath());
		}
		this.picture4 = picture4;
	}

	public Blob getPicture5() {
		return picture5;
	}

	public void setPicture5(Blob picture5) {
		if(picture5 != null){
			CompressPic.compressPic(picture5.getFile().getPath(), picture5.getFile().getPath());
		}
		this.picture5 = picture5;
	}

	public Blob getPicture6() {
		return picture6;
	}

	public void setPicture6(Blob picture6) {
		if(picture6 != null){
			CompressPic.compressPic(picture6.getFile().getPath(), picture6.getFile().getPath());
		}
		this.picture6 = picture6;
	}

	public Blob getPicture7() {
		return picture7;
	}

	public void setPicture7(Blob picture7) {
		if(picture7 != null){
			CompressPic.compressPic(picture7.getFile().getPath(), picture7.getFile().getPath());
		}
		this.picture7 = picture7;
	}

	public Blob getPicture8() {
		return picture8;
	}

	public void setPicture8(Blob picture8) {
		if(picture8 != null){
			CompressPic.compressPic(picture8.getFile().getPath(), picture8.getFile().getPath());
		}
		this.picture8 = picture8;
	}

	public Blob getPicture9() {
		return picture9;
	}

	public void setPicture9(Blob picture9) {
		if(picture9 != null){
			CompressPic.compressPic(picture9.getFile().getPath(), picture9.getFile().getPath());
		}
		this.picture9 = picture9;
	}

	public Blob getPicture10() {
		return picture10;
	}

	public void setPicture10(Blob picture10) {
		if(picture10 != null){
			CompressPic.compressPic(picture10.getFile().getPath(), picture10.getFile().getPath());
		}
		this.picture10 = picture10;
	}
	
	@Required
	@Column(columnDefinition="TEXT")
	@MaxSize(1000)
	public String txt; // 详细内容
	
	@Required
	public String downloadurl;
	
	@Required
	public int ranking;
	
	public String toString(){
		return title;
	}
}