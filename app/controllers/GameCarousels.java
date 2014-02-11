package controllers;


import java.lang.reflect.Constructor;
import java.util.List;

import models.CarouselType;
import models.FirmNew;
import models.GameCarousel;
import models.New;
import models.Pack;
import play.data.binding.Binder;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
public class GameCarousels extends CRUD {
	
	  public static void getEntityList(String id, String selval, Long gid) throws Exception {
	       ObjectType type = ObjectType.get(getControllerClass());
	        notFoundIfNull(type);
	        GameCarousel object = (GameCarousel)type.findById(id);
	        notFoundIfNull(object);
	        
	        List l = null;
	      if("新闻".equals(selval)){
			  l = FirmNew.find("game_id=?", gid).fetch();
		  }else if("礼包".equals(selval)){
			  l = Pack.find("game_id=?", gid).fetch();
		  }
	      Long ad_id = object.ad_id;
        try {
            render("GameCarousels/show.html", type, object, l, selval, ad_id);
        } catch (TemplateNotFoundException e) {
            render("GameCarousels/show.html", type, object);
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
        if(params.get("selName") != null && !params.get("selName").isEmpty()){
        	object.ct = CarouselType.find("byType", params.get("selName")).first();
        }
        if(params.get("selName2") != null && !params.get("selName2").isEmpty()){
        	object.ct2 = CarouselType.find("byType", params.get("selName2")).first();
        }
        if(params.get("selName3") != null && !params.get("selName3").isEmpty()){
        	object.ct3 = CarouselType.find("byType", params.get("selName3")).first();
        }
        if(params.get("selName4") != null && !params.get("selName4").isEmpty()){
        	object.ct4 = CarouselType.find("byType", params.get("selName4")).first();
        }
        if(params.get("selName5") != null && !params.get("selName5").isEmpty()){
        	object.ct5 = CarouselType.find("byType", params.get("selName5")).first();
        }
        if(params.get("selName6") != null && !params.get("selName6").isEmpty()){
        	object.ct6 = CarouselType.find("byType", params.get("selName6")).first();
        }
        if(params.get("selName7") != null && !params.get("selName7").isEmpty()){
        	object.ct7 = CarouselType.find("byType", params.get("selName7")).first();
        }
        if(params.get("selName8") != null && !params.get("selName8").isEmpty()){
        	object.ct8 = CarouselType.find("byType", params.get("selName8")).first();
        }
        if(params.get("ad_name1") != null && !params.get("ad_name1").isEmpty()){
        	object.ad_id = Long.parseLong(params.get("ad_name1"));
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
        
        List l = null;
        String selval = null;
        if(object.ct != null){
        	if("新闻".equals(object.ct.type)){
  			  l = FirmNew.find("byGame", object.game).fetch();
  		  }else if("礼包".equals(object.ct.type)){
  			  l = Pack.find("byGame", object.game).fetch();
  		  }
        	selval = object.ct.type;
        }
	  Long ad_id = object.ad_id;
	  
        try {
            render(type, object, l, selval, ad_id);
        } catch (TemplateNotFoundException e) {
            render("CRUD/show.html", type, object);
        }
    }
}
