package models.db;

import com.avaje.ebean.Model;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Michael Vaughan
 */
@Entity
public class Widget extends Model {

    @Id
    private UUID id;
    private String make;
    private String model;
    private String color;

    public Widget(UUID id, String make, String model, String color) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.color = color;
    }

    public Widget(String make, String model, String color) {
        this.make = make;
        this.model = model;
        this.color = color;
    }

    public Widget() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
}
