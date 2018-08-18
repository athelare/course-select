<!DOCTYPE html>
<?php 
    session_start();
    $db = new mysqli('localhost','testu','123','cs');
	if($db -> connect_error){
		die("连接失败：".$db->connect_error);
	}
	$db->query("set names 'utf8'");
?>
<!--
    $_SESSION['selectedCS']:array of courseId;
    $_POST['courseId']:array of classes.

-->
<html>
<?php
    foreach($_SESSION["selectedCS"]as $courseId){
        foreach($_POST[$courseId]as $lessonId){
            $result = $db->query("SELECT halfA,halfB from Lesson where lessonId = ".$lessonId);
            while($row = $result->fetch_assoc()){
                $halfA[$lessonId] = $row['halfA'];
                $halfB[$lessonId] = $row['halfB'];
            }
        }
    }
    foreach($halfA as $ha){
        echo $ha.'<br>';
    }
?>
</html>