package controllers;

import java.lang.reflect.Constructor;

import models.Game;
import models.GameCarousel;
import play.data.binding.Binder;
import play.db.Model;
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
	        gc._save();
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
}