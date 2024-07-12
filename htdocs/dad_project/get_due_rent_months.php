<?php
include 'conn.php';

$email = $_POST['email'];

$sql = "SELECT month_year FROM rent WHERE email = ? AND status = 'Due'";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

$response = array();
if ($result->num_rows > 0) {
    $response['status'] = 'success';
    $response['due_months'] = array();
    while ($row = $result->fetch_assoc()) {
        $response['due_months'][] = $row['month_year'];
    }
} else {
    $response['status'] = 'error';
    $response['message'] = 'No due rent months found.';
}

$stmt->close();
$conn->close();

echo json_encode($response);
?>
