package controllers;

import java.util.List;

import models.User;
import notifiers.Notifier;
import play.Logger;
import play.Play;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

    static Integer pageSize = Integer.parseInt(Play.configuration.getProperty("forum.pageSize", "10"));
    
    // ~~~~~~~~~~~~ @Before interceptors
    
    @Before
    static void globals() {
        renderArgs.put("connected", connectedUser());
        renderArgs.put("pageSize", pageSize);
    }

    @Before
    static void checkSecure() {
        Secure secure = getActionAnnotation(Secure.class);
        if (secure != null) {
            if (connectedUser() == null || (secure.admin() && !connectedUser().isAdmin())) {
                forbidden();
            }
        }
    }
    // ~~~~~~~~~~~~ Actions
    
    public static void signup() {
        render();
    }

    public static void register(@Required @Email String m, @Required @MinSize(5) String password, @Equals("password") String password2, @Required String name) {
        if (validation.hasErrors()) {
            validation.keep();
            params.flash();
            flash.error("Please correct these errors !");
            signup();
        }
        User user = new User(m, password, name);
        try {
            if (Notifier.welcome(user)) {
                flash.success("Your account is created. Please check your emails ...");
                login();
            }
        } catch (Exception e) {
            Logger.error(e, "Mail error");
        }
        flash.error("Oops ... (the email cannot be sent)");
        login();
    }
    
    public static void login() {
        render();
    }

    public static void authenticate(String m, String password) {
    	
        
        User user = User.findByMobile(m);
        if (user == null || !user.checkPassword(password)) {
            flash.error("Bad mobile or bad password");
            flash.put("mobile", m);
            login();
        }
        connect(user);
        flash.success("Welcome back %s !", user.nickname);
        Users.show(user.id);
    }

    public static void logout() {
        flash.success("You've been logged out");
        session.clear();
        Forums.index();
    }
    
    static void connect(User user) {
        session.put("logged", user.id);
    }

    static User connectedUser() {
        String userId = session.get("logged");
        return userId == null ? null : (User) User.findById(Long.parseLong(userId));
    }
}