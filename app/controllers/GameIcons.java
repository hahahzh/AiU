package controllers;


import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

import controllers.CRUD.ObjectType;
import models.Carousel;
import models.CarouselType;
import models.EveryGame;
import models.Game;
import models.New;
import models.Pack;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
public class GameIcons extends CRUD {}