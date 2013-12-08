package controllers;


import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

import controllers.CRUD.ObjectType;
import models.Carousel;
import models.CarouselType;
import models.EveryGame;
import models.Game;
import models.New;
import models.Pack;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
public class Carousels extends CRUD {
	
	  public static void getEntityList(String selval, String mtype) throws Exception {
		   ObjectType type = ObjectType.get(getControllerClass());
	        notFoundIfNull(type);
	        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
	        constructor.setAccessible(true);
	        Model object = (Model) constructor.newInstance();
	        List l = null;
		  if("每日一游&玩嗨周五".equals(selval)){
			  l = EveryGame.findAll();
		  } else if("游戏".equals(selval)){
			  l = Game.findAll();
		  }else if("新闻".equals(selval)){
			  l = New.findAll();
		  }else if("礼包".equals(selval)){
			  l = Pack.findAll();
		  }
        try {
        	
            render("Carousels/show.html", type, object, l, selval, mtype);
        } catch (TemplateNotFoundException e) {
            render("Carousels/show.html", type, object);
        }
    }
	  
	public static List<CarouselType> findSimpleCarouselType() {
		return CarouselType.findAll(); 
	}
	
	public static void save(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Carousel object = null;
        if(id == null || id.isEmpty()){
            Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            object = (Carousel) constructor.newInstance();
        }else{
        	object = (Carousel)type.findById(id);
        }
        notFoundIfNull(object);
        Binder.bindBean(params.getRootParamNode(), "object", object);
        if(params.get("selName") != null && !params.get("selName").isEmpty()){
        	object.ct = CarouselType.find("byType", params.get("selName")).first();
        }
        if(params.get("type_id") != null && !params.get("type_id").isEmpty()){
        	object.ad_id = Long.parseLong(params.get("type_id"));
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
        Carousel object = (Carousel)type.findById(id);
        notFoundIfNull(object);
        
        List l = null;
        
	  if("每日一游&玩嗨周五".equals(object.ct.type)){
		  l = EveryGame.findAll();
	  } else if("游戏".equals(object.ct.type)){
		  l = Game.findAll();
	  }else if("新闻".equals(object.ct.type)){
		  l = New.findAll();
	  }else if("礼包".equals(object.ct.type)){
		  l = Pack.findAll();
	  }
	  Long ad_id = object.ad_id;
	  String selval = object.ct.type;
        try {
            render(type, object, l, selval, ad_id);
        } catch (TemplateNotFoundException e) {
            render("CRUD/show.html", type, object);
        }
    }
}

