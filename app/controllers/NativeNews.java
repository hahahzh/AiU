package controllers;


import java.lang.reflect.Constructor;

import models.NativeNew;
import play.data.binding.Binder;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
public class NativeNews extends CRUD {}