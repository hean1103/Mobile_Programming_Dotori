<?php
$con=mysqli_connect("127.0.0.1","root","1234","dotori");
if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$ID = $_GET['PID'];
$NAME = $_GET['PName'];
$ListName = $_GET['ListName'];
$result = mysqli_query($con,"SELECT *  FROM list where PID='$ID' AND PName='$NAME' AND ListName='$ListName'");
if ($result) {
    while ($row = mysqli_fetch_array($result)) {
        $flag[] = $row;
    }
    print(json_encode($flag));
}
mysqli_close($con);
?>
