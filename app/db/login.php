<?php

$server = "192.168.1.8";
$port = 3306
$dbusername = "root";
$password = "";
$dbname = "appdevelopment";

$email = $POST["email"];
$password = $POST["password"];

$connection = mysqli_connect($server, $dbusername, $password, $dbname, $port);

$statement = mysqli_prepare($connection, "SELECT * FROM users WHERE email = ? AND password = ?");
mysqli_stmt_bind_param($statement, "ss", $email, $password);
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $userID, $firstname, $lastname, $email, $password);

$response = array();
$response["success"] = false;

while(mysql_stmt_fetch($statement)) {
  $response["success"] = true;
  $response["firstname"] = true;
  $response["lastname"] = true;
  $response["email"] = true;
  $response["password"] = true;

}

echo json_encode($response)