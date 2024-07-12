<?php
include 'conn.php';

$sql = "SELECT id, name, address, amount, tenant_name FROM properties";
$result = $conn->query($sql);

$properties = array();

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $properties[] = $row;
    }
}

$conn->close();

echo json_encode($properties);
?>
