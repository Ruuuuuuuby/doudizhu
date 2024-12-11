package com.ruuuuuuuby.game;

import com.ruuuuuuuby.domain.User;
import com.ruuuuuuuby.util.CodeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class LoginJFrame extends JFrame implements MouseListener {

    // List of all users
    static ArrayList<User> allUsers = new ArrayList<>();

    // Initialize some default users
    static {
        allUsers.add(new User("zhangsan", "123"));
        allUsers.add(new User("lisi", "1234"));
    }

    // Buttons for login and registration
    JButton login = new JButton();
    JButton register = new JButton();

    // Input fields for username, password, and captcha
    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();
    JTextField code = new JTextField();

    // Correct captcha
    JLabel rightCode = new JLabel();

    public LoginJFrame() {
        // Initialize the interface
        initJFrame();
        // Add components to this interface
        initView();
        // Make the current interface visible
        this.setVisible(true);
    }

    public void initView() {
        // 1. Add the username label
        Font usernameFont = new Font(null, 1, 16);
        JLabel usernameText = new JLabel("Username");
        usernameText.setForeground(Color.white);
        usernameText.setFont(usernameFont);
        usernameText.setBounds(140, 55, 55, 22);
        this.getContentPane().add(usernameText);

        // 2. Add the username input field
        username.setBounds(223, 46, 200, 30);
        this.getContentPane().add(username);

        // 3. Add the password label
        JLabel passwordText = new JLabel("Password");
        Font passwordFont = new Font(null, 1, 16);
        passwordText.setForeground(Color.white);
        passwordText.setFont(passwordFont);
        passwordText.setBounds(197, 95, 40, 22);
        this.getContentPane().add(passwordText);

        // 4. Add the password input field
        password.setBounds(263, 87, 160, 30);
        this.getContentPane().add(password);

        // Add the captcha label
        JLabel codeText = new JLabel("Captcha");
        Font codeFont = new Font(null, 1, 16);
        codeText.setForeground(Color.white);
        codeText.setFont(codeFont);
        codeText.setBounds(215, 142, 55, 22);
        this.getContentPane().add(codeText);

        // Add the captcha input field
        code.setBounds(291, 133, 100, 30);
        this.getContentPane().add(code);

        // Get the correct captcha
        String codeStr = CodeUtil.getCode();
        Font rightCodeFont = new Font(null, 1, 15);
        // Set color
        rightCode.setForeground(Color.RED);
        // Set font
        rightCode.setFont(rightCodeFont);
        // Set text
        rightCode.setText(codeStr);
        // Bind mouse events
        rightCode.addMouseListener(this);
        // Set position and size
        rightCode.setBounds(400, 133, 100, 30);
        // Add to the interface
        this.getContentPane().add(rightCode);

        // 5. Add the login button
        login.setBounds(123, 310, 128, 47);
        login.setIcon(new ImageIcon("image/login/login.png"));
        // Remove the button border
        login.setBorderPainted(false);
        // Remove the button background
        login.setContentAreaFilled(false);
        // Bind mouse events to the login button
        login.addMouseListener(this);
        this.getContentPane().add(login);

        // 6. Add the register button
        register.setBounds(256, 310, 128, 47);
        register.setIcon(new ImageIcon("image/login/register.png"));
        // Remove the button border
        register.setBorderPainted(false);
        // Remove the button background
        register.setContentAreaFilled(false);
        // Bind mouse events to the register button
        register.addMouseListener(this);
        this.getContentPane().add(register);

        // 7. Add the background image
        JLabel background = new JLabel(new ImageIcon("image/login/background.png"));
        background.setBounds(0, 0, 633, 423);
        this.getContentPane().add(background);
    }

    public void initJFrame() {
        this.setSize(633, 423); // Set width and height
        this.setTitle("Landlord Game V1.0 Login"); // Set title
        this.setDefaultCloseOperation(3); // Set close mode
        this.setLocationRelativeTo(null); // Center the window
        this.setAlwaysOnTop(true); // Set always on top
        this.setLayout(null); // Remove the default internal layout
    }

    // On click
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == login) {
            System.out.println("Login button clicked");
            // Get the content of the two input fields
            String usernameInput = username.getText();
            String passwordInput = password.getText();
            // Get the captcha entered by the user
            String codeInput = code.getText();

            // Check if the captcha is empty
            if (codeInput.length() == 0) {
                showJDialog("Captcha cannot be empty");
                return;
            }

            // Check if username and password are empty
            if (usernameInput.length() == 0 || passwordInput.length() == 0) {
                showJDialog("Username or password is empty");
                return;
            }

            // Check if the captcha is correct
            if (!codeInput.equalsIgnoreCase(rightCode.getText())) {
                showJDialog("Incorrect captcha entered");
                return;
            }

            // Check if the current user exists in the collection
            // Essentially verifying if the username and password match
            // The contains method internally depends on the equals method, so equals needs to be overridden
            User userInfo = new User(usernameInput, passwordInput);
            if (allUsers.contains(userInfo)) {
                // Close the current login interface
                this.setVisible(false);
                // Open the main game interface
                new GameJFrame();
            } else {
                showJDialog("Incorrect username or password");
            }
        } else if (e.getSource() == register) {
            System.out.println("Register button clicked");
        } else if (e.getSource() == rightCode) {
            System.out.println("Change captcha");
            // Get a new captcha
            String code = CodeUtil.getCode();
            rightCode.setText(code);
        }
    }

    public void showJDialog(String content) {
        // Create a dialog box object
        JDialog jDialog = new JDialog();
        // Set the size of the dialog box
        jDialog.setSize(200, 150);
        // Make the dialog box always on top
        jDialog.setAlwaysOnTop(true);
        // Center the dialog box
        jDialog.setLocationRelativeTo(null);
        // The dialog box must be closed before interacting with the underlying interface
        jDialog.setModal(true);

        // Create a JLabel object to manage text and add it to the dialog box
        JLabel warning = new JLabel(content);
        warning.setBounds(0, 0, 200, 150);
        jDialog.getContentPane().add(warning);
        // Display the dialog box
        jDialog.setVisible(true);
    }

    // Press and hold
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == login) {
            login.setIcon(new ImageIcon("image/login/login-press.png"));
        } else if (e.getSource() == register) {
            register.setIcon(new ImageIcon("image/login/register-press.png"));
        }
    }

    // Button released
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == login) {
            login.setIcon(new ImageIcon("image/login/login.png"));
        } else if (e.getSource() == register) {
            register.setIcon(new ImageIcon("image/login/register.png"));
        }
    }

    // Mouse enters
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    // Mouse exits
    @Override
    public void mouseExited(MouseEvent e) {
    }
}