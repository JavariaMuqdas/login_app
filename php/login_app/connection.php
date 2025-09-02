<?php
$server = "localhost";
$username = "root";
$password = "";
$database = "login_app";

$conn = new mysqli($server, $username, $password, $database);
if ($conn->connect_error) {
    echo json_encode([
        "success" => false,
        "message" => "Connection Failed: " . $conn->connect_error
    ]);
    exit;
}
?>
