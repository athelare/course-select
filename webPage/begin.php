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
<head>
    <title>选课结果</title>
</head>
<?php
function count1($a){
    $n=0;
    while($a!=0){
        $a&=($a-1);
        $n++;
    }
    return $n;
}
$planRecord=0;
$Max_Conflict = 0;
$N = sizeof($_SESSION['selectedCS']);
$selectedLessons = [];
function searchLesson($curIndex,$sumHalfA,$sumHalfB,$conflictNum){
    if($planRecord >=100)return;
    global $N,$selectedLessons;
    if($conflictNum > $Max_Conflict)return;
    if($curIndex == $N){
        print_r($selectedLessons);
        echo'<br />';
        $planRecord++;
        if($planRecord >= 100){
            echo '可行的选课方案太多（>=100)，不需要本软件协助）<br />';
        }
        return;
    }
    for($i=0;$i<sizeof($_POST[$_SESSION['selectedCS'][$curIndex]]);++$i){
        $lsId = $_POST[$_SESSION['selectedCS'][$curIndex]][$i];
        array_push($selectedLessons,$lsId);
        searchLesson(
            $curIndex +1 ,
            $halfA[$lsId] | $sunHalfA,
            $halfB[$lsId] | $sunHalfB ,
            $conflictNum + count1($halfA[$lsId] & $sunHalfA)+count1($halfB[$lsId] & $sunHalfB)
        );
        array_pop($selectedLessons);
    }
}
/*-----------------------------------------------------------------*/
foreach($_SESSION["selectedCS"]as $courseId){
    foreach($_POST[$courseId]as $lessonId){
        $result = $db->query("SELECT halfA,halfB from Lesson where lessonId = ".$lessonId);
        while($row = $result->fetch_assoc()){
            $halfA[$lessonId] = $row['halfA'];
            $halfB[$lessonId] = $row['halfB'];
        }
    }
}
searchLesson(0,0,0,0);
?>
</html>