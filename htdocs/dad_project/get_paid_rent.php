<?php
header('Content-Type: application/json');
include 'conn.php';

$email = $_POST['email'];

$sql = "SELECT property_id, rent_amount, utillity_amount, total_rent, month_year, payment_date FROM rent WHERE status = 'Paid' AND email = '$email'";
$result = $conn->query($sql);

$response = array();

if ($result->num_rows > 0) {
    $response['status'] = 'success';
    $response['history'] = array();

    while($row = $result->fetch_assoc()) {
        $response['history'][] = $row;
    }
} else {
    $response['status'] = 'error';
    $response['message'] = 'You have no due rent.';
}

$conn->close();

echo json_encode($response);
?>
