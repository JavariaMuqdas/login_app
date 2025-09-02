<?php
header("Content-Type: application/json");
error_reporting(0); // hide warnings

include("connection.php");

$username = $_POST['username'] ?? '';
$password = $_POST['password'] ?? '';

if (!$conn) {
    echo json_encode(["success"=>false, "message"=>"Database connection failed"]);
    exit;
}

if (empty($username) || empty($password)) {
    echo json_encode(["success"=>false, "message"=>"Missing username or password"]);
    exit;
}

// Check login
$stmt = $conn->prepare("SELECT * FROM users WHERE username=? AND password=?");
$stmt->bind_param("ss", $username, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    echo json_encode(["success"=>true, "message"=>"Login successful"]);
} else {
    // Check if user exists
    $stmt2 = $conn->prepare("SELECT * FROM users WHERE username=?");
    $stmt2->bind_param("s", $username);
    $stmt2->execute();
    $res2 = $stmt2->get_result();
    if ($res2->num_rows == 0) {
        echo json_encode(["success"=>false, "message"=>"User not registered"]);
    } else {
        echo json_encode(["success"=>false, "message"=>"Incorrect password"]);
    }
}
