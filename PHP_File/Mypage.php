<?php
$con=mysqli_connect("13.124.77.84","root","1234","dotori");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$res = mysqli_query($con,"SELECT * FROM info");

$result = array();


while( $row = mysqli_fetch_array($res) )
{
  array_push($result,array('id'=>$row[0], 'password'=>$row[1], 'name'=>$row[2], 'phone_number'=>$row[3], 'birth_date'=>$row[4], 'point' => $row[5]
  ));
}



echo json_encode(array("result"=>$result));

mysqli_close($con);
?>
