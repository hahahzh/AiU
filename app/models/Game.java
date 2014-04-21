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
	public String star; // 热度
	
	@URL
	public String bbsurl;
	
	@Required
	@ManyToOne(fetch=FetchType.EAGER,cascade=javax.persistence.CascadeType.REFRESH)
	public GameType gtype;
	
	public String exp;
	
	@Required
	public String size;
	
	@Required
	public String version;
	
// Start 增加到20
	@CheckWith(PicCompres.class)
	public Blob picture1_ip5;
	
	@CheckWith(PicCompres.class)
	public Blob picture1;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt1; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture2;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt2; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture3;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt3; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture4;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt4; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture5;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt5; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture6;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt6; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture7;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt7; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture8;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt8; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture9;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt9; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture10;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt10; // 详细内容
	
	//11-25
	@CheckWith(PicCompres.class)
	public Blob picture11;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt11; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture12;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt12; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture13;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt13; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture14;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt14; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture15;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt15; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture16;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt16; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture17;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt17; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture18;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt18; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture19;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt19; // 详细内容
	
	@CheckWith(PicCompres.class)
	public Blob picture20;
	
	@Column(columnDefinition="TEXT")
	@MaxSize(2000)
	public String txt20; // 详细内容
	
	public Blob getPicture1() {
		return picture1;
	}

	public void setPicture1(Blob picture1) {
		if(picture1 != null){
			CompressPic.compressPic(picture1.getFile().getPath(), picture1.getFile().getPath());
		}
		this.picture1 = picture1;
	}
	
	public Blob getPicture1_ip5() {
		return picture1_ip5;
	}

	public void setPicture1_ip5(Blob picture1_ip5) {
		if(picture1_ip5 != null){
			CompressPic.compressPic(picture1_ip5.getFile().getPath(), picture1_ip5.getFile().getPath());
		}
		this.picture1_ip5 = picture1_ip5;
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
		if(picture10 != null && picture10.getFile() != null){
			CompressPic.compressPic(picture10.getFile().getPath(), picture10.getFile().getPath());
		}
		this.picture10 = picture10;
	}
	
	// 11-25
	public Blob getPicture11() {
		return picture11;
	}

	public void setPicture11(Blob picture11) {
		if(picture11 != null){
			CompressPic.compressPic(picture11.getFile().getPath(), picture11.getFile().getPath());
		}
		this.picture11 = picture11;
	}

	public Blob getPicture12() {
		return picture12;
	}

	public void setPicture12(Blob picture12) {
		if(picture12 != null){
			CompressPic.compressPic(picture12.getFile().getPath(), picture12.getFile().getPath());
		}
		this.picture12 = picture12;
	}

	public Blob getPicture13() {
		return picture13;
	}

	public void setPicture13(Blob picture13) {
		if(picture13 != null){
			CompressPic.compressPic(picture13.getFile().getPath(), picture13.getFile().getPath());
		}
		this.picture13 = picture13;
	}

	public Blob getPicture14() {
		return picture14;
	}

	public void setPicture14(Blob picture14) {
		if(picture14 != null){
			CompressPic.compressPic(picture14.getFile().getPath(), picture14.getFile().getPath());
		}
		this.picture14 = picture14;
	}

	public Blob getPicture15() {
		return picture15;
	}

	public void setPicture15(Blob picture15) {
		if(picture15 != null){
			CompressPic.compressPic(picture15.getFile().getPath(), picture15.getFile().getPath());
		}
		this.picture15 = picture15;
	}

	public Blob getPicture16() {
		return picture16;
	}

	public void setPicture16(Blob picture16) {
		if(picture16 != null){
			CompressPic.compressPic(picture16.getFile().getPath(), picture16.getFile().getPath());
		}
		this.picture16 = picture16;
	}

	public Blob getPicture17() {
		return picture17;
	}

	public void setPicture17(Blob picture17) {
		if(picture17 != null){
			CompressPic.compressPic(picture17.getFile().getPath(), picture17.getFile().getPath());
		}
		this.picture17 = picture17;
	}

	public Blob getPicture18() {
		return picture18;
	}

	public void setPicture18(Blob picture18) {
		if(picture18 != null){
			CompressPic.compressPic(picture18.getFile().getPath(), picture18.getFile().getPath());
		}
		this.picture18 = picture18;
	}

	public Blob getPicture19() {
		return picture19;
	}

	public void setPicture19(Blob picture19) {
		if(picture19 != null){
			CompressPic.compressPic(picture19.getFile().getPath(), picture19.getFile().getPath());
		}
		this.picture19 = picture19;
	}

	public Blob getPicture20() {
		return picture20;
	}

	public void setPicture20(Blob picture20) {
		if(picture20 != null && picture20.getFile() != null){
			CompressPic.compressPic(picture20.getFile().getPath(), picture20.getFile().getPath());
		}
		this.picture20 = picture20;
	}
	
	// END
	
	@Required
	@Column(columnDefinition="TEXT")
	@MaxSize(1000)
	public String txt; // 详细内容
	
	// IOS正版
	public String downloadurl1;
	
	// Android
	public String downloadurl2;
	
	// IOS越狱
	public String downloadurl3;
	
	// IPad
	public String downloadurl4;
	
	// OPad
	public String downloadurl5;
	
	@Required
	public int ranking;
	
	public String toString(){
		return title;
	}
}