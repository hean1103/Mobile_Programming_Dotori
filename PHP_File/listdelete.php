<?php
$con = mysqli_connect("127.0.0.1", "root", "1234", "dotori");
if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$PID = $_GET['PID'];
$PName = $_GET['PName'];
$ListName = $_GET['ListName'];

$query = "DELETE FROM list WHERE PID='$PID' AND PName='$PName' AND ListName='$ListName'";

$result = mysqli_query($con,$query);
if($result){
        echo $ListName;
}
else{
        echo mysql_error();
}
mysqli_close($con);
?>