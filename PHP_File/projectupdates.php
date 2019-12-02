<?php
$con = mysqli_connect("127.0.0.1", "root", "1234", "dotori");
mysqli_set_charset($con,"utf8");
if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$PID = $_GET['pid'];
$PName = $_GET['pname'];
$DateFrom = $_GET['datefrom'];
$DateTo = $_GET['dateto'];
$NewName = $_GET['newname'];
$query = "UPDATE project SET PName='$PName', DateFrom='$DateFrom',DateTo='$DateTo' WHERE PID='$PID' AND PName='$NewName'";
$result = mysqli_query($con,$query);
  if($result){
    echo $PName;
  }
  else{
    echo mysql_error();
  }
mysqli_close($con);
?>
