<?php
header("Content-Type: application/json");
error_reporting(E_ALL);
ini_set('display_errors', 1);

include("connection.php"); 

$username = $_POST['username'] ?? '';
$password = $_POST['password'] ?? '';

if (!$conn) {
    echo json_encode(["success"=>false,"message"=>"DB connection failed"]);
    exit;
}

if (empty($username) || empty($password)) {
    echo json_encode(["success"=>false,"message"=>"Missing fields"]);
    exit;
}

$stmt = $conn->prepare("SELECT * FROM users WHERE username=?");
$stmt->bind_param("s", $username);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    echo json_encode(["success"=>false,"message"=>"Username already taken"]);
} else {
    $stmt = $conn->prepare("INSERT INTO users (username, password) VALUES (?, ?)");
    $stmt->bind_param("ss", $username, $password);
    if ($stmt->execute()) {
        echo json_encode(["success"=>true,"message"=>"Registration successful"]);
    } else {
        echo json_encode(["success"=>false,"message"=>"Error saving user"]);
    }
}
