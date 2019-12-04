<?php
$con = mysqli_connect("127.0.0.1", "root", "1234", "dotori");
mysqli_set_charset($con,"utf8");
if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$PID = $_GET['PID'];
$PName = $_GET['PName'];
$ListName = $_GET['ListName'];
$Memo = $_GET['Memo'];
$result = mysqli_query($con,"INSERT into list (PID,PName,ListName,Memo) values ('$PID','$PName','$ListName','$Memo')");
  if($result){
    echo $PName;
  }
  else{
    echo mysql_error();
  }
mysqli_close($con);
?>
