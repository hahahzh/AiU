package controllers;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import models.Pack;
import models.PackPKey;
import play.data.binding.Binder;
import play.db.DB;
import play.db.Model;
import play.db.jpa.JPA;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;
import utils.ExcelTool;
import utils.TransactionUtil;

@Check("admin")
@With(Secure.class)
public class Packs extends CRUD {

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

    public static void show(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Model object = type.findById(id);
        notFoundIfNull(object);
        try {
            render(type, object);
        } catch (TemplateNotFoundException e) {
            render("CRUD/show.html", type, object);
        }
    }

    @SuppressWarnings("deprecation")
    public static void attachment(String id, String field) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Model object = type.findById(id);
        notFoundIfNull(object);
        Object att = object.getClass().getField(field).get(object);
        if(att instanceof Model.BinaryField) {
            Model.BinaryField attachment = (Model.BinaryField)att;
            if (attachment == null || !attachment.exists()) {
                notFound();
            }
            response.contentType = attachment.type();
            renderBinary(attachment.get(), attachment.length());
        }
        // DEPRECATED
        if(att instanceof play.db.jpa.FileAttachment) {
            play.db.jpa.FileAttachment attachment = (play.db.jpa.FileAttachment)att;
            if (attachment == null || !attachment.exists()) {
                notFound();
            }
            renderBinary(attachment.get(), attachment.filename);
        }
        notFound();
    }

    public static void save(String id, File pack_file) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Pack object = (Pack)type.findById(id);
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
        Pack p = object.save();
        if(pack_file != null && pack_file.isFile()){
        	savePkey(pack_file.getPath(), p);
        	pack_file.delete();
        }
        flash.success(play.i18n.Messages.get("crud.saved", type.modelName));
        if (params.get("_save") != null) {
            redirect(request.controller + ".list");
        }
        redirect(request.controller + ".show", object._key());
    }

    public static void blank() throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = (Model) constructor.newInstance();
        try {
            render(type, object);
        } catch (TemplateNotFoundException e) {
            render("CRUD/blank.html", type, object);
        }
    }

    public static void create(File pack_file) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Pack object = (Pack) constructor.newInstance();
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

        Pack p = object.save();
        if(pack_file != null && pack_file.isFile()){
        	savePkey(pack_file.getPath(), p);
        	pack_file.delete();
        }
        
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
        Model object = type.findById(id);
        notFoundIfNull(object);
        try {
            object._delete();
        } catch (Exception e) {
            flash.error(play.i18n.Messages.get("crud.delete.error", type.modelName));
            redirect(request.controller + ".show", object._key());
        }
        flash.success(play.i18n.Messages.get("crud.deleted", type.modelName));
        redirect(request.controller + ".list");
    }
    
    private static void savePkey(String path, Pack p) throws IOException{
    	List<String> l = ExcelTool.poiReadExcel(path);
    	Connection conn = null;
		try {
			conn = DB.datasource.getConnection();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
	    	for(String s: l){
	    		st.addBatch("INSERT INTO `t_pkey` (pkey, pack_id) VALUES ('"+s+"', "+p.getId()+")");
	    	}
	    	st.executeBatch();
	    	conn.commit();
		} catch (SQLException e) {
			if(conn != null)
				try {
					conn.rollback();
				} catch (SQLException e1) {}
		}finally{
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					play.Logger.error("Connection Not Closed ");
				}
		}
    
    }
}