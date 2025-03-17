import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class PersistentLoginSystem {

    private static final Logger logger = Logger.getLogger(PersistentLoginSystem.class.getName());
    private static final UserData userData = new UserData();

    public static void main(String[] args) {
        setupLogger();
        userData.loadData();

        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(1920, 1080);
        loginFrame.setLayout(null);
        loginFrame.getContentPane().setBackground(new Color(41, 50, 65));

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(255, 255, 255));
        loginPanel.setBounds(810, 390, 300, 270);

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(41, 50, 65));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 10, 300, 30);
        loginPanel.add(titleLabel);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setBounds(20, 50, 100, 20);
        loginPanel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(20, 70, 260, 30);
        userField.setBorder(BorderFactory.createLineBorder(new Color(41, 50, 65), 1));
        loginPanel.add(userField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passLabel.setBounds(20, 110, 100, 20);
        loginPanel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(20, 130, 260, 30);
        passField.setBorder(BorderFactory.createLineBorder(new Color(41, 50, 65), 1));
        loginPanel.add(passField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20, 180, 260, 30);
        loginButton.setBackground(new Color(41, 50, 65));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginPanel.add(loginButton);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(20, 220, 260, 30);
        signUpButton.setBackground(new Color(88, 130, 193));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginPanel.add(signUpButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                if (userData.login(username, password)) {
                    logger.info("Login successful for username: " + username);
                    loginFrame.dispose();
                    showMainWindow(username);
                } else {
                    logger.warning("Login failed for username: " + username);
                    JOptionPane.showMessageDialog(loginFrame, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSignUpWindow();
            }
        });

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);
    }

    private static void showSignUpWindow() {
        JFrame signUpFrame = new JFrame("Sign Up");
        signUpFrame.setSize(400, 350);
        signUpFrame.setLayout(null);
        signUpFrame.setLocationRelativeTo(null);

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(null);
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setBounds(50, 20, 300, 270);
        signUpFrame.add(registerPanel);

        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 10, 300, 30);
        registerPanel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(20, 50, 100, 20);
        registerPanel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(20, 70, 260, 30);
        registerPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(20, 110, 100, 20);
        registerPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 130, 260, 30);
        registerPanel.add(passwordField);

        JButton registerButton = new JButton("Sign Up");
        registerButton.setBounds(20, 180, 260, 30);
        registerButton.setBackground(new Color(41, 50, 65));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerPanel.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (userData.isUserExists(username)) {
                    JOptionPane.showMessageDialog(signUpFrame, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(signUpFrame, "All fields must be filled!", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    userData.register(username, password);
                    JOptionPane.showMessageDialog(signUpFrame, "Registration successful!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    signUpFrame.dispose();
                }
            }
        });

        signUpFrame.setVisible(true);
    }

    private static void showMainWindow(String username) {
        JFrame mainFrame = new JFrame("Main Window");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1920, 1080);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(41, 50, 65));
        mainFrame.add(welcomeLabel, BorderLayout.CENTER);

        mainFrame.setVisible(true);
    }

    static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String username;
        private final String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    static class UserData {
        private final List<User> users = new ArrayList<>();
        private static final String FILE_NAME = "users.dat";

        public void register(String username, String password) {
            users.add(new User(username, password));
            saveData();
        }

        public boolean login(String username, String password) {
            return users.stream().anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
        }

        public boolean isUserExists(String username) {
            return users.stream().anyMatch(user -> user.getUsername().equals(username));
        }

        @SuppressWarnings("unchecked")
        public void loadData() {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                Object obj = ois.readObject();
                if (obj instanceof List<?>) {
                    users.addAll((List<User>) obj);
                    logger.info("User data loaded successfully.");
                }
            } catch (FileNotFoundException e) {
                logger.warning("No user data found.");
            } catch (IOException | ClassNotFoundException e) {
                logger.severe("Failed to load user data: " + e.getMessage());
            }
        }

        public void saveData() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(users);
                logger.info("User data saved successfully.");
            } catch (IOException e) {
                logger.severe("Failed to save user data: " + e.getMessage());
            }
        }
    }

    private static void setupLogger() {
        try {
            LogManager.getLogManager().reset();
            logger.setLevel(Level.ALL);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            FileHandler fileHandler = new FileHandler("login_system.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            logger.info("Logger initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }
}
