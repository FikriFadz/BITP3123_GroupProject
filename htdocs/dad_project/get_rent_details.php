<?php
include 'conn.php';

$email = $_POST['email'];
$month_year = $_POST['month_year'];

$sql = "SELECT total_rent FROM rent WHERE email = ? AND month_year = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ss", $email, $month_year);
$stmt->execute();
$result = $stmt->get_result();

$response = array();
if ($result->num_rows > 0) {
    $response['status'] = 'success';
    $response['total_rent'] = $result->fetch_assoc()['total_rent'];
} else {
    $response['status'] = 'error';
    $response['message'] = 'No rent details found for the selected month/year.';
}

$stmt->close();
$conn->close();

echo json_encode($response);
?>
