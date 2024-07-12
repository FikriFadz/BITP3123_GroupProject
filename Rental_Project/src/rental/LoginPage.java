package rental;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class LoginPage extends JFrame {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginPage window = new LoginPage();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LoginPage() {
        frame = new JFrame();
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JLabel lblTitle = new JLabel("Tenant App Login");
        lblTitle.setBounds(140, 20, 200, 25);
        frame.getContentPane().add(lblTitle);
        
        JLabel lblUsername = new JLabel("Email:");
        lblUsername.setBounds(50, 70, 80, 25);
        frame.getContentPane().add(lblUsername);
        
        usernameField = new JTextField();
        usernameField.setBounds(140, 70, 200, 25);
        frame.getContentPane().add(usernameField);
        usernameField.setColumns(10);
        
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 120, 80, 25);
        frame.getContentPane().add(lblPassword);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(140, 120, 200, 25);
        frame.getContentPane().add(passwordField);
        
        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                // Perform login validation by sending a request to the backend
                try {
                    URL url = new URL("http://localhost/dad_project/login.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    String urlParameters = "email=" + email + "&password=" + password;
                    OutputStream os = conn.getOutputStream();
                    os.write(urlParameters.getBytes());
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Log the response for debugging
                        System.out.println("Response from server: " + response.toString());

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        if ("success".equals(jsonResponse.getString("status"))) {
                            // If login is successful, open the TenantFrontEnd window
                            TenantFrontEnd tenantFrontEnd = new TenantFrontEnd(email);
                            tenantFrontEnd.setVisible(true);
                            frame.dispose(); // Close the login window
                        } else {
                            // Display an error message dialog
                            JOptionPane.showMessageDialog(frame, jsonResponse.getString("message"), "Login Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        System.out.println("POST request not worked");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnLogin.setBounds(140, 200, 100, 25);
        frame.getContentPane().add(btnLogin);
    }
}
