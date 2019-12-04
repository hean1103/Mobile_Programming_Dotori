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
        $NewName = $_GET['newname'];
        $query = "UPDATE project SET PName='$PName', ListName='$ListName',Memo='$Memo' WHERE PID='$PID' AND ListName='$NewName'";
        $result = mysqli_query($con,$query);
        if($result){
        echo $ListName;
        }
        else{
        echo mysql_error();
        }
        mysqli_close($con);
        ?>
