package services.impl;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import models.db.Widget;
import models.dto.WidgetRequest;
import services.WidgetService;

import java.util.List;
import java.util.UUID;

/**
 * @author Michael Vaughan
 */
public class WidgetServiceImpl implements WidgetService {

    @Override
    public Widget create(WidgetRequest request) {
        Widget widget = request.toWidget();
        widget.save();
        return widget;
    }

    @Override
    public List<Widget> list(String make, String model, String color) {
        ExpressionList<Widget> expressionList = Ebean.find(Widget.class).where();
        if (make != null) {
            expressionList.eq("make", make);
        }
        if (model != null) {
            expressionList.eq("model", model);
        }
        if (color != null) {
            expressionList.eq("color", color);
        }
        return expressionList.findList();
    }

    @Override
    public Widget get(UUID id) {
        return Ebean.find(Widget.class).where().idEq(id).findUnique();
    }
}
