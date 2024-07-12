<?php
include 'conn.php';

// Get POST data
$email = $_POST['email'];
$password = $_POST['password'];


// Prepare and bind
$stmt = $conn->prepare("SELECT * FROM tenants WHERE email = ? AND password = ?");
$stmt->bind_param("ss", $email, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    echo json_encode(["status" => "success", "message" => "Login successful"]);
} else {
    echo json_encode(["status" => "error", "message" => "Invalid email or password"]);
}

$stmt->close();
$conn->close();
?>
