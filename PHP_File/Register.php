<?php
	$con = mysqli_connect("127.0.0.1", "root", "1234", "dotori");
	mysqli_set_charset($con,"utf8");
	if (mysqli_connect_errno($con))
	{
	   echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}
	$userid = $_POST['id'];
	$userpw = $_POST['password'];
	$username = $_POST['name'];
	$userphone = $_POST['phone_number'];
	$userbirth = $_POST['birth_date'];
	$result = mysqli_query($con,"INSERT into info (id,password,name,phone_number,birth_date) values ('$userid','$userpw','$username','$userphone',$userbirth)");
	  if($result){
	    echo '회원가입 성공';
	  }
	  else{
	    echo '형식에 맞게 다시 입력하세요.';
	  }
	mysqli_close($con);
?>