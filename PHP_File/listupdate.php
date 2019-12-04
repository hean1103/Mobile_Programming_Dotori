<?php
$con = mysqli_connect("127.0.0.1", "root", "1234", "dotori");
mysqli_set_charset($con,"utf8");
if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$PID = $_GET['PID'];
$PName = $_GET['PName'];
$NewListName = $_GET['NewListName']
$Memo = $_GET['Memo'];
$ListName = $_GET['ListName'];
$result = mysqli_query($con,"UPDATE list SET ListName='$NewListName',Memo='$Memo' WHERE PID='$PID' AND PName='$PName' AND ListName='$ListName'");
  if($result){
    echo $PName;
  }
  else{
    echo mysql_error();
  }
mysqli_close($con);
?>

http://13.124.77.84/listsetting.php?PID=hean&PName=aaa&ListName=bk&Memo+newMenu