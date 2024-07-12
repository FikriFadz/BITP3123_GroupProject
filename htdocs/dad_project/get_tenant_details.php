<?php
include 'conn.php';

// Check if property_id is set in the GET request
if (!isset($_GET['property_id'])) {
    echo json_encode(["error" => "property_id not provided"]);
    exit();
}

$propertyId = intval($_GET['property_id']);

// Debugging: print the propertyId to ensure it's correct
error_log("Property ID: " . $propertyId);

$sql = "SELECT tenants.name AS tenantName, tenants.email AS tenantEmail, properties.amount AS rentAmount
        FROM tenants
        JOIN properties ON tenants.property_id = properties.id
        WHERE properties.id = ?";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $propertyId);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    echo json_encode($row);
} else {
    echo json_encode(["error" => "No tenant found for the selected property"]);
}

// Debugging: print the SQL error if any
if ($conn->error) {
    error_log("SQL Error: " . $conn->error);
}

$stmt->close();
$conn->close();
?>
