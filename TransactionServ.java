package GuiProject;
import java.sql.*;
import javax.swing.*;

public class TransactionServ {

    public void withdraw(String cardNumber) {
        try (Connection conn = DatabaseCon.get_connection()) {
            String getAcc = "SELECT Account_no, Balance FROM Users WHERE Card_no = ?";
            PreparedStatement stmt = conn.prepareStatement(getAcc);
            stmt.setString(1, cardNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String acc = rs.getString("Account_no");
                double bal = rs.getDouble("Balance");

                String input = JOptionPane.showInputDialog("Enter amount to withdraw:");
                if (input == null) return;
                double amt = Double.parseDouble(input);

                if (amt > bal) {
                    JOptionPane.showMessageDialog(null, "Insufficient balance.");
                    return;
                }

                conn.setAutoCommit(false);
                try {
                    updateBalance(conn, acc, bal - amt);
                    logTransaction(conn, acc, "WITHDRAW", amt);
                    conn.commit();
                    JOptionPane.showMessageDialog(null, "Withdrawal successful.");
                } catch (SQLException e) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(null, "Error. Withdrawal failed.");
                } finally {
                    conn.setAutoCommit(true);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Account not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deposit(String cardNumber) {
        try (Connection conn = DatabaseCon.get_connection()) {
            String getAcc = "SELECT Account_no, Balance FROM Users WHERE Card_no = ?";
            PreparedStatement stmt = conn.prepareStatement(getAcc);
            stmt.setString(1, cardNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String acc = rs.getString("Account_no");
                double bal = rs.getDouble("Balance");

                String input = JOptionPane.showInputDialog("Enter amount to deposit:");
                if (input == null) return;
                double amt = Double.parseDouble(input);

                conn.setAutoCommit(false);
                try {
                    updateBalance(conn, acc, bal + amt);
                    logTransaction(conn, acc, "DEPOSIT", amt);
                    conn.commit();
                    JOptionPane.showMessageDialog(null, "Deposit successful.");
                } catch (SQLException e) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(null, "Error. Deposit failed.");
                } finally {
                    conn.setAutoCommit(true);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Account not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkBalance(String cardNumber) {
        try (Connection conn = DatabaseCon.get_connection()) {
            String query = "SELECT Balance FROM Users WHERE Card_no = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cardNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double bal = rs.getDouble("balance");
                JOptionPane.showMessageDialog(null, "Your current balance is " + bal);
            } else {
                JOptionPane.showMessageDialog(null, "Account not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quickWithdraw(String cardNumber) {
        String[] options = {"500", "1000", "2000", "Cancel"};
        int choice = JOptionPane.showOptionDialog(null, "Choose quick withdraw amount:",
                "Quick Withdraw", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        double amt = 0;
        switch (choice) {
            case 0:
                amt = 500;
                break;
            case 1:
                amt = 1000;
                break;
            case 2:
                amt = 2000;
                break;
            default:
                JOptionPane.showMessageDialog(null, "Quick withdrawal cancelled.");
                return;
        }

        try (Connection conn = DatabaseCon.get_connection()) {
            String getAcc = "SELECT Account_no, Balance FROM Users WHERE Card_no = ?";
            PreparedStatement stmt = conn.prepareStatement(getAcc);
            stmt.setString(1, cardNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String acc = rs.getString("Account_no");
                double bal = rs.getDouble("Balance");

                if (amt > bal) {
                    JOptionPane.showMessageDialog(null, "Insufficient balance for " + amt + " withdrawal.");
                    return;
                }

                conn.setAutoCommit(false);
                try {
                    updateBalance(conn, acc, bal - amt);
                    logTransaction(conn, acc, "Q-WITHDRAW", amt);
                    conn.commit();
                    JOptionPane.showMessageDialog(null, "Quick withdrawal of " + amt + " successful.");
                } catch (SQLException e) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(null, "Quick withdrawal failed.");
                } finally {
                    conn.setAutoCommit(true);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Account not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   

    private void updateBalance(Connection conn, String accountNumber, double newBalance) throws SQLException {
        String update = "Update Users set Balance=? where Account_no=?";
        PreparedStatement stmt = conn.prepareStatement(update);
        stmt.setDouble(1, newBalance);
        stmt.setString(2, accountNumber);
        stmt.executeUpdate();
    }

    private void logTransaction(Connection conn, String accountNumber, String type, double amount) throws SQLException {
        String log = "insert into Transaction(Account_no,Amount,Account_type) values (?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(log);
        stmt.setString(1, accountNumber);
        stmt.setDouble(2, amount);
        stmt.setString(3, type);
        stmt.executeUpdate();
    }
}