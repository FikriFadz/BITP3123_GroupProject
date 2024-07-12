<?php
header("Content-Type: application/json");
include 'conn.php';

$data = json_decode(file_get_contents('php://input'), true);

$property_id = $data['id'];
$property_name = $data['name'];
$address = $data['address'];
$amount = $data['amount'];
$tenant_name = $data['tenant_name'];

if (empty($property_id) || empty($property_name) || empty($address) || empty($amount)) {
    echo json_encode(array("error" => "All fields except tenant name are required."));
    exit();
}

// Prepare and execute the update query for properties table
$sql_update_property = "UPDATE properties SET name=?, address=?, amount=?, tenant_name=? WHERE id=?";
$stmt_property = $conn->prepare($sql_update_property);
$stmt_property->bind_param("ssdsi", $property_name, $address, $amount, $tenant_name, $property_id);

$update_property_success = $stmt_property->execute();

if (!$update_property_success) {
    echo json_encode(array("error" => "Error updating property: " . $conn->error));
    exit();
}

// Update property_id for the current tenant
if ($tenant_name !== 'none') {
    // Retrieve the current property_id from tenants table
    $sql_get_current_property_id = "SELECT property_id FROM tenants WHERE name=?";
    $stmt_get_property_id = $conn->prepare($sql_get_current_property_id);
    $stmt_get_property_id->bind_param("s", $tenant_name);
    $stmt_get_property_id->execute();
    $stmt_get_property_id->bind_result($current_property_id);
    $stmt_get_property_id->fetch();
    $stmt_get_property_id->close();

    // Update property_id in tenants table for the current tenant
    $sql_update_tenant = "UPDATE tenants SET property_id=? WHERE name=?";
    $stmt_update_tenant = $conn->prepare($sql_update_tenant);
    $stmt_update_tenant->bind_param("is", $property_id, $tenant_name);
    $update_tenant_success = $stmt_update_tenant->execute();
    $stmt_update_tenant->close();

    // If the update was successful, set property_id to NULL for the previous tenant
    if ($update_tenant_success && !empty($current_property_id)) {
        $sql_update_previous_tenant = "UPDATE tenants SET property_id=NULL WHERE property_id=? AND name!=?";
        $stmt_update_previous_tenant = $conn->prepare($sql_update_previous_tenant);
        $stmt_update_previous_tenant->bind_param("is", $property_id, $tenant_name);
        $stmt_update_previous_tenant->execute();
        $stmt_update_previous_tenant->close();
    }
}

$stmt_property->close();
$conn->close();

echo json_encode(array("success" => "Property updated successfully."));
?>
