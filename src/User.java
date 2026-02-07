import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class User {
    
    private final IntegerProperty id;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty role;
    
    // Constructor when creating from UI (no id yet)
    public User(String username, String password, String role) {
        this.id = new SimpleIntegerProperty(0);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
    }
    
    // Constructor when loading from DB (id exists)
    public User(int id, String username, String password, String role) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
    }
    
    // -------- getters --------
    public int getId() { return id.get(); }
    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }
    public String getRole() { return role.get(); }
    
    // -------- properties --------
    public IntegerProperty idProperty() { return id; }
    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty roleProperty() { return role; }
    
    // ========================================
    // NEW: PASSWORD VALIDATION METHOD
    // ========================================
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        // Check for at least one digit
        boolean hasDigit = false;
        // Check for at least one letter
        boolean hasLetter = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (Character.isLetter(c)) {
                hasLetter = true;
            }
            
            if (hasDigit && hasLetter) {
                break;
            }
        }
        
        return hasDigit && hasLetter;
    }
    
    // Optional: Password strength feedback
    public static String getPasswordStrengthFeedback(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }
        
        if (password.length() < 6) {
            return "Password must be at least 6 characters";
        }
        
        boolean hasDigit = false;
        boolean hasLetter = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            if (Character.isLetter(c)) hasLetter = true;
        }
        
        if (!hasDigit) {
            return "Password should contain at least one digit";
        }
        if (!hasLetter) {
            return "Password should contain at least one letter";
        }
        
        return "Password is strong";
    }
}
