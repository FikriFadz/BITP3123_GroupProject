<?php
include 'conn.php';

// Get POST data
$propertyId = $_POST['property_id'];
$email = $_POST['email'];
$rentAmount = $_POST['rent_amount'];
$utilityAmount = $_POST['utility_amount'];
$totalRent = $rentAmount + $utilityAmount;
$monthYear = $_POST['month_year'];
$status = 'Due';
$paymentDate = '0000-00-00';

// Insert data into rent table
$sql = "INSERT INTO rent (property_id, email, rent_amount, utillity_amount, total_rent, month_year, status, payment_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
$stmt = $conn->prepare($sql);
$stmt->bind_param("issddsss", $propertyId, $email, $rentAmount, $utilityAmount, $totalRent, $monthYear, $status, $paymentDate);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Rent details inserted successfully"]);
} else {
    echo json_encode(["status" => "error", "message" => "Error: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>
