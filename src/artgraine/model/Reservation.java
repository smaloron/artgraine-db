package artgraine.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;

public class Reservation {

    private String title;
    private String description;
    private Long sculptureId;
    private SimpleBooleanProperty selected;

    public Reservation() {
        this.selected = new SimpleBooleanProperty();
    }

    public String getTitle() {
        return title;
    }

    public Reservation setTitle(String title) {
        this.title = title;
        return this;
    }

    public Long getSculptureId() {
        return sculptureId;
    }

    public Reservation setSculptureId(Long sculptureId) {
        this.sculptureId = sculptureId;
        return this;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public String getDescription() {
        return description;
    }

    public Reservation setDescription(String description) {
        this.description = description;
        return this;
    }
}
