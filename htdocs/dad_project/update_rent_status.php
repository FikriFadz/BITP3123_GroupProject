<?php
include 'conn.php';

$email = $_POST['email'];
$month_year = $_POST['month_year'];
$payment_date = $_POST['payment_date'];

$sql = "UPDATE rent SET status = 'Paid', payment_date = ? WHERE email = ? AND month_year = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("sss", $payment_date, $email, $month_year);
if ($stmt->execute()) {
    $response = array('status' => 'success');
} else {
    $response = array('status' => 'error', 'message' => 'Failed to update rent status.');
}

$stmt->close();
$conn->close();

echo json_encode($response);
?>
