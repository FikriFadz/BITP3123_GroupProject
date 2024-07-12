<?php
header('Content-Type: application/json'); // Ensure the response is JSON
include 'conn.php';

// Get POST data
$email = isset($_POST['email']) ? $_POST['email'] : '';


// Fetch property details based on tenant's email
$sql = "SELECT p.id, p.name, p.address, p.amount, p.tenant_name 
        FROM properties p
        JOIN tenants t ON p.id = t.property_id
        WHERE t.email = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $propertyDetails = $result->fetch_assoc();
    echo json_encode(["status" => "success", "property" => $propertyDetails]);
} else {
    echo json_encode(["status" => "error", "message" => "No property details found for the given email"]);
}

$stmt->close();
$conn->close();
?>
