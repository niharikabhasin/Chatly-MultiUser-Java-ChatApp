package client;

import javax.swing.*;
import java.awt.*;
import utils.DBUtil;

public class LoginScreen {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> avatarCombo;
    public static String username;
    private String avatar;

    private final String[] avatars = {"😀", "😎", "🧠", "🐱", "🐶", "👩‍💻", "🦊", "🐼", "🦄", "🕶️"};

    public LoginScreen() {
        frame = new JFrame("Chatly Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 250);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        avatarCombo = new JComboBox<>(avatars);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.");
                return;
            }

            if (DBUtil.validateLogin(user, pass)) {
                username = user;
                avatar = (String) avatarCombo.getSelectedItem();
                frame.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.setAlwaysOnTop(false);

                frame.dispose(); 
            } else {
            	JOptionPane.showMessageDialog(frame, "User not registered", "Failure", JOptionPane.WARNING_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in both username and password.");
                return;
            }

            String selectedAvatar = (String) avatarCombo.getSelectedItem();
            boolean success = DBUtil.registerUser(user, selectedAvatar, pass);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Registration successful. You can now log in.");
            } else {
                JOptionPane.showMessageDialog(frame, "Username already taken. Try another.");
            }
        });

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Choose Avatar:"));
        panel.add(avatarCombo);
        panel.add(loginButton);
        panel.add(registerButton);

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void waitForSubmit() {
        while (username == null || avatar == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }
    }
}
