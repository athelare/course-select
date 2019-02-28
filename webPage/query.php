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
    //不同的查询类型

    //1代表课程名称
    //需要参数：nm:代表课程名称
    if($_GET['qtype']=="1"){
        if($_GET['nm'] == "")$csName = "NoMatchName";
        else $csName=$_GET['nm'];
		$result = $db->query("SELECT * from Course WHERE name LIKE '%".$csName."%'");
		if($result->num_rows >0){
			echo'<thead><tr><th>课程号</th><th>课程名称</th><th>学分</th><th>开课学院</th><th>
			<input type = "button" name = "cs" value = "全选" onclick = "chooseAll()">
			</th></tr></thead>';
			while($row =$result -> fetch_assoc()){
                echo'<tr>
                    <td>'.$row['courseId'].'</td>
                    <td>'.$row['name'].'</td>
                    <td>'.$row['credit'].'</td>
                    <td>'.$row['faculty'].'</td>
                    <td><input type = "button" name = "cs" value = "选择" onclick = "chooseThis(\''.$row['courseId'].'\',\''.$row['name'].'\')"></td>
                </tr>';
			}
			echo '<tfoot><tr><td colspan = 5>*注:以上仅显示本学期开设的课程，如果课程重名请参考本专业教学计划。选课愉快！</td></tr></tfoot>';
		}else{
		echo "没有搜索结果</br>";
		}
    }
    //根据学年查询专业代号(更改专业select里面的值)
    //提供参数：gradeId
    else if($_GET['qtype']=="2"){
		$mjrs = $db -> query("SELECT majorId,majorName FROM GradeMajor WHERE gradeId = '".$_GET['gradeId']."'");
		while($row = $mjrs -> fetch_assoc()){
			echo '<option value = "'.$row['majorId'].'">'.$row['majorName'].'</option>';
        }
    }
    //根据年级，专业，学期查询课程
    //需要的参数：gradeId,majorId,semester
    else if($_GET['qtype']=="3"){
		$result = $db -> query('SELECT * FROM Course INNER JOIN TeachPlan ON(TeachPlan.courseId = Course.courseId)
			WHERE TeachPlan.gradeId = \''.$_GET['gradeId'].'\'
			AND TeachPlan.majorId = \''.$_GET['majorId'].'\'
			AND TeachPlan.semesterId LIKE \'%'.$_GET['semester'].'%\'');
		if($result->num_rows >0){
            echo'<thead>
                    <tr>
                        <th>课程号</th><th>课程名称</th><th>学分</th><th>开课学院</th><th>
						<input type = "button" name = "cs" value = "全选" onclick = "chooseAll()">
						</th>
                    </tr>
                </thead>';
			while($row =$result -> fetch_assoc()){
                echo'<tr>
                    <td>'.$row['courseId'].'</td>
                    <td>'.$row['name'].'</td>
                    <td>'.$row['credit'].'</td>
                    <td>'.$row['faculty'].'</td>
                    <td><input type = "button" name = "cs" value = "选择" onclick = "chooseThis(\''.$row['courseId'].'\',\''.$row['name'].'\')"></td>
                </tr>';
			}
			echo '<tfoot><tr><td colspan = 5>*注:以上仅显示本学期开设的课程，如果课程重名请参考本专业教学计划。选课愉快！</td></tr></tfoot>';
		}else{
		echo "没有搜索结果</br>";
		}
    }
    //查询文化素质类，体育类，计算机类，英语类课程
    //需要参数:qtype = 4;ctype:代表课程的种类，filltime为设置的代表有空的时间值（64位制）
    else if($_GET['qtype']=="4"){
		$count = 0;
        $fillTime = ~((int)$_GET['filltime']);
        $result0 = $db->query('SELECT courseId from SpecialCourses WHERE courseType = \''.$_GET['ctype'].'\'');
		echo'<thead><tr><th>课程号</th><th>课程名称</th><th>学分</th><th>开课学院</th><th>
		<input type = "button" name = "cs" value = "全选" onclick = "chooseAll()">
		</th></tr></thead>';
		while($row0 =$result0 -> fetch_assoc()){
			$flag = 0;
			$result = $db->query('SELECT halfA,halfB from Lesson WHERE courseId =\''.$row0['courseId'].'\'');
			while($row = $result->fetch_assoc()){
				if(($fillTime & ($row['halfA']|$row['halfB']))==0){
					$flag = 1;//有一个可以的时间，相与的结果就为零，说明这一个课程可以选择
					break;
				}
			}
			if($flag == 0)continue;
			$count++;
			$result = $db->query('SELECT name,credit,faculty FROM Course WHERE courseId = \''.$row0['courseId'].'\'');
			$row = $result->fetch_assoc();
			echo'<tr>
                <td>'.$row0['courseId'].'</td>
     		    <td>'.$row['name'].'</td>
                <td>'.$row['credit'].'</td>
                <td>'.$row['faculty'].'</td>
                <td><input type = "button" name = "cs" value = "选择" onclick = "chooseThis(\''.$row0['courseId'].'\',\''.$row['name'].'\')"></td>
            	</tr>';
		}
		echo '<tfoot><tr><td colspan = 4>所选时间段内共有'.$count.'门课。选课愉快！</td><td><input type = button value = "返回" onclick = "openChoice(\'byType\');spareTime()"></td></tr></tfoot>';
	}
	//在给出选课时段二进制表示情况下查找所有的文素、体育、计算机、英语选课方案
	else if($_GET['qtype']=="5"){
		$count = 0;
		$halfA = (int)$_GET['halfA'];
		$halfB = (int)$_GET['halfB'];
		echo'<thead><tr><th>课程号</th><th>课程名称</th><th>学分</th><th>开课学院</th><th>选择</th></tr></thead>';
		$result0 = $db->query('SELECT courseId from SpecialCourses');
		while($row0 =$result0 -> fetch_assoc()){
			$flag = 0;
			$result = $db->query('SELECT halfA,halfB from Lesson WHERE courseId =\''.$row0['courseId'].'\'');
			while($row = $result->fetch_assoc()){
				if(($halfA & $row['halfA'])==0&&($halfB & $row['halfB'])==0){
					$flag = 1;//有一个可以的时间，相与的结果就为零，说明这一个课程可以选择
					break;
				}
			}
			if($flag == 0)continue;
			$count++;
			$result = $db->query('SELECT name,credit,faculty FROM Course WHERE courseId = \''.$row0['courseId'].'\'');
			$row = $result->fetch_assoc();
			echo'<tr>
                <td>'.$row0['courseId'].'</td>
     		    <td>'.$row['name'].'</td>
                <td>'.$row['credit'].'</td>
				<td>'.$row['faculty'].'</td>
				<td><a href = "http://jwdep.dhu.edu.cn/dhu/student/selectcourse/teachclasslist.jsp?courseId='.$row0['courseId'].'&courseName='.urlencode(iconv("UTF-8", "gb2312", $row['name'])).'" target = "view_window">选择</a></td>
            	</tr>';
		}
		echo '<tfoot><tr><td colspan = 5>*注:以上仅显示本学期开设的课程，如果课程重名请参考本专业教学计划。选课愉快！</td></tr></tfoot>';
	}
?>