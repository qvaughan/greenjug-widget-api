package controllers;

import actions.MagicWordAuth;
import models.db.Widget;
import models.dto.WidgetRequest;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.WidgetService;

import java.util.List;
import java.util.UUID;
import javax.inject.Inject;

/**
 * @author Michael Vaughan
 */
public class WidgetController extends Controller {

    @Inject
    private WidgetService widgetService;

    @With(MagicWordAuth.class)
    public Result create() {
        WidgetRequest widgetRequest = Json.fromJson(request().body().asJson(), WidgetRequest.class);
        Widget widget = widgetService.create(widgetRequest);
        return ok(Json.toJson(widget));
    }

    public Result list(String make, String model, String color) {
        List<Widget> widgetList = widgetService.list(make, model, color);
        return ok(Json.toJson(widgetList));
    }

    public Result get(UUID id) {
        Widget widget = widgetService.get(id);
        return widget != null ? ok(Json.toJson(widget)) : notFound();
    }

}
