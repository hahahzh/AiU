package controllers;


import java.lang.reflect.Constructor;

import controllers.CRUD.ObjectType;
import models.GameEvaluating;
import models.NativeNew;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
public class GameEvaluatings extends CRUD {
	
   public static void create() throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        GameEvaluating object = (GameEvaluating) constructor.newInstance();
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
        flash.success(play.i18n.Messages.get("crud.created", type.modelName));
        object._save();
        if(object.isShowNews == 1){
        	NativeNew nn = new NativeNew();
 	        nn.author = object.author;
 	        nn.data = object.data;
 	        nn.describe_aiu = object.describe_aiu;
 	        nn.game = object.game;
 	        nn.hit = object.hit;
 	        nn.icon = object.icon;
 	        nn.mtype = object.mtype;
 	        nn.picture1 = object.picture1;
 	        nn.picture10 = object.picture10;
 	        nn.picture1_ip5 = object.picture1_ip5;
 	        nn.picture2 = object.picture2;
 	        nn.picture3 = object.picture3;
 	        nn.picture4 = object.picture4;
 	        nn.picture5 = object.picture5;
 	        nn.picture6 = object.picture6;
 	        nn.picture7 = object.picture7;
 	        nn.picture8 = object.picture8;
 	        nn.picture9 = object.picture9;
 	        nn.res = object.res;
 	        nn.title = object.title;
 	        nn.txt1 = object.txt1;
 	        nn.txt10 = object.txt10;
 	        nn.txt2 = object.txt2;
 	        nn.txt3 = object.txt3;
 	        nn.txt4 = object.txt4;
 	        nn.txt5 = object.txt5;
 	        nn.txt6 = object.txt6;
 	        nn.txt7 = object.txt7;
 	        nn.txt8 = object.txt8;
 	        nn.txt9 = object.txt9;
 	        nn.URL = object.URL;
 	        nn._save(); 	
        }
       	        
        if (params.get("_save") != null) {
            redirect(request.controller + ".list");
        }
        if (params.get("_saveAndAddAnother") != null) {
            redirect(request.controller + ".blank");
        }
        redirect(request.controller + ".show", object._key());
    }
	   
   public static void save(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        GameEvaluating object = (GameEvaluating)type.findById(id);
        notFoundIfNull(object);
        Binder.bindBean(params.getRootParamNode(), "object", object);
        validation.valid(object);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
            try {
                render(request.controller.replace(".", "/") + "/show.html", type, object);
            } catch (TemplateNotFoundException e) {
                render("CRUD/show.html", type, object);
            }
        }
        object._save();
        if(object.isShowNews == 1){
        	NativeNew nn = NativeNew.find("game_id=? and title=?", object.game.id, object.title).first();
        	if(nn == null)nn = new NativeNew();
        	else nn.id = object.id;
 	        nn.author = object.author;
 	        nn.data = object.data;
 	        nn.describe_aiu = object.describe_aiu;
 	        nn.game = object.game;
 	        nn.hit = object.hit;
 	        nn.icon = object.icon;
 	        nn.mtype = object.mtype;
 	        nn.picture1 = object.picture1;
 	        nn.picture10 = object.picture10;
 	        nn.picture1_ip5 = object.picture1_ip5;
 	        nn.picture2 = object.picture2;
 	        nn.picture3 = object.picture3;
 	        nn.picture4 = object.picture4;
 	        nn.picture5 = object.picture5;
 	        nn.picture6 = object.picture6;
 	        nn.picture7 = object.picture7;
 	        nn.picture8 = object.picture8;
 	        nn.picture9 = object.picture9;
 	        nn.res = object.res;
 	        nn.title = object.title;
 	        nn.txt1 = object.txt1;
 	        nn.txt10 = object.txt10;
 	        nn.txt2 = object.txt2;
 	        nn.txt3 = object.txt3;
 	        nn.txt4 = object.txt4;
 	        nn.txt5 = object.txt5;
 	        nn.txt6 = object.txt6;
 	        nn.txt7 = object.txt7;
 	        nn.txt8 = object.txt8;
 	        nn.txt9 = object.txt9;
 	        nn.URL = object.URL;
 	        nn._save(); 	
        }
        
        flash.success(play.i18n.Messages.get("crud.saved", type.modelName));
        if (params.get("_save") != null) {
            redirect(request.controller + ".list");
        }
        redirect(request.controller + ".show", object._key());
    }
}