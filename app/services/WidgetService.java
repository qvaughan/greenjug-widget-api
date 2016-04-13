package services;

import models.db.Widget;
import models.dto.WidgetRequest;

import java.util.List;
import java.util.UUID;

/**
 * @author Michael Vaughan
 */
public interface WidgetService {

    Widget create(WidgetRequest request);

    List<Widget> list(String make, String model, String color);

    Widget get(UUID id);

}
