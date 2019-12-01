<?php
	$con=mysqli_connect("127.0.0.1","root","1234","dotori");

	if (mysqli_connect_errno($con))
	{
	   echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}

	$ID = $_GET['id'];
	$MYIMAGE = $_GET['myImage'];

	$query = "UPDATE info SET myImage='$MYIMAGE' where id='$ID'";
	$result = mysqli_query($con,$query);


	mysqli_close($con);
?>
~ 
