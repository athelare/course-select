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
$planRecord = [];
$planCount=0;
$Max_Conflict = (int)(2*(float)$_POST['MaxConflict']);
$N = sizeof($_SESSION['selectedCS']);
$selectedLessons = [];
function searchLesson($curIndex,$sumHalfA,$sumHalfB,$conflictNum){
    global $halfA,$halfB,$N,$selectedLessons,$planCount,$planRecord,$Max_Conflict;
    if($planCount >=100)return;
    if($conflictNum > $Max_Conflict)return;
    if($curIndex == $N){
        array_push($planRecord,[]);
        foreach($selectedLessons as $sels)array_push($planRecord[$planCount],$sels);
        $planCount++;
        if($planCount >= 100){
            echo '可行的选课方案大于等于100，几乎可以随便选课.<br />';
        }
        return;
    }
    for($i=0;$i<sizeof($_POST[$_SESSION['selectedCS'][$curIndex]]);++$i){
        $lsId = $_POST[$_SESSION['selectedCS'][$curIndex]][$i];
        array_push($selectedLessons,$lsId);
        searchLesson(
            $curIndex +1 ,
            $halfA[$lsId] | $sumHalfA,
            $halfB[$lsId] | $sumHalfB ,
            $conflictNum + count1($halfA[$lsId] & $sumHalfA)+count1($halfB[$lsId] & $sumHalfB)
        );
        array_pop($selectedLessons);
    }
}
function printLessonTable($lessonSet){
    global $halfA,$halfB,$db;
    $lessonTimeId=[];
    $lessonTable = [];
    $used = [];
    /*-------------Init data--------------------*/
    for($i=0;$i<5;++$i){
        array_push($lessonTable,[]);
        for($j=0;$j<=13;++$j){
            array_push($lessonTable[$i],[]);
            $lessonTable[$i][$j]['ls']=[];
        }
    }
    for($i=0;$i<5;++$i){
        array_push($used,[]);
        for($j=0;$j<=13;++$j)array_push($lessonTable[$i],0);
    }
    /*---------------------------------------------*/
    foreach($lessonSet as $ls){
        $lessonTimeId[$ls]=0;
        for($i=0;$i<64;++$i){
            if(((1<<$i) & $halfA[$ls]) != 0 || (((1<<$i) & $halfB[$ls])!=0)){
                $j = $i;
                for(;((1<<$j) & $halfA[$ls]) != 0|| ((1<<$j) & $halfB[$ls]) != 0;++$j);
                array_push(
                    $lessonTable[$i/13][$i%13+1]['ls'],
                    $ls
                );
                $lessonTable[$i/13][$i%13+1]['mxrow'] = max($lessonTable[$i/13][$i%13+1]['mxrow'],$j-$i);
                $i=$j;
            }
        }
    }
    echo'
    <table align = center border = 1><tr>
    <td>节</td>
    <td>星期一</td>
    <td>星期二</td>
    <td>星期三</td>
    <td>星期四</td>
    <td>星期五</td>
    </tr>';
    for($j=0;$j<5;++$j){
        for($i=1;$i<=13;++$i){
            if($lessonTable[$j][$i]['ls'] == []){
                $lessonTable[$j][$i]['mxrow']=1;
                $cu=$i;
                while($lessonTable[$j][$i+1]['ls'] == [] && $i!=13 && $i!=9 && $i!=4 ){
                    $lessonTable[$j][$cu]['mxrow']++;
                    $i++;
                }
            }else{
                $i+=$lessonTable[$j][$i]['mxrow']-1;
            }
        }
    }
    for($i=1;$i<=13;++$i){
        echo'<tr><td>'.$i.'</td>';
        for($j=0;$j<5;++$j){
            if($lessonTable[$j][$i]['mxrow']){
                echo'<td rowspan='.$lessonTable[$j][$i]['mxrow'].'>';
                foreach($lessonTable[$j][$i]['ls'] as $lesson ){
                    $result = $db->query("SELECT courseId,teacher FROM Lesson WHERE lessonId = ".$lesson);
                    $row = $result->fetch_assoc();
                    $result = $db->query("SELECT name from Course WHERE courseId = ".$row['courseId']);
                    echo $result->fetch_assoc()['name']." ".$row['teacher']." ";
                    $result = $db->query("SELECT week,place FROM lessonTime WHERE lessonId = ".$lesson." and timeId = ".++$lessonTimeId[$lesson]);
                    $row = $result->fetch_assoc();
                    echo $row['week']." ".$row['place']."<br />";
                }
                echo'</td>';
            }
        }
        echo'</tr>';
    }
    echo'</table>';
}
/*-----------------------------------------------------------------*/
foreach($_SESSION["selectedCS"]as $courseId){
    foreach($_POST[$courseId]as $lessonId){
        $result = $db->query("SELECT halfA,halfB from Lesson where lessonId = ".$lessonId);
        while($row = $result->fetch_assoc()){
            $halfA[$lessonId] = (int)$row['halfA'];
            $halfB[$lessonId] = (int)$row['halfB'];
        }
    }
}
searchLesson(0,0,0,0);
echo "<h3 align = center>总共方案数：".$planCount.'。</h3>';


foreach($planRecord as $pl){
    echo'<p><table><tr><td>';
    printLessonTable($pl);
    echo'</td></tr></table></p>';
}


?>
</html>