package controllers;

import models.AdminManagement;


public class Security extends Secure.Security {

    static boolean authenticate(String username, String password) {

    	
		if(AdminManagement.count() == 0){
			return "admin".equals(username)&&"admin".equals(password);
		}
    	//初始化状态
		AdminManagement user = AdminManagement.find("byM_number", username).first();
		if(user == null){
			return false;
		}
		return user != null && user.psd.equals(password);
    	}

    static void onDisconnected() {
        render();
    }

    static void onAuthenticated() {
    	render();
    }
}

