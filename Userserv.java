package GuiProject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;

public class Userserv {

    private static final int MAX_ATTEMPT = 3; 
    private static final int LOCK_HOURS = 2; 

    public boolean verifyCardGUI(String cardNumber) throws Exception {
        try (Connection conn = DatabaseCon.get_connection()) {
            String query = "SELECT Pin, Failed_attempt, last_Failed FROM Users WHERE Card_no= ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cardNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int attempts = rs.getInt("failed_attempt");
                Timestamp lastFailed = rs.getTimestamp("last_failed");

                if (attempts >= MAX_ATTEMPT && lastFailed != null) {
                    long hours = ChronoUnit.HOURS.between(lastFailed.toLocalDateTime(), LocalDateTime.now());
                    if (hours < LOCK_HOURS) {
                        JOptionPane.showMessageDialog(null, "Card is locked. Try after " + (LOCK_HOURS - hours) + " hours.");
                        return false;
                    } else {
                        resetAttempts(conn, cardNumber);
                    }
                }

                String correctPin = rs.getString("pin");

                for (int i = 0; i < MAX_ATTEMPT; i++) {
                    String enteredPin = JOptionPane.showInputDialog("Enter PIN:");
                    if (enteredPin == null) return false; // cancel

                    if (enteredPin.equals(correctPin)) {
                        resetAttempts(conn, cardNumber);
                        return true;
                    } else {
                        incrementAttempts(conn, cardNumber);
                        JOptionPane.showMessageDialog(null, "Incorrect PIN");
                    }
                }

                JOptionPane.showMessageDialog(null, "Card locked due to too many failed attempts.");
            } else {
                JOptionPane.showMessageDialog(null, "Card not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void resetAttempts(Connection conn, String cardNumber) throws SQLException {
        String update = "UPDATE Users SET Failed_attempt = 0, last_Failed = NULL WHERE Card_no = ?";
        PreparedStatement stmt = conn.prepareStatement(update);
        stmt.setString(1, cardNumber);
        stmt.executeUpdate();
    }

    private void incrementAttempts(Connection conn, String cardNumber) throws SQLException {
        String update = "UPDATE Users SET Failed_attempt = Failed_attempt + 1, last_Failed = NOW() WHERE Card_no = ?";
        PreparedStatement stmt = conn.prepareStatement(update);
        stmt.setString(1, cardNumber);
        stmt.executeUpdate();
    }
}