<?php
header("Content-Type: application/json");

include 'conn.php';

$data = json_decode(file_get_contents('php://input'), true);

$property_id = $data['id'];

if (empty($property_id)) {
    echo json_encode(array("error" => "Property ID is required."));
    exit();
}

$sql = "DELETE FROM properties WHERE id = ?";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $property_id);

if ($stmt->execute()) {
    echo json_encode(array("success" => "Property deleted successfully."));
} else {
    echo json_encode(array("error" => "Error deleting property: " . $conn->error));
}

$stmt->close();
$conn->close();
?>
