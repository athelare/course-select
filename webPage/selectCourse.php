<!DOCTYPE html> 
<?php 
	session_start(); 
	if(!isset($_SESSION['selectedCS']))
	{
	    $_SESSION['selectedCS']=[];
	}
	$db = new mysqli('localhost','testu','123','cs');
	if($db -> connect_error){
		die("连接失败：".$db->connect_error);
	}
	$db->query("set names 'utf8'");
?>
<html>
<head>
<link rel="stylesheet" type="text/css" href="tablestyle.css" />
<title>选课样例</title>
<style type="text/css"> 
	.whole{width:1000px;margin:auto;border:0px solid #000066;background-color:WHITE;overflow:hidden;}
	.main{margin-left:10px;width:600px;float:left;}
	.right{margin-left:40px;width:300px; float:right;}
</style> 
</head>

<body>

<div class = "whole">
	<div class = "main" align = "center">
		<h3>选课方式</h3>
		<table><thead><tr>
			<th align = "right"><form style = "margin:0px；display:inline;" action = "" method = get name = "how1">
					<input type = "hidden" name = "choice" value = "1"><input type = "submit" value = "通过教学计划">
				</form>
			</th><th>OR</th>
			<th align = "left"><form action = "" method = get name = "how2">
					<input type = "hidden" name = "choice" value = "2"><input type = "submit" value = "通过课程名称">
				</form>
			</th></tr></thead>
		</table>



<?php
	if($_GET['choice'] != "")$_SESSION['choice']=$_GET['choice'];
	if($_SESSION['choice'] == "2"){
	echo "
		<form action = \"\" method = \"get\" name = \"searchCs\">
			<label>课程名称</label><input name = \"nm\" value = \"\"><input type = \"submit\" value = \"提交\">
		</form>
		";
	$csName = $_GET['nm'];
	if($csName == "")$csName = "NoasdNOasd";
	$result = $db->query("SELECT * from Course WHERE name LIKE '%".$csName."%'");
	}else if($_SESSION['choice'] == "1"){
	if($_SESSION['gradeId'] == "")$_SESSION['gradeId'] = "2014a";
	if($_POST['gradeId']!="")$_SESSION['gradeId']=$_POST['gradeId'];
	$Grades = ["2014a","2015a","2016a","2017a","2018a"];
	echo "
	<form method = \"post\" name = \"pln\">
	<table><tr>
	<td>学年:</td>
	<td>
		<select name=\"gradeId\" onchange=\"pln.submit();\">";
		foreach($Grades as $g){
			echo "<option value =\"".$g."\"";if($_SESSION['gradeId'] == $g)echo "selected";echo ">".$g."</option>";
		}
	echo "</select></td>";
	if($_SESSION['gradeId'] != ""){
		if($_POST['majorId']!=""){$_SESSION['majorId'] = $_POST['majorId'];$_SESSION['semester'] = $_POST['semester'];}
		echo "<td>专业:</td><td>
			<select name = \"majorId\">";
			$mjrs = $db -> query("SELECT majorId,majorName FROM GradeMajor WHERE gradeId = '".$_SESSION['gradeId']."'");
			while($row = $mjrs -> fetch_assoc()){
				echo "<option value = \"".$row['majorId']."\"";if($_SESSION['majorId'] == $row['majorId'])echo "selected";echo ">".$row['majorName']."</option>";
			}
		echo "</select></td> <td>学期:</td>";
		echo "<td><select name = \"semester\">
			<option value = \"\">不限</option>
			<option value = \"1a\">大一上</option>
			<option value = \"1b\">大一下</option>
			<option value = \"2a\">大二上</option>
			<option value = \"2b\">大二下</option>
			<option value = \"3a\">大三上</option>
			<option value = \"3b\">大三下</option>
			<option value = \"4a\">大四上</option>
			<option value = \"4b\">大四下</option>
			</select></td>";
		echo "<td><input type = \"submit\" value = \"确定\"></td>";
	}
	echo "</table></form>";
	if($_SESSION['majorId'] != ""){
		$result = $db -> query("SELECT * FROM Course INNER JOIN TeachPlan ON(TeachPlan.courseId = Course.courseId) ".
			"WHERE TeachPlan.gradeId = '".$_SESSION['gradeId']."' ".
			"AND TeachPlan.majorId = '".$_SESSION['majorId']."' ".
			"AND TeachPlan.semesterId LIKE '%".$_SESSION['semester']."%'");
	}
	}
	if($result->num_rows >0){
	echo "<table class = \"bordered\">\n";
	echo "<thead><tr><th>课程号</th><th>课程名称</th><th>学分</th><th>开课学院</th><th>选择</th></tr></thead>\n";
	while($row =$result -> fetch_assoc()){
		echo "<tr><td>".$row['courseId']."</td><td>".$row['name']."</td><td>".$row['credit']."</td><td>".$row['faculty']."</td>".
		"<td><form style = \"margin:0px;display:inline;\" action = \"\" method = \"post\" name = \"sc\">".
		"<input type = \"hidden\" name = \"csId\" value = \"".$row['courseId']."\">".
		"<input type = \"hidden\" name = \"csf\" value = \"1\">".
		"<input type = \"submit\" name = \"cs\" value = \"选择\"></form></td></tr>\n";
	}
	echo "<tfoot><tr><td colspan = 5>*注:以上仅显示本学期开设的课程。</td></tr></tfoot></table>";
	}else{
	echo "没有搜索结果</br>";
	}
?>
</div>

<div class = "right" align = "center">
	<h3 color = \"blue\">已经选择的课程:</h3><br>
<?php
	if($_POST['dtf'] == "1"){
	$key = array_search($_POST['dt'],$_SESSION['selectedCS']);
	array_splice($_SESSION['selectedCS'],$key,1);
	}
	if($_POST['csf'] == "1"){
	array_push($_SESSION['selectedCS'],$_POST['csId']);
	}
	$select = $_SESSION['selectedCS'];
	echo '';
	if(!empty($select)){
		echo "<input type = button value = \"开始选择开课班级\" onclick = \"window.open('csclass.php')\">";
		echo "<table>";
		foreach($select as $choice){
			$name = $result = $db->query("SELECT name from Course where courseId = ".htmlentities($choice));
			echo "<tr><td><form action = \"\" method = \"post\" name = \"delete\"><label></td><td>".htmlentities($choice).
			$name ->fetch_assoc()['name']. 
			"<input type = \"hidden\" name = \"dt\" value = \"".htmlentities($choice)."\">".
			"<input type = \"hidden\" name = \"dtf\" value = \"1\">".
			"<td><input type = \"submit\" value = \"删除\"></form></td></tr>";
		}
		echo "</table>";
	}else{
		echo "选择课程为空！";
	}
?>
</div>
</div>
</body>
</html>