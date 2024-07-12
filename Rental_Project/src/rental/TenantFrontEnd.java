package rental;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;

public class TenantFrontEnd extends JFrame {
    private JTextField rentalAmountField;
    private JTable detailsTable;
    private JTable historyTable;
    private DefaultTableModel tableModel, tableModel2;
    private String userEmail;
    private JTextArea detailsTextArea;
    private JComboBox<String> monthYearComboBox;
    private JScrollPane scrollPane, scrollPane2;

    public TenantFrontEnd(String userEmail) {
        this.userEmail = userEmail;

        setTitle("Tenant Application");
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        
        JLabel lblTitle = new JLabel("Tenant Dashboard");
        lblTitle.setBounds(340, 20, 150, 30);
        getContentPane().add(lblTitle);
        
        JButton btnViewPropertyDetails = new JButton("View Property Details");
        btnViewPropertyDetails.setBounds(150, 100, 200, 30);
        btnViewPropertyDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewPropertyDetails();
            }
        });
        getContentPane().add(btnViewPropertyDetails);
        
        JButton btnViewMonthlyRental = new JButton("View Due Rent Detail");
        btnViewMonthlyRental.setBounds(450, 100, 200, 30);
        btnViewMonthlyRental.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewMonthlyRentalDetails();
            }
        });
        getContentPane().add(btnViewMonthlyRental);
        
        JButton btnPayRent = new JButton("Pay Rent");
        btnPayRent.setBounds(495, 298, 200, 30);
        btnPayRent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                payRent();
            }
        });
        getContentPane().add(btnPayRent);
        
        JButton btnViewPaymentHistory = new JButton("View Payment History");
        btnViewPaymentHistory.setBounds(290, 175, 200, 30);
        btnViewPaymentHistory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewPaymentHistory();
            }
        });
        getContentPane().add(btnViewPaymentHistory);
        
        JLabel lblRentalAmount = new JLabel("Rental Amount:");
        lblRentalAmount.setBounds(150, 257, 100, 30);
        getContentPane().add(lblRentalAmount);
        
        rentalAmountField = new JTextField();
        rentalAmountField.setBounds(260, 257, 200, 30);
        getContentPane().add(rentalAmountField);
        rentalAmountField.setColumns(10);
        
        // Details TextArea for Property Details
        detailsTextArea = new JTextArea();
        detailsTextArea.setBounds(68, 350, 678, 150);
        getContentPane().add(detailsTextArea);
        
        // Define table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Property ID");
        tableModel.addColumn("Rent Amount");
        tableModel.addColumn("Utility Amount");
        tableModel.addColumn("Total Rent");
        tableModel.addColumn("Month/Year");
        tableModel.addColumn("Payment Status");

        // Set up JTable
        detailsTable = new JTable(tableModel);
        scrollPane = new JScrollPane(detailsTable);
        scrollPane.setBounds(68, 350, 678, 150);
        getContentPane().add(scrollPane);
        
        tableModel2 = new DefaultTableModel();
        tableModel2.addColumn("Property ID");
        tableModel2.addColumn("Rent Amount");
        tableModel2.addColumn("Utility Amount");
        tableModel2.addColumn("Total Rent");
        tableModel2.addColumn("Month/Year");
        tableModel2.addColumn("Payment Date");
        
        historyTable = new JTable(tableModel2);
        scrollPane2 = new JScrollPane(historyTable);
        scrollPane2.setBounds(68, 350, 678, 150);
        getContentPane().add(scrollPane2);
        
        JLabel lblMonthYear = new JLabel("Rent Month");
        lblMonthYear.setBounds(150, 298, 100, 30);
        getContentPane().add(lblMonthYear);
        
        monthYearComboBox = new JComboBox<>();
        monthYearComboBox.setBounds(260, 300, 200, 26);
        getContentPane().add(monthYearComboBox);
        
        populateMonthYearComboBox();
    }

    private void populateMonthYearComboBox() {
        try {
            URL url = new URL("http://localhost/dad_project/get_due_rent_months.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String urlParameters = "email=" + userEmail;
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

                JSONObject jsonResponse = new JSONObject(response.toString());
                if ("success".equals(jsonResponse.getString("status"))) {
                    JSONArray dueMonths = jsonResponse.getJSONArray("due_months");
                    monthYearComboBox.removeAllItems();
                    for (int i = 0; i < dueMonths.length(); i++) {
                        monthYearComboBox.addItem(dueMonths.getString(i));
                    }
                } else {
                    JOptionPane.showMessageDialog(this, jsonResponse.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("POST request not worked");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void viewPropertyDetails() {
        try {
            URL url = new URL("http://localhost/dad_project/get_user_property_details.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String urlParameters = "email=" + userEmail;
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
                    JSONObject property = jsonResponse.getJSONObject("property");
                    String propertyDetails = "Property ID: " + property.getInt("id") + "\n"
                                            + "Name: " + property.getString("name") + "\n"
                                            + "Address: " + property.getString("address") + "\n"
                                            + "Amount: " + property.getDouble("amount") + "\n"
                                            + "Tenant Name: " + property.getString("tenant_name");
                    detailsTextArea.setText(propertyDetails);
                    detailsTextArea.setVisible(true);  // Show text area
//                    historyTable.setVisible(false);
//                    detailsTable.setVisible(false);
                    scrollPane.setVisible(false); // Hide details table
                    scrollPane2.setVisible(false); // Hide history table
                } else {
                    JOptionPane.showMessageDialog(this, jsonResponse.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("POST request not worked");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void viewMonthlyRentalDetails() {
        try {
            URL url = new URL("http://localhost/dad_project/get_monthly_rental_details.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String urlParameters = "email=" + userEmail;
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
                    JSONArray details = jsonResponse.getJSONArray("details");
                    tableModel.setRowCount(0); // Clear existing data
                    for (int i = 0; i < details.length(); i++) {
                        JSONObject detail = details.getJSONObject(i);
                        tableModel.addRow(new Object[]{
                            detail.getInt("property_id"),
                            new BigDecimal(detail.getString("rent_amount")),
                            new BigDecimal(detail.getString("utillity_amount")),
                            new BigDecimal(detail.getString("total_rent")),
                            detail.getString("month_year"),
                            detail.getString("status"),
                            //detail.optString("payment_date", "N/A")
                        });
                    }
                    scrollPane.setVisible(true);  // Show details table
                    scrollPane2.setVisible(false); 
//                    detailsTable.setVisible(true);
//                    historyTable.setVisible(false);
                    detailsTextArea.setVisible(false);  // Hide text area
                    detailsTextArea.setText(""); 
                } else {
                    JOptionPane.showMessageDialog(this, jsonResponse.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("POST request not worked");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void payRent() {
        try {
            String selectedMonthYear = (String) monthYearComboBox.getSelectedItem();
            if (selectedMonthYear == null || selectedMonthYear.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a month/year.");
                return;
            }

            BigDecimal enteredAmount = new BigDecimal(rentalAmountField.getText());

            // Fetch the total rent for the selected month/year
            URL url = new URL("http://localhost/dad_project/get_rent_details.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String urlParameters = "email=" + userEmail + "&month_year=" + selectedMonthYear;
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

                JSONObject jsonResponse = new JSONObject(response.toString());
                if ("success".equals(jsonResponse.getString("status"))) {
                    BigDecimal totalRent = new BigDecimal(jsonResponse.getString("total_rent"));
                    if (enteredAmount.compareTo(totalRent) == 0) {
                        // Update payment status and date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String paymentDate = sdf.format(new Date());

                        URL updateUrl = new URL("http://localhost/dad_project/update_rent_status.php");
                        HttpURLConnection updateConn = (HttpURLConnection) updateUrl.openConnection();
                        updateConn.setRequestMethod("POST");
                        updateConn.setDoOutput(true);

                        String updateParameters = "email=" + userEmail + "&month_year=" + selectedMonthYear + "&payment_date=" + paymentDate;
                        OutputStream updateOs = updateConn.getOutputStream();
                        updateOs.write(updateParameters.getBytes());
                        updateOs.flush();
                        updateOs.close();

                        int updateResponseCode = updateConn.getResponseCode();
                        if (updateResponseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader updateIn = new BufferedReader(new InputStreamReader(updateConn.getInputStream()));
                            String updateInputLine;
                            StringBuilder updateResponse = new StringBuilder();

                            while ((updateInputLine = updateIn.readLine()) != null) {
                                updateResponse.append(updateInputLine);
                            }
                            updateIn.close();

                            JSONObject updateJsonResponse = new JSONObject(updateResponse.toString());
                            if ("success".equals(updateJsonResponse.getString("status"))) {
                                JOptionPane.showMessageDialog(this, "Rent paid successfully!");
                                populateMonthYearComboBox(); // Refresh the combo box
                            } else {
                                JOptionPane.showMessageDialog(this, updateJsonResponse.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to update rent status. Response code: " + updateResponseCode);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Entered amount does not match the total rent.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, jsonResponse.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to fetch rent details. Response code: " + responseCode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void viewPaymentHistory() {
        try {
            URL url = new URL("http://localhost/dad_project/get_paid_rent.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String urlParameters = "email=" + userEmail;
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
                    JSONArray history = jsonResponse.getJSONArray("history");
                    tableModel2.setRowCount(0); // Clear existing data
                    for (int i = 0; i < history.length(); i++) {
                        JSONObject detail = history.getJSONObject(i);
                        tableModel2.addRow(new Object[]{
                            detail.getInt("property_id"),
                            new BigDecimal(detail.getString("rent_amount")),
                            new BigDecimal(detail.getString("utillity_amount")),
                            new BigDecimal(detail.getString("total_rent")),
                            detail.getString("month_year"),
                            detail.optString("payment_date", "N/A")
                        });
                    }
                    scrollPane.setVisible(false);
                    scrollPane2.setVisible(true); 
//                    historyTable.setVisible(true);
//                    detailsTable.setVisible(false);
                    detailsTextArea.setVisible(false);
                    detailsTextArea.setText(""); 
                } else {
                    JOptionPane.showMessageDialog(this, jsonResponse.getString("message"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("POST request not worked");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TenantFrontEnd frame = new TenantFrontEnd("test@example.com");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
