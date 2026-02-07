import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {

    private static final String URL = "jdbc:sqlite:booking.db";

    // -----------------------------
    // CONNECT
    // -----------------------------
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // -----------------------------
    // CREATE TABLES
    // -----------------------------
    public static void createTables() {
        createBookingsTable();
        createUsersTable();
        createDefaultUser();
    }

    public static void createBookingsTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS bookings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                date TEXT NOT NULL,
                time TEXT NOT NULL,
                notes TEXT,
                status TEXT DEFAULT 'Pending'
            )
        """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Bookings table ready.");

        } catch (SQLException e) {
            printError(e);
        }
    }

    public static void createUsersTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                role TEXT DEFAULT 'user'
            )
        """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Users table ready.");

        } catch (SQLException e) {
            printError(e);
        }
    }

    // -----------------------------
    // CREATE DEFAULT USER
    // -----------------------------
    public static void createDefaultUser() {
        // Check if any users exist
        String checkSql = "SELECT COUNT(*) FROM users";
        String insertSql = "INSERT INTO users(username, password, role) VALUES(?, ?, ?)";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                // No users exist, create default admin
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setString(1, "admin");
                    ps.setString(2, "admin123");
                    ps.setString(3, "admin");
                    ps.executeUpdate();
                    System.out.println("Default admin user created (username: admin, password: admin123)");
                }
            }

        } catch (SQLException e) {
            printError(e);
        }
    }

    // -----------------------------
    // USER AUTHENTICATION
    // -----------------------------
    public static User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                }
            }

        } catch (SQLException e) {
            printError(e);
        }

        return null;
    }

    // -----------------------------
    // ADD USER
    // -----------------------------
    public static boolean addUser(String username, String password, String role) {
        String sql = "INSERT INTO users(username, password, role) VALUES(?,?,?)";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            printError(e);
            return false;
        }
    }

    // -----------------------------
    // ADD BOOKING
    // -----------------------------
    public static boolean addBooking(String name, String date, String time, String notes) {
        String sql = "INSERT INTO bookings(name,date,time,notes,status) VALUES(?,?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, date);
            ps.setString(3, time);
            ps.setString(4, notes);
            ps.setString(5, "Pending");
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            printError(e);
            return false;
        }
    }

    // -----------------------------
    // GET ALL BOOKINGS
    // -----------------------------
    public static List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings ORDER BY date DESC, time DESC";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Booking(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getString("time"),
                        rs.getString("notes"),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            printError(e);
        }

        return list;
    }

    // -----------------------------
    // UPDATE BOOKING STATUS
    // -----------------------------
    public static boolean updateBookingStatus(int id, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            printError(e);
            return false;
        }
    }

    // -----------------------------
    // DELETE BOOKING
    // -----------------------------
    public static void deleteBooking(int id) {
        String sql = "DELETE FROM bookings WHERE id=?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            printError(e);
        }
    }

    // ========================================
    // NEW: BOOKING CONFLICT DETECTION
    // ========================================
    public static boolean isBookingConflict(String date, String time) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE date = ? AND time = ? AND status IN ('Pending', 'Confirmed')";
        
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, date);
            ps.setString(2, time);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            printError(e);
        }
        
        return false;
    }

    // -----------------------------
    // REPORTING: Get Bookings by Date Range
    // -----------------------------
    public static List<Booking> getBookingsByDateRange(String startDate, String endDate) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE date BETWEEN ? AND ? ORDER BY date, time";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, startDate);
            ps.setString(2, endDate);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Booking(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("date"),
                            rs.getString("time"),
                            rs.getString("notes"),
                            rs.getString("status")
                    ));
                }
            }

        } catch (SQLException e) {
            printError(e);
        }

        return list;
    }

    // -----------------------------
    // REPORTING: Get Bookings by Status
    // -----------------------------
    public static List<Booking> getBookingsByStatus(String status) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE status = ? ORDER BY date DESC, time DESC";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Booking(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("date"),
                            rs.getString("time"),
                            rs.getString("notes"),
                            rs.getString("status")
                    ));
                }
            }

        } catch (SQLException e) {
            printError(e);
        }

        return list;
    }

    // -----------------------------
    // REPORTING: Get Statistics
    // -----------------------------
    public static String getBookingStatistics() {
        StringBuilder stats = new StringBuilder();
        
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // Total bookings
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM bookings")) {
                if (rs.next()) {
                    stats.append("Total Bookings: ").append(rs.getInt(1)).append("\n");
                }
            }

            // Pending bookings
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM bookings WHERE status='Pending'")) {
                if (rs.next()) {
                    stats.append("Pending: ").append(rs.getInt(1)).append("\n");
                }
            }

            // Confirmed bookings
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM bookings WHERE status='Confirmed'")) {
                if (rs.next()) {
                    stats.append("Confirmed: ").append(rs.getInt(1)).append("\n");
                }
            }

            // Cancelled bookings
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM bookings WHERE status='Cancelled'")) {
                if (rs.next()) {
                    stats.append("Cancelled: ").append(rs.getInt(1)).append("\n");
                }
            }

            // Completed bookings
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM bookings WHERE status='Completed'")) {
                if (rs.next()) {
                    stats.append("Completed: ").append(rs.getInt(1)).append("\n");
                }
            }

        } catch (SQLException e) {
            printError(e);
        }

        return stats.toString();
    }

    // -----------------------------
    // ERROR HELPER
    // -----------------------------
    public static void printError(SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
        e.printStackTrace();
    }
}












