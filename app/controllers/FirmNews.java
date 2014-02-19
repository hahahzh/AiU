package controllers;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.AdminManagement;
import models.FirmNew;
import models.Game;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
public class FirmNews extends CRUD {
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
        List<FirmNew> objects = new ArrayList<FirmNew>();
        Long count = 0L;
        for(Game g : lg){
        	List<FirmNew> t = FirmNew.find("byGame", g).fetch();
        	if(t.size() > 0){
        		objects.addAll(t);
        		count += t.size();
        	}
        }
        Long totalCount = count;
        
        Collections.sort(objects);
        Collections.sort(objects,new Comparator<FirmNew>(){  
            public int compare(FirmNew arg0, FirmNew arg1) {  
                return arg1.id.compareTo(arg0.id);  
            }  
        }); 
        try {
            render(type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("CRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
        }
    }
}