<?php
	$con = mysqli_connect("13.124.77.84", "root", "1234", "dotori");
	$id = $_POST["id"];
	$password = $_POST["password"];
	$statement = mysqli_prepare($con, "SELECT * FROM info WHERE id = ? AND password = ?");
	mysqli_stmt_bind_param($statement, "ss", $id , $password);
	mysqli_stmt_execute($statement);
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $id, $password, $name, $phone_number, $birth_date, $point);
	$response = array();
	$response["success"] = false;
	while(mysqli_stmt_fetch($statement))
	{
	        $response["success"] = true;
	        $response["id"] = $id;
	        $response["password"] = $password;
	        $response["name"] = $name;
	        $response["phone_number"] = $phone_number;
	        $response["birth_date"] = $birth_date;
	        $response["point"] = $point;
	}
	echo json_encode($response);
?>
