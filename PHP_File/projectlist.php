<?php
$con=mysqli_connect("127.0.0.1","root","1234","dotori");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$ID = $_GET['PID'];
$result = mysqli_query($con,"SELECT PName FROM project where PID='$ID'");

if ($result) {
    while ($row = mysqli_fetch_array($result)) {
        $flag[] = $row;
    }
    print(json_encode($flag));
}

mysqli_close($con);
?>
