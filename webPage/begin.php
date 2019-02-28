<!DOCTYPE html>
<?php 
    session_start();
    $db = new mysqli('localhost','testu','123','cs');
	if($db -> connect_error){
		die("连接失败：".$db->connect_error);
	}

    $db->query("set names 'utf8'");
    
?>

<script language = "javascript">


document.onkeydown=function(e){
    console.log(e.keyCode)
    e=e||window.event;  
	e.preventDefault();
	switch(e.keyCode){  
		case 37:
            RollShow(-1);
			break; 
		case 39:
            RollShow(1);
			break;
	}
}
//查询，使用ajax在页面内不刷新返回内容
function query()
{
    elemId = "continuePlan";
    str = "qtype=5&"+document.getElementById("Plan"+Display).title;
    document.getElementById(elemId).style.display="";
    var xmlhttp;
    if (str=="") {
        document.getElementById(elemId).innerHTML="";
        return;
    }
    if (window.XMLHttpRequest)
        xmlhttp=new XMLHttpRequest();
    else
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
            document.getElementById(elemId).innerHTML=xmlhttp.responseText;
    }
    xmlhttp.open("GET","query.php?"+str,true);
    xmlhttp.send();
}

var Display = 1;
function RollShow(direction){
    var planCount = parseInt(document.getElementById("totalPlans").abbr);
    document.getElementById("Plan"+Display).style.display="none";
    if(Display+direction >=1 && Display+direction <=planCount){
        Display+=direction;
    }
    document.getElementById("currentShow").innerHTML=Display+"";
    document.getElementById("Plan"+Display).style.display="";
}
</script>

<!--
    $_SESSION['selectedCS']:array of courseId;
    $_POST['courseId']:array of classes.
-->
<html>
<head>
    <title>选课结果</title>
</head>
<?php
//统计学分冲突数（通过计算二进制表示中有多少1相同）
function count1($a){
    $n=0;
    while($a!=0){
        $a&=($a-1);
        $n++;
    }
    return $n;
}

//接下来：递归搜索选课方案，存放在全局变量LessonSet中
$planRecord = [];
$planHalfA=[];//方案筛选完毕以后的上半学期和下半学期的时刻信息
$planHalfB=[];
$planCount=0;
$Max_Conflict = (int)(2*(float)$_POST['MaxConflict']);
$N = sizeof($_SESSION['selectedCS']);
$selectedLessons = [];
function searchLesson($curIndex,$sumHalfA,$sumHalfB,$conflictNum){
    global $planHalfA,$planHalfB,$halfA,$halfB,$N,$selectedLessons,$planCount,$planRecord,$Max_Conflict;
    if($planCount >=100)return;
    if($conflictNum > $Max_Conflict)return;
    if($curIndex == $N){
        //到达这一步就说明找到了一个新的选课方案
        $planRecord[]=[];
        foreach($selectedLessons as $sels)$planRecord[$planCount][]=$sels;
        $planCount++;
        //记录方案所占的时刻信息
        $planHalfA[$planCount]=$sumHalfA;
        $planHalfB[$planCount]=$sumHalfB;
        //echo $planCount.'<br />'.$planHalfA[$planCount].'<br />';
        if($planCount >= 100){
            echo '可行的选课方案大于等于100，几乎可以随便选课.<br />';
        }
        return;
    }
    for($i=0;$i<sizeof($_POST[$_SESSION['selectedCS'][$curIndex]]);++$i){
        $lsId = $_POST[$_SESSION['selectedCS'][$curIndex]][$i];
        $selectedLessons[]=$lsId;
        searchLesson(
            $curIndex +1 ,
            $halfA[$lsId] | $sumHalfA,
            $halfB[$lsId] | $sumHalfB ,
            $conflictNum + count1($halfA[$lsId] & $sumHalfA)+count1($halfB[$lsId] & $sumHalfB)
        );
        array_pop($selectedLessons);
    }
}
//函数功能：打印课程表
function printLessonTable($lessonSet){
    global $halfA,$halfB,$db;
    $lessonTimeId=[];
    $lessonTable = [];
    /*-------------Init data--------------------*/
    for($i=0;$i<5;++$i){
        $lessonTable[]=[];
        for($j=0;$j<=13;++$j){
            $lessonTable[$i][]=[];
            $lessonTable[$i][$j]['ls']=[];
        }
    }

    /*-----------以下将课程全部填入表格，只要时间段里有那个课程就填进去----------------*/
    foreach($lessonSet as $ls){
        $lessonTimeId[$ls]=0;
        for($i=0;$i<64;++$i){
            if(((1<<$i) & $halfA[$ls]) != 0 || (((1<<$i) & $halfB[$ls])!=0)){
                $lessonTable[$i/13][$i%13+1]['ls'][]=$ls;
            }
        }
    }
    echo'
    <table align = center border = "1px"cellspacing="0"><tr>
    <td>节</td>
    <td>星期一</td>
    <td>星期二</td>
    <td>星期三</td>
    <td>星期四</td>
    <td>星期五</td>
    </tr>';

    //对非空白的时间段进行划分
    for($j=0;$j<5;++$j){
        for($i=1;$i<=13;++$i){
            $lessonCount = sizeof($lessonTable[$j][$i]['ls']);
            if($lessonCount==0)continue;
            //变量意义：同一时间段里课程数量
            //接下来制作课程表，如果一个时间段与其后连续的时间段课程完全相同，就合并在一起
            for($maxRow=0,$flag = TRUE;
                $lessonCount == sizeof($lessonTable[$j][$i+$maxRow]['ls']) && $i+$maxRow<=13;
                ++$maxRow){
                    foreach($lessonTable[$j][$i]['ls'] as $lessonInFirst)
                        if(!in_array($lessonInFirst,$lessonTable[$j][$i+$maxRow]['ls'])){
                            $flag = FALSE;
                            break;
                        }
                if($flag == FALSE)break;
            }
            $lessonTable[$j][$i]['mxrow']=$maxRow;
            $i += $maxRow-1;
        }
    }

    //对空白时间段的时间进行划分
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
function PrintLessonLink($lessonSet){
    global $db;
    echo'<h3>选课链接：</h3>';
    echo'<table>';
    foreach($lessonSet as $lesson){
        $result = $db->query("SELECT courseId FROM Lesson WHERE lessonId = ".$lesson);
        $row = $result->fetch_assoc();
        $csId = $row['courseId'];
        $result = $db->query("SELECT name from Course WHERE courseId = ".$csId);
        $name = $result->fetch_assoc()['name'];
        
        echo '<tr><td>
        <a href=
        "http://jwdep.dhu.edu.cn/dhu/student/selectcourse/selectcourse2.jsp?courseNo='.$lesson.'&courseId='.$csId.'&courseName='.urlencode(iconv("UTF-8", "gb2312", $name)).'"
        target="view_window" >'.$name.'</a>
        </td></tr>';
    }
    echo'</table>';
}
//以下：获取halfA,halfB的值
foreach($_SESSION["selectedCS"]as $courseId){
    foreach($_POST[$courseId]as $lessonId){
        $result = $db->query("SELECT halfA,halfB from Lesson where lessonId = ".$lessonId);
        while($row = $result->fetch_assoc()){
            $halfA[$lessonId] = (int)$row['halfA'];
            $halfB[$lessonId] = (int)$row['halfB'];
        }
    }
}
while(true){
    searchLesson(0,0,0,0);
    if($planCount==0){
        echo"当前学分冲突数 ".($Max_Conflict/2)." 下无可行方案，尝试增加 0.5 个学分冲突上限。<br />";
        $Max_Conflict++;
    }else break;
}
echo "<h3 align = center>总共方案数：".$planCount.'。冲突学分： '.($Max_Conflict/2).'</h3>';
echo "<table align = center><tr><td><input type='button' value = '上一个' onClick=RollShow(-1)></td><td>
<table><tr><td id = currentShow>1</td><td id = totalPlans abbr = ".$planCount.">/".$planCount."</td></tr></table>
</td><td><input type=button value = '下一个' onClick=RollShow(1)></td></tr></table>";

$count=1;

foreach($planRecord as $pl){
    if($count == 1)echo'<div ';
    else echo'<div style="display:none"';
    echo 'align = center id="Plan'.$count.'" title = "halfA='.$planHalfA[$count].'&halfB='.$planHalfA[$count].'">';
    $count++;
    
    echo'<table><tr><td><div>';
    printLessonTable($pl);
    echo'</div></td>';

    echo'<td style="vertical-align:top;width:200px"><div>';
    PrintLessonLink($pl);
    echo'</div></td></tr></table>';
    
    echo'</div>';
}

echo '<div align = center><input type = button onClick = "query()"value="继续查找文素、体育、计算机、英语课的选课方案"></div>'
?>

<table id="continuePlan"style="display:none"align="center" border = "1px"cellspacing="0"></table>
</html>