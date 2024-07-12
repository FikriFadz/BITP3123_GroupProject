<?php
header("Content-Type: application/json");

include 'conn.php';

$sql = "SELECT * FROM tenants";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $tenants = array();
    while($row = $result->fetch_assoc()) {
        $tenants[] = array(
            'id' => $row['id'],
            'name' => $row['name']
        );
    }
    echo json_encode($tenants);
} else {
    echo json_encode(array());
}

$conn->close();
?>
