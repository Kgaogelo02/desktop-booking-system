import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class Booking {

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty date;
    private final StringProperty time;
    private final StringProperty notes;
    private final StringProperty status;

    public Booking(String name, String date, String time, String notes) {
        this.id = new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty(name);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.notes = new SimpleStringProperty(notes);
        this.status = new SimpleStringProperty("Pending");
    }

    public Booking(int id, String name, String date, String time, String notes, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.notes = new SimpleStringProperty(notes);
        this.status = new SimpleStringProperty(status);
    }

    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getDate() { return date.get(); }
    public String getTime() { return time.get(); }
    public String getNotes() { return notes.get(); }
    public String getStatus() { return status.get(); }

    public void setStatus(String status) { this.status.set(status); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty dateProperty() { return date; }
    public StringProperty timeProperty() { return time; }
    public StringProperty notesProperty() { return notes; }
    public StringProperty statusProperty() { return status; }
    
    public static boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
    
    public static boolean isValidTime(String time) {
        return time.matches("\\d{2}:\\d{2}");
    }
}


