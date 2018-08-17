<!DOCTYPE html>
<?php 
    session_start();
    $db = new mysqli('localhost','testu','123','cs');
	if($db -> connect_error){
		die("连接失败：".$db->connect_error);
	}
	$db->query("set names 'utf8'");
?>
<script language="javascript">
 function selecAll(a){
    o=document.getElementsByName(a)
    for(i=0;i<o.length;i++)
        o[i].checked=event.srcElement.checked
 }
</script>
<html>
<style type="text/css"> 
	.whole{width:1000px;margin:auto;border:2px solid #000066;background-color:#99cc99;overflow:hidden;}
    .main{margin-left:auto;width:800px;float:left;}
</style>
<title>选择开课班级</title>
<div class = "whole" align = "center">
    <form action = "" name = "chooseClass">
        <?php
            foreach($_SESSION['selectedCS'] as $csId){
                echo "<div class = \"main\">";
                $result = $db -> query("SELECT name FROM Course WHERE courseId = ".$csId);
                echo $result->fetch_assoc()['name'].'<br>';
                echo "<table><tr><td>教师</td><td>选课建议</td><td>上课时间地点</td><td>
                <input type = \"checkBox\" onchange = \"selecAll('".$csId."')\">
                </td></tr>";
                $result = $db -> query("SELECT lessonId,teacher,recommend FROM Lesson WHERE courseId = ".$csId);
                    while($row = $result->fetch_assoc()){
                        echo "<tr><td>".$row['teacher']."</td><td>".$row['recommend']."</td><td><table>";
                        $timer = $db -> query("SELECT time,place FROM lessonTime WHERE lessonId = ".$row['lessonId']);
                        while($trow = $timer -> fetch_assoc()){
                            echo "<tr><td>".$trow['time']."</td><td>".$trow['place']."</td></tr>";
                        }
                        echo"</table></td>";
                        echo"<td><input type = \"checkbox\" name = \"".$csId."\" value = \"".$csId."\"></td></tr>";
                    }

                echo "</table></div>";
            }
        ?>
    </form>
</div>
</html>