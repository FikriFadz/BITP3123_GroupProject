package rental;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.util.Calendar;

public class LandlordFrontEnd extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTable propertyTable;
    private JComboBox<String> propertyComboBoxUpdate;
    private JComboBox<String> propertyComboBoxRemove;
    private JComboBox<String> tenantComboBoxList;
    private JComboBox<String> tenantComboBoxUpdate;
    private JComboBox<String> propertyComboBoxSendRent;
    private JTextField tenantNameField;
    private JTextField tenantEmailField;
    private JTextField rentAmountField;
    private JTextField utilitiesAmountField;
    public LandlordFrontEnd() {
        setTitle("Landlord Application");
        setSize(910, 797);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Creating the individual panels
        JPanel listPropertyPanel = createListPropertyPanel();
        JPanel viewPropertiesPanel = createViewPropertiesPanel();
        JPanel updatePropertyPanel = createUpdatePropertyPanel();
        JPanel removePropertyPanel = createRemovePropertyPanel();
        JPanel sendRentalDetailsPanel = createSendRentalDetailsPanel();
        JPanel addTenantPanel = createAddTenantPanel();

        // Adding panels to the main panel
        mainPanel.add(listPropertyPanel, "List Property");
        mainPanel.add(viewPropertiesPanel, "View Properties");
        mainPanel.add(updatePropertyPanel, "Update Property");
        mainPanel.add(removePropertyPanel, "Remove Property");
        mainPanel.add(sendRentalDetailsPanel, "Send Rental Details");
        mainPanel.add(addTenantPanel, "Add Tenant");

        // Navigation panel
        JPanel navPanel = new JPanel();
        String[] buttons = {"List Property", "View Properties", "Update Property", "Remove Property", "Send Rental Details", "Add Tenant",};
        for (String btn : buttons) {
            JButton button = new JButton(btn);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(mainPanel, btn);
                }
            });
            navPanel.add(button);
        }

        // Adding panels to the frame
        getContentPane().add(navPanel, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createListPropertyPanel() {
        JPanel listPropertyPanel = new JPanel();
        listPropertyPanel.setLayout(null);

        JLabel label = new JLabel("Property Name:");
        label.setBounds(41, 2, 126, 91);
        listPropertyPanel.add(label);
        JTextField propertyNameField = new JTextField();
        propertyNameField.setBounds(371, 2, 392, 91);
        listPropertyPanel.add(propertyNameField);

        JLabel label_1 = new JLabel("Address:");
        label_1.setBounds(41, 93, 126, 91);
        listPropertyPanel.add(label_1);
        JTextField addressField = new JTextField();
        addressField.setBounds(371, 93, 392, 91);
        listPropertyPanel.add(addressField);

        JLabel label_2 = new JLabel("Rent Amount:");
        label_2.setBounds(41, 184, 126, 91);
        listPropertyPanel.add(label_2);
        JTextField rentField = new JTextField();
        rentField.setBounds(371, 184, 392, 91);
        listPropertyPanel.add(rentField);

        // ComboBox to hold tenant names
        tenantComboBoxList = new JComboBox<>();
        tenantComboBoxList.setBounds(371, 271, 392, 91);
        listPropertyPanel.add(tenantComboBoxList);
        
        // Populate tenant names in the combo box
        populateTenantsComboBox(tenantComboBoxList);

        JButton listButton = new JButton("List Property");
        listButton.setBounds(423, 373, 301, 91);
        listPropertyPanel.add(listButton);
        
        JLabel label_2_1 = new JLabel("Tenant Name:");
        label_2_1.setBounds(41, 271, 126, 91);
        listPropertyPanel.add(label_2_1);

        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String propertyName = propertyNameField.getText();
                    String address = addressField.getText();
                    String rentAmount = rentField.getText();
                    String tenantName = (String) tenantComboBoxList.getSelectedItem(); 

                    String urlParameters = "property_name=" + propertyName + "&address=" + address + "&rent_amount=" + rentAmount + "&tenant_name=" + tenantName;
                    byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

                    URL url = new URL("http://localhost/dad_project/insert_property.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("charset", "utf-8");
                    conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
                    conn.setUseCaches(false);

                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(postData);
                    }

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(listPropertyPanel, "Property listed successfully!");
                        populatePropertiesComboBox(propertyComboBoxUpdate);
                        populatePropertiesComboBox(propertyComboBoxRemove);
                    } else {
                        JOptionPane.showMessageDialog(listPropertyPanel, "Failed to list property. Response code: " + responseCode);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(listPropertyPanel, "Error: " + ex.getMessage());
                }
            }
        });

        return listPropertyPanel;
    }
    

    private JPanel createViewPropertiesPanel() {
        JPanel listedPropertyPanel = new JPanel(new BorderLayout());
        listedPropertyPanel.add(new JLabel("View Listed Properties"), BorderLayout.NORTH);

        // Initializing the table with no data
        String[] columnNames = {"Property Name", "Address", "Rent Amount", "Tenant Name"};
        Object[][] data = {};

        propertyTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(propertyTable);
        listedPropertyPanel.add(scrollPane, BorderLayout.CENTER);

        propertyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> fetchPropertyData());
        listedPropertyPanel.add(refreshButton, BorderLayout.SOUTH);

        return listedPropertyPanel;
    }
    
    private JPanel createUpdatePropertyPanel() {
        JPanel updatePropertyPanel = new JPanel();
        updatePropertyPanel.setLayout(null);

        JLabel label = new JLabel("Select Property:");
        label.setBounds(10, 3, 298, 68);
        updatePropertyPanel.add(label);
        propertyComboBoxUpdate = new JComboBox<>();
        propertyComboBoxUpdate.setBounds(308, 3, 208, 68);
        updatePropertyPanel.add(propertyComboBoxUpdate);
        
        populatePropertiesComboBox(propertyComboBoxUpdate);

        JButton refreshButton = new JButton("Refresh Properties");
        refreshButton.setBounds(526, 3, 246, 68);
        updatePropertyPanel.add(refreshButton);

        JLabel label_1 = new JLabel("New Property Name:");
        label_1.setBounds(10, 88, 298, 68);
        updatePropertyPanel.add(label_1);
        JTextField newPropertyNameField = new JTextField();
        newPropertyNameField.setBounds(308, 88, 464, 68);
        updatePropertyPanel.add(newPropertyNameField);

        JLabel label_2 = new JLabel("New Address:");
        label_2.setBounds(10, 167, 298, 68);
        updatePropertyPanel.add(label_2);
        JTextField newAddressField = new JTextField();
        newAddressField.setBounds(308, 167, 464, 68);
        updatePropertyPanel.add(newAddressField);

        JLabel label_3 = new JLabel("New Rent Amount:");
        label_3.setBounds(10, 246, 298, 68);
        updatePropertyPanel.add(label_3);
        JTextField newRentField = new JTextField();
        newRentField.setBounds(308, 246, 464, 68);
        updatePropertyPanel.add(newRentField);

        JLabel label_4 = new JLabel("New Tenant Name:");
        label_4.setBounds(10, 323, 298, 68);
        updatePropertyPanel.add(label_4);
        
        tenantComboBoxUpdate = new JComboBox<>();
        tenantComboBoxUpdate.setBounds(308, 323, 464, 68);
        updatePropertyPanel.add(tenantComboBoxUpdate);
        
        populateTenantsComboBox(tenantComboBoxUpdate);

        JButton updateButton = new JButton("Update Property");
        updateButton.setBounds(358, 402, 392, 68);
        updatePropertyPanel.add(updateButton);
        
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String selectedProperty = (String) propertyComboBoxUpdate.getSelectedItem();
                    if (selectedProperty == null || selectedProperty.isEmpty()) {
                        JOptionPane.showMessageDialog(updatePropertyPanel, "Please select a property.");
                        return;
                    }

                    int propertyId = Integer.parseInt(selectedProperty.split(":")[0].trim());
                    String propertyName = newPropertyNameField.getText();
                    String address = newAddressField.getText();
                    double rentAmount = Double.parseDouble(newRentField.getText());
                    String tenantName = (String) tenantComboBoxUpdate.getSelectedItem(); 

                    updateProperty(propertyId, propertyName, address, rentAmount, tenantName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(updatePropertyPanel, "Error: " + ex.getMessage());
                }
            }
        });
        
        propertyComboBoxUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedProperty = (String) propertyComboBoxUpdate.getSelectedItem();
                if (selectedProperty != null && !selectedProperty.isEmpty()) {
                    int propertyId = Integer.parseInt(selectedProperty.split(":")[0].trim());
                    fetchPropertyDetails(propertyId, newPropertyNameField, newAddressField, newRentField, tenantComboBoxUpdate);
                }
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populatePropertiesComboBox(propertyComboBoxUpdate);
            }
        });
        


        return updatePropertyPanel;
    }

    private JPanel createRemovePropertyPanel() {
        JPanel removePropertyPanel = new JPanel();
        removePropertyPanel.setLayout(null);

        JLabel label = new JLabel("Select Property:");
        label.setBounds(10, 59, 308, 102);
        removePropertyPanel.add(label);
        propertyComboBoxRemove = new JComboBox<>();
        propertyComboBoxRemove.setBounds(328, 74, 244, 73);
        removePropertyPanel.add(propertyComboBoxRemove);

        JButton refreshButton = new JButton("Refresh Properties");
        refreshButton.setBounds(582, 74, 155, 73);
        removePropertyPanel.add(refreshButton);

        JButton removeButton = new JButton("Remove Property");
        removeButton.setBounds(328, 172, 409, 73);
        removePropertyPanel.add(removeButton);

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String selectedProperty = (String) propertyComboBoxRemove.getSelectedItem();
                    if (selectedProperty == null || selectedProperty.isEmpty()) {
                        JOptionPane.showMessageDialog(removePropertyPanel, "Please select a property.");
                        return;
                    }

                    int propertyId = Integer.parseInt(selectedProperty.split(":")[0].trim());

                    deleteProperty(propertyId, propertyComboBoxRemove);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(removePropertyPanel, "Error: " + ex.getMessage());
                }
            }
        });
        
        populatePropertiesComboBox(propertyComboBoxRemove);

        return removePropertyPanel;
    }
    
 

    private JPanel createSendRentalDetailsPanel() {
        JPanel sendRentAmountPanel = new JPanel();
        sendRentAmountPanel.setLayout(null);

        JLabel label = new JLabel("Tenant Email:");
        label.setBounds(29, 201, 134, 76);
        sendRentAmountPanel.add(label);
        tenantEmailField = new JTextField();
        tenantEmailField.setEditable(false);
        tenantEmailField.setBounds(307, 201, 468, 76);
        sendRentAmountPanel.add(tenantEmailField);

        JLabel label_1 = new JLabel("Rent Amount:");
        label_1.setBounds(29, 288, 134, 76);
        sendRentAmountPanel.add(label_1);
        rentAmountField = new JTextField();
        rentAmountField.setEditable(false);
        rentAmountField.setBounds(307, 288, 468, 76);
        sendRentAmountPanel.add(rentAmountField);

        JButton sendButton = new JButton("Send Rental Details");
        sendButton.setBounds(307, 636, 384, 65);
        sendRentAmountPanel.add(sendButton);

        JLabel lblSelectProperty = new JLabel("Tenant Name:");
        lblSelectProperty.setBounds(29, 109, 134, 87);
        sendRentAmountPanel.add(lblSelectProperty);

        tenantNameField = new JTextField();
        tenantNameField.setEditable(false);
        tenantNameField.setBounds(307, 114, 468, 76);
        sendRentAmountPanel.add(tenantNameField);

        JLabel lblSelectProperty_1 = new JLabel("Select Property:");
        lblSelectProperty_1.setBounds(29, 11, 134, 87);
        sendRentAmountPanel.add(lblSelectProperty_1);

        propertyComboBoxSendRent = new JComboBox<>();
        propertyComboBoxSendRent.setBounds(307, 11, 264, 87);
        sendRentAmountPanel.add(propertyComboBoxSendRent);

        populatePropertiesComboBox(propertyComboBoxSendRent);

        JButton refreshButton = new JButton("Refresh Property");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                populatePropertiesComboBox(propertyComboBoxSendRent);
            }
        });
        refreshButton.setBounds(598, 11, 177, 87);
        sendRentAmountPanel.add(refreshButton);

        utilitiesAmountField = new JTextField();
        utilitiesAmountField.setBounds(307, 375, 468, 76);
        sendRentAmountPanel.add(utilitiesAmountField);

        JLabel label_1_1 = new JLabel("Utilities Amount:");
        label_1_1.setBounds(29, 375, 134, 76);
        sendRentAmountPanel.add(label_1_1);

        // Month and Year combo boxes
        JLabel lblSelectMonth = new JLabel("Select Month:");
        lblSelectMonth.setBounds(29, 462, 134, 76);
        sendRentAmountPanel.add(lblSelectMonth);

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setBounds(307, 462, 200, 76);
        sendRentAmountPanel.add(monthComboBox);

        JLabel lblSelectYear = new JLabel("Select Year:");
        lblSelectYear.setBounds(29, 549, 134, 76);
        sendRentAmountPanel.add(lblSelectYear);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[10];
        for (int i = 0; i < 10; i++) {
            years[i] = String.valueOf(currentYear - 5 + i);
        }
        JComboBox<String> yearComboBox = new JComboBox<>(years);
        yearComboBox.setBounds(307, 549, 200, 76);
        sendRentAmountPanel.add(yearComboBox);

        propertyComboBoxSendRent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedProperty = (String) propertyComboBoxSendRent.getSelectedItem();
                if (selectedProperty != null && !selectedProperty.isEmpty()) {
                    int propertyId = Integer.parseInt(selectedProperty.split(":")[0].trim());
                    fetchTenantDetails(propertyId, tenantNameField, tenantEmailField, rentAmountField);
                }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendRentalDetails(monthComboBox.getSelectedItem().toString(), yearComboBox.getSelectedItem().toString());
            }
        });

        return sendRentAmountPanel;
    }
    
    private JPanel createAddTenantPanel() {
    	JPanel addTenantPanel = new JPanel();
        addTenantPanel.setLayout(null);
    	
        JLabel label = new JLabel("Tenant Name:");
        label.setBounds(23, 0, 359, 110);
        addTenantPanel.add(label);
        JTextField tenantNameField = new JTextField();
        tenantNameField.setBounds(392, 0, 382, 110);
        addTenantPanel.add(tenantNameField);

        JLabel label_1 = new JLabel("Tenant Email:");
        label_1.setBounds(23, 110, 359, 110);
        addTenantPanel.add(label_1);
        JTextField tenantEmailField = new JTextField();
        tenantEmailField.setBounds(392, 110, 382, 110);
        addTenantPanel.add(tenantEmailField);

        JLabel label_2 = new JLabel("Tenant Phone Number:");
        label_2.setBounds(23, 220, 359, 110);
        addTenantPanel.add(label_2);
        JTextField phoneNumberField = new JTextField();
        phoneNumberField.setBounds(392, 220, 382, 110);
        addTenantPanel.add(phoneNumberField);
        
        JButton addTenantButton = new JButton("Add Tenant");
        addTenantButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                try {
                    String tenantName = tenantNameField.getText();
                    String tenantEmail = tenantEmailField.getText();
                    String phoneNumber = phoneNumberField.getText();

                    String urlParameters = "tenant_name=" + tenantName + "&email=" + tenantEmail + "&phone_number=" + phoneNumber;
                    byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

                    URL url = new URL("http://localhost/dad_project/insert_tenant.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("charset", "utf-8");
                    conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
                    conn.setUseCaches(false);

                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(postData);
                    }

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(addTenantPanel, "Tenant added successfully!");
                        populateTenantsComboBox(tenantComboBoxList);
                        populateTenantsComboBox(tenantComboBoxUpdate);
                    } else {
                        JOptionPane.showMessageDialog(addTenantPanel, "Failed to add tenant. Response code: " + responseCode);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(addTenantPanel, "Error: " + ex.getMessage());
                }
            }
        });
        
        addTenantButton.setBounds(392, 341, 382, 99);
        addTenantPanel.add(addTenantButton);
    	
		return addTenantPanel;
    }
    
    private void fetchPropertyData() {
        try {
            URL url = new URL("http://localhost/dad_project/get_properties.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONArray propertiesArray = new JSONArray(response.toString());
                Object[][] data = new Object[propertiesArray.length()][4];

                for (int i = 0; i < propertiesArray.length(); i++) {
                    JSONObject property = propertiesArray.getJSONObject(i);
                    data[i][0] = property.getString("name");
                    data[i][1] = property.getString("address");
                    data[i][2] = property.getDouble("amount");
                    data[i][3] = property.getString("tenant_name");
                }

                // Update table model
                String[] columnNames = {"Property Name", "Address", "Rent Amount", "Tenant Name"};
                propertyTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

                adjustColumnWidths(propertyTable);

            } else {
                JOptionPane.showMessageDialog(this, "Failed to fetch properties. Response code: " + responseCode);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void adjustColumnWidths(JTable table) {
        // Set preferred widths for "Rent Amount" and "Tenant Name"
        TableColumn rentAmountColumn = table.getColumnModel().getColumn(2);
        TableColumn tenantNameColumn = table.getColumnModel().getColumn(3);
        rentAmountColumn.setPreferredWidth(100); // Adjust as needed
        tenantNameColumn.setPreferredWidth(100); // Adjust as needed

        for (int column = 0; column < table.getColumnCount(); column++) {
            if (column == 2 || column == 3) continue; // Skip "Rent Amount" and "Tenant Name" columns
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                // We've exceeded the maximum width, no need to check other rows
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth);
        }
    }
    
    private void fetchTenantDetails(int propertyId, JTextField tenantNameField, JTextField tenantEmailField, JTextField rentAmountField) {
        try {
            URL url = new URL("http://localhost/dad_project/get_tenant_details.php?property_id=" + propertyId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONObject tenantDetails = new JSONObject(response.toString());
                if (tenantDetails.has("error")) {
                    JOptionPane.showMessageDialog(null, tenantDetails.getString("error"));
                } else {
                    tenantNameField.setText(tenantDetails.getString("tenantName"));
                    tenantEmailField.setText(tenantDetails.getString("tenantEmail"));
                    rentAmountField.setText(String.valueOf(tenantDetails.getInt("rentAmount")));
                }

            } else {
                JOptionPane.showMessageDialog(null, "Failed to fetch tenant details. Response code: " + responseCode);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
    
    private void fetchPropertyDetails(int propertyId, JTextField propertyNameField, JTextField addressField, JTextField rentField, JComboBox<String> tenantComboBox) {
        try {
            URL url = new URL("http://localhost/dad_project/get_property_details.php?id=" + propertyId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONObject property = new JSONObject(response.toString());
                String propertyName = property.getString("name");
                String address = property.getString("address");
                double rentAmount = property.getDouble("amount");
                String tenantName = property.getString("tenant_name");

                propertyNameField.setText(propertyName);
                addressField.setText(address);
                rentField.setText(String.valueOf(rentAmount));
                
                // Select the tenant in the tenantComboBox
                boolean tenantFound = false;
                for (int i = 0; i < tenantComboBox.getItemCount(); i++) {
                    if (tenantComboBox.getItemAt(i).equals(tenantName)) {
                        tenantComboBox.setSelectedIndex(i);
                        tenantFound = true;
                        break;
                    }
                }

                if (!tenantFound) {
                    tenantComboBox.setSelectedItem("none");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Failed to fetch property details. Response code: " + responseCode);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
    
    private void sendRentalDetails(String month, String year) {
        try {
            String selectedProperty = (String) propertyComboBoxSendRent.getSelectedItem();
            if (selectedProperty == null || selectedProperty.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a property.");
                return;
            }

            int propertyId = Integer.parseInt(selectedProperty.split(":")[0].trim());
            String email = tenantEmailField.getText();
            double rentAmount = Double.parseDouble(rentAmountField.getText());
            double utilityAmount = Double.parseDouble(utilitiesAmountField.getText());
            double totalRent = rentAmount + utilityAmount;
            String monthYear = month + "-" + year;

            URL url = new URL("http://localhost/dad_project/insert_rent_details.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String urlParameters = "property_id=" + propertyId + "&email=" + email + "&rent_amount=" + rentAmount + "&utility_amount=" + utilityAmount + "&total_rent=" + totalRent + "&month_year=" + monthYear;
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
                    JOptionPane.showMessageDialog(null, "Rental details sent successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, jsonResponse.getString("message"));
                }

            } else {
                JOptionPane.showMessageDialog(null, "Failed to send rental details. Response code: " + responseCode);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void updateProperty(int propertyId, String propertyName, String address, double rentAmount, String tenantName) {
        try {
            URL url = new URL("http://localhost/dad_project/update_property.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject propertyData = new JSONObject();
            propertyData.put("id", propertyId);
            propertyData.put("name", propertyName);
            propertyData.put("address", address);
            propertyData.put("amount", rentAmount);
            propertyData.put("tenant_name", tenantName);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = propertyData.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

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
                if (jsonResponse.has("success")) {
                    JOptionPane.showMessageDialog(this, "Property updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error: " + jsonResponse.getString("error"));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update property. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void populatePropertiesComboBox(JComboBox<String> propertyComboBox) {
        try {
            URL url = new URL("http://localhost/dad_project/get_properties.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONArray propertiesArray = new JSONArray(response.toString());
                propertyComboBox.removeAllItems();
                for (int i = 0; i < propertiesArray.length(); i++) {
                    JSONObject property = propertiesArray.getJSONObject(i);
                    int id = property.getInt("id");
                    String name = property.getString("name");
                    propertyComboBox.addItem(id + ": " + name);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Failed to fetch properties. Response code: " + responseCode);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
    
    private void populateTenantsComboBox(JComboBox<String> tenantComboBox) {
        try {
            URL url = new URL("http://localhost/dad_project/get_tenants.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                tenantComboBox.removeAllItems();
                // Parse JSON response
                JSONArray tenantsArray = new JSONArray(response.toString());
                tenantComboBox.addItem("none");
                for (int i = 0; i < tenantsArray.length(); i++) {
                    JSONObject tenant = tenantsArray.getJSONObject(i);
                    String tenantName = tenant.getString("name");
                    tenantComboBox.addItem(tenantName);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Failed to fetch tenants. Response code: " + responseCode);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void deleteProperty(int propertyId, JComboBox<String> propertyComboBox) {
        try {
            URL url = new URL("http://localhost/dad_project/delete_property.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject propertyData = new JSONObject();
            propertyData.put("id", propertyId);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = propertyData.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

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
                if (jsonResponse.has("success")) {
                    JOptionPane.showMessageDialog(this, "Property deleted successfully!");
                    populatePropertiesComboBox(propertyComboBox); // Refresh combo box
                } else {
                    JOptionPane.showMessageDialog(this, "Error: " + jsonResponse.getString("error"));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete property. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    

    public static void main(String[] args) {
        new LandlordFrontEnd();
    }
}
