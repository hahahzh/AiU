package controllers;


import java.util.List;

import models.AdminManagement;
import models.Game;
import controllers.CRUD.ObjectType;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
public class GameVideos extends CRUD {
	
	public static List<Game> findGames() {
		AdminManagement am = AdminManagement.findById(Long.parseLong(session.get("admin_id")));
		return am.game;
	}
	
    public static void list(int page, String search, String searchFields, String orderBy, String order) {
    	
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        if (page < 1) {
            page = 1;
        }
        
        Long admin_id = Long.parseLong(session.get("admin_id"));
      	List<Game> lg = ((AdminManagement)AdminManagement.findById(admin_id)).game;
      	StringBuffer where = new StringBuffer(32);
      	int i = lg.size();
      	if(i>0)where.append("game_id in(");
      	for(Game g:lg){
      		where.append(g.id+",");
      	}
      	if(i>0)where.append("0)");
        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, where.toString());
        Long count = type.count(search, searchFields, where.toString());
        Long totalCount = type.count(null, null, where.toString());
        try {
            render(type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("CRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
        }
        
    }
}