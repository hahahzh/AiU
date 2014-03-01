package controllers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import controllers.CRUD.ObjectType;

import models.AdminManagement;
import models.Game;
import models.GameCarousel;
import models.GameIcon;
import play.data.binding.Binder;
import play.db.Model;
import play.db.jpa.Blob;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
public class Games extends CRUD {

	public static void create() throws Exception {
	        ObjectType type = ObjectType.get(getControllerClass());
	        notFoundIfNull(type);
	        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
	        constructor.setAccessible(true);
	        Game object = (Game) constructor.newInstance();
	        Binder.bindBean(params.getRootParamNode(), "object", object);
	        validation.valid(object);
	        if (validation.hasErrors()) {
	            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
	            try {
	                render(request.controller.replace(".", "/") + "/blank.html", type, object);
	            } catch (TemplateNotFoundException e) {
	                render("CRUD/blank.html", type, object);
	            }
	        }
	        Game g = object.save();
	        GameCarousel gc = new GameCarousel();
	        gc.game = g;
	        gc.mtype = 1;
	        gc._save();
	        GameCarousel gc2 = new GameCarousel();
	        gc2.game = g;
	        gc2.mtype = 2;
	        gc2._save();
	        GameIcon gi = new GameIcon();
	        gi.game = g;
	        gi.picture1_g_icon = new Blob();
	        gi._save();
	        flash.success(play.i18n.Messages.get("crud.created", type.modelName));
	        if (params.get("_save") != null) {
	            redirect(request.controller + ".list");
	        }
	        if (params.get("_saveAndAddAnother") != null) {
	            redirect(request.controller + ".blank");
	        }
	        redirect(request.controller + ".show", object._key());
	    }

	    public static void delete(String id) throws Exception {
	        ObjectType type = ObjectType.get(getControllerClass());
	        notFoundIfNull(type);
	        Game object = (Game)type.findById(id);
	        notFoundIfNull(object);
	        try {
	        	GameCarousel.delete("game_id=?", object.id);
	            object._delete();
	        } catch (Exception e) {
	            flash.error(play.i18n.Messages.get("crud.delete.error", type.modelName));
	            redirect(request.controller + ".show", object._key());
	        }
	        flash.success(play.i18n.Messages.get("crud.deleted", type.modelName));
	        redirect(request.controller + ".list");
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
	    	if(admin_group == 0){
	            
	            objects = type.findPage(page, search, searchFields, orderBy, order, (String) request.args.get("where"));
	            Long count = type.count(search, searchFields, (String) request.args.get("where"));
	            Long totalCount = type.count(null, null, (String) request.args.get("where"));
	            try {
	                render(type, objects, count, totalCount, page, orderBy, order);
	            } catch (TemplateNotFoundException e) {
	                render("CRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
	            }
	    	}else if(admin_group == 1){
		    	notFoundIfNull(type);
	    		List<Game> lg = ((AdminManagement)AdminManagement.findById(admin_id)).game;
	    		for(Game g : lg){
	    			Model t = (Model)g;
	    			objects.add(t);
	    		}
            	Long count = Long.parseLong(String.valueOf(lg.size()));
            	Long totalCount = count;
	            try {
		            render(type, objects, count, totalCount, page, orderBy, order);
		        } catch (TemplateNotFoundException e) {
		            render("CRUD/list.html", type, lg, count, totalCount, page, orderBy, order);
		        }
	    	}

	    }
}