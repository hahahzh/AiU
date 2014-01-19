package controllers;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import models.CPkey;
import models.Carousel;
import models.CheckDigit;
import models.ClientVersion;
import models.Customer;
import models.EveryGame;
import models.Game;
import models.GameCarousel;
import models.GameDownloadCount;
import models.GameIcon;
import models.GameMessage;
import models.GameType;
import models.IndexPage;
import models.LevelType;
import models.Log;
import models.New;
import models.NewType;
import models.Pack;
import models.PackPKey;
import models.PlusGame;
import models.PublicChannel;
import models.Session;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.db.Model;
import play.db.jpa.Blob;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Controller;
import utils.Coder;
import utils.DateUtil;
import utils.Demo_Mt;
import utils.JSONUtil;
import utils.SendSMS;
import controllers.CRUD.ObjectType;

/**
 * ......
 * 
 * @author hanzhao
 * 
 */
//@With(Compress.class)
public class AiU extends Controller {

	public static final String SUCCESS = "1";
	public static final String FAIL = "0";
	
	public static final String ONE = "1";
	public static final String TWO = "2";
	
	public static final int upgrade_flag = 1;// .......
	public static final int error_parameter_required = 1;// .......
	public static final int error_username_already_used = 2;// ........
	public static final int error_username_not_exist = 3;// .......
	public static final int error_userid_not_exist = 4;// ..id....
	public static final int error_not_owner = 5;// &{%s} .........
	public static final int error_unknown = 6;// ....,......
	public static final int error_locator_not_exist = 7;// .......
	public static final int error_both_email_phonenumber_empty = 8;// ..............
	public static final int error_username_or_password_not_match = 9;// ..........
	public static final int error_session_expired = 10;// .....,......
	public static final int error_mail_resetpassword = 11;// ..........,.........
															// &{%s} .
	public static final int error_locator_bind_full = 12;// ... &{%s} ..........
	public static final int error_locator_already_bind = 13;// ... &{%s} .......
	public static final int error_unknown_waring_format = 14;// .............
	public static final int error_unknown_command = 15;// ..... &{%s}.
	public static final int error_locator_not_confirmed = 16;// ..........,...........
	public static final int error_dateformat = 17;// .......
	public static final int error_locator_max = 18;// ......
	public static final int error_download = 19;// ....
	public static final int error_send_mail_fail = 20;
	public static final int error_already_exists = 21;// ........
	/**
	 * ...............
	 */
	private static ThreadLocal<Session> sessionCache = new ThreadLocal<Session>();
	
//
//	/**
//	 * ....
//	 * 
//	 * @param username
//	 * @param password
//	 */
//	@Before(unless = { "register", "sendResetPasswordMail", "checkVersion","getCellLocation" }, priority = 0)
//	public static void validateMember(@Required String userName,
//			@Required String password) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//
//		Member user = Member.find("byUsername", userName).first();
//		if (user == null || !user.password.equals(password)) {
//			renderFail("error_username_or_password_not_match", doc,
//					error_username_or_password_not_match);
//		}
//		memberCache.set(user);
//
//	}
//
	/**
	 * ....
	 * 
	 * @param sessionID
	 */
	@Before(unless={"startPage", "checkDigit", "register", "register2", "login", "sendResetPasswordMail", "update"},priority=1)
	public static void validateSessionID(@Required String z) {
		
		Session s = Session.find("bySessionID",z).first();
		sessionCache.set(s);
		if (s == null) {
			renderFail("error_session_expired");
			if(new Date().getTime() - s.data > 1800000){
				
			}
		}
	}
	
	public static void checkDigit(@Required String m) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		if(!Validation.phone(SUCCESS, m).ok){
			renderFail("error_parameter_required");
		}
		Random r = new Random();
		int n = Math.abs(r.nextInt())/10000;
		CheckDigit cd = new CheckDigit();
		cd.d = n;
		cd.updatetime = new Date().getTime();
		cd.m = m;
		cd._save();
		
		try {
			String s = SendSMS.send(m, ""+n);
			if(!s.contains("success")){
				renderText("failed");
			}
		} catch (Exception e) {
			cd._delete();
			play.Logger.error(e.getMessage());
			renderText("failed");
		}
		renderText("OK");
	}
	
	// ..
	public static void register(@Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}

		try {			
			byte[] b = Coder.decryptBASE64(z);
			String src = new String(b);
			String[] arr = src.split("\\|");
		
			int i = Integer.parseInt(arr[7]);
			CheckDigit c = CheckDigit.find("d=?", i).first();
			if(c == null){
				renderFail("error_checkdigit");
			}
			if(!c.m.equals(arr[6])){
				renderFail("error_checkdigit");
			}
			if(new Date().getTime() - c.updatetime > 1800000){
				c.delete();
				renderFail("error_checkdigit");
			}
			c.delete();
			
			Customer m = Customer.find("byM_number", arr[6]).first();
			if (m == null) {
				// .......
				Customer newUser = new Customer();
				newUser.cid = arr[1];
				newUser.mac = arr[3];
				newUser.os = Integer.parseInt(arr[4]);
				newUser.type = arr[5];
				newUser.m_number = arr[6];
				newUser.nickname = arr[8];
				newUser.psd = arr[9];
				newUser.exp = 0L;
				newUser.lv = (LevelType)LevelType.findAll().get(0);
				newUser.save();
				
				Session s = new Session();
				s.customer = newUser;
				s.data = new Date().getTime();
				s.sessionID = UUID.randomUUID().toString();
				s.save();
				
				JSONObject results = initResultJSON();				
				results.put("uid", newUser.getId());
				results.put("phone", newUser.m_number);
				results.put("exp", newUser.exp);
				results.put("lv", newUser.lv.level_name);
				results.put("name", newUser.nickname);
				results.put("session", s.sessionID);
				renderSuccess(results);
			} else {
				// .......
				renderFail("error_username_already_used");
			}
		} catch (Exception e) {
			renderFail("error_unknown");
		}
		
	}

	// ..
	public static void register2(@Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}

		try {
			byte[] b = Coder.decryptBASE64(z);
			String src = new String(b);
			String[] arr = src.split("\\|");
			
			Customer m = Customer.find("byMac", arr[3]).first();
			if (m == null) {
				// .......
				Customer newUser = new Customer();
				newUser.cid = arr[1];
				newUser.mac = arr[3];
				newUser.os = Integer.parseInt(arr[4]);
				newUser.type = arr[5];
				newUser.m_number = arr[6];
				newUser.nickname = arr[8];
				newUser.psd = arr[9];
				newUser.exp = 0L;
				newUser.lv = (LevelType)LevelType.findAll().get(0);
				newUser.save();
				
				Session s = new Session();
				s.customer = newUser;
				s.data = new Date().getTime();
				s.sessionID = UUID.randomUUID().toString();
				s.save();
				
				JSONObject results = initResultJSON();				
				results.put("uid", newUser.getId());
				results.put("phone", newUser.m_number);
				results.put("exp", newUser.exp);
				results.put("lv", newUser.lv.level_name);
				results.put("name", newUser.nickname);
				results.put("session", s.sessionID);
				renderSuccess(results);
			} else {
				// .......
				renderFail("error_username_already_used");
			}
		} catch (Exception e) {
			renderFail("error_unknown");
		}
		
	}
		
	/**
	 * ....
	 * 
	 * @param username
	 * @param password
	 * @param type
	 *            .....,......,iphone.......push..
	 * @param serialNumber
	 *            iphone.......,push..
	 */
	public static void login(@Required String phone,
			@Required String psd, @Required Integer type,
			String serialNumber, String ip, String imei, String mac) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}

		if (type != null && type == 1 && serialNumber.isEmpty()) {
			renderFail("error_parameter_required");
		}
		Customer customer = null;
		if(mac != null && !mac.isEmpty()){
			customer = Customer.find("byMac", mac).first();
		}else{
			customer = Customer.find("byM_number", phone).first();
		}
		
		if(customer == null || !customer.psd.equals(psd)){
			renderFail("error_username_or_password_not_match");
		}
//		customer.os = type;
//		customer.serialNumber = serialNumber;
//		customer.save();
		
		Session s = Session.find("byCustomer", customer).first();
		if(s == null){
			s = new Session();
			s.customer = customer;
			s.sessionID = UUID.randomUUID().toString();
			s.data = new Date().getTime();
			s.save();
		}

		JSONObject results = initResultJSON();
		results.put("uid", customer.getId());
		results.put("phone", customer.m_number);
		results.put("exp", customer.exp);
		results.put("lv", customer.lv.level_name);
		results.put("name", customer.nickname);
		results.put("session", s.sessionID);
		
		if((ip != null && !ip.isEmpty()) || (imei != null && imei.isEmpty())){
			Log log = new Log();
			log.customer_name = customer.m_number;
			log.data = new Date();
			log.ip = ip;
			log.imei = imei;
			log._save();
		}
		renderSuccess(results);
	}
	
	public static void indexPage(@Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer customer = s.customer;

		JSONObject results = initResultJSON();
		
		JSONObject user = initResultJSON();
		user.put("exp", customer.exp);
		user.put("lv", customer.lv.level_name);
		results.put("user", user);
		
		JSONArray adlistArr = initResultJSONArray();
		List<Carousel> adlistData = Carousel.find("mtype=? order by id desc",customer.os).fetch(0,5);
		for(Carousel data:adlistData){
			JSONObject subad = initResultJSON();
			subad.put("id", data.id);
	        
	  	  if("....&....".equals(data.ct.type)){
	  		EveryGame l = EveryGame.findById(data.ad_id);
	  		subad.put("icon", "/c/download?id=" + l.id + "&fileID=picture&entity=" + l.getClass().getName() + "&z=" + z);
			subad.put("url", "/c/invite?num=5&page=1&z="+z);
			subad.put("type", l.type);
			subad.put("star", l.star);
			subad.put("data", l.data+"");
			subad.put("t_id", l.game.id);
			subad.put("t_type", "eg");
	  	  } else if("..".equals(data.ct.type)){
	  		Game l = Game.findById(data.ad_id);
		  	subad.put("icon", "/c/download?id=" + l.id + "&fileID=picture1&entity=" + l.getClass().getName() + "&z=" + z);
			subad.put("url", "/c/gameinfo?id="+l.id+"&z="+z);
			subad.put("type", l.type);
			subad.put("star", l.star);
			subad.put("data", l.data+"");
			subad.put("t_id", l.id);
			subad.put("t_type", "g");
	  	  }else if("..".equals(data.ct.type)){
	  		New l = New.findById(data.ad_id);
		  	subad.put("icon", "/c/download?id=" + l.id + "&fileID=picture1&entity=" + l.getClass().getName() + "&z=" + z);
			subad.put("url", "/c/newinfo?id="+l.id+"&z="+z);
			subad.put("data", l.data+"");
			subad.put("t_id", l.id);
			subad.put("t_type", "n");
	  	  }else if("..".equals(data.ct.type)){
	  		Pack l = Pack.findById(data.ad_id);
		  	subad.put("icon", "/c/download?id=" + l.id + "&fileID=icon&entity=" + l.getClass().getName() + "&z=" + z);
			subad.put("url", "c/package?num=5&page=1&z="+z);
			subad.put("data", l.data+"");
			subad.put("t_id", l.id);
			subad.put("t_type", "p");
	  	  }
			
		adlistArr.add(subad);
		}
		results.put("adlist", adlistArr);

		//....
		JSONArray pubchannel = initResultJSONArray();
		JSONObject subpubc = initResultJSON();
		List<PublicChannel> publicChannel = PublicChannel.find("order by id desc").fetch(0, 6);
		for(PublicChannel pc : publicChannel){
			subpubc.put("id", pc.id+"");
			subpubc.put("icon", "/c/download?id=" + pc.id + "&fileID=icon&entity=" + pc.getClass().getName() + "&z=" + z);
//			subad.put("url", pc..ad_game.);
			subpubc.put("exp", pc.exp+"");
			subpubc.put("data", pc.data+"");
			pubchannel.add(subpubc);
		}
		results.put("pubchannel", pubchannel);
		
		//....
		JSONArray prechannel = initResultJSONArray();
		JSONObject subprec = initResultJSON();
		List<Game> l = customer.addgame;
		for(Game g : l){
			subprec.put("id", g.id+"");
			subprec.put("title", g.title);
			//TODO
//			subad.put("url", data.ad_game.);
			subprec.put("icon", "/c/download?id=" + g.id + "&fileID=icon&entity=models.Game&z=" + z);
			subprec.put("type", g.type+"");
			subprec.put("exp", g.exp+"");
			subprec.put("star", g.star+"");
			subprec.put("data", g.data+"");
			prechannel.add(subprec);
		}
		if(l != null && l.size() != 0)results.put("prechannel", prechannel);
		renderSuccess(results);
	}
	
	public static void getNews(int num, long time, int page, Long type, Long gid, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer c = s.customer;

		JSONObject results = initResultJSON();
		
		JSONObject user = initResultJSON();
		user.put("exp", c.exp);
		user.put("lv", c.lv.level_name);
		results.put("user", user);
		
		String condition = null;
		if(type == null){
			condition = "newtype_id=1";
		}else{
			condition = "newtype_id="+type;
		}
		if(gid != null){
			condition += " and game_id="+gid;
		}
				
		JSONArray newlist = initResultJSONArray();
		List<New> newlistData = New.find("mtype = ? and "+condition+" order by id desc", c.os).fetch(page, num);
		for(New data:newlistData){
			JSONObject subad = initResultJSON();
			subad.put("id", data.id);
			subad.put("icon", "/c/download?id=" + data.id + "&fileID=icon&entity=" + data.getClass().getName() + "&z=" + z);
			subad.put("pic", "/c/download?id=" + data.id + "&fileID=picture1&entity=" + data.getClass().getName() + "&z=" + z);
//			subad.put("url", data.ad_game.);
			subad.put("title", data.title);
			subad.put("txt", data.describe_aiu);
			subad.put("hit", data.hit);
			subad.put("data", data.data);
			if(data.game != null){
				subad.put("g_id", data.game.id);
			}
			newlist.add(subad);
		}
		results.put("newlist", newlist);
		
		renderSuccess(results);
	}
	
	public static void getNewsType(@Required String z){
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		JSONObject results = initResultJSON();
		JSONArray newstypes = initResultJSONArray();
		
		List<NewType> l = NewType.findAll();
		for(NewType n : l){
			JSONObject subad = initResultJSON();
			subad.put("id", n.id);
			subad.put("name", n.newtype);
			newstypes.add(subad);
		}
		results.put("newstypes", newstypes);
		renderSuccess(results);
	}
	
	public static void getNewInfo(@Required Long id, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer c = s.customer;

		JSONObject results = initResultJSON();
		
		JSONObject user = initResultJSON();
		user.put("exp", c.exp);
		user.put("lv", c.lv.level_name);
		results.put("user", user);
		
		JSONObject newinfo = initResultJSON();
		New data = New.findById(id);
		newinfo.put("id", data.id);
		newinfo.put("icon", "/c/download?id=" + data.id + "&fileID=icon&entity=" + data.getClass().getName() + "&z=" + z);
		//TODO
//			subad.put("url", data.ad_game.);
		newinfo.put("title", data.title);
		newinfo.put("author", data.author);
		newinfo.put("res", data.res);
		newinfo.put("hit", data.hit);
		newinfo.put("data", data.data);
		
		JSONArray jsonarr = initResultJSONArray(); 
		if(data.picture1.exists()){
			JSONObject img1 = initResultJSON();
			img1.put("picture", "/c/download?id=" + data.id + "&fileID=picture1&entity=" + data.getClass().getName() + "&z=" + z);
			img1.put("txt", data.txt1);
			jsonarr.add(img1);
		}
		if(data.picture2.exists()){
			JSONObject img2 = initResultJSON();
			img2.put("picture", "/c/download?id=" + data.id + "&fileID=picture2&entity=" + data.getClass().getName() + "&z=" + z);
			img2.put("txt", data.txt2);
			jsonarr.add(img2);
		}
		if(data.picture3.exists()){
			JSONObject img3 = initResultJSON();
			img3.put("picture", "/c/download?id=" + data.id + "&fileID=picture3&entity=" + data.getClass().getName() + "&z=" + z);
			img3.put("txt", data.txt3);
			jsonarr.add(img3);
		}
		if(data.picture4.exists()){
			JSONObject img4 = initResultJSON();
			img4.put("picture", "/c/download?id=" + data.id + "&fileID=picture4&entity=" + data.getClass().getName() + "&z=" + z);
			img4.put("txt", data.txt4);
			jsonarr.add(img4);
		}
		if(data.picture5.exists()){
			JSONObject img5 = initResultJSON();
			img5.put("picture", "/c/download?id=" + data.id + "&fileID=picture5&entity=" + data.getClass().getName() + "&z=" + z);
			img5.put("txt", data.txt5);
			jsonarr.add(img5);
		}
		if(data.picture6.exists()){
			JSONObject img6 = initResultJSON();
			img6.put("picture", "/c/download?id=" + data.id + "&fileID=picture6&entity=" + data.getClass().getName() + "&z=" + z);
			img6.put("txt", data.txt6);
			jsonarr.add(img6);
		}
		if(data.picture7.exists()){
			JSONObject img7 = initResultJSON();
			img7.put("picture", "/c/download?id=" + data.id + "&fileID=picture7&entity=" + data.getClass().getName() + "&z=" + z);
			img7.put("txt", data.txt7);
			jsonarr.add(img7);
		}
		if(data.picture8.exists()){
			JSONObject img8 = initResultJSON();
			img8.put("picture", "/c/download?id=" + data.id + "&fileID=picture8&entity=" + data.getClass().getName() + "&z=" + z);
			img8.put("txt", data.txt8);
			jsonarr.add(img8);
		}
		if(data.picture9.exists()){
			JSONObject img9 = initResultJSON();
			img9.put("picture", "/c/download?id=" + data.id + "&fileID=picture9&entity=" + data.getClass().getName() + "&z=" + z);
			img9.put("txt", data.txt9);
			jsonarr.add(img9);
		}
		if(data.picture10.exists()){
			JSONObject img10 = initResultJSON();
			img10.put("picture", "/c/download?id=" + data.id + "&fileID=picture10&entity=" + data.getClass().getName() + "&z=" + z);
			img10.put("txt", data.txt10);
			jsonarr.add(img10);
		}
		
		newinfo.put("imgs", jsonarr);
		results.put("newinfo", newinfo);
		data.hit++;
		data.save();
		renderSuccess(results);
	}
	
	//....
	public static void invite(int num, long time, int page, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer c = s.customer;

		JSONObject results = initResultJSON();
		
		JSONObject user = initResultJSON();
		user.put("exp", c.exp);
		user.put("lv", c.lv.level_name);
		results.put("user", user);
		
		JSONArray list = initResultJSONArray();
		List<EveryGame> listData = EveryGame.find("mtype = ? and everygametype_id=1 order by id desc", c.os).fetch(page,num);
		for(EveryGame data:listData){
			JSONObject subad = initResultJSON();
			New survey = New.find("mtype = ? and newtype_id = 3 and game_id=? order by id desc", c.os, data.game).first();
			if(survey != null){
				subad.put("id", survey.id);
			}
			//TODO
//			subad.put("url", data.ad_game.);
			subad.put("title", data.title);
			subad.put("txt", data.txt);
			subad.put("hit", data.hit);
			subad.put("data", data.data);
			subad.put("picture", "/c/download?id=" + data.id + "&fileID=picture&entity=" + data.getClass().getName() + "&z=" + z);
			subad.put("g_id", data.game.id);
			list.add(subad);
		}
		results.put("list", list);
		
		renderSuccess(results);
	}
	
	//.....
	public static void gameCarosel(@Required Long id, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer customer = s.customer;

		JSONObject results = initResultJSON();
		
		GameCarousel data = GameCarousel.find("mtype=? and game_id=? order by id desc",customer.os,id).first();
		if(data == null) renderFail("error_game_deleted");
		results.put("id", data.id);
		results.put("g_id", data.game.id);
		
		JSONArray adlistArr = initResultJSONArray();
		if(data.ct != null && data.ad_id != null){
			adlistArr.add(setGameCarosel(data.ct.type,data.ad_id, z));
		}
		if(data.ct2 != null && data.ad_id2 != null){
			adlistArr.add(setGameCarosel(data.ct2.type,data.ad_id2, z));
		}
		if(data.ct3 != null && data.ad_id3 != null){
			adlistArr.add(setGameCarosel(data.ct3.type,data.ad_id3, z));
		}   
		if(data.ct4 != null && data.ad_id4 != null){
			adlistArr.add(setGameCarosel(data.ct4.type,data.ad_id4, z));
		}
		if(data.ct5 != null && data.ad_id5 != null){
			adlistArr.add(setGameCarosel(data.ct5.type,data.ad_id5, z));
		}
		if(data.ct6 != null && data.ad_id6 != null){
			adlistArr.add(setGameCarosel(data.ct6.type,data.ad_id6, z));
		}
		if(data.ct7 != null && data.ad_id7 != null){
			adlistArr.add(setGameCarosel(data.ct7.type,data.ad_id7, z));
		}
		if(data.ct8 != null && data.ad_id8 != null){
			adlistArr.add(setGameCarosel(data.ct8.type,data.ad_id8, z));
		}
results.put("downloadurl", data.game.downloadurl);
		results.put("adlist", adlistArr);

		renderSuccess(results);
	}
	
	private static JSONObject setGameCarosel(String type, Long ad_id, String z){
		JSONObject subad = new JSONObject();
	  	  if("..".equals(type)){
	  		New l = New.findById(ad_id);
		  	subad.put("icon", "/c/download?id=" + l.id + "&fileID=picture1&entity=" + l.getClass().getName() + "&z=" + z);
			subad.put("url", "/c/newinfo?id="+l.id+"&z="+z);
			subad.put("data", l.data+"");
			subad.put("t_id", l.id);
			subad.put("t_type", "n");
	  	  }else if("..".equals(type)){
	  		Pack l = Pack.findById(ad_id);
		  	subad.put("icon", "/c/download?id=" + l.id + "&fileID=icon&entity=" + l.getClass().getName() + "&z=" + z);
			subad.put("url", "c/package?num=5&page=1&z="+z);
			subad.put("data", l.data+"");
			subad.put("t_id", l.id);
			subad.put("t_type", "p");
	  	  }
	  	  return subad;
	}
	
	public static void getPackage(int num, long time, int page, String skey, Long gid, @Required String z) throws ParseException {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer c = s.customer;

		JSONObject results = initResultJSON();
		
		JSONObject user = initResultJSON();
		user.put("exp", c.exp);
		user.put("lv", c.lv.level_name);
		results.put("user", user);
		
		//TODO .....
		JSONArray packagelist = initResultJSONArray();
		String sTmp = "";
		if(skey != null && !skey.isEmpty()){
			sTmp = " and title like '%" + skey + "%' ";
		}
		if(gid != null){
			sTmp += " and game_id = "+gid; 
		}
		List<Pack> listData = Pack.find("mtype="+ c.os + sTmp + " and ranking>0 order by ranking desc,id desc").fetch(page, num);
		for(Pack data:listData){
			JSONObject subad = initResultJSON();
			subad.put("id", data.id);
			subad.put("game_id", data.game.id);
			subad.put("icon", "/c/download?id=" + data.id + "&fileID=icon&entity=" + data.getClass().getName() + "&z=" + z);
			if(data.pack_pic.exists()){
				subad.put("pic", "/c/download?id=" + data.id + "&fileID=pack_pic&entity=" + data.getClass().getName() + "&z=" + z);
			}
			//TODO
//			subad.put("url", data.ad_game.);
			subad.put("title", data.title);
			subad.put("star", data.star+"");
			subad.put("data", data.data);
			
			long allnum = PackPKey.count("pack_id=?", data.id);
			long day = DateUtil.intervalOfDay(new Date(), data.remaining);
			if(allnum == 0){
				subad.put("num", "0");
			}else if(allnum <= day){
				subad.put("num", "1");
			}else{
				subad.put("num", allnum/day+"");
			}
			subad.put("allnum", allnum);
			
			packagelist.add(subad);
		}
		List<Pack> listData1 = Pack.find("mtype="+ c.os + sTmp + " and ranking=0 order by id desc").fetch(page, num);
		for(Pack data:listData1){
			JSONObject subad = initResultJSON();
			subad.put("id", data.id);
			subad.put("game_id", data.game.id);
			subad.put("icon", "/c/download?id=" + data.id + "&fileID=icon&entity=" + data.getClass().getName() + "&z=" + z);
			if(data.pack_pic.exists()){
				subad.put("pic", "/c/download?id=" + data.id + "&fileID=pack_pic&entity=" + data.getClass().getName() + "&z=" + z);
			}
			//TODO
//			subad.put("url", data.ad_game.);
			subad.put("title", data.title);
			subad.put("star", data.star+"");
			
			long allnum = PackPKey.count("pack_id=?", data.id);
			long day = DateUtil.intervalOfDay(new Date(), data.remaining);
			if(allnum == 0){
				subad.put("num", "0");
			}else if(allnum <= day){
				subad.put("num", "1");
			}else{
				subad.put("num", allnum/day+"");
			}
			subad.put("allnum", allnum);
			
			subad.put("data", data.data);
			
			packagelist.add(subad);
		}
		results.put("packagelist", packagelist);
		
		renderSuccess(results);
	}
	
	public static void packageInfo(@Required Long id, @Required String z) throws ParseException {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer c = s.customer;

		JSONObject results = initResultJSON();
		
		JSONObject user = initResultJSON();
		user.put("exp", c.exp);
		user.put("lv", c.lv.level_name);
		results.put("user", user);
		
		Pack data = Pack.findById(id);
		
		JSONObject subad = initResultJSON();
		subad.put("id", data.id);
		subad.put("icon", "/c/download?id=" + data.id + "&fileID=icon&entity=" + data.getClass().getName() + "&z=" + z);
		//TODO
//			subad.put("url", data.ad_game.);
		subad.put("title", data.title);
		subad.put("txt", data.describe_aiu);
		subad.put("star", data.star+"");
		long allnum = PackPKey.count("pack_id=?", data.id);
		long day = DateUtil.intervalOfDay(new Date(), data.remaining);
		if(allnum == 0){
			subad.put("num", "0");
		}else if(allnum <= day){
			subad.put("num", "1");
		}else{
			subad.put("num", allnum/day+"");
		}
		
		subad.put("allnum", allnum);
		subad.put("data", data.data);
		subad.put("g_id", data.game.id);
	
		results.put("packinfo", subad);
		renderSuccess(results);
	}
	
	public static void packagePay(@Required Long id, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		// ....
		if (z.equals(ONE) || z.equals(TWO)) {
			renderFail("error_permission_denied");
		}
		Customer c = s.customer;

		CPkey oldcpk= CPkey.find("c_id=? and p_id=? order by id desc", c.id, id).first();
		PackPKey data = PackPKey.find("pack_id=?", id).first();

		if(oldcpk != null){
			if(new Date().getTime() - oldcpk.updatetime <= 86400000){
				renderFail("error_exists_pay");
			}
		}
		
		if(data == null){
			renderFail("error_over_pay");
		}
		CPkey cpk = new CPkey();
			cpk.c = c;
			cpk.p = Pack.findById(id);
			cpk.updatetime = new Date().getTime();
			cpk.pkey = data.pkey;
			cpk.save();
			

		JSONObject results = initResultJSON();
		
		JSONObject user = initResultJSON();
		user.put("exp", c.exp);
		user.put("lv", c.lv.level_name);
		results.put("user", user);
		
		JSONObject pkey = initResultJSON();
		pkey.put("pkey", data.pkey);
		PackPKey.delete("id=?", data.id);
		results.put("pack", pkey);
		renderSuccess(results);
	}
	
	//....
	public static void gameplay(int num, long time, int page, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer c = s.customer;

		JSONObject results = initResultJSON();
		
		JSONObject user = initResultJSON();
		user.put("exp", c.exp);
		user.put("lv", c.lv.level_name);
		results.put("user", user);
		
		JSONArray list = initResultJSONArray();
		List<EveryGame> listData = EveryGame.find("mtype = ? and everygametype_id=2 order by id desc", c.os).fetch(page, num);
		for(EveryGame data:listData){
			JSONObject subad = initResultJSON();
			subad.put("id", data.id);
			//TODO
//				subad.put("url", data.ad_game.);
			subad.put("title", data.title);
			subad.put("txt", data.txt);
			subad.put("hit", data.hit);
			subad.put("data", data.data);
			subad.put("picture", "/c/download?id=" + data.id + "&fileID=picture&entity=" + data.getClass().getName() + "&z=" + z);
			subad.put("g_id", data.game.id);
			list.add(subad);
		}
		results.put("list", list);
		
		renderSuccess(results);
	}
	
	//.... ....
	public static void gameinvite(int num, long time, int page, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer c = s.customer;

		JSONObject results = initResultJSON();
		
		JSONObject user = initResultJSON();
		user.put("exp", c.exp);
		user.put("lv", c.lv.level_name);
		results.put("user", user);
		
		JSONArray list = initResultJSONArray();
		List<Game> listData = Game.find("mtype = ? order by ranking desc,id desc", c.os).fetch();
		for(Game data:listData){
			JSONObject subad = initResultJSON();
			subad.put("id", data.id);
			subad.put("icon", "/c/download?id=" + data.id + "&fileID=icon&entity=models.Game&z=" + z);
			subad.put("pic", "/c/download?id=" + data.id + "&fileID=rankingicon&entity=models.Game&z=" + z);
			subad.put("url", "/c/gameinfo?id="+data.id+"&z="+z);
			subad.put("title", data.title);
			subad.put("star", data.star);
			subad.put("data", data.data);
			subad.put("gtype", data.gtype.gametype_name);
			subad.put("downloadurl", data.downloadurl);
			subad.put("size", data.size);
			subad.put("version", data.version);
			subad.put("comment", GameMessage.count("byGame", data)+"");
			String count = null;
			GameDownloadCount gdc = GameDownloadCount.find("byG", data).first();
			if(gdc == null){
				count = "0";
			}else{
				count = gdc.gcount + "";
			}
			subad.put("downloadcount", count);
			list.add(subad);
		}
		
		results.put("list", list);
		
		renderSuccess(results);
	}
	
	public static void getGameinfo(@Required Long id, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer c = s.customer;

		JSONObject results = initResultJSON();
		
		JSONObject user = initResultJSON();
		user.put("exp", c.exp);
		user.put("lv", c.lv.level_name);
		results.put("user", user);
		
		JSONObject game = initResultJSON();
		Game data = Game.findById(id);
		game.put("id", data.id);
		Pack p = Pack.find("byGame", data).first();
		if(p != null && p.id != null){
			game.put("p_id", p.id);
		}
		game.put("icon", "/c/download?id=" + data.id + "&fileID=icon&entity=" + data.getClass().getName() + "&z=" + z);
		game.put("title", data.title);
		game.put("res", data.res);
		game.put("belong", data.gtype.gametype_name);
		game.put("bbsurl", data.bbsurl);
		game.put("type", data.type);
		game.put("star", data.star+"");
		game.put("hit", data.hit);
		game.put("data", data.data);
		JSONArray imgs = initResultJSONArray();
		if(data.picture1.exists()){
			imgs.add("/c/download?id=" + data.id + "&fileID=picture1&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.picture2.exists()){
			imgs.add("/c/download?id=" + data.id + "&fileID=picture2&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.picture3.exists()){
			imgs.add("/c/download?id=" + data.id + "&fileID=picture3&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.picture4.exists()){
			imgs.add("/c/download?id=" + data.id + "&fileID=picture4&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.picture5.exists()){
			imgs.add("/c/download?id=" + data.id + "&fileID=picture5&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.picture6.exists()){
			imgs.add("/c/download?id=" + data.id + "&fileID=picture6&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.picture7.exists()){
			imgs.add("/c/download?id=" + data.id + "&fileID=picture7&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.picture8.exists()){
			imgs.add("/c/download?id=" + data.id + "&fileID=picture8&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.picture9.exists()){
			imgs.add("/c/download?id=" + data.id + "&fileID=picture9&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.picture10.exists()){
			imgs.add("/c/download?id=" + data.id + "&fileID=picture10&entity=" + data.getClass().getName() + "&z=" + z);
		}
		game.put("imgs", imgs);
		game.put("txt", data.txt);
		game.put("size", data.size);
		game.put("version", data.version);
		game.put("comment", GameMessage.count("byGame", data)+"");
		game.put("subscription", "/c/subscription?id="+data.id+"&z="+z);
		game.put("downloadurl", data.downloadurl);
//		game.put("downloadcount", ((GameDownloadCount)GameDownloadCount.find("byGame", data).first()).gcount+"");
		results.put("game", game);
		
		data.hit++;
		data.save();
		renderSuccess(results);
	}
	
	//..
	public static void subscription(@Required long id, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer customer = s.customer;
		Game g = Game.findById(id);
		if(g == null){
			renderFail("error_not_find_game");
		}
		if(customer.addgame != null && customer.addgame.size() > 40){
			renderFail("error_too_big_size");
		}
		for(Game gg:customer.addgame){
			if(g.id == gg.id){
				renderFail("error_exists_ss");
			}
		}
		customer.addgame.add(g);
		customer.save();
		renderSuccess(initResultJSON());

	}
	
	//..
	public static void unsubscription(@Required long id, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer customer = s.customer;
		Game g = Game.findById(id);
		if(g == null){
			renderFail("error_not_find_game");
		}

		customer.addgame.remove(g);		
		customer.save();
		renderSuccess(initResultJSON());
	}
	
	public static void addComment(Long id, @Required String type, @Required String msg, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		// ....
		if (z.equals(ONE) || z.equals(TWO)) {
			renderFail("error_permission_denied");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		GameMessage gm = new GameMessage();
		if(id != null){
			if("g".equals(type)){
				gm.game = Game.findById(id);
			}else if("n".equals(type)){
				gm.news = New.findById(id);
			}
		}
		gm.data = new Date().getTime();
		gm.msg = msg;
		gm.c = s.customer;
		
		gm.save();
		renderSuccess(initResultJSON());
	}
	
	public static void getComment(@Required Long id, @Required String type, int num, long time, int page, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer c = s.customer;

		JSONObject results = initResultJSON();
		
		JSONObject user = initResultJSON();
		user.put("exp", c.exp);
		user.put("lv", c.lv.level_name);
		results.put("user", user);
		
		JSONArray list = initResultJSONArray();
		String sql = null;
		if("g".equals(type)){
			sql = "game_id="+id;
		}else if("n".equals(type)){
			sql = "news_id="+id;
		}
		List<GameMessage> listData = GameMessage.find(sql+" order by id desc").fetch(page, num);
		for(GameMessage data:listData){
			JSONObject subad = initResultJSON();
			subad.put("id", data.id);
			if("g".equals(type)){
				subad.put("game_id", data.game.id);
			}else if("n".equals(type)){
				subad.put("news_id", data.news.id);
			}
			subad.put("msg", data.msg);
			subad.put("data", data.data);
			subad.put("username", data.c.nickname);
			if(data.c.portrait.exists()){
				subad.put("portrait", "/c/download?id=" + data.c.id + "&fileID=portrait&entity=" + data.c.getClass().getName() + "&z=" + z);
			}
			list.add(subad);
		}
		results.put("list", list);
		
		renderSuccess(results);
	}
	
	//+
	public static void plus(@Required String key, Long gtype, @Required String z){
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer c = s.customer;

		JSONObject results = initResultJSON();
		
		JSONObject user = initResultJSON();
		user.put("exp", c.exp);
		user.put("lv", c.lv.level_name);
		results.put("user", user);
		
		JSONArray advertisementlist = initResultJSONArray();
		List<PlusGame> ll = PlusGame.findAll();
		int i=0;
		for(PlusGame pg : ll){
			JSONObject subad = initResultJSON();
			subad.put("id", pg.game.id);
			subad.put("title", pg.game.title);
			subad.put("icon", "/c/download?id=" + pg.game.id + "&fileID=icon&entity=models.Game&z=" + z);
			advertisementlist.add(subad);
			i++;
			if(i == 2)break;
		}
		results.put("advertisementlist", advertisementlist);
		
		JSONArray gamelist = initResultJSONArray();
		key = "title like '%"+key.trim()+"%' order by id desc";
		if(gtype != null){
			key = "gtype_id = "+ gtype + " and " + key;
		}
		List<Game> l = Game.find(key).fetch();
		for(Game g : l){
			JSONObject subad = initResultJSON();
			subad.put("id", g.id);
			subad.put("icon", "/c/download?id=" + g.id + "&fileID=icon&entity=models.Game&z=" + z);
			subad.put("img", "/c/download?id=" + g.id + "&fileID=picture1&entity=models.Game&z=" + z);
			subad.put("url", "/c/gameinfo?id="+g.id+"&z="+z);
			subad.put("title", g.title);
			subad.put("star", g.star);
			subad.put("data", g.data);
			gamelist.add(subad);
		}
		results.put("gamelist", gamelist);
		renderSuccess(results);
	}
	
	public static void getGameType(@Required String z){
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		JSONObject results = initResultJSON();
		JSONArray gametypes = initResultJSONArray();
		
		List<GameType> l = GameType.findAll();
		for(GameType g : l){
			JSONObject subad = initResultJSON();
			subad.put("id", g.id);
			subad.put("name", g.gametype_name);
			gametypes.add(subad);
		}
		results.put("gametypes", gametypes);
		renderSuccess(results);
	}
	
	//...
	public static void startPage(){

		JSONObject results = initResultJSON();
		JSONArray list = new JSONArray();
		List<IndexPage> l = IndexPage.findAll();
		for(IndexPage ip : l){
			list.add("/c/download?id=" + ip.id + "&fileID=pic&entity=models.IndexPage&z=1");
		}
		results.put("imgs", list);
		renderSuccess(results);
	}
	
	//........
	public static void gameDownloadCount(@Required Long id, @Required String z){
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		GameDownloadCount gdc = GameDownloadCount.find("g_id=?", id).first();
		if(gdc == null){
			gdc = new GameDownloadCount();
			gdc.g = Game.findById(id);
		}
		gdc.gcount++;
		gdc._save();
		renderText("OK");
	}
	
	// ........
	public static void getMyPackage(@Required String z){
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		Customer c = s.customer;
		JSONObject results = initResultJSON();
		JSONArray list = new JSONArray();
		List<CPkey> l = CPkey.find("c_id=?", c.id).fetch();
		for(CPkey k : l){
			JSONObject subad = initResultJSON();
			subad.put("name", k.p.title);
			subad.put("data", k.updatetime);
			subad.put("key", k.pkey);
			list.add(subad);
		}
		results.put("packs", list);
		renderSuccess(results);
	}
	
	// ......
	public static void getGameIcon(@Required Long id, @Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}

		JSONObject results = initResultJSON();
		
		JSONObject gameicon = initResultJSON();
		GameIcon data = GameIcon.find("game_id=?",id).first();
		JSONArray gamearray = initResultJSONArray();
		if(data == null){
			gamearray.add("/public/images/defaulticon1.png");
			gamearray.add("/public/images/defaulticon2.png");
			gamearray.add("/public/images/defaulticon3.png");
			gamearray.add("/public/images/defaulticon4.png");
			gamearray.add("/public/images/defaulticon5.png");
			gamearray.add("/public/images/defaulticon6.png");
			gamearray.add("/public/images/gamebackgroundimg.png");
		}else{
			gameicon.put("id", data.id);
			if(data.picture1.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=picture1&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/defaulticon1.png");
			}
			if(data.picture2.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=picture2&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/defaulticon2.png");
			}
			if(data.picture3.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=picture3&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/defaulticon3.png");
			}
			if(data.picture4.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=picture4&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/defaulticon4.png");
			}
			if(data.picture5.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=picture5&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/defaulticon5.png");
			}
			if(data.picture6.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=picture6&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/defaulticon6.png");
			}
			if(data.backgroundpicture.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=backgroundpicture&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/gamebackgroundimg.png");
			}
		}
		
		gameicon.put("icons", gamearray);

		results.put("gameicon", gameicon);
		renderSuccess(results);
	}
	
//	searchGame
	/**
	 * ....
	 * 
	 * @param username
	 * @param password
	 * @param sessionID
	 */
	public static void logout(@Required String z) {
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		Session s = sessionCache.get();
		if(s != null && s.id != 1 && s.id != 2){
			s.delete();
		}
		renderSuccess(initResultJSON());
	}

//	/**
//	 * .............
//	 * 
//	 * @param username
//	 * @param password
//	 * @param sessionID
//	 * @throws UnsupportedEncodingException
//	 */
//	@SuppressWarnings("deprecation")
//	public static void sendResetPasswordMail(@Required String userName)
//			throws UnsupportedEncodingException {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//
//		// .......
//		Member member = Member.find("byUsername", userName).first();
//		if (member == null) {
//			renderFail("error_username_not_exist", doc,
//					error_username_not_exist);
//		}
//
//		if(member.updateTime != null && (new Date().getDate() != member.updateTime.getDate())){
//			member.sendPasswordCount = 1;
//		}
//		// ..........10.
//		if(member.sendPasswordCount > 10){
//			renderFail("error_send_mail_fail",doc,error_send_mail_fail);
//		}
//
//		SendMail mail = new SendMail(
//				Play.configuration.getProperty("mail.smtp.host"),
//				Play.configuration.getProperty("mail.smtp.user"),
//				Play.configuration.getProperty("mail.smtp.pass"));
//
//		mail.setSubject(Messages.get("mail_resetpassword_title"));
//		mail.setBodyAsText(Messages.get("mail_resetpassword_content",
//				member.username, member.username, member.password,
//				DateUtil.toDate(new Date())));
//
//		// ..........
//		String nick = Messages.get("mail_show_name");
//		try {
//			nick = javax.mail.internet.MimeUtility.encodeText(nick);
//			mail.setFrom(new InternetAddress(nick + " <"
//					+ Play.configuration.getProperty("mail.smtp.from") + ">")
//					.toString());
//			mail.setTo(member.email);
//			mail.send();
//			member.sendPasswordCount++;
//			member.updateTime = new Date();
//			member.save();
//		} catch (Exception e) {
//			renderFail("error_mail_resetpassword", doc,
//					error_mail_resetpassword);
//		}
//		renderSuccess("mail_resetpassword_success", doc);
//	}
//

	public static void updateMemberInfo(Integer os, String type, String m_number, String nickname,
			String psd, String gender, Blob portrait, @Required String z) {

		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}

		Session s = sessionCache.get();
		Customer c = s.customer;
		
		if(os != null){
			c.os = os;
		}
		if(type != null && !type.isEmpty()){
			c.type = type;
		}
		if(m_number != null && !m_number.isEmpty()){
			c.m_number = m_number;
		}
		if(nickname != null && !nickname.isEmpty()){
			c.nickname = nickname;
		}
		if(psd != null && !psd.isEmpty()){
			c.psd = psd;
		}
		if(gender != null && !gender.isEmpty()){
			c.gender = Byte.valueOf(gender);
		}
		if(portrait != null){
			c.portrait.getFile().delete();
			c.portrait = portrait;
		}
		c.save();
		renderSuccess(initResultJSON());
	}

//
//	/**
//	 * .......
//	 * 
//	 * @param userName
//	 *            ...
//	 * @param password
//	 *            ..
//	 * @param name
//	 * @param phoneNumber
//	 * @param serialNumber
//	 */
//	public static void addLocator(@Required String userName,
//			@Required String password, String name,
//			@Required String phoneNumber, @Required String serialNumber) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//
//		Member member = memberCache.get();
//		long count = Locator.count("byGuardian1", member);
//		int tmpMax = Integer.parseInt(Play.configuration
//				.getProperty("locator.max"));
//		if (tmpMax == 0) {
//			tmpMax = 5;// ........
//		}
//		if (count > tmpMax) {
//			renderFail("error_locator_max", doc, error_parameter_required);
//		}
//		// .........
//		Locator locator = Locator.find("bySerialNumber", serialNumber).first();
//		if (locator == null) {
//			locator = new Locator();
//			// .....
//			locator.serialNumber = serialNumber;
//			locator.confirmed = false;
//			// ..........
//			locator.warning = 0;
//			// ....180S
//			locator.mode = "3";
//		}
//
//		locator.name = name;
//		locator.phoneNumber = phoneNumber;
//
//		if (locator.guardian1 == null) {
//			Logger.debug("[addLocator] new Locator,name = " + name
//					+ ",phoneNumber = " + phoneNumber + ",serialNumber = "
//					+ serialNumber);
//			locator.guardian1 = member;
//			locator.bindDate = new Date();
//			locator.save();
//			// ....XML
//			Element addLocatorRsp = doc.createElement("addLocatorRsp");
//			Element locatorId = doc.createElement("locatorId");
//			locatorId.setTextContent("" + locator.id);
//			doc.getDocumentElement().appendChild(addLocatorRsp);
//			addLocatorRsp.appendChild(locatorId);
//			renderSuccess("locator_bind_success", doc);
//		} else if (locator.guardian1.id.equals(member.id)) {
//			Logger.debug("[addLocator] readd Locator,name = " + name
//					+ ",phoneNumber = " + phoneNumber + ",serialNumber = "
//					+ serialNumber);
//			Element addLocatorRsp = doc.createElement("addLocatorRsp");
//			Element locatorId = doc.createElement("locatorId");
//			locatorId.setTextContent("" + locator.id);
//			doc.getDocumentElement().appendChild(addLocatorRsp);
//			addLocatorRsp.appendChild(locatorId);
//			if (locator.confirmed) {
//				renderFail("error_locator_already_bind", doc,
//						error_locator_already_bind);
//			} else {
//				// ........,..........,....
//				locator.bindDate = new Date();
//				locator.save();
//				renderFail("error_locator_not_confirmed", doc,
//						error_locator_not_confirmed);
//			}
//		}
//		try {
//			// .............
//			if (DateUtil.intervalOfHour(locator.bindDate, new Date()) > 0.17
//					&& !locator.confirmed) {
//				Logger.debug("[addLocator] other one Locator,name = " + name
//						+ ",phoneNumber = " + phoneNumber + ",serialNumber = "
//						+ serialNumber);
//				locator.guardian1 = member;
//				locator.bindDate = new Date();
//				locator.save();
//				// ....XML
//				Element addLocatorRsp = doc.createElement("addLocatorRsp");
//				Element locatorId = doc.createElement("locatorId");
//				locatorId.setTextContent("" + locator.id);
//				doc.getDocumentElement().appendChild(addLocatorRsp);
//				addLocatorRsp.appendChild(locatorId);
//				renderSuccess("locator_bind_success", doc);
//			} else {
//				renderFail("error_already_exists", doc, error_already_exists);
//			}
//		} catch (ParseException e) {
//			renderFail("error_locator_bind_full", doc, error_locator_bind_full);
//		}
//	}
//
//	/**
//	 * .......
//	 * 
//	 * @param userName
//	 * @param password
//	 * @param locatorId
//	 * @param name
//	 * @param guardian1
//	 * @param guardian2
//	 * @param emergencyContact1
//	 * @param emergencyContact2
//	 * @param mode
//	 * @param warning
//	 * @param locatorSticker
//	 */
//	public static void modifyLocator(@Required String userName,
//			@Required String password, @Required Long locatorId, String name,
//			String guardian1, String guardian2, String emergencyContact1,
//			String emergencyContact2, String mode, Integer warning,
//			Blob locatorSticker) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//
//		Member member = memberCache.get();
//
//		Locator locator = Locator.findById(locatorId);
//		validateLocator(locator, member, doc);
//
//		if (name != null) {
//			locator.name = name;
//		}
//
//		if (guardian1 != null) {
//			locator.guardian1Number = guardian1;
//		}
//
//		if (guardian2 != null) {
//			locator.guardian2Number = guardian2;
//		}
//
//		if (emergencyContact1 != null) {
//			locator.emergencyContact1 = emergencyContact1;
//		}
//
//		if (emergencyContact2 != null) {
//			locator.emergencyContact2 = emergencyContact2;
//		}
//
//		if (mode != null) {
//			locator.mode = mode;
//		}
//
//		if (warning != null) {
//			locator.warning = warning;
//		}
//
//		if (locatorSticker != null) {
//			locator.locatorSticker = locatorSticker;
//		}
//
//		locator.save();
//
//		// renderSuccess("locator_modify_success",doc,locator.name);
//		renderSuccess("locator_modify_success", doc);
//	}
//
//	/**
//	 * ........
//	 * 
//	 * @param userName
//	 *            ...
//	 * @param password
//	 *            ..
//	 * @param locatorId
//	 *            ...id
//	 */
//	public static void deleteLocator(@Required String userName,
//			@Required String password, @Required Long locatorId) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//
//		Member member = memberCache.get();
//
//		Locator locator = Locator.findById(locatorId);
//		if (locator != null) {
//			if (locator.guardian1 != null
//					&& locator.guardian1.id.equals(member.id)) {
//				// ..........
//				locator.delete();
//				play.Logger
//						.debug("[GPS] delete locator,clear cache. serialNumber = "
//								+ locator.serialNumber);
//			}
//			// else
//			// if(locator.guardian2!=null&&locator.guardian2.id.equals(member.id)){
//			// //..........
//			// locator.guardian2 = null;
//			// }
//			else {
//				// ..........
//				// renderFail("error_not_owner", doc,error_not_owner,
//				// locator.name);
//				renderFail("error_not_owner", doc, error_not_owner);
//			}
//			// .....locator,.................
//			// if(locator.guardian1 == null &&locator.guardian2 == null){
//			// locator.delete();
//			// }else{
//			// locator.save();
//			// }
//			// locator.save();
//		} else {
//			renderFail("error_locator_not_exist", doc, error_locator_not_exist);
//		}
//		// renderSuccess("locator_unbind_success", doc, locator.name);
//		renderSuccess("locator_unbind_success", doc);
//	}
//
//	/**
//	 * ............
//	 * 
//	 * @param userName
//	 *            ...
//	 * @param password
//	 *            ..
//	 */
//	public static void getLocatorList(@Required String userName,
//			@Required String password) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//
//		Member member = memberCache.get();
//
//		List<Locator> locators = Locator.find("guardian1 = ? or guardian2 = ?",
//				member, member).fetch();
//		if (!locators.isEmpty()) {
//			Element getLocatorListRsp = doc.createElement("getLocatorListRsp");
//			Element count = doc.createElement("count");
//			doc.getDocumentElement().appendChild(getLocatorListRsp);
//			getLocatorListRsp.appendChild(count);
//			count.setTextContent("" + locators.size());
//			for (Locator l : locators) {
//				Element locator = doc.createElement("locator");
//				getLocatorListRsp.appendChild(locator);
//				Element locatorId = doc.createElement("locatorId");
//				locatorId.setTextContent("" + l.id);
//				locator.appendChild(locatorId);
//				Element name = doc.createElement("name");
//				name.setTextContent(l.name);
//				locator.appendChild(name);
//				Element phoneNumber = doc.createElement("phoneNumber");
//				phoneNumber.setTextContent(l.phoneNumber);
//				locator.appendChild(phoneNumber);
//				Element serialNumber = doc.createElement("serialNumber");
//				locator.appendChild(serialNumber);
//				serialNumber.setTextContent(l.serialNumber);
//				Element controlNumber = doc.createElement("controlNumber");
//				locator.appendChild(controlNumber);
//				controlNumber.setTextContent(l.controlNumber);
//				if (l.emergencyContact1 != null
//						&& !"".equals(l.emergencyContact1.trim())) {
//					Element emergencyContact1 = doc
//							.createElement("emergencyContact1");
//					locator.appendChild(emergencyContact1);
//					emergencyContact1.setTextContent(l.emergencyContact1);
//				}
//				if (l.emergencyContact2 != null
//						&& !l.emergencyContact2.trim().equals("")) {
//					Element emergencyContact2 = doc
//							.createElement("emergencyContact2");
//					locator.appendChild(emergencyContact2);
//					emergencyContact2.setTextContent(l.emergencyContact2);
//				}
//
//				if (l.guardian1Number != null
//						&& !l.guardian1Number.trim().equals("")) {
//					Element guardian1Number = doc.createElement("guardian1");
//					locator.appendChild(guardian1Number);
//					guardian1Number.setTextContent(l.guardian1Number);
//				}
//
//				if (l.guardian2Number != null
//						&& !l.guardian2Number.trim().equals("")) {
//					Element guardian2Number = doc.createElement("guardian2");
//					locator.appendChild(guardian2Number);
//					guardian2Number.setTextContent(l.guardian2Number);
//				}
//
//				Element warning = doc.createElement("warning");
//				locator.appendChild(warning);
//				warning.setTextContent("" + l.warning);
//
//				Element status = doc.createElement("status");
//				locator.appendChild(status);
//				status.setTextContent(l.confirmed ? "0" : "1");
//
//				// .........,.....
//				if (l.electronicFence != null) {// && l.electronicFence.on){
//												// ....2011/12/07
//					Element startLat = doc.createElement("startLat");
//					locator.appendChild(startLat);
//					startLat.setTextContent("" + l.electronicFence.latitude1);
//					Element endLat = doc.createElement("endLat");
//					locator.appendChild(endLat);
//					endLat.setTextContent("" + l.electronicFence.latitude2);
//					Element startLon = doc.createElement("startLon");
//					locator.appendChild(startLon);
//					startLon.setTextContent("" + l.electronicFence.longitude1);
//					Element endLon = doc.createElement("endLon");
//					locator.appendChild(endLon);
//					endLon.setTextContent("" + l.electronicFence.longitude2);
//					Element fenceMode = doc.createElement("fenceMode");
//					locator.appendChild(fenceMode);
//					fenceMode.setTextContent(l.electronicFence.in ? "0" : "1");
//					Element fenceTime = doc.createElement("fenceTime");
//					locator.appendChild(fenceTime);
//					fenceTime.setTextContent("" + l.electronicFence.dateTime);
//
//				}
//				// .........,.....
//				if (l.moveAlarm != null) {// && l.moveAlarm.switch_on){
//											// ....2011/12/07
//					Element originLat = doc.createElement("originLat");
//					locator.appendChild(originLat);
//					originLat.setTextContent("" + l.moveAlarm.latitude);
//
//					Element originLon = doc.createElement("originLon");
//					locator.appendChild(originLon);
//					originLon.setTextContent("" + l.moveAlarm.longitude);
//
//					Element radius = doc.createElement("radius");
//					locator.appendChild(radius);
//					radius.setTextContent("" + l.moveAlarm.radius);
//
//					Element alarmMode = doc.createElement("alarmMode");
//					locator.appendChild(alarmMode);
//					alarmMode.setTextContent(l.moveAlarm.switch_on ? "1" : "0");
//				}
//				Element mode = doc.createElement("mode");
//				locator.appendChild(mode);
//				mode.setTextContent("" + l.mode);
//
//				if (l.locatorSticker != null && l.locatorSticker.exists()) {
//					Element sticker = doc.createElement("locatorSticker");
//					locator.appendChild(sticker);
//					sticker.setTextContent("/download?id=" + l.id
//							+ "&userName=" + member.username + "&password="
//							+ member.password
//							+ "&fileID=locatorSticker&entity="
//							+ l.getClass().getName());
//				}
//
//			}
//		}
//		renderSuccess("get_locator_list_success", doc);
//	}
//
//	/**
//	 * .....
//	 * 
//	 * @param userName
//	 *            ...
//	 * @param password
//	 *            ..
//	 * @param locatorId
//	 *            ...id
//	 * @param name
//	 *            ..
//	 * @param emergencyContact1
//	 *            .....1
//	 * @param emergencyContact2
//	 *            .....2
//	 * @param guardian
//	 *            ....
//	 * @param warning
//	 *            ....
//	 */
//	public static void setLocator(@Required String userName,
//			@Required String password, @Required Long locatorId, String name,
//			String emergencyContact1, String emergencyContact2,
//			String guardian1, String guardian2, String warning) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//
//		Member member = memberCache.get();
//
//		Locator locator = Locator.findById(locatorId);
//		validateLocator(locator, member, doc);
//		// ...........
//		if (name != null)
//			locator.name = name;
//		if (emergencyContact1 != null)
//			locator.emergencyContact1 = emergencyContact1;
//		if (emergencyContact2 != null)
//			locator.emergencyContact2 = emergencyContact2;
//		if (warning != null) {
//			try {
//				locator.warning = Integer.parseInt(warning);
//			} catch (Exception e) {
//				renderFail("error_unknown_waring_format", doc,
//						error_unknown_waring_format);
//			}
//		}
//		if (guardian1 != null && !guardian1.trim().equals("")) {
//			locator.guardian1Number = guardian1;
//		}
//		if (guardian2 != null && !guardian2.trim().equals("")) {
//			locator.guardian2Number = guardian2;
//		}
//		locator.save();
//
//		// .........
//		// S25ConfirmMessage message = S25ConfirmMessage.find("byLocator",
//		// locator).first();
//		// if(message!=null){
//		// Element setLocatorRsp = doc.createElement("setLocatorRsp");
//		// doc.getDocumentElement().appendChild(setLocatorRsp);
//		// Element m_dateTime = doc.createElement("dateTime");
//		// m_dateTime.setTextContent(""+new
//		// SimpleDateFormat("yyyyMMdd'T'hhmmss'Z'").format(message.dateTime));
//		// setLocatorRsp.appendChild(m_dateTime);
//		// Element m_name = doc.createElement("name");
//		// m_name.setTextContent(""+message.name);
//		// setLocatorRsp.appendChild(m_name);
//		// Element m_emergencyContact1 = doc.createElement("emergencyContact1");
//		// m_emergencyContact1.setTextContent(""+message.emergencyContact1);
//		// setLocatorRsp.appendChild(m_emergencyContact1);
//		// Element m_emergencyContact2 = doc.createElement("emergencyContact2");
//		// m_emergencyContact2.setTextContent(""+message.emergencyContact2);
//		// setLocatorRsp.appendChild(m_emergencyContact2);
//		// Element m_warning = doc.createElement("warning");
//		// m_warning.setTextContent(""+message.warning);
//		// setLocatorRsp.appendChild(m_warning);
//		// }
//		// renderSuccess("locator_set_success", doc, locator.name);
//		renderSuccess("locator_set_success", doc);
//	}
//
//	/**
//	 * ......
//	 * 
//	 * @param userName
//	 * @param password
//	 * @param locatorId
//	 * @param mode
//	 * @param startTime
//	 * @param endTime
//	 * @param keepReadState
//	 * @param page
//	 * @param num
//	 */
//	public static void getLocation(@Required String userName,
//			@Required String password, @Required Long locatorId, Integer mode,
//			String startTime, String endTime, boolean keepReadState,Integer page,Integer num) {
//		long start = System.currentTimeMillis();int count=0;
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//		if (mode == null) {
//			mode = -1;
//		}
//
//		//...
//		int begin = 0;
//		if(num == null){
//			num = 100;
//		}
//		num = num > 100? 100:num;
//		if(page != null && page > 1){
//			begin = (page-1)*num;
//		}
//
//		Member member = memberCache.get();
//		Locator locator = Locator.findById(locatorId);
//		validateLocator(locator, member, doc);
//		
//		// ...........
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//		String startTimeDate = null;
//		String endTimeDate = null;
//		try {
//			if (startTime != null) {
//				startTimeDate = sdf.format(sdf.parse(startTime));
//			}
//			if (endTime != null) {
//				endTimeDate = sdf.format(sdf.parse(endTime));
//			}
//		} catch (ParseException e) {
//			renderFail("error_dateformat", doc, error_dateformat);
//		}
//
//		String sql = "";
//		String tableNum = getLocationTableNum(locator);
//		if (mode == 0) {
//			if (startTimeDate != null && endTimeDate != null) {
//				sql = "select * from "+tableNum+" where locator_id = "+locator.id+" and receivedTime >= '"+startTimeDate+"' and receivedTime <= '"+endTimeDate+"' and readed = 0 order by id desc limit " + begin + "," + num;
//			} else if (startTimeDate != null) {
//				sql = "select * from "+tableNum+" where locator_id = "+locator.id+" and receivedTime >= '"+startTimeDate+"' and readed = 0 order by id desc limit " + begin + "," + num;
//			} else {
//				if(page == null){
//					sql = "select * from "+tableNum+" where locator_id = "+ locator.id	+ " and readed = 0 order by id desc";//limit " + begin + "," + num;
//				}else{
//					sql = "select * from "+tableNum+" where locator_id = "+ locator.id	+ " and readed = 0 order by id desc limit " + begin + "," + num;
//				}
//			}
//		} else if (mode < 0) {
//			if (startTimeDate != null && endTimeDate != null) {
//				sql = "select * from "+tableNum+" where locator_id = "+ locator.id+ " and receivedTime >= '"+startTimeDate+"' and receivedTime <= '"+endTimeDate+"' order by id desc limit " + begin + "," + num;
//			} else if (startTimeDate != null) {
//				sql = "select * from "+tableNum+" where locator_id = "+ locator.id+ " and receivedTime >= '"+startTimeDate+"' order by id desc limit " + begin + "," + num;
//			} else {
//				if(page == null){
//					sql = "select * from "+tableNum+" where locator_id = "+ locator.id+" order by id desc";//+ " limit " + begin + "," + num;
//				}else{
//					sql = "select * from "+tableNum+" where locator_id = "+ locator.id+ " order by id desc limit " + begin + "," + num;
//				}
//			}
//		} else {
//			if(page == null){
//				sql = "select * from "+tableNum+" where locator_id = "+ locator.id + " order by id desc limit " + mode;
//			}else{
//				sql = "select * from "+tableNum+" where locator_id = "+ locator.id + " order by id desc limit " + begin + "," + num;
//			}
//		}
//		Connection conn = DB.getConnection();
//		Statement st = null;
//		PreparedStatement pst = null;
//		ResultSet rs = null;
//		
//		try {
//				conn.setAutoCommit(false);
//				conn.setTransactionIsolation(conn.TRANSACTION_READ_UNCOMMITTED);
//				st = conn.createStatement();
//				pst = conn.prepareStatement("update locations set readed = 1 where id = ?");
//				rs = st.executeQuery(sql);
//				long id;
//				double latitude;
//				double longitude;
//				double cell_latitude;
//				double cell_longitude;
//				int cell_accuracy;
//				int cell_coordinateType;
//				int speed;
//				int direction;
//				Date dateTime;
//				Date receivedTime;
//				String status;
//				boolean readed;
//				int mcc;
//				int mnc;
//				int lac;
//				int cellid;
//				int valid;
//				int latitudeFlag;
//				int longitudeFlag;
//				double latitudeForTest;
//				double longitudeForTest;
//				
//				while (rs.next()) {
//					id = rs.getLong("id");
//					latitude = rs.getDouble("latitude");
//					longitude = rs.getDouble("longitude");
//					cell_latitude = rs.getDouble("cell_latitude");
//					cell_longitude = rs.getDouble("cell_longitude");
//					cell_accuracy = rs.getInt("cell_accuracy");
//					cell_coordinateType = rs.getInt("cell_coordinateType");
//					speed = rs.getInt("speed");
//					direction = rs.getInt("direction");
//					dateTime = rs.getTimestamp("dateTime");
//					receivedTime = rs.getTimestamp("receivedTime");
//					status = rs.getString("status");
//					readed = rs.getBoolean("readed");
//					mcc = rs.getInt("mcc");
//					mnc = rs.getInt("mnc");
//					lac = rs.getInt("lac");
//					cellid = rs.getInt("cellid");
//					valid = rs.getInt("valid");
//					latitudeFlag = rs.getInt("latitudeFlag");
//					longitudeFlag = rs.getInt("longitudeFlag");
//					latitudeForTest = rs.getDouble("latitudeForTest");
//					longitudeForTest = rs.getDouble("longitudeForTest");
//					
//					Element m_location = doc.createElement("location");
//					doc.getDocumentElement().appendChild(m_location);
//
//					Element eleId = doc.createElement("id");
//					eleId.setTextContent("" + id);
//					m_location.appendChild(eleId);
//
//					Element eleMcc = doc.createElement("mcc");
//					eleMcc.setTextContent("" + mcc);
//					m_location.appendChild(eleMcc);
//					
//					Element eleMnc = doc.createElement("mnc");
//					eleMnc.setTextContent("" + mnc);
//					m_location.appendChild(eleMnc);
//					
//					Element eleLac = doc.createElement("lac");
//					eleLac.setTextContent("" + lac);
//					m_location.appendChild(eleLac);
//					
//					Element eleCellid = doc.createElement("cellid");
//					eleCellid.setTextContent("" + cellid);
//					m_location.appendChild(eleCellid);
//					
//					Element eleCell_latitude = doc.createElement("cell_latitude");
//					eleCell_latitude.setTextContent("" + cell_latitude);
//					m_location.appendChild(eleCell_latitude);
//					
//					Element eleCell_longitude = doc.createElement("cell_longitude");
//					eleCell_longitude.setTextContent("" + cell_longitude);
//					m_location.appendChild(eleCell_longitude);
//					
//					Element eleCell_accuracy = doc.createElement("cell_accuracy");
//					eleCell_accuracy.setTextContent("" + cell_accuracy);
//					m_location.appendChild(eleCell_accuracy);
//					
//					Element eleCell_coordinateType = doc
//							.createElement("cell_coordinateType");
//					eleCell_coordinateType.setTextContent(""
//							+ cell_coordinateType);
//					m_location.appendChild(eleCell_coordinateType);
//					
//					Element eleValid = doc.createElement("valid");
//					eleValid.setTextContent("" + (valid&3));
//					m_location.appendChild(eleValid);
//					
//					Element eleLatitudeFlag = doc.createElement("latitudeFlag");
//					eleLatitudeFlag.setTextContent("" + latitudeFlag);
//					m_location.appendChild(eleLatitudeFlag);
//					
//					Element eleLatitude = doc.createElement("latitude");
//					eleLatitude.setTextContent("" + latitude);
//					m_location.appendChild(eleLatitude);
//					
//					Element eleLongitudeFlag = doc.createElement("longitudeFlag");
//					eleLongitudeFlag.setTextContent("" + longitudeFlag);
//					m_location.appendChild(eleLongitudeFlag);
//					
//					Element eleLongitude = doc.createElement("longitude");
//					eleLongitude.setTextContent("" + longitude);
//					m_location.appendChild(eleLongitude);
//					
//					Element eleSpeed = doc.createElement("speed");
//					eleSpeed.setTextContent("" + speed);
//					m_location.appendChild(eleSpeed);
//					
//					Element eleDirection = doc.createElement("direction");
//					eleDirection.setTextContent("" + direction);
//					m_location.appendChild(eleDirection);
//					
//					Element eleDateTime = doc.createElement("dateTime");
//					eleDateTime.setTextContent(""
//							+ new SimpleDateFormat("yyyyMMdd'T'HHmmss")
//									.format(dateTime));
//					m_location.appendChild(eleDateTime);
//					
//					Element eleReceivedTime = doc.createElement("receivedTime");
//					eleReceivedTime.setTextContent(""
//							+ new SimpleDateFormat("yyyyMMdd'T'HHmmss")
//									.format(receivedTime));
//					m_location.appendChild(eleReceivedTime);
//					
//					Element eleStatus = doc.createElement("status");
//					eleStatus.setTextContent("" + status);
//					m_location.appendChild(eleStatus);
//
//					Element eleElectronicfence = doc.createElement("electronicfence");
//					if(locator.electronicFence != null && locator.electronicFence.on){
//						eleElectronicfence.setTextContent(""+((valid >> 4) & 3));
//					}else{
//						eleElectronicfence.setTextContent("0");
//					}
//					m_location.appendChild(eleElectronicfence);
//
//					if(locator.moveAlarm != null && locator.moveAlarm.switch_on && (valid >> 7) == 1){
//						Element eleMove_alarm = doc.createElement("move_alarm");
//						eleMove_alarm.setTextContent("0");
//						m_location.appendChild(eleMove_alarm);
//					}
//					// TODO .....GPS
//					Element eleLatitudeForTest = doc.createElement("latitudeForTest");
//					eleLatitudeForTest.setTextContent("" + latitudeForTest);
//					m_location.appendChild(eleLatitudeForTest);
//					
//					Element eleLongitudeForTest = doc.createElement("longitudeForTest");
//					eleLongitudeForTest.setTextContent("" + longitudeForTest);
//					m_location.appendChild(eleLongitudeForTest);
//					count++;
//					// .....
//					if (!keepReadState && !readed) {
//						pst.setLong(1, id);
//						pst.addBatch();
//					}
//				}
//				pst.executeBatch();
//				conn.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			DB.close();
//		}
//		long end = System.currentTimeMillis();
//		if(end - start >200){
//			Logger.info("Time:"+(end-start)+"  User:"+member.username+"  Count:"+count);
//		}
//		renderSuccess("location_get_success", doc);
//	}
//
//	/**
//	 * ....
//	 * 
//	 * @param userName
//	 *            ...
//	 * @param password
//	 *            ..
//	 * @param locatorId
//	 *            ...id
//	 * @param interval
//	 * @param count
//	 */
//	public static void track(@Required String userName,
//			@Required String password, @Required Long locatorId,
//			Integer interval, Integer count) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//		if (interval == null || interval < 0) {
//			interval = 30;
//		}
//		if (count == null) {
//			count = -1;
//		}
//
//		Member member = memberCache.get();
//
//		Locator locator = Locator.findById(locatorId);
//		validateLocator(locator, member, doc);
//		renderSuccess("locator_set_success", doc);
//	}
//
//	/**
//	 * .........setInterval.
//	 * 
//	 * @param userName
//	 * @param password
//	 * @param locatorId
//	 * @param interval
//	 * @param count
//	 */
//	public static void setInterval(@Required String userName,
//			@Required String password, @Required Long locatorId,
//			Integer interval, Integer count) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//		if (interval == null || interval < 10 || interval > 65535) {
//			interval = 10;
//		}
//
//		if (count == null || count < 5 || count > 65535) {
//			count = 5;
//		}
//
//		Member member = memberCache.get();
//
//		Locator locator = Locator.findById(locatorId);
//		validateLocator(locator, member, doc);
//
//		// .........
//		// G1ConfirmMessage message = G1ConfirmMessage.find("byLocator",
//		// locator).first();
//		// if(message!=null){
//		// Element setIntervalRsp = doc.createElement("setIntervalRsp");
//		// doc.getDocumentElement().appendChild(setIntervalRsp);
//		// Element m_dateTime = doc.createElement("dateTime");
//		// m_dateTime.setTextContent(""+new
//		// SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'").format(message.dateTime));
//		// setIntervalRsp.appendChild(m_dateTime);
//		// Element m_interval = doc.createElement("interval");
//		// m_interval.setTextContent(""+message.intervals);
//		// setIntervalRsp.appendChild(m_interval);
//		// Element m_count = doc.createElement("count");
//		// m_count.setTextContent(""+message.count);
//		// setIntervalRsp.appendChild(m_count);
//		// }
//
//		// renderSuccess("locator_set_success", doc, locator.name);
//		renderSuccess("locator_set_success", doc);
//	}
//
//	/**
//	 * ....
//	 * 
//	 * @param userName
//	 * @param password
//	 * @param locatorId
//	 */
//
//	public static void powerOff(@Required String userName,
//			@Required String password, @Required Long locatorId) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//
//		Member member = memberCache.get();
//		Locator locator = Locator.findById(locatorId);
//		validateLocator(locator, member, doc);
//
//		// .........
//		S1ConfirmMessage message = S1ConfirmMessage.find("byLocator", locator)
//				.first();
//		if (message != null) {
//			Element powerOffRsp = doc.createElement("powerOffRsp");
//			doc.getDocumentElement().appendChild(powerOffRsp);
//			Element m_dateTime = doc.createElement("dateTime");
//			m_dateTime.setTextContent(""
//					+ new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
//							.format(message.dateTime));
//			powerOffRsp.appendChild(m_dateTime);
//		}
//
//		// renderSuccess("locator_set_success", doc, locator.name);
//		renderSuccess("locator_set_success", doc);
//	}
//
//	/**
//	 * .....listen.
//	 * 
//	 * @param userName
//	 * @param password
//	 * @param locatorId
//	 */
//	public static void listen(@Required String userName,
//			@Required String password, @Required Long locatorId) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//
//		Member member = memberCache.get();
//		Locator locator = Locator.findById(locatorId);
//		validateLocator(locator, member, doc);
//		// renderSuccess("locator_set_success", doc, locator.name);
//		renderSuccess("locator_set_success", doc);
//	}
//
//	/**
//	 * ......
//	 * 
//	 * @param userName
//	 * @param password
//	 * @param locatorId
//	 * @param command
//	 *            ..S1,D1
//	 */
//	public static void getResponse(@Required String userName,
//			@Required String password, @Required Long locatorId,
//			@Required String command) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//		Member member = memberCache.get();
//		Locator locator = Locator.findById(locatorId);
//		validateLocator(locator, member, doc);
//
//		AbstractConfirmMessage message = null;
//		if (command.trim().equals("G1")) {
//			message = G1ConfirmMessage.find("byLocator", locator).first();
//		} else if (command.trim().equals("D1")) {
//			message = D1ConfirmMessage.find("byLocator", locator).first();
//		} else if (command.trim().equals("S1")) {
//			message = S1ConfirmMessage.find("byLocator", locator).first();
//		} else if (command.trim().equals("S2")) {
//			message = S2ConfirmMessage.find("byLocator", locator).first();
//		} else if (command.trim().equals("S23")) {
//			message = S23ConfirmMessage.find("byLocator", locator).first();
//		} else if (command.trim().equals("S25")) {
//			message = S25ConfirmMessage.find("byLocator", locator).first();
//		} else if (command.trim().equals("S28")) {
//			message = S28ConfirmMessage.find("byLocator", locator).first();
//		} else {
//			renderFail("error_unknown_command", doc, error_unknown_command);
//		}
//
//		if (message != null) {
//			Element getResponse = doc.createElement("getResponse");
//			doc.getDocumentElement().appendChild(getResponse);
//			Element m_command = doc.createElement("command");
//			Element m_dateTime = doc.createElement("dateTime");
//			Element m_param = doc.createElement("param");
//			m_command.setTextContent(command);
//			m_dateTime.setTextContent(""
//					+ new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
//							.format(message.dateTime));
//			m_param.setTextContent(message.message);
//			getResponse.appendChild(m_command);
//			getResponse.appendChild(m_dateTime);
//			getResponse.appendChild(m_param);
//		}
//		renderSuccess("get_response_success", doc);
//	}
//
//	/**
//	 * ......
//	 * 
//	 * @param userName
//	 * @param password
//	 */
//	public static void getUserInfo(@Required String userName,
//			@Required String password) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//		Member member = memberCache.get();
//
//		Element getUserInfoRsp = doc.createElement("getUserInfoRsp");
//		doc.getDocumentElement().appendChild(getUserInfoRsp);
//		Element m_name = doc.createElement("name");
//		Element m_password = doc.createElement("password");
//		Element m_phoneNumber = doc.createElement("phoneNumber");
//		Element m_email = doc.createElement("email");
//		Element m_birthday = doc.createElement("birthday");
//		Element m_gender = doc.createElement("gender");
//		Element m_clientSticker = doc.createElement("clientSticker");
//		if (member.birthday != null) {
//			java.text.DateFormat format1 = new java.text.SimpleDateFormat(
//					"yyyy-MM-dd");
//			m_birthday.setTextContent(format1.format(member.birthday));
//		}
//		m_gender.setTextContent(member.gender);
//		m_name.setTextContent(userName);
//		m_password.setTextContent(password);
//		m_phoneNumber.setTextContent(member.phoneNumber);
//		m_email.setTextContent(member.email);
//		if (member.clientSticker != null && member.clientSticker.exists()) {
//			m_clientSticker.setTextContent("/download?id=" + member.id
//					+ "&userName=" + member.username + "&password="
//					+ member.password + "&fileID=clientSticker&entity="
//					+ member.getClass().getName());
//		}
//
//		getUserInfoRsp.appendChild(m_name);
//		getUserInfoRsp.appendChild(m_password);
//		getUserInfoRsp.appendChild(m_phoneNumber);
//		getUserInfoRsp.appendChild(m_email);
//		getUserInfoRsp.appendChild(m_birthday);
//		getUserInfoRsp.appendChild(m_gender);
//		getUserInfoRsp.appendChild(m_clientSticker);
//
//		renderSuccess("get_userinfo_success", doc);
//	}
//
//	/**
//	 * ......
//	 * 
//	 * @param userName
//	 * @param password
//	 * @param newPassword
//	 *            ...
//	 * @param phoneNumber
//	 * @param email
//	 * @param clientSticker
//	 *            ....
//	 */
//	public static void modifyUserInfo(@Required String userName,
//			@Required String password, String newPassword, String phoneNumber,
//			String email, String gender, String birthday, Blob clientSticker) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//		Member member = memberCache.get();
//
//		if (newPassword != null && !newPassword.trim().equals("")) {
//			member.password = newPassword;
//		}
//
//		if (phoneNumber != null && !phoneNumber.trim().equals("")) {
//			member.phoneNumber = phoneNumber;
//		}
//
//		if (email != null && !email.trim().equals("")) {
//			member.email = email;
//		}
//
//		if (gender != null && !gender.trim().equals("")) {
//			member.gender = gender;
//		}
//
//		if (birthday != null) {
//			java.text.DateFormat format1 = new java.text.SimpleDateFormat(
//					"yyyy-MM-dd");
//
//			Date birthdayDate = null;
//			try {
//				birthdayDate = format1.parse(birthday);
//				member.birthday = birthdayDate;
//			} catch (ParseException e) {
//				// renderFail("error_dateformat",doc,error_dateformat,birthday);
//				renderFail("error_dateformat", doc, error_dateformat);
//			}
//		}
//		if (clientSticker != null) {
//			member.clientSticker = clientSticker;
//		}
//		member.save();
//
//		renderSuccess("modify_userinfo_success", doc);
//	}
//
//	/**
//	 * HTTP.....
//	 * 
//	 * @param userName
//	 * @param password
//	 * @param serialNumber
//	 * @param latitude
//	 * @param longitude
//	 */
//	public static void insertLocationData2(@Required String userName,
//			@Required String password, @Required String serialNumber,
//			@Required Double latitude, @Required Double longitude, String host,
//			String receivedTime) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//
//		DecimalFormat df = new DecimalFormat("#.0000");
//		// 2402DFDC1C3D 074659260911 30115485 00 121118003
//		// E000000FFFFFBFFFF001120FF0FFFFF6318FF58289F0
//		String signal = "24";
//
//		signal += "0" + Long.toHexString((Long) Long.parseLong(serialNumber));
//		signal += receivedTime;
//		signal += String.valueOf(df.format(latitude)).replace(".", "");
//		signal += "00";
//		signal += String.valueOf(df.format(longitude)).replace(".", "");
//		signal += "E000000FFFFFBFFFF000000FF0FFFFF0000FF00000F09";
//		TcpClient.setGPS(signal, host);
//
//	}
//
//	/**
//	 * ......
//	 * 
//	 * @param userName
//	 * @param password
//	 * @param locatorId
//	 * @param on
//	 * @param in
//	 * @param latitude1
//	 * @param longitude1
//	 * @param latitude2
//	 * @param longitude2
//	 */
//	public static void setElectronicFence(@Required String userName,
//			@Required String password, @Required Long locatorId,
//			@Required Boolean on, Boolean in, Double latitude1,
//			Double longitude1, Double latitude2, Double longitude2) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//		Member member = memberCache.get();
//		Locator locator = Locator.findById(locatorId);
//		validateLocator(locator, member, doc);
//
//		ElectronicFence ef = ElectronicFence.find("byLocator", locator).first();
//		if (ef == null) {
//			ef = new ElectronicFence();
//			ef.locator = locator;
//		}
//		ef.dateTime = new Date();
//		ef.on = on;
//		if (on) {
//			// .....
//			locator.warning = locator.warning | 2;
//			if (in != null) {
//				ef.in = in;
//			}
//			if (latitude1 != null) {
//				ef.latitude1 = latitude1;
//			}
//			if (longitude1 != null) {
//				ef.longitude1 = longitude1;
//			}
//			if (latitude2 != null) {
//				ef.latitude2 = latitude2;
//			}
//			if (longitude2 != null) {
//				ef.longitude2 = longitude2;
//			}
//
//		} else {
//			// .....
//			locator.warning = locator.warning & 0xFFFFFFFD;
//		}
//		ef.save();
//		Cache.delete("electronicFence_" + locator.id);
//		Cache.set("electronicFence_" + locator.id, ef);
//		renderSuccess("set_electronic_fence_success", doc);
//
//	}
//
//	/**
//	 * ......
//	 * 
//	 * @param userName
//	 * @param password
//	 * @param locatorId
//	 * @param on
//	 * @param latitude
//	 * @param longitude
//	 * @param radius
//	 */
//	public static void setMoveAlarm(@Required String userName,
//			@Required String password, @Required Long locatorId,
//			@Required Boolean on, Double latitude, Double longitude,
//			Double radius) {
//		Document doc = initResultJSON();
//		// ....
//		if (Validation.hasErrors()) {
//			renderFail("error_parameter_required", doc,
//					error_parameter_required);
//		}
//		Member member = memberCache.get();
//		Locator locator = Locator.findById(locatorId);
//		validateLocator(locator, member, doc);
//
//		MoveAlarm ma = MoveAlarm.find("byLocator", locator).first();
//		if (ma == null) {
//			ma = new MoveAlarm();
//			ma.locator = locator;
//		}
//		ma.switch_on = on;
//		if (on) {
//			// .....
//			locator.warning = locator.warning | 1;
//			// ........
//			if (latitude != null) {
//				ma.latitude = latitude;
//			}
//			if (longitude != null) {
//				ma.longitude = longitude;
//			}
//			if (radius != null) {
//				ma.radius = radius;
//			}
//		} else {
//			// .....
//			locator.warning = locator.warning & 0xFFFFFFFE;
//		}
//		ma.dateTime = new Date();
//		ma.save();
//		Cache.delete("moveAlarm_" + locator.id);
//		Cache.set("moveAlarm_" + locator.id, ma);
//		renderSuccess("set_move_alarm_success", doc);
//
//	}
//
	public static void clearCache(@Required String z) {
		JSONObject results = initResultJSON();
		// ....
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}

		Cache.clear();
		renderSuccess(results);

	}

	/**
	 * ......
	 * 
	 * @param username
	 *            ...
	 * @param password
	 *            ..
	 * @param sessionID
	 *            ..
	 * @param fileID
	 *            ..uuid
	 * @param fileName
	 *            ....
	 */
	public static void download(@Required String id, @Required String fileID, @Required String entity, @Required String z) {

		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		ObjectType type;
		try {
			type = new ObjectType(entity);
			notFoundIfNull(type);

			Model object = type.findById(id);
			notFoundIfNull(object);
			Object att = object.getClass().getField(fileID).get(object);
			if (att instanceof Model.BinaryField) {
				Model.BinaryField attachment = (Model.BinaryField) att;
				if (attachment == null || !attachment.exists()) {
					renderFail("error_download");
				}
				response.contentType = attachment.type();
				renderBinary(attachment.get(), attachment.length());
			}
		} catch (Exception e) {
			renderText("Download failed");
		}

		// // DEPRECATED
		// if (att instanceof play.db.jpa.FileAttachment) {
		// play.db.jpa.FileAttachment attachment = (play.db.jpa.FileAttachment)
		// att;
		// if (attachment == null || !attachment.exists()) {
		// renderText("THE FILE HAS BEEN LOST");
		// }
		// response.setHeader("Content-Disposition", "attachment; filename=\""
		// + encoder.encode(attachment.filename, "utf-8") + "\"");
		// renderBinary(attachment.get());
		// }
		renderFail("error_download");
	}

	/**
	 * .......
	 * 
	 * @param version
	 * @param type
	 * @throws JSONException 
	 */
	public static void update(@Required String version,
			@Required Integer type) {
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		ClientVersion cv = ClientVersion.find("byMobiletype", type).first();
		if (cv != null && !cv.version.equals(version)) {
			
			JSONObject results = initResultJSON();
			results.put("url", cv.url);
			results.put("update", cv.update_aiu);
			renderSuccess(results);
		} else {
			// ......
			renderFail("");
			
		}
	}


	protected static JSONObject initResultJSON() {
		return JSONUtil.getNewJSON();
	}
	
	protected static JSONArray initResultJSONArray() {
		return JSONUtil.getNewJSONArray();
	}


	protected static void renderSuccess(JSONObject results) {
		JSONObject jsonDoc = new JSONObject();
		jsonDoc.put("state", SUCCESS);
		jsonDoc.put("results",results);
		renderJSON(jsonDoc.toString());
	}

	protected static void renderFail(String key, Object... objects) {
		JSONObject jsonDoc = new JSONObject();
		jsonDoc.put("state", FAIL);
		jsonDoc.put("msg", Messages.get(key));
		renderJSON(jsonDoc.toString());
	}

}
