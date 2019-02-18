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
<script language="javascript">
 function selectAll(a){
    o=document.getElementsByName(a)
    for(i=0;i<o.length;i++)
        o[i].checked=event.srcElement.checked
 }
</script>
<html>
<head>
<link rel="stylesheet" type="text/css" href="tablestyle.css" />
<title>选课样例</title>
<style type="text/css">
	.whole{width:1000px;margin:auto;border:0px solid #000066;background-color:WHITE;overflow:hidden;height:auto}
	.main{margin-left:10px;width:600px;float:left;}
	.right{margin-left:40px;width:300px; float:right;}
</style> 
</head>

<body>

<div class = "whole">

<div class = "main" align = "center">
		<h3>选课方式</h3>
		<table><thead><tr>
			<th align = "center"><form style = "margin:0px；display:inline;" action = "" method = get name = "how1">
					<input type = "hidden" name = "choice" value = "1"><input type = "submit" value = "通过教学计划">
				</form>
			</th><th>OR</th>
			<th align = "center"><form action = "" method = get name = "how2">
					<input type = "hidden" name = "choice" value = "2"><input type = "submit" value = "通过课程名称">
				</form>
			</th><th>OR</th>
			<th align = "center"><form action = "" method = get name = "how3">
					<input type = "hidden" name = "choice" value = "3"><input type = "submit" value = "选择文素，体育等">
				</form>
			</tr></thead>
		</table>
	<?php
		if($_GET['choice'] != "")$_SESSION['choice']=$_GET['choice'];
		if($_SESSION['choice'] == "1"){
			if($_SESSION['gradeId'] == "")$_SESSION['gradeId'] = "2014a";
			if($_POST['gradeId']!="")$_SESSION['gradeId']=$_POST['gradeId'];
			$Grades = ["2014a","2015a","2016a","2017a","2018a"];
			echo'<form method = "post" name = "pln">
				<table><tr>
				<td>学年:</td>
				<td>
				<select name="gradeId" onchange="pln.submit();">';
				foreach($Grades as $g){
					echo '<option value ="'.$g.'"';
					if($_SESSION['gradeId'] == $g)echo 'selected';
					echo '>'.$g.'</option>';
				}
			echo "</select></td>";
			if($_SESSION['gradeId'] != ""){
				if($_POST['majorId']!=""){$_SESSION['majorId'] = $_POST['majorId'];$_SESSION['semester'] = $_POST['semester'];}
				echo '<td>专业:</td><td>
					<select name = "majorId">';
					$mjrs = $db -> query("SELECT majorId,majorName FROM GradeMajor WHERE gradeId = '".$_SESSION['gradeId']."'");
					while($row = $mjrs -> fetch_assoc()){
						echo '<option value = "'.$row['majorId'].'"';
						if($_SESSION['majorId'] == $row['majorId'])echo "selected";
						echo ">".$row['majorName']."</option>";
					}
				echo'</select></td>
					<td>学期:</td>
					<td><select name = "semester">
						<option value = "">不限</option>
						<option value = "1a">大一上</option>
						<option value = "1b">大一下</option>
						<option value = "2a">大二上</option>
						<option value = "2b">大二下</option>
						<option value = "3a">大三上</option>
						<option value = "3b">大三下</option>
						<option value = "4a">大四上</option>
						<option value = "4b">大四下</option>
					</select></td>
					<td><input type = "submit" value = "确定"></td>';
			}
			echo "</table></form>";
			if($_SESSION['majorId'] != ""){
				$result = $db -> query('SELECT * FROM Course INNER JOIN TeachPlan ON(TeachPlan.courseId = Course.courseId)
					WHERE TeachPlan.gradeId = \''.$_SESSION['gradeId'].'\'
					AND TeachPlan.majorId = \''.$_SESSION['majorId'].'\'
					AND TeachPlan.semesterId LIKE \'%'.$_SESSION['semester'].'%\'');
			}
			if($result->num_rows >0){
				echo'<form action = "" method = "post" name = "sc" >
					<input type = "hidden" name = "csf" value = "2">
					<table class = "bordered">';
				echo '<thead><tr><th>课程号</th><th>课程名称</th><th>学分</th><th>开课学院</th><th>
				<input type = checkbox onchange = "selectAll(\'csId[]\')"></th></tr></thead>';
				while($row =$result -> fetch_assoc()){
					echo '<tr><td>'.$row['courseId'].'</td><td>'.$row['name'].'</td><td>'.$row['credit'].'</td><td>'.$row['faculty'].'</td>
					<td><input type = "checkbox" name = "csId[]" value = "'.$row['courseId'].'"></td></tr>';
				}
				echo '<tfoot><tr><td colspan = 5>*注:以上仅显示本学期开设的课程，如果课程重名请参考本专业教学计划。选课愉快！</td></tr></tfoot></table>
					 <input type = "submit" name = "cs" value = "确定选择以上课程"></form></td></tr>';
			}else{
			echo "没有搜索结果</br>";
			}
		}else if($_SESSION['choice'] == "2"){
			echo'
				<form action = "" method = "get" name = "searchCs">
					<label>课程名称</label><input name = "nm" value = ""><input type = "submit" value = "提交">
				</form>
				';
			$csName = $_GET['nm'];
			if($csName == "")$csName = "NoMatchName";
			$result = $db->query("SELECT * from Course WHERE name LIKE '%".$csName."%'");
			if($result->num_rows >0){
				echo'<table class = "bordered">
					<thead><tr><th>课程号</th><th>课程名称</th><th>学分</th><th>开课学院</th><th>选择</th></tr></thead>';
				while($row =$result -> fetch_assoc()){
					echo'<tr><td>'.$row['courseId'].'</td><td>'.$row['name'].'</td><td>'.$row['credit'].'</td><td>'.$row['faculty'].'</td>
					<td><form style = "margin:0px;display:inline;" action = "" method = "post" name = "sc">
					<input type = "hidden" name = "csId" value = "'.$row['courseId'].'">
					<input type = "hidden" name = "csf" value = "1">
					<input type = "submit" name = "cs" value = "选择"></form></td></tr>';
				}
				echo '<tfoot><tr><td colspan = 5>*注:以上仅显示本学期开设的课程，如果课程重名请参考本专业教学计划。选课愉快！</td></tr></tfoot></table>';
			}else{
			echo "没有搜索结果</br>";
			}
		}else if($_SESSION['choice'] == "3"){
			if($_POST['courseType'] == ''){
				echo'<form action = "" method = "post" name = "coursetype">
					<table><tr>
					<td>请选择课程类别</td>
					<td><select name = "courseType">
						<option value = "1">文化素质类</option>
						<option value = "2">英语类</option>
						<option value = "3">计算机类</option>
						<option value = "4">体育类</option>
					</select></td>
					<td><input type = "submit" value = "确认" name = specialCs></td>
					</tr></table>';
				echo'<h4>请勾选空出来的课程</h4><table class="zebra">';
				echo'
<style>.cc {color: rgb(247, 236, 236);font-weight: bold;padding: 10px;border: dotted 1px black;background: rgb(247, 236, 236);cursor: pointer;transition: background 0.5s, color 0.5s;-webkit-transition: background 0.5s, color 0.5s;}.cc:hover {color: black;background: rgb(175, 231, 110); }</style><thead><tr><th>节</th><th>周一</th><th>周二</th><th>周三</th><th>周四</th><th>周五</th></tr></thead>';
					for($ithlesson = 0;$ithlesson<13;++$ithlesson){
						echo'<tr><td>'.($ithlesson+1).'</td>';

						if($ithlesson == 0 || $ithlesson == 2||$ithlesson == 4||$ithlesson == 9||$ithlesson == 11)
						for($ithweekday = 0;$ithweekday <5;++$ithweekday){
							echo'<td rowspan = 2 class = "cc" abbr = "'.
							((1<<($ithlesson+13*$ithweekday))|(1<<($ithlesson+1+13*$ithweekday))).
							'" onclick = query(\'qtype=4&ctype=\'+document.getElementById("courseType").value+\'&filltime=\'+this.abbr,\'mainTable\')>选择此处</td>';
						}else if($ithlesson == 6)
						for($ithweekday = 0;$ithweekday <5;++$ithweekday){
							echo'<td rowspan = 3 class = "cc" abbr = "'.
							((1<<($ithlesson+13*$ithweekday))|(1<<($ithlesson+1+13*$ithweekday))|(1<<($ithlesson+2+13*$ithweekday))).
							'" onclick = query(\'qtype=4&ctype=\'+document.getElementById("courseType").value+\'&filltime=\'+this.abbr,\'mainTable\')>选择此处</td>';
						}
						echo'</tr>';
					}
				echo'
				</table></form>';
			}else{
				$fillTime = 0;
				$count = 0;
				foreach($_POST['timeAvail'] as $tm){
					$fillTime |= (int)$tm;
				}
				//echo $fillTime.'<br>';
				$fillTime = ~$fillTime;
				//echo $fillTime.'<br>';
				$result0 = $db->query('SELECT courseId from SpecialCourses WHERE courseType = \''.$_POST['courseType'].'\'');
				echo'<table class = "bordered">
					<thead><tr><th>课程号</th><th>课程名称</th><th>学分</th><th>开课学院</th><th>选择</th></tr></thead>';
				while($row0 =$result0 -> fetch_assoc()){
					$flag = 0;
					$result = $db->query('SELECT halfA,halfB from Lesson WHERE courseId =\''.$row0['courseId'].'\'');
					while($row = $result->fetch_assoc()){
						if(($fillTime & ($row['halfA']|$row['halfB']))==0){
							$flag = 1;
							break;
						}
					}
					if($flag == 0)continue;
					$count++;
					$result = $db->query('SELECT name,credit,faculty FROM Course WHERE courseId = \''.$row0['courseId'].'\'');
					$row = $result->fetch_assoc();
					echo'<tr><td>'.$row0['courseId'].'</td><td>'.$row['name'].'</td><td>'.$row['credit'].'</td><td>'.$row['faculty'].'</td>
					<td><form style = "margin:0px;display:inline;" action = "" method = "post" name = "sc">
					<input type = "hidden" name = "csId" value = "'.$row0['courseId'].'">
					<input type = "hidden" name = "csf" value = "1">
					<input type = "submit" name = "cs" value = "选择"></form></td></tr>';
				}
				echo '<tfoot><tr><td colspan = 5>所选时间段内共有'.$count.'门课。选课愉快！</td></tr></tfoot></table>';
			}
		}

	?>
</div>

<div class = "right" align = "center">
	<h3 color = "red">已经选择的课程:</h3><br>
	<?php
		//Delete an element.
		if($_POST['dtf'] == "1"){
			$key = array_search($_POST['dt'],$_SESSION['selectedCS']);
			array_splice($_SESSION['selectedCS'],$key,1);
		}else if($_POST['dtf'] == '2'){//Clear all
			$_SESSION['selectedCS'] = [];
		}
		//add a course.
		if($_POST['csf'] == "1"){
			array_push($_SESSION['selectedCS'],$_POST['csId']);
			//去重
			$_SESSION['selectedCS'] = array_unique($_SESSION['selectedCS']);
		}else if($_POST['csf'] == "2"){
			foreach($_POST['csId'] as $csid)array_push($_SESSION['selectedCS'],$csid);
			//去重
			$_SESSION['selectedCS'] = array_unique($_SESSION['selectedCS']);
		}
		if(!empty( $_SESSION['selectedCS'])){
			echo '<input type = button value = "开始选择开课班级" onclick = "window.open(\'csclass.php\')">
				<table>';
			foreach( $_SESSION['selectedCS'] as $choice){
				$name = $result = $db->query("SELECT name from Course where courseId = ".htmlentities($choice));
				echo '<tr><td>'.htmlentities($choice).':'.
				$name ->fetch_assoc()['name'].
				'<form action = "" method = "post" name = "delete">'.
				'<input type = "hidden" name = "dt" value = "'.htmlentities($choice).'">'.
				'<input type = "hidden" name = "dtf" value = "1">'.
				'<td><input type = "submit" value = "删除">'.
				'</form></td></tr>';
			}
			echo'</table>';
			echo'<form action = "" method = post name = delete><input type = "hidden" name = "dtf" value = "2"><input type = submit value = 删除全部已选课程></form>';
		}else{
			echo "选择课程为空！";
		}
	?>
</div>

</div>
</body>
</html>