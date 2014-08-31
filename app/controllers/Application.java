package controllers;

import play.*;
import play.mvc.*;
import org.onebusaway.siri.

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

}
