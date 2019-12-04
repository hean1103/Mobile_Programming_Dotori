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
$result = mysqli_query($con,"UPDATE list SET CheckBox=IF(CheckBox='1','0','1') WHERE PID='$PID' AND PName='$PName' AND ListName='$ListName'");


$checkResult = mysqli_query($con,"SELECT CheckBox FROM list WHERE PID='$PID' AND PName='$PName' AND ListName='$ListName'");

$row = mysqli_fetch_array($checkResult);
$data = $row[0];
if( $data == 1 ) {
        $result1 = mysqli_query($con, "UPDATE info SET point = point + 20 where id='$PID'");
}
else {
        $result1 = mysqli_query($con, "UPDATE info SET point = point - 20 where id='$PID'");
}
mysqli_close($con);
?>