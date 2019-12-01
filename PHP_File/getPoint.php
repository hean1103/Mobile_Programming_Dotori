<?php
$con=mysqli_connect("127.0.0.1","root","1234","dotori");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$ID = $_GET['id'];
$query = "SELECT point FROM info where id='$ID'";
$result = mysqli_query($con,$query);
$row = mysqli_fetch_array($result);
$data = $row[0];

if($data) {
        print $data;
}


mysqli_close($con);
?>
