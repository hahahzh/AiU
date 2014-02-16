package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import play.data.validation.CheckWith;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import utils.CompressPic;
import check.PicCompres;

@MappedSuperclass
public class New extends ThirdModel {
	public Blob icon;
	@Required
	public String describe_aiu; // 简介
	
	@Required
	@CheckWith(PicCompres.class)
	public Blob picture1;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt1; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture2;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt2; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture3;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt3; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture4;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt4; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture5;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt5; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture6;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt6; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture7;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt7; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture8;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt8; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture9;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt9; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture10;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(500)
	public String txt10; // 详细内容
	
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

	public String toString(){
		return title;
	}
}