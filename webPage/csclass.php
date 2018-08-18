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
<head>
<style type="text/css"> 
	.whole{margin:auto;width:800px;border:0px;background-color:white;overflow:hidden;}
    .main{margin-left:auto;width:600px;float:left;}
</style>
<link rel="stylesheet" type="text/css" href="tablestyle.css">
<title>选择开课班级</title>
</head>
<div class = whole align = "center">
    <form action = "begin.php" method = "post" name = "chooseClass">
        <?php
            foreach($_SESSION['selectedCS'] as $csId){
                $_SESSION[$csId] = $_POST[$csId];
                echo "<div class = main>";
                $result = $db -> query("SELECT name FROM Course WHERE courseId = ".$csId);
                echo $result->fetch_assoc()['name'].'<br>';
                echo "<table class = zebra><thead><tr><th>教师</th><th>选课建议</th><th>上课时间地点</th><th>
                <input type = checkBox onchange = selecAll('".$csId."[]')>
                </th></tr></thead>";
                $result = $db -> query("SELECT lessonId,teacher,recommend FROM Lesson WHERE courseId = ".$csId);
                    while($row = $result->fetch_assoc()){
                        echo "<tr><td>".$row['teacher']."</td><td>".$row['recommend']."</td><td>";
                        $timer = $db -> query("SELECT time,place FROM lessonTime WHERE lessonId = ".$row['lessonId']);
                        while($trow = $timer -> fetch_assoc()){
                            echo $trow['time']." ".$trow['place']."<br>";
                        }
                        echo"</td>";
                        echo"<td><input type = checkbox name = ".$csId."[] value = \"".$row['lessonId']."\"></td></tr>";
                    }
                echo "</table></div>";
            }
        ?>
    <div><input type = submit value = 确定><div>
    </form>
</div>
</html>