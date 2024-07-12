<?php
include 'conn.php';

// Check if POST request contains all required fields
if (isset($_POST['tenant_name']) && isset($_POST['email']) && isset($_POST['phone_number'])) {
    $tenant_name = $_POST['tenant_name'];
    $email = $_POST['email'];
    $phone_number = $_POST['phone_number'];

    $sql = "INSERT INTO tenants (name, email, phone) VALUES ('$tenant_name', '$email', '$phone_number')";
    

    if ($conn->query($sql) === TRUE) {
        echo "New record created successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
} else {
    echo "Required fields are missing.";
}

$conn->close();
?>
