package controllers;


import java.util.List;

import controllers.CRUD.ObjectType;

import models.AdminManagement;
import models.CarouselType;
import models.Game;
import play.db.Model;
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
        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, (String) request.args.get("where"));
        Long count = type.count(search, searchFields, (String) request.args.get("where"));
        Long totalCount = type.count(null, null, (String) request.args.get("where"));
        try {
            render(type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("CRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
        }
    }
}