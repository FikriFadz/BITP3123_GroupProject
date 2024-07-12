<?php
header('Content-Type: application/json');
include 'conn.php';

$email = $_POST['email'];

$sql = "SELECT property_id, rent_amount, utillity_amount, total_rent, month_year, status FROM rent WHERE status = 'Due' AND email = '$email'";
$result = $conn->query($sql);

$response = array();

if ($result->num_rows > 0) {
    $response['status'] = 'success';
    $response['details'] = array();

    while($row = $result->fetch_assoc()) {
        $response['details'][] = $row;
    }
} else {
    $response['status'] = 'error';
    $response['message'] = 'You have no due rent.';
}

$conn->close();

echo json_encode($response);
?>
