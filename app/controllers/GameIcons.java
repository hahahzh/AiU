package controllers;


import java.util.ArrayList;
import java.util.List;

import models.AdminManagement;
import models.Game;
import models.GameIcon;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
public class GameIcons extends CRUD {
	
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
    			List<GameIcon> gc = GameIcon.find("byGame", g).fetch();
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