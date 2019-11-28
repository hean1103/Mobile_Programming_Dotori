<?php
     $con = mysqli_connect("13.124.77.84", "root", "1234", "dotori");

     $id = $_POST["id"];
     
     //$statement = mysqli_prepare($con, "SELECT id FROM USER WHERE id = ?");
     $statement = mysqli_prepare($con, "SELECT id FROM info WHERE id = ?");
     mysqli_stmt_bind_param($statement, "s", $id);
     mysqli_stmt_execute($statement);
     mysqli_stmt_store_result($statement);//결과를 클라이언트에 저장함
     mysqli_stmt_bind_result($statement, $id);//결과를 $id에 바인딩함
     $response = array();
     $response["success"] = true;
     
     while(mysqli_stmt_fetch($statement)){
       $response["success"] = false;//회원가입불가를 나타냄
       $response["id"] = $id;
     }
     
     //데이터베이스 작업이 성공 혹은 실패한것을 알려줌
     echo json_encode($response);
?>
