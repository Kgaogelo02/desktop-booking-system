import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import java.util.List;

public class Main extends Application {

    private User currentUser;

    @Override
    public void start(Stage primaryStage) {
        DB.createTables();

        primaryStage.setTitle("Booking Management System");
        showLoginScene(primaryStage);
    }

    // ========================================
    // LOGIN SCENE
    // ========================================
    private void showLoginScene(Stage stage) {
        GridPane loginGrid = new GridPane();
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        loginGrid.setPadding(new Insets(25));

        Label titleLabel = new Label("Booking Management System");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        loginGrid.add(titleLabel, 0, 0, 2, 1);

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        userField.setPromptText("Enter username");
        
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter password");
        
        Button loginBtn = new Button("Login");
        loginBtn.setDefaultButton(true);
        loginBtn.setPrefWidth(100);
        
        Label infoLabel = new Label("Default: admin / admin123");
        infoLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

        loginGrid.add(userLabel, 0, 1);
        loginGrid.add(userField, 1, 1);
        loginGrid.add(passLabel, 0, 2);
        loginGrid.add(passField, 1, 2);
        loginGrid.add(loginBtn, 1, 3);
        loginGrid.add(infoLabel, 0, 4, 2, 1);

        Scene loginScene = new Scene(loginGrid, 400, 250);
        stage.setScene(loginScene);
        stage.show();

        // Login action
        loginBtn.setOnAction(e -> handleLogin(stage, userField.getText(), passField.getText()));
        
        // Allow Enter key to login
        passField.setOnAction(e -> handleLogin(stage, userField.getText(), passField.getText()));
    }

    private void handleLogin(Stage stage, String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Please enter both username and password.");
            return;
        }

        // Check password strength (optional feedback)
        if (!User.isPasswordStrong(password)) {
            showAlert(Alert.AlertType.WARNING, "Weak Password", 
                "Password should be at least 6 characters long for security.");
            // Continue anyway, just warning
        }

        User user = DB.authenticateUser(username, password);
        
        if (user != null) {
            currentUser = user;
            showAlert(Alert.AlertType.INFORMATION, "Login Success", 
                "Welcome, " + user.getUsername() + "!\nRole: " + user.getRole());
            showBookingScene(stage);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", 
                "Invalid username or password.\n\nDefault credentials:\nUsername: admin\nPassword: admin123");
        }
    }

    // ========================================
    // BOOKING SCENE
    // ========================================
    private void showBookingScene(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top - Welcome message and logout
        HBox topBox = new HBox(10);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setPadding(new Insets(5));
        Label welcomeLabel = new Label("Logged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        welcomeLabel.setStyle("-fx-font-weight: bold;");
        Button logoutBtn = new Button("Logout");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBox.getChildren().addAll(welcomeLabel, spacer, logoutBtn);
        root.setTop(topBox);

        // Center - Input form and table
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));

        // Input Form
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(10));
        inputGrid.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5;");

        Label formTitle = new Label("Add New Booking");
        formTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        inputGrid.add(formTitle, 0, 0, 2, 1);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter customer name");
        
        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker();
        
        Label timeLabel = new Label("Time:");
        TextField timeField = new TextField();
        timeField.setPromptText("HH:MM (e.g., 14:30)");
        
        Label notesLabel = new Label("Notes:");
        TextField notesField = new TextField();
        notesField.setPromptText("Additional information");

        Button addBtn = new Button("Add Booking");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        Button clearBtn = new Button("Clear Form");

        inputGrid.add(nameLabel, 0, 1);
        inputGrid.add(nameField, 1, 1);
        inputGrid.add(dateLabel, 0, 2);
        inputGrid.add(datePicker, 1, 2);
        inputGrid.add(timeLabel, 0, 3);
        inputGrid.add(timeField, 1, 3);
        inputGrid.add(notesLabel, 0, 4);
        inputGrid.add(notesField, 1, 4);
        
        HBox btnBox = new HBox(10);
        btnBox.getChildren().addAll(addBtn, clearBtn);
        inputGrid.add(btnBox, 1, 5);

        // TableView
        TableView<Booking> tableView = new TableView<>();
        tableView.setPrefHeight(300);

        TableColumn<Booking, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Booking, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Booking, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(100);

        TableColumn<Booking, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeCol.setPrefWidth(80);

        TableColumn<Booking, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        notesCol.setPrefWidth(200);

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        // Fix for TableView warning - use addAll instead of individual adds
        tableView.getColumns().addAll(idCol, nameCol, dateCol, timeCol, notesCol, statusCol);

        // Action buttons
        HBox actionBox = new HBox(10);
        actionBox.setPadding(new Insets(10, 0, 0, 0));
        
        Button refreshBtn = new Button("Refresh Table");
        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        
        Button updateStatusBtn = new Button("Update Status");
        updateStatusBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        
        Button reportBtn = new Button("View Reports");
        reportBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");

        actionBox.getChildren().addAll(refreshBtn, deleteBtn, updateStatusBtn, reportBtn);

        centerBox.getChildren().addAll(inputGrid, new Label("All Bookings:"), tableView, actionBox);
        root.setCenter(centerBox);

        // Scene
        Scene scene = new Scene(root, 900, 700);
        stage.setScene(scene);

        // ========================================
        // EVENT HANDLERS
        // ========================================

        // Add Booking
        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
            String time = timeField.getText().trim();
            String notes = notesField.getText().trim();

            // Validation
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Name is required.");
                return;
            }
            
            if (date.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Date is required.");
                return;
            }
            
            if (!isValidTime(time)) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", 
                    "Invalid time format. Use HH:MM (24-hour format, e.g., 14:30)\nHours: 00-23, Minutes: 00-59");
                return;
            }

            // Check for booking conflict
            if (DB.isBookingConflict(date, time)) {
                showAlert(Alert.AlertType.WARNING, "Booking Conflict", 
                    "A booking already exists for " + date + " at " + time + "\nPlease choose a different time.");
                return;
            }

            if (DB.addBooking(name, date, time, notes)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Booking added successfully!");
                refreshTable(tableView);
                
                // Clear form
                nameField.clear();
                datePicker.setValue(null);
                timeField.clear();
                notesField.clear();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add booking.");
            }
        });

        // Clear Form
        clearBtn.setOnAction(e -> {
            nameField.clear();
            datePicker.setValue(null);
            timeField.clear();
            notesField.clear();
        });

        // Delete Booking
        deleteBtn.setOnAction(e -> {
            Booking selected = tableView.getSelectionModel().getSelectedItem();

            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Delete");
                confirm.setHeaderText("Delete Booking");
                confirm.setContentText("Are you sure you want to delete this booking?");

                if (confirm.showAndWait().get() == ButtonType.OK) {
                    DB.deleteBooking(selected.getId());
                    tableView.getItems().remove(selected);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Booking deleted successfully!");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a booking to delete.");
            }
        });

        // Update Status
        updateStatusBtn.setOnAction(e -> {
            Booking selected = tableView.getSelectionModel().getSelectedItem();

            if (selected != null) {
                ChoiceDialog<String> dialog = new ChoiceDialog<>("Pending", 
                    FXCollections.observableArrayList("Pending", "Confirmed", "Cancelled", "Completed"));
                dialog.setTitle("Update Status");
                dialog.setHeaderText("Update Booking Status");
                dialog.setContentText("Select new status:");

                dialog.showAndWait().ifPresent(status -> {
                    if (DB.updateBookingStatus(selected.getId(), status)) {
                        selected.setStatus(status);
                        tableView.refresh();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Status updated to: " + status);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update status.");
                    }
                });
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a booking to update.");
            }
        });

        // Refresh Table
        refreshBtn.setOnAction(e -> refreshTable(tableView));

        // View Reports
        reportBtn.setOnAction(e -> showReportsWindow());

        // Logout
        logoutBtn.setOnAction(e -> {
            currentUser = null;
            showLoginScene(stage);
        });

        // Load table initially
        refreshTable(tableView);
    }

    // ========================================
    // REPORTS WINDOW
    // ========================================
    private void showReportsWindow() {
        Stage reportStage = new Stage();
        reportStage.setTitle("Booking Reports");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Booking Reports & Statistics");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Statistics
        TextArea statsArea = new TextArea();
        statsArea.setEditable(false);
        statsArea.setPrefHeight(150);
        statsArea.setText(DB.getBookingStatistics());

        // Filter by Status
        HBox statusBox = new HBox(10);
        Label statusLabel = new Label("Filter by Status:");
        ComboBox<String> statusCombo = new ComboBox<>(
            FXCollections.observableArrayList("All", "Pending", "Confirmed", "Cancelled", "Completed"));
        statusCombo.setValue("All");
        Button filterBtn = new Button("Apply Filter");
        statusBox.getChildren().addAll(statusLabel, statusCombo, filterBtn);

        // Results Table
        TableView<Booking> reportTable = new TableView<>();
        reportTable.setPrefHeight(300);

        TableColumn<Booking, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Booking, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Booking, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Booking, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Fix for TableView warning
        reportTable.getColumns().addAll(idCol, nameCol, dateCol, timeCol, statusCol);

        // Filter action
        filterBtn.setOnAction(e -> {
            String selectedStatus = statusCombo.getValue();
            List<Booking> bookings;
            
            if ("All".equals(selectedStatus)) {
                bookings = DB.getAllBookings();
            } else {
                bookings = DB.getBookingsByStatus(selectedStatus);
            }
            
            reportTable.getItems().clear();
            reportTable.getItems().addAll(bookings);
        });

        // Load all bookings initially
        reportTable.getItems().addAll(DB.getAllBookings());

        root.getChildren().addAll(titleLabel, new Label("Statistics:"), statsArea, 
            new Separator(), statusBox, reportTable);

        Scene scene = new Scene(root, 700, 600);
        reportStage.setScene(scene);
        reportStage.show();
    }

    // ========================================
    // HELPER METHODS
    // ========================================
    private void refreshTable(TableView<Booking> tableView) {
        tableView.getItems().clear();
        List<Booking> bookings = DB.getAllBookings();
        tableView.getItems().addAll(bookings);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // ========================================
    // NEW VALIDATION METHOD
    // ========================================
    private boolean isValidTime(String time) {
        if (!time.matches("\\d{2}:\\d{2}")) {
            return false;
        }
        
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        
        return hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59;
    }

    public static void main(String[] args) {
        launch(args);
    }
}



