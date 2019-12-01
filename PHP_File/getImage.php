<?php
	$con=mysqli_connect("127.0.0.1","root","1234","dotori");

	if (mysqli_connect_errno($con))
	{
	   echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}

	$ID = $_GET['id'];
	$query = "SELECT imgName FROM image where id='$ID'";
	$result = mysqli_query($con,$query);

	$response = array();

	while($row= mysqli_fetch_array($result)) {
	//      array_push($response, $row[0]);
	        echo $row[0].',';
	}
	//echo json_encode($response);
	
	mysqli_close($con);
?>
