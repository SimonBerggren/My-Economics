<?php

$server = "192.168.1.8";
$port = 3306;
$dbusername = "root";
$dbpassword = "";
$dbname = "appdevelopment";

$firstname = $POST["firstname"];
$lastname = $POST["lastname"];
$email = $POST["email"];
$password = $POST["password"];

$connection = mysqli_connect($server, $dbusername, $dbpassword, $dbname, $port);

$statement = mysqli_prepare($connection, "INSERT INTO users VALUES (?,?,?,?)");
mysqli_stmt_bind_param($statement, "ssss", $firstname, $lastname, $email, $password);
mysqli_stmt_execute($statement);

$response = array();
$response["success"] = true;

}

echo json_encode($response)