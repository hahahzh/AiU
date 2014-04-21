package controllers;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import models.AdminManagement;
import models.CarouselType;
import models.FirmNew;
import models.Game;
import models.GameCarousel;
import models.Pack;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;
import utils.JSONUtil;

@Check("admin")
@With(Secure.class)
public class GameCarousels extends CRUD {
	
	public static void getEntityList(String id, String selval, Long gid, Integer mType) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		GameCarousel object = (GameCarousel)type.findById(id);
		notFoundIfNull(object);
	        
		
		JSONObject results = JSONUtil.getNewJSON();
		JSONArray jsonArr = JSONUtil.getNewJSONArray();
		if("4".equals(selval)){
			List<FirmNew> l = FirmNew.find("game_id=? and (mtype=? or mtype=3)", gid, mType).fetch();
			for(FirmNew fn : l){
				JSONObject subdata = JSONUtil.getNewJSON();
				subdata.put("id", fn.id);
				subdata.put("title", fn.title);
				jsonArr.add(subdata);
			}
		}else if("5".equals(selval)){ 
			List<Pack> l = Pack.find("game_id=? and (mtype=? or mtype=3)", gid, mType).fetch();
			for(Pack fn : l){
				JSONObject subdata = JSONUtil.getNewJSON();
				subdata.put("id", fn.id);
				subdata.put("title", fn.title);
				jsonArr.add(subdata);
			}
		}
		
		try {
			if(jsonArr.size()==0){
				results.put("state", "0");
				results.put("msg", "No found data.");
			}else{
				results.put("state", "1");
				results.put("results", jsonArr);
			}
			renderJSON(results);
        } catch (TemplateNotFoundException e) {
        	results.put("state", "0");
        	results.put("msg", "System Error");
        	renderJSON(results);
        }
    }
	  
	public static List<CarouselType> findSimpleCarouselType() {
		return CarouselType.find("id = 4 or id = 5").fetch(); 
	}
	
	public static void save(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        GameCarousel object = null;
        if(id == null || id.isEmpty()){
            Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            object = (GameCarousel) constructor.newInstance();
        }else{
        	object = (GameCarousel)type.findById(id);
        }
        notFoundIfNull(object);
        Binder.bindBean(params.getRootParamNode(), "object", object);
        if(params.get("selName1") != null && !params.get("selName1").isEmpty()){
        	object.ct1 = CarouselType.findById(Long.parseLong(params.get("selName1")));
        }
        if(params.get("selName2") != null && !params.get("selName2").isEmpty()){
        	object.ct2 = CarouselType.findById(Long.parseLong(params.get("selName2")));
        }
        if(params.get("selName3") != null && !params.get("selName3").isEmpty()){
        	object.ct3 = CarouselType.findById(Long.parseLong(params.get("selName3")));
        }
        if(params.get("selName4") != null && !params.get("selName4").isEmpty()){
        	object.ct4 = CarouselType.findById(Long.parseLong(params.get("selName4")));
        }
        if(params.get("selName5") != null && !params.get("selName5").isEmpty()){
        	object.ct5 = CarouselType.findById(Long.parseLong(params.get("selName5")));
        }
        if(params.get("selName6") != null && !params.get("selName6").isEmpty()){
        	object.ct6 = CarouselType.findById(Long.parseLong(params.get("selName6")));
        }
        if(params.get("selName7") != null && !params.get("selName7").isEmpty()){
        	object.ct7 = CarouselType.findById(Long.parseLong(params.get("selName7")));
        }
        if(params.get("selName8") != null && !params.get("selName8").isEmpty()){
        	object.ct8 = CarouselType.findById(Long.parseLong(params.get("selName8")));
        }
        
        if(params.get("ad_name1") != null && !params.get("ad_name1").isEmpty()){
        	object.ad_id1 = Long.parseLong(params.get("ad_name1"));
        }
        if(params.get("ad_name2") != null && !params.get("ad_name2").isEmpty()){
        	object.ad_id2 = Long.parseLong(params.get("ad_name2"));
        }
        if(params.get("ad_name3") != null && !params.get("ad_name3").isEmpty()){
        	object.ad_id3 = Long.parseLong(params.get("ad_name3"));
        }
        if(params.get("ad_name4") != null && !params.get("ad_name4").isEmpty()){
        	object.ad_id4 = Long.parseLong(params.get("ad_name4"));
        }
        if(params.get("ad_name5") != null && !params.get("ad_name5").isEmpty()){
        	object.ad_id5 = Long.parseLong(params.get("ad_name5"));
        }
        if(params.get("ad_name6") != null && !params.get("ad_name6").isEmpty()){
        	object.ad_id6 = Long.parseLong(params.get("ad_name6"));
        }
        if(params.get("ad_name7") != null && !params.get("ad_name7").isEmpty()){
        	object.ad_id7 = Long.parseLong(params.get("ad_name7"));
        }
        if(params.get("ad_name8") != null && !params.get("ad_name8").isEmpty()){
        	object.ad_id8 = Long.parseLong(params.get("ad_name8"));
        }
        validation.valid(object);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
            try {
                render(request.controller.replace(".", "/") + "/show.html", type, object);
            } catch (TemplateNotFoundException e) {
                render("CRUD/show.html", type, object);
            }
        }
        object.save();
 
        flash.success(play.i18n.Messages.get("crud.saved", type.modelName));
        if (params.get("_save") != null) {
            redirect(request.controller + ".list");
        }
        redirect(request.controller + ".show", object._key());
    }
	
    public static void show(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        GameCarousel object = (GameCarousel)type.findById(id);
        notFoundIfNull(object);

		try {
		    render(type, object);
		} catch (TemplateNotFoundException e) {
		    render("CRUD/show.html", type, object);
		}
    }
    
    public static void list(int page, String search, String searchFields, String orderBy, String order) {
    	Long admin_id = Long.parseLong(session.get("admin_id"));
    	Long admin_group = Long.parseLong(session.get("admin_group"));
    	ObjectType type = ObjectType.get(getControllerClass());
    	notFoundIfNull(type);
        if (page < 1) {
            page = 1;
        }
        List<Model> objects = new ArrayList<Model>();
        Long count = 0L;
        if(admin_group == 0){
            objects = type.findPage(page, search, searchFields, orderBy, order, (String) request.args.get("where"));
            count = type.count(search, searchFields, (String) request.args.get("where"));
            Long totalCount = type.count(null, null, (String) request.args.get("where"));
            try {
                render(type, objects, count, totalCount, page, orderBy, order);
            } catch (TemplateNotFoundException e) {
                render("CRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
            }
    	}else if(admin_group == 1){
    		List<Game> lg = ((AdminManagement)AdminManagement.findById(admin_id)).game;
    		for(Game g : lg){
    			List<GameCarousel> gc = GameCarousel.find("byGame", g).fetch();
    			for(Model m : gc){
    				objects.add(m);
    				count++;
    			}
    		}
        	Long totalCount = count;
            try {
	            render(type, objects, count, totalCount, page, orderBy, order);
	        } catch (TemplateNotFoundException e) {
	            render("CRUD/list.html", type, lg, count, totalCount, page, orderBy, order);
	        }
    	}

    }
}
