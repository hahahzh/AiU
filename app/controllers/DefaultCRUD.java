package controllers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.codec.net.URLCodec;

import play.Play;
import play.cache.Cache;
import play.data.binding.Binder;
import play.db.Model;
import play.db.jpa.Blob;
import play.db.jpa.JPA;
import play.exceptions.TemplateNotFoundException;
import play.i18n.Messages;
import play.mvc.Before;

public class DefaultCRUD extends CRUD {

	protected static URLCodec encoder = new URLCodec();
	
	/**
	 * 设置用户信息
	 */
	@Before
	public static void setConnectedUser() {
		if (Security.isConnected()) {
			// Member user = Member.find("byImei",
			// Security.connected()).first();
			renderArgs.put("user", Security.connected());
		}
	}

	/**
	 * 重写list，修正搜索时默认忽略关系实体的问题
	 * 
	 * @param page
	 *            页码
	 * @param search
	 *            检索词
	 * @param searchFields
	 *            检索字段
	 * @param orderBy
	 *            排序字段
	 * @param order
	 *            排序规则
	 */
	public static void list(int page, String search, String searchFields,
			String orderBy, String order) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		if (page < 1) {
			page = 1;
		}

		List<Model> objects = findPage(page, search, searchFields, orderBy,
				order, (String) request.args.get("where"));
		Long count = count(search, searchFields, (String) request.args
				.get("where"));
		Long totalCount = type.count(null, null, (String) request.args
				.get("where"));
		try {
			render(type, objects, count, totalCount, page, orderBy, order);
		} catch (TemplateNotFoundException e) {
			render("CRUD/list.html", type, objects, count, totalCount,
					page, orderBy, order);
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

	public static void save(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);

		Binder.bind(object, "object", params.all());
		validation.valid(object);
		if (validation.hasErrors()) {
			renderArgs.put("error", Messages.get("crud.hasErrors"));
			try {
				render(request.controller.replace(".", "/") + "/show.html",
						type, object);
			} catch (TemplateNotFoundException e) {
				render("CRUD/show.html", type, object);
			}
		}
		object._save();
		flash.success(Messages.get("crud.saved", type.modelName));
		if (params.get("_save") != null) {
			redirect(request.controller + ".list");
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
            flash.error(Messages.get("crud.delete.error", type.modelName));
            redirect(request.controller + ".show", object._key());
        }
        flash.success(Messages.get("crud.deleted", type.modelName));
        redirect(request.controller + ".list");
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

	public static void create() throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);

		Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
		constructor.setAccessible(true);
		Model object = (Model) constructor.newInstance();
		Binder.bind(object, "object", params.all());
		validation.valid(object);
		if (validation.hasErrors()) {
			renderArgs.put("error", Messages.get("crud.hasErrors"));
			try {
				render(request.controller.replace(".", "/") + "/blank.html",
						type, object);
			} catch (TemplateNotFoundException e) {
				render("CRUD/blank.html", type, object);
			}
		}
		object._save();
		flash.success(Messages.get("crud.created", type.modelName));
		if (params.get("_save") != null) {
			redirect(request.controller + ".list");
		}
		if (params.get("_saveAndAddAnother") != null) {
			redirect(request.controller + ".blank");
		}
		redirect(request.controller + ".show", object._key());
	}

	@SuppressWarnings("unchecked")
	public static List<Model> findPage(int page, String search,
			String searchFields, String orderBy, String order, String where) {
		return fetch((page - 1) * getPageSize(), getPageSize(), orderBy, order,
				searchFields == null ? new ArrayList<String>() : Arrays
						.asList(searchFields.split("[ ]")), search, where);
	}

	public static Long count(String search, String searchFields, String where) {
		return count(searchFields == null ? new ArrayList<String>() : Arrays
				.asList(searchFields.split("[ ]")), search, where);
	}

	protected static String getSearchQuery(List<String> searchFields) {
		String q = "";
		// 实体没有在crud.search tag中自定义了搜索字段，默认搜索实体的所有字段
		if (searchFields == null || searchFields.isEmpty()) {
			for (Model.Property property : Model.Manager.factoryFor(
					ObjectType.get(getControllerClass()).entityClass)
					.listProperties()) {
				if (property.isSearchable) {
					if (!q.equals("")) {
						q += " or ";
					}
					q += "lower(" + property.name + ") like ?1";
				}
			}
		} else {
			for (String field : searchFields) {
				if (!q.equals("")) {
					q += " or ";
				}
				q += "lower(" + field + ") like ?1";
			}
		}

		return q;
	}

	public static Long count(List<String> searchFields, String keywords,
			String where) {
		ObjectType type = ObjectType.get(getControllerClass());
		String q = "select count(e) from " + type.modelName + " e";
		if (keywords != null && !keywords.equals("")) {
			String searchQuery = getSearchQuery(searchFields);
			if (!searchQuery.equals("")) {
				q += " where (" + searchQuery + ")";
			}
			q += (where != null ? " and " + where : "");
		} else {
			q += (where != null ? " where " + where : "");
		}
		Query query = JPA.em().createQuery(q);
		if (keywords != null && !keywords.equals("") && q.indexOf("?1") != -1) {
			query.setParameter(1, "%" + keywords.toLowerCase() + "%");
		}
		return Long.decode(query.getSingleResult().toString());
	}

	@SuppressWarnings("unchecked")
	public static List<Model> fetch(int offset, int size, String orderBy,
			String order, List<String> searchFields, String keywords,
			String where) {
		ObjectType type = ObjectType.get(getControllerClass());
		String q = "from " + type.modelName;
		if (keywords != null && !keywords.equals("")) {
			String searchQuery = getSearchQuery(searchFields);
			if (!searchQuery.equals("")) {
				q += " where (" + searchQuery + ")";
			}
			q += (where != null ? " and " + where : "");
		} else {
			q += (where != null ? " where " + where : "");
		}
		if (orderBy == null && order == null) {
			orderBy = "id";
			order = "ASC";
		}
		if (orderBy == null && order != null) {
			orderBy = "id";
		}
		if (order == null || (!order.equals("ASC") && !order.equals("DESC"))) {
			order = "ASC";
		}
		q += " order by " + orderBy + " " + order;
		Query query = JPA.em().createQuery(q);
		if (keywords != null && !keywords.equals("") && q.indexOf("?1") != -1) {
			query.setParameter(1, "%" + keywords.toLowerCase() + "%");
		}
		query.setFirstResult(offset);
		query.setMaxResults(size);
		return query.getResultList();
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
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encoder.encode(attachment.filename, "utf-8") + "\"");
            renderBinary(attachment.get());
        }
        notFound();
    }

}
