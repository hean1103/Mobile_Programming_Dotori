<?php
$con = mysqli_connect("127.0.0.1", "root", "1234", "dotori");
if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$PID = $_GET['PID'];
$PName = $_GET['PName'];
$NewListName = $_GET['NewListName'];
$Memo = $_GET['Memo'];
$ListName = $_GET['ListName'];
//$query = "Select ListName from list";

$query = "UPDATE list SET ListName='$NewListName', Memo='$Memo' WHERE PID='$PID' AND PName='$PName' AND ListName='$ListName'";

$result = mysqli_query($con,$query);
if($result){
    echo $NewListName;
  }
  else{
    echo mysql_error();
  }
mysqli_close($con);
?>