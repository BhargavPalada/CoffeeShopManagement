import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginGUI extends JFrame {
    private JTextField phoneField;
    private JPasswordField passwordField;

    public LoginGUI() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton newUserButton = new JButton("New User");

        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(newUserButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phone = phoneField.getText();
                String password = new String(passwordField.getPassword());
                int id = validateLogin(phone, password);
                if (id != -1) {
                    dispose(); // Close the login window
                    // Open the menu page
                    MenuGUI menuGUI = new MenuGUI(id);
                    menuGUI.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        newUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNewUserWindow();
            }
        });
    }

    private int validateLogin(String phone, String password) {
        String query = "SELECT * FROM customers WHERE phone = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/DELIVERY_MANAGEMENT", "root", "Bhargav@2003");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                String columnValue = resultSet.getString("cust_id");
                try{
                    return Integer.parseInt(columnValue);
                } catch(NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
            return -1;
        } catch (SQLException ex) {
            System.out.println(ex);
            return -1;
        }
    }

    private void openNewUserWindow() {
        NewUserGUI newUserGUI = new NewUserGUI();
        newUserGUI.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
        });
    }
}
