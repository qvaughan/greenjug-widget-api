package models.dto;

import models.db.Widget;

/**
 * @author Michael Vaughan
 */
public class WidgetRequest {

    private String make;
    private String model;
    private String color;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Widget toWidget() {
        return new Widget(make, model, color);
    }

}
