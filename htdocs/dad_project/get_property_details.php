<?php
header("Content-Type: application/json");

include 'conn.php';

$property_id = $_GET['id'];

if (empty($property_id)) {
    echo json_encode(array("error" => "Property ID is required."));
    exit();
}

$sql = "SELECT * FROM properties WHERE id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $property_id);

if ($stmt->execute()) {
    $result = $stmt->get_result();
    if ($result->num_rows > 0) {
        $property = $result->fetch_assoc();
        echo json_encode($property);
    } else {
        echo json_encode(array("error" => "Property not found."));
    }
} else {
    echo json_encode(array("error" => "Error fetching property details: " . $conn->error));
}

$stmt->close();
$conn->close();
?>
