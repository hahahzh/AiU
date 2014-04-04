package controllers;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import job.RecordJob;
import models.CPkey;
import models.Carousel;
import models.CheckDigit;
import models.ClientVersion;
import models.Customer;
import models.EveryGame;
import models.FirmNew;
import models.Game;
import models.GameCarousel;
import models.GameDownloadCount;
import models.GameEvaluating;
import models.GameIcon;
import models.GameMessage;
import models.GameStrategy;
import models.GameType;
import models.IndexPage;
import models.LevelType;
import models.NativeNew;
import models.New;
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
import play.mvc.Http.Header;
import utils.Coder;
import utils.DateUtil;
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
	
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIVE = 5;
	
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
		}else{
//			if(!ONE.equals(s.sessionID) && !TWO.equals(s.sessionID)){
//				if(new Date().getTime() - s.data > 3600000){
//					s.delete();
//					renderFail("error_session_expired");
//				}
//			}
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
		
		try {
			String s = SendSMS.send(m, "您的验证码是：" + n + "。请不要把验证码泄露给其他人。");
			if(!"2".equals(s)){
				play.Logger.error("checkDigit: result="+s+" PNumber="+m+" digit="+n);
				renderText(s);
			}
			CheckDigit cd = new CheckDigit();
			cd.d = n;
			cd.updatetime = new Date().getTime();
			cd.m = m;
			cd._save();
		} catch (Exception e) {
			play.Logger.error("checkDigit: PNumber="+m+" digit="+n);
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
				RecordJob.logRecord.offer(arr[6]+","+"null"+","+"null"+","+arr[3]+","+"reg");
				renderSuccess(results);
			} else {
				// .......
				play.Logger.info("register:src");
				renderFail("error_username_already_used");
			}
		} catch (Exception e) {
			play.Logger.info("register:src");
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
				RecordJob.logRecord.offer(arr[6]+","+"null"+","+"null"+","+arr[3]+","+"reg2");
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

		if (type != null && type == 1 && (serialNumber == null || serialNumber.isEmpty())) {
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
		RecordJob.logRecord.offer(customer.m_number+","+imei+","+ip+","+mac+","+"login");
		renderSuccess(results);
	}
	
	public static void indexPage(@Required String z, String ios_t) {
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
		List<Carousel> adlistData = Carousel.find("mtype=? or mtype=3 order by id desc",customer.os).fetch(0,5);
		for(Carousel data:adlistData){
			JSONObject subad = initResultJSON();
			subad.put("id", data.id);
	        
	  	  if("每日一游&玩嗨周五".equals(data.ct.type)){
	  		EveryGame l = EveryGame.findById(data.ad_id);
	  		if(ios_t == null || ios_t.isEmpty() || "4".equals(ios_t)){
	  			subad.put("icon", "/c/download?id=" + l.id + "&fileID=picture&entity=" + l.getClass().getName() + "&z=" + z);
	  		}else if("5".equals(ios_t)){
	  			subad.put("icon", "/c/download?id=" + l.id + "&fileID=picture_ip5&entity=" + l.getClass().getName() + "&z=" + z);
	  		}
			subad.put("url", "/c/invite?num=5&page=1&z="+z);
			subad.put("type", l.type);
			subad.put("star", l.star);
			subad.put("data", l.data+"");
			subad.put("t_id", l.game.id);
			subad.put("t_type", "eg");
			subad.put("title", l.title);
	  	  } else if("游戏".equals(data.ct.type)){
	  		Game l = Game.findById(data.ad_id);
	  		if(ios_t == null || ios_t.isEmpty() || "4".equals(ios_t)){
	  			subad.put("icon", "/c/download?id=" + l.id + "&fileID=picture1&entity=" + l.getClass().getName() + "&z=" + z);
	  		}else if("5".equals(ios_t)){
	  			subad.put("icon", "/c/download?id=" + l.id + "&fileID=picture1_ip5&entity=" + l.getClass().getName() + "&z=" + z);
	  		}
			subad.put("url", "/c/gameinfo?id="+l.id+"&z="+z);
			subad.put("type", l.type);
			subad.put("star", l.star);
			subad.put("data", l.data+"");
			subad.put("t_id", l.id);
			subad.put("t_type", "g");
			subad.put("title", l.title);
	  	  }else if("新闻".equals(data.ct.type)){
	  		NativeNew l = NativeNew.findById(data.ad_id);
	  		if(ios_t == null || ios_t.isEmpty() || "4".equals(ios_t)){
	  			subad.put("icon", "/c/download?id=" + l.id + "&fileID=picture1&entity=" + l.getClass().getName() + "&z=" + z);
	  		}else if("5".equals(ios_t)){
	  			subad.put("icon", "/c/download?id=" + l.id + "&fileID=picture1_ip5&entity=" + l.getClass().getName() + "&z=" + z);
	  		}
			subad.put("url", "/c/newinfo?id="+l.id+"&z="+z);
			subad.put("data", l.data+"");
			subad.put("t_id", l.id);
			subad.put("t_type", "nn");
			subad.put("title", l.title);
	  	  }else if("礼包".equals(data.ct.type)){
	  		Pack l = Pack.findById(data.ad_id);
	  		if(ios_t == null || ios_t.isEmpty() || "4".equals(ios_t)){
	  			subad.put("icon", "/c/download?id=" + l.game.id + "&fileID=picture1&entity=models.Game&z=" + z);
	  		}else if("5".equals(ios_t)){
	  			subad.put("icon", "/c/download?id=" + l.game.id + "&fileID=picture1_ip5&entity=models.Game&z=" + z);
	  		}
			subad.put("url", "/c/package?num=5&page=1&z="+z);
			subad.put("data", l.data+"");
			subad.put("t_id", l.id);
			subad.put("t_type", "p");
			subad.put("title", l.title);
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
		play.Logger.info("getNews:num="+num+",time="+time+",page="+page+",type="+type+",gid="+gid+"z="+z);
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
		
		JSONArray newlist = initResultJSONArray();
		if(gid == null){
			List<NativeNew> newlistData = NativeNew.find("mtype = ? or mtype=3 order by id desc", c.os).fetch(page, num);
			for(NativeNew data:newlistData){
				JSONObject subad = initResultJSON();
				subad.put("id", data.id);
				subad.put("icon", "/c/download?id=" + data.id + "&fileID=icon&entity=" + data.getClass().getName() + "&z=" + z);
				subad.put("pic", "/c/download?id=" + data.id + "&fileID=picture1&entity=" + data.getClass().getName() + "&z=" + z);
				subad.put("title", data.title);
				subad.put("txt", data.describe_aiu);
				subad.put("hit", data.hit);
				subad.put("data", data.data);
				if(data.game != null){
					subad.put("g_id", gid);
					subad.put("t_type", "ge");
				}else{
					subad.put("t_type", "nn");
				}
				newlist.add(subad);
			}
		}else{
			if(type == null || type == 1){
				List<FirmNew> newlistData = FirmNew.find("(mtype = ? or mtype=3) and game_id = "+gid+" order by id desc", c.os).fetch(page, num);				
				for(FirmNew data:newlistData){
					JSONObject subad = initResultJSON();
					subad.put("id", data.id);
					subad.put("icon", "/c/download?id=" + data.game.id + "&fileID=icon&entity=models.Game&z=" + z);
					subad.put("pic", "/c/download?id=" + data.id + "&fileID=picture1&entity=" + data.getClass().getName() + "&z=" + z);
					subad.put("title", data.title);
					subad.put("txt", data.describe_aiu);
					subad.put("hit", data.hit);
					subad.put("data", data.data);
					subad.put("t_type", "n");
					if(data.game != null){
						subad.put("g_id", gid);
					}
					newlist.add(subad);
				}
			}else if(type == 2){
				List<GameStrategy> newlistData = GameStrategy.find("(mtype = ? or mtype=3) and game_id = "+gid+" order by id desc", c.os).fetch(page, num);
				for(GameStrategy data:newlistData){
					JSONObject subad = initResultJSON();
					subad.put("id", data.id);
					subad.put("icon", "/c/download?id=" + data.game.id + "&fileID=icon&entity=models.Game&z=" + z);
					subad.put("pic", "/c/download?id=" + data.id + "&fileID=picture1&entity=" + data.getClass().getName() + "&z=" + z);
					subad.put("title", data.title);
					subad.put("txt", data.describe_aiu);
					subad.put("hit", data.hit);
					subad.put("data", data.data);
					subad.put("t_type", "gs");
					if(data.game != null){
						subad.put("g_id", gid);
					}
					newlist.add(subad);
				}
			}else if(type == 3){
				List<GameEvaluating> newlistData = GameEvaluating.find("(mtype = ? or mtype=3) and game_id = "+gid+" order by id desc", c.os).fetch(page, num);
				for(GameEvaluating data:newlistData){
					JSONObject subad = initResultJSON();
					subad.put("id", data.id);
					subad.put("icon", "/c/download?id=" + data.game.id + "&fileID=icon&entity=models.Game&z=" + z);
					subad.put("pic", "/c/download?id=" + data.id + "&fileID=picture1&entity=" + data.getClass().getName() + "&z=" + z);
					subad.put("title", data.title);
					subad.put("txt", data.describe_aiu);
					subad.put("hit", data.hit);
					subad.put("data", data.data);
					subad.put("t_type", "ge");
					if(data.game != null){
						subad.put("g_id", gid);
					}
					newlist.add(subad);
				}
			}
		}
		results.put("newlist", newlist);
		renderSuccess(results);
	}
	
	public static void getEvaluatings(int num, long time, int page, Long gid, @Required String z, String ios_t) {
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
		
		JSONArray newlist = initResultJSONArray();
		
		String condition = "";
		if(gid != null){
			condition = "and game_id = "+gid;
		}
		List<GameEvaluating> newlistData = GameEvaluating.find("(mtype = ? or mtype=3) "+condition+" order by id desc", c.os).fetch(page, num);
		for(GameEvaluating data:newlistData){
			JSONObject subad = initResultJSON();
			subad.put("id", data.id);
			subad.put("icon", "/c/download?id=" + data.game.id + "&fileID=icon&entity=models.Game&z=" + z);
			if(ios_t == null || ios_t.isEmpty() || "4".equals(ios_t)){
	  			subad.put("pic", "/c/download?id=" + data.id + "&fileID=picture1&entity=" + data.getClass().getName() + "&z=" + z);
	  		}else if("5".equals(ios_t)){
	  			subad.put("pic", "/c/download?id=" + data.id + "&fileID=picture1_ip5&entity=" + data.getClass().getName() + "&z=" + z);
	  		}			
			subad.put("title", data.title);
			subad.put("txt", data.describe_aiu);
			subad.put("hit", data.hit);
			subad.put("data", data.data);
			subad.put("t_type", "ge");
			subad.put("g_id", data.game.id);
			subad.put("game_title", data.game.title);
			subad.put("star", data.game.star+"");
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
		
		JSONObject subad1 = initResultJSON();
		subad1.put("id", "1");
		subad1.put("name", "游戏新闻");
		newstypes.add(subad1);
		
		JSONObject subad2 = initResultJSON();
		subad2.put("id", "2");
		subad2.put("name", "攻略");
		newstypes.add(subad2);
		
		JSONObject subad3 = initResultJSON();
		subad3.put("id", "3");
		subad3.put("name", "评测");
		newstypes.add(subad3);

		results.put("newstypes", newstypes);
		renderSuccess(results);
	}
	
	private static JSONArray setJson(JSONArray jsonarr, New data, String z){
		
		JSONObject img1 = initResultJSON();
		if(data.picture1.exists()){
			img1.put("picture", "/c/download?id=" + data.id + "&fileID=picture1&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(!data.txt1.isEmpty()){
			img1.put("txt", data.txt1);
		}
		if(!img1.isNullObject() && !img1.isEmpty()){
			jsonarr.add(img1);
		}
		
		JSONObject img2 = initResultJSON();
		if(data.picture2.exists()){
			img2.put("picture", "/c/download?id=" + data.id + "&fileID=picture2&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(!data.txt2.isEmpty()){
			img2.put("txt", data.txt2);
		}
		if(!img2.isNullObject() && !img2.isEmpty()){
			jsonarr.add(img2);
		}
		
		JSONObject img3 = initResultJSON();
		if(data.picture3.exists()){
			img3.put("picture", "/c/download?id=" + data.id + "&fileID=picture3&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(!data.txt3.isEmpty()){
			img3.put("txt", data.txt3);
		}
		if(!img3.isNullObject() && !img3.isEmpty()){
			jsonarr.add(img3);
		}
		
		JSONObject img4 = initResultJSON();
		if(data.picture4.exists()){
			img4.put("picture", "/c/download?id=" + data.id + "&fileID=picture4&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(!data.txt4.isEmpty()){
			img4.put("txt", data.txt4);
		}
		if(!img4.isNullObject() && !img4.isEmpty()){
			jsonarr.add(img4);
		}
		
		JSONObject img5 = initResultJSON();
		if(data.picture5.exists()){
			img5.put("picture", "/c/download?id=" + data.id + "&fileID=picture5&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(!data.txt5.isEmpty()){
			img5.put("txt", data.txt5);
		}
		if(!img5.isNullObject() && !img5.isEmpty()){
			jsonarr.add(img5);
		}
		
		JSONObject img6 = initResultJSON();
		if(data.picture6.exists()){
			img6.put("picture", "/c/download?id=" + data.id + "&fileID=picture6&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(!data.txt6.isEmpty()){
			img6.put("txt", data.txt6);
		}
		if(!img6.isNullObject() && !img6.isEmpty()){
			jsonarr.add(img6);
		}
		
		JSONObject img7 = initResultJSON();
		if(data.picture7.exists()){
			img7.put("picture", "/c/download?id=" + data.id + "&fileID=picture7&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(!data.txt7.isEmpty()){
			img7.put("txt", data.txt7);
		}
		if(!img7.isNullObject() && !img7.isEmpty()){
			jsonarr.add(img7);
		}
		
		JSONObject img8 = initResultJSON();
		if(data.picture8.exists()){
			img8.put("picture", "/c/download?id=" + data.id + "&fileID=picture8&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(!data.txt8.isEmpty()){
			img8.put("txt", data.txt8);
		}
		if(!img8.isNullObject() && !img8.isEmpty()){
			jsonarr.add(img8);
		}
		
		JSONObject img9 = initResultJSON();
		if(data.picture9.exists()){
			img9.put("picture", "/c/download?id=" + data.id + "&fileID=picture9&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(!data.txt9.isEmpty()){
			img9.put("txt", data.txt9);
		}
		if(!img9.isNullObject() && !img9.isEmpty()){
			jsonarr.add(img9);
		}
		
		JSONObject img10 = initResultJSON();
		if(data.picture10.exists()){
			img10.put("picture", "/c/download?id=" + data.id + "&fileID=picture10&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(!data.txt10.isEmpty()){
			img10.put("txt", data.txt10);
		}
		if(!img10.isNullObject() && !img10.isEmpty()){
			jsonarr.add(img10);
		}
		
		//11--25
		JSONObject img11 = initResultJSON();
		if(data.picture11.exists()){
			img11.put("picture", "/c/download?id=" + data.id + "&fileID=picture11&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt11.isEmpty()){
			img1.put("txt", data.txt11);
		}
		if(!img11.isNullObject() && !img11.isEmpty()){
			jsonarr.add(img11);
		}
		
		JSONObject img12 = initResultJSON();
		if(data.picture12.exists()){
			img12.put("picture", "/c/download?id=" + data.id + "&fileID=picture12&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt12.isEmpty()){
			img12.put("txt", data.txt12);
		}
		if(!img12.isNullObject() && !img12.isEmpty()){
			jsonarr.add(img12);
		}
		
		JSONObject img13 = initResultJSON();
		if(data.picture13.exists()){
			img13.put("picture", "/c/download?id=" + data.id + "&fileID=picture13&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt13.isEmpty()){
			img13.put("txt", data.txt13);
		}
		if(!img13.isNullObject() && !img13.isEmpty()){
			jsonarr.add(img13);
		}
		
		JSONObject img14 = initResultJSON();
		if(data.picture14.exists()){
			img14.put("picture", "/c/download?id=" + data.id + "&fileID=picture14&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt14.isEmpty()){
			img14.put("txt", data.txt14);
		}
		if(!img14.isNullObject() && !img14.isEmpty()){
			jsonarr.add(img14);
		}
		
		JSONObject img15 = initResultJSON();
		if(data.picture15.exists()){
			img15.put("picture", "/c/download?id=" + data.id + "&fileID=picture15&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt15.isEmpty()){
			img15.put("txt", data.txt15);
		}
		if(!img15.isNullObject() && !img15.isEmpty()){
			jsonarr.add(img15);
		}
		
		JSONObject img16 = initResultJSON();
		if(data.picture16.exists()){
			img16.put("picture", "/c/download?id=" + data.id + "&fileID=picture16&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt16.isEmpty()){
			img16.put("txt", data.txt16);
		}
		if(!img16.isNullObject() && !img16.isEmpty()){
			jsonarr.add(img16);
		}
		
		JSONObject img17 = initResultJSON();
		if(data.picture17.exists()){
			img17.put("picture", "/c/download?id=" + data.id + "&fileID=picture17&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt17.isEmpty()){
			img17.put("txt", data.txt17);
		}
		if(!img17.isNullObject() && !img17.isEmpty()){
			jsonarr.add(img17);
		}
		
		JSONObject img18 = initResultJSON();
		if(data.picture18.exists()){
			img18.put("picture", "/c/download?id=" + data.id + "&fileID=picture18&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt18.isEmpty()){
			img18.put("txt", data.txt18);
		}
		if(!img18.isNullObject() && !img18.isEmpty()){
			jsonarr.add(img18);
		}
		
		JSONObject img19 = initResultJSON();
		if(data.picture19.exists()){
			img19.put("picture", "/c/download?id=" + data.id + "&fileID=picture19&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt19.isEmpty()){
			img19.put("txt", data.txt19);
		}
		if(!img19.isNullObject() && !img19.isEmpty()){
			jsonarr.add(img19);
		}
		
		JSONObject img20 = initResultJSON();
		if(data.picture20.exists()){
			img20.put("picture", "/c/download?id=" + data.id + "&fileID=picture20&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt20.isEmpty()){
			img20.put("txt", data.txt20);
		}
		if(!img20.isNullObject() && !img20.isEmpty()){
			jsonarr.add(img20);
		}
		
		JSONObject img21 = initResultJSON();
		if(data.picture21.exists()){
			img21.put("picture", "/c/download?id=" + data.id + "&fileID=picture21&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt21.isEmpty()){
			img21.put("txt", data.txt21);
		}
		if(!img21.isNullObject() && !img21.isEmpty()){
			jsonarr.add(img21);
		}
		
		JSONObject img22 = initResultJSON();
		if(data.picture22.exists()){
			img22.put("picture", "/c/download?id=" + data.id + "&fileID=picture22&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt22.isEmpty()){
			img22.put("txt", data.txt22);
		}
		if(!img22.isNullObject() && !img22.isEmpty()){
			jsonarr.add(img22);
		}
		
		JSONObject img23 = initResultJSON();
		if(data.picture23.exists()){
			img23.put("picture", "/c/download?id=" + data.id + "&fileID=picture23&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt23.isEmpty()){
			img23.put("txt", data.txt23);
		}
		if(!img23.isNullObject() && !img23.isEmpty()){
			jsonarr.add(img23);
		}
		
		JSONObject img24 = initResultJSON();
		if(data.picture24.exists()){
			img24.put("picture", "/c/download?id=" + data.id + "&fileID=picture24&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt24.isEmpty()){
			img24.put("txt", data.txt24);
		}
		if(!img24.isNullObject() && !img24.isEmpty()){
			jsonarr.add(img24);
		}
		
		JSONObject img25 = initResultJSON();
		if(data.picture25.exists()){
			img25.put("picture", "/c/download?id=" + data.id + "&fileID=picture25&entity=" + data.getClass().getName() + "&z=" + z);
		}
		if(data.txt11 != null && !data.txt25.isEmpty()){
			img25.put("txt", data.txt25);
		}
		if(!img25.isNullObject() && !img25.isEmpty()){
			jsonarr.add(img25);
		}
		return jsonarr;
	}
	//游戏新闻
	public static void getNewInfo(@Required Long id, Long gid, @Required String z) {
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
		
		New data = null;
		if(gid == null){
			data = NativeNew.findById(id);
		}else{
			data = FirmNew.findById(id);
		}
		
		JSONObject newinfo = initResultJSON();
		if(data != null){
			newinfo.put("id", data.id);
			if(data instanceof NativeNew){
				newinfo.put("icon", "/c/download?id=" + data.id + "&fileID=icon&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				FirmNew d = (FirmNew)data;
				newinfo.put("icon", "/c/download?id=" + d.game.id + "&fileID=icon&entity=models.Game&z=" + z);
			}
			newinfo.put("title", data.title);
			newinfo.put("author", data.author);
			newinfo.put("res", data.res);
			newinfo.put("hit", data.hit);
			newinfo.put("data", data.data);
			
			JSONArray jsonarr = initResultJSONArray();

			jsonarr = setJson(jsonarr, data, z);
			
			newinfo.put("imgs", jsonarr);
			data.hit++;
			data.save();
		}
		results.put("newinfo", newinfo);
		renderSuccess(results);
	}
	
	//游戏攻略
	public static void getGameStrategyInfo(@Required Long id, @Required String z) {
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
		
		GameStrategy data = GameStrategy.findById(id);
		
		JSONObject newinfo = initResultJSON();
		if(data != null){
			newinfo.put("id", data.id);
			newinfo.put("icon", "/c/download?id=" + data.game.id + "&fileID=icon&entity=models.Game&z=" + z);
			newinfo.put("title", data.title);
			newinfo.put("author", data.author);
			newinfo.put("res", data.res);
			newinfo.put("hit", data.hit);
			newinfo.put("data", data.data);
			
			JSONArray jsonarr = initResultJSONArray(); 
			
			jsonarr = setJson(jsonarr, data, z);
			
			newinfo.put("imgs", jsonarr);
			data.hit++;
			data.save();
		}
		results.put("newinfo", newinfo);

		renderSuccess(results);
	}
	
	//游戏评测
	public static void getGameEvaluatingInfo(@Required Long id, @Required String z) {
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
		
		GameEvaluating data = GameEvaluating.findById(id);
		JSONObject newinfo = initResultJSON();
		if(data != null){
			
			newinfo.put("id", data.id);
			newinfo.put("icon", "/c/download?id=" + data.game.id + "&fileID=icon&entity=models.Game&z=" + z);
			newinfo.put("title", data.title);
			newinfo.put("author", data.author);
			newinfo.put("res", data.res);
			newinfo.put("hit", data.hit);
			newinfo.put("data", data.data);
			newinfo.put("describe_aiu", data.describe_aiu);
			
			JSONArray jsonarr = initResultJSONArray();
			
			jsonarr = setJson(jsonarr, data, z);
			
			newinfo.put("imgs", jsonarr);
			newinfo.put("g_id", data.game.id);
			newinfo.put("g_title", data.game.title);
			newinfo.put("g_star", data.game.star);
			data.hit++;
			data.save();
		}
		results.put("newinfo", newinfo);
	
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
		List<EveryGame> listData = EveryGame.find("(mtype = ? or mtype=3) and everygametype_id=1 order by id desc", c.os).fetch(page,num);
		for(EveryGame data:listData){
			JSONObject subad = initResultJSON();
			FirmNew survey = FirmNew.find("(mtype = ? or mtype=3) and game_id=? order by id desc", c.os, data.game).first();
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
	public static void gameCarosel(@Required Long id, @Required String z, String ios_t) {
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
		
		GameCarousel data = GameCarousel.find("(mtype=? or mtype=3) and game_id=? order by id desc",customer.os,id).first();
		if(data == null) renderFail("error_game_deleted");
		results.put("id", data.id);
		results.put("g_id", data.game.id);
		
		JSONArray adlistArr = initResultJSONArray();
		if(data.ct != null && data.ad_id != null){
			adlistArr.add(setGameCarosel(data.ct.type,data.ad_id, z, ios_t, data.game.id));
		}
		if(data.ct2 != null && data.ad_id2 != null){
			adlistArr.add(setGameCarosel(data.ct2.type,data.ad_id2, z, ios_t, data.game.id));
		}
		if(data.ct3 != null && data.ad_id3 != null){
			adlistArr.add(setGameCarosel(data.ct3.type,data.ad_id3, z, ios_t, data.game.id));
		}   
		if(data.ct4 != null && data.ad_id4 != null){
			adlistArr.add(setGameCarosel(data.ct4.type,data.ad_id4, z, ios_t, data.game.id));
		}
		if(data.ct5 != null && data.ad_id5 != null){
			adlistArr.add(setGameCarosel(data.ct5.type,data.ad_id5, z, ios_t, data.game.id));
		}
		if(data.ct6 != null && data.ad_id6 != null){
			adlistArr.add(setGameCarosel(data.ct6.type,data.ad_id6, z, ios_t, data.game.id));
		}
		if(data.ct7 != null && data.ad_id7 != null){
			adlistArr.add(setGameCarosel(data.ct7.type,data.ad_id7, z, ios_t, data.game.id));
		}
		if(data.ct8 != null && data.ad_id8 != null){
			adlistArr.add(setGameCarosel(data.ct8.type,data.ad_id8, z, ios_t, data.game.id));
		}
		
		switch (customer.os){
			case ONE : results.put("downloadurl", data.game.downloadurl1);
			break;
			case TWO : results.put("downloadurl", data.game.downloadurl2);
			break;
			case THREE : results.put("downloadurl", data.game.downloadurl3);
			break;
			case FOUR : results.put("downloadurl", data.game.downloadurl4);
			break;
			case FIVE : results.put("downloadurl", data.game.downloadurl5);
			break;
		}
	
		results.put("adlist", adlistArr);

		renderSuccess(results);
	}
	
	private static JSONObject setGameCarosel(String type, Long ad_id, String z, String ios_t, Long gid){
		JSONObject subad = new JSONObject();
	  	  if("新闻".equals(type)){
	  		FirmNew l = FirmNew.findById(ad_id);
	  		if(ios_t == null || ios_t.isEmpty() || "4".equals(ios_t)){
	  			subad.put("icon", "/c/download?id=" + l.id + "&fileID=picture1&entity=" + l.getClass().getName() + "&z=" + z);
	  		}else if("5".equals(ios_t)){
	  			subad.put("icon", "/c/download?id=" + l.id + "&fileID=picture1_ip5&entity=" + l.getClass().getName() + "&z=" + z);
	  		}
			subad.put("url", "/c/newinfo?id="+l.id+"&gid="+gid+"&z="+z);
			subad.put("data", l.data+"");
			subad.put("t_id", l.id);
			subad.put("t_type", "n");
			subad.put("title", l.title);
	  	  }else if("礼包".equals(type)){
	  		Pack l = Pack.findById(ad_id);
		  	subad.put("icon", "/c/download?id=" + l.id + "&fileID=icon&entity=" + l.getClass().getName() + "&z=" + z);
			subad.put("url", "/c/package?num=5&page=1&gid="+gid+"&z="+z);
			subad.put("data", l.data+"");
			subad.put("t_id", l.id);
			subad.put("t_type", "p");
			subad.put("title", l.title);
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
			sTmp = " and g.title like '%" + skey + "%' ";
		}
		if(gid != null){
			sTmp += " and g.id = "+gid;
		}

		List<Game> listData = Game.find("select g from Game g, Pack p where p.game = g.id and g.mtype="+ c.os + sTmp + " GROUP BY g.id order by p.ranking desc,p.id desc").fetch(page, num);		
		for(Game data:listData){
			JSONObject subad = initResultJSON();
			subad.put("id", data.id);
			subad.put("game_id", data.id);
			subad.put("icon", "/c/download?id=" + data.id + "&fileID=icon&entity=" + data.getClass().getName() + "&z=" + z);
			if(data.rankingicon.exists()){
				subad.put("pic", "/c/download?id=" + data.id + "&fileID=rankingicon&entity=" + data.getClass().getName() + "&z=" + z);
			}
			subad.put("title", data.title);
			subad.put("star", data.star+"");
			subad.put("data", data.data);
			
			packagelist.add(subad);
		}
		results.put("packagelist", packagelist);
		
		renderSuccess(results);
	}
	
	public static void packageInfo(@Required Long gid, @Required String z) throws ParseException {
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
		
		List<Pack> l = Pack.find("game_id = ?", gid).fetch();
		
		JSONArray packagelist = initResultJSONArray();
		for(Pack data : l){
			JSONObject subad = initResultJSON();
			subad.put("id", data.id);
			subad.put("icon", "/c/download?id=" + data.id + "&fileID=icon&entity=" + data.getClass().getName() + "&z=" + z);
			//TODO
//				subad.put("url", data.ad_game.);
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
			packagelist.add(subad);
		}
		results.put("packinfo", packagelist);
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
		List<EveryGame> listData = EveryGame.find("(mtype = ? or mtype=3) and everygametype_id=2 order by id desc", c.os).fetch(page, num);
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
		List<Game> listData = Game.find("mtype = ? or mtype=3 order by ranking desc,id desc", c.os).fetch();
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
			switch (c.os){
				case ONE : results.put("downloadurl", data.downloadurl1);
				break;
				case TWO : results.put("downloadurl", data.downloadurl2);
				break;
				case THREE : results.put("downloadurl", data.downloadurl3);
				break;
				case FOUR : results.put("downloadurl", data.downloadurl4);
				break;
				case FIVE : results.put("downloadurl", data.downloadurl5);
				break;
			}
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
		switch (c.os){
			case ONE : results.put("downloadurl", data.downloadurl1);
			break;
			case TWO : results.put("downloadurl", data.downloadurl2);
			break;
			case THREE : results.put("downloadurl", data.downloadurl3);
			break;
			case FOUR : results.put("downloadurl", data.downloadurl4);
			break;
			case FIVE : results.put("downloadurl", data.downloadurl5);
			break;
		}
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
	
	public static void addComment(Long id, String type, @Required String msg, @Required String z) {
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
				gm.news = FirmNew.findById(id);
			}else if("nn".equals(type)){
				gm.nativeNew = NativeNew.findById(id);
			}else if("ge".equals(type)){
				gm.gameEvaluating = GameEvaluating.findById(id);
			}else if("gs".equals(type)){
				gm.gameStrategy = GameStrategy.findById(id);
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
		}else if("nn".equals(type)){
			sql = "nativeNew_id="+id;
		}else if("ge".equals(type)){
			sql = "gameEvaluating_id="+id;
		}else if("gs".equals(type)){
			sql = "gameStrategy_id="+id;
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
				subad.put("portrait", "/c/download?id=" + data.c.id + "&fileID=portrait&entity=models.Customer&z=" + z);
			}else{
				if(c.gender == null || c.gender == 0){
					subad.put("portrait", "/public/images/boy.jpg");
				}else{
					subad.put("portrait", "/public/images/girl.jpg");
				}
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
			subad.put("rankingicon", "/c/download?id=" + pg.game.id + "&fileID=rankingicon&entity=models.Game&z=" + z);
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
			gamearray.add("/public/images/btnGame_bg_hover1.png");
			gamearray.add("/public/images/btnGame_bg_hover2.png");
			gamearray.add("/public/images/btnGame_bg_hover3.png");
			gamearray.add("/public/images/btnGame_bg_hover4.png");
			gamearray.add("/public/images/btnGame_bg_hover5.png");
			gamearray.add("/public/images/btnGame_bg_hover6.png");
			gamearray.add("/public/images/gamebackgroundimg.jpg");
		}else{
			gameicon.put("id", data.id);
			if(data.picture1_g_icon.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=picture1_g_icon&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/btnGame_bg_hover1.png");
			}
			if(data.picture2_g_icon.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=picture2_g_icon&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/btnGame_bg_hover2.png");
			}
			if(data.picture3_g_icon.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=picture3_g_icon&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/btnGame_bg_hover3.png");
			}
			if(data.picture4_g_icon.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=picture4_g_icon&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/btnGame_bg_hover4.png");
			}
			if(data.picture5_g_icon.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=picture5_g_icon&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/btnGame_bg_hover5.png");
			}
			if(data.picture6_g_icon.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=picture6_g_icon&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/btnGame_bg_hover6.png");
			}
			if(data.backgroundpicture.exists()){
				gamearray.add("/c/download?id=" + data.id + "&fileID=backgroundpicture&entity=" + data.getClass().getName() + "&z=" + z);
			}else{
				gamearray.add("/public/images/gamebackgroundimg.jpg");
			}
		}
		
		gameicon.put("icons", gamearray);

		results.put("gameicon", gameicon);
		renderSuccess(results);
	}
	
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
			if(c.portrait.exists()){
				c.portrait.getFile().delete();
			}
			c.portrait = portrait;
		}
		c.save();
		renderSuccess(initResultJSON());
	}

	/**
	 */
	public static void getMemberInfo(@Required String z) {
		
		if (Validation.hasErrors()) {
			renderFail("error_parameter_required");
		}
		
		Session s = sessionCache.get();
		if(s == null){
			renderFail("error_session_expired");
		}
		
		Customer c = s.customer;
		JSONObject results = initResultJSON();
	
		results.put("exp", c.exp);
		results.put("level", c.lv.level_name);
		results.put("phonenumber", c.m_number);
		results.put("nickname", c.nickname);
		results.put("type", c.type);
		if(c.vid != null){
			results.put("version", c.vid.version);
		}
		if(c.portrait != null && c.portrait.exists()){
			results.put("portrait", "/c/download?id=" + c.id + "&fileID=portrait&entity=" + c.getClass().getName() + "&z=" + z);
		}else{
			if(c.gender == null || c.gender == 0){
				results.put("portrait", "/public/images/boy.jpg");
			}else{
				results.put("portrait", "/public/images/girl.jpg");
			}
		}
		renderSuccess(results);
	}

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
				long p = 0;
				Header h = request.headers.get("Range");
				play.Logger.info("download header:", h);
				if(h != null){
					p = Long.parseLong(h.value().replaceAll("bytes=", "").replaceAll("-", ""));
				}
				play.Logger.info("download header:", p);
				response.contentType = attachment.type();
				if(p > 0){
					renderBinary(attachment.get(), attachment.get().skip(p));
				}else{
					renderBinary(attachment.get(), attachment.length());
				}
				
			}
		} catch (Exception e) {
			renderText("Download failed");
		}
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
