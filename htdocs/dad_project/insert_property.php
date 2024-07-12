<?php
include 'conn.php';
// Check if POST request contains all required fields
if (isset($_POST['property_name']) && isset($_POST['address']) && isset($_POST['rent_amount']) && isset($_POST['tenant_name'])) {
    $property_name = $_POST['property_name'];
    $address = $_POST['address'];
    $rent_amount = $_POST['rent_amount'];
    $tenant_name = $_POST['tenant_name'];

    // Insert property into properties table
    $sql_insert_property = "INSERT INTO properties (name, address, amount, tenant_name) 
                            VALUES ('$property_name', '$address', '$rent_amount', '$tenant_name')";

    if ($conn->query($sql_insert_property) === TRUE) {
        // Retrieve the auto-generated property_id
        $property_id = $conn->insert_id;

        // Update property_id in tenants table if tenant_name is not 'none'
        if ($tenant_name !== 'none') {
            $sql_update_tenant = "UPDATE tenants SET property_id = '$property_id' WHERE name = '$tenant_name'";

            if ($conn->query($sql_update_tenant) === TRUE) {
                echo "New property and tenant record created successfully";
            } else {
                echo "Error updating tenant record: " . $conn->error;
            }
        } else {
            echo "New property record created successfully";
        }
    } else {
        echo "Error inserting property record: " . $conn->error;
    }
} else {
    echo "Required fields are missing.";
}

$conn->close();
?>
