<!DOCTYPE html>
<?php

$db = new mysqli('localhost', 'testu', '123', 'cs');
if ($db->connect_error) {
    die("连接失败：" . $db->connect_error);
}
$db->query("set names 'utf8'");
?>
<script language="javascript">
var spareTime=0n;

function selecAll(a){
    o=document.getElementsByName(a)
    for(i=0;i<o.length;i++)
        o[i].checked=event.srcElement.checked
}

function setBit(id){
    tm = BigInt(id);
    spareTime^=tm;
    var currentItem = document.getElementById(id);
    if((spareTime&tm)==0n){
        currentItem.setAttribute("class","s_ok");
        currentItem.innerHTML="有空"
    }
    else{
        currentItem.setAttribute("class","s_busy");
        currentItem.innerHTML="没空"
    }
        
    document.getElementById("sp_Time").setAttribute("value",spareTime);
}

</script>
<html>

<head>
    <style type="text/css">
        .whole {
            margin: auto;
            width: 800px;
            border: 0px;
            background-color: white;
            overflow: hidden;
        }

        .main {
            margin-left: auto;
            width: 600px;
            float: left;
        }

        .right {
            margin-left: 40px;
            width: 300px;
            float: right;
        }

        .s_ok {
            color: black;
            background: rgb(158, 231, 74);
            font-weight: bold;
            padding: 10px;
            border: dotted 1px black;
            cursor: pointer;
            transition: background 0.5s, color 0.5s;
            -webkit-transition: background 0.5s, color 0.5s;
        }

        .s_ok:hover {
            color: rgb(19, 14, 14);
            background: rgb(243, 105, 105);
        }

        .s_busy {
            color: rgb(19, 14, 14);
            background: rgb(243, 105, 105);
            font-weight: bold;
            padding: 10px;
            border: dotted 1px black;
            cursor: pointer;
            transition: background 0.5s, color 0.5s;
            -webkit-transition: background 0.5s, color 0.5s;
        }

        .s_busy:hover {
            color: black;
            background: rgb(158, 231, 74);
        }
    </style>
    <link rel="stylesheet" type="text/css" href="tablestyle.css">
    <title>选择开课班级</title>
</head>
<h4 align=center>请将不准备上课的时间标红</h4>
<div class=whole align="center">
    <form action="begin.php" method="get" name="chooseClass">
        <input type = "hidden" id = "sp_Time"name = "spare" value = "0">
        <div>
            <table>
                <tr>
                    <td>
                        最大冲突学分（0.5整数倍）：
                    </td>
                    <td>
                        <input name="MaxConflict" value="0">
                    </td>
                    <td>
                        <input type=submit value=完成>
                    </td>
                </tr>
            </table>
            <table id="chooseTime" class="bordered" align = "center">
                <thead>
                    <tr>
                        <th>节</th>
                        <th>周一</th>
                        <th>周二</th>
                        <th>周三</th>
                        <th>周四</th>
                        <th>周五</th>
                    </tr>
                </thead>
                <tr>
                    <td>1</td>
                    <td rowspan=2 id="3" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="24576" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="201326592" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="1649267441664" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="13510798882111488" class="s_ok" onclick=setBit(this.id)>有空</td>
                </tr>
                <tr>
                    <td>2</td>
                </tr>
                <tr>
                    <td>3</td>
                    <td rowspan=2 id="12" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="98304" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="805306368" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="6597069766656" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="54043195528445952" class="s_ok" onclick=setBit(this.id)>有空</td>
                </tr>
                <tr>
                    <td>4</td>
                </tr>
                <tr>
                    <td>5</td>
                    <td rowspan=2 id="48" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="393216" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="3221225472" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="26388279066624" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="216172782113783808" class="s_ok" onclick=setBit(this.id)>有空</td>
                </tr>
                <tr>
                    <td>6</td>
                </tr>
                <tr>
                    <td>7</td>
                    <td rowspan=3 id="448" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=3 id="3670016" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=3 id="30064771072" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=3 id="246290604621824" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=3 id="2017612633061982208" class="s_ok" onclick=setBit(this.id)>有空</td>
                </tr>
                <tr>
                    <td>8</td>
                </tr>
                <tr>
                    <td>9</td>
                </tr>
                <tr>
                    <td>10</td>
                    <td rowspan=2 id="1536" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="12582912" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="103079215104" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="844424930131968" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="6917529027641081856" class="s_ok" onclick=setBit(this.id)>有空</td>
                </tr>
                <tr>
                    <td>11</td>
                </tr>
                <tr>
                    <td>12</td>
                    <td rowspan=2 id="6144" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="50331648" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="412316860416" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="3377699720527872" class="s_ok" onclick=setBit(this.id)>有空</td>
                    <td rowspan=2 id="-9223372036854775807" class="s_ok" onclick=setBit(this.id)>有空</td>
                </tr>
                <tr>
                    <td>13</td>
                </tr>
            </table>
            <br />
            <input id = "showLessons" type="button" value="如果选择了思政课、体育课或者文素课，请点击此按钮，并将不属于自己校区的课程取消选择" onclick = "document.getElementById('chooseLesson').style.display='';document.getElementById('showLessons').style.display='none'">
            <div id=chooseLesson style="display:none;">
                <?php
                foreach ($_GET['selectedCS'] as $csId) {
                    echo '<input type = "hidden" name = "selectedCS[]" value = "' . $csId . '">';
                
                    $result = $db->query("SELECT name FROM Course WHERE courseId = " . $csId);
                    echo "<h3 align = center>" . $result->fetch_assoc()['name'] . '</h3>';
                    echo "<table border=1px align = center><thead><tr><th>教师</th><th>选课建议</th><th>上课时间地点</th><th>
                                <input type = checkBox checked = true onchange = selecAll('" . $csId . "[]')>
                                </th></tr></thead>";
                    $result = $db->query("SELECT lessonId,teacher,recommend FROM Lesson WHERE courseId = " . $csId);
                    while ($row = $result->fetch_assoc()) {
                        echo "<tr><td align = center>" . $row['teacher'] . "</td><td align = center>" . $row['recommend'] . "</td><td align = center>";
                        $timer = $db->query("SELECT time,place FROM lessonTime WHERE lessonId = " . $row['lessonId']);
                        while ($trow = $timer->fetch_assoc()) {
                            echo $trow['time'] . " " . $trow['place'] . "<br>";
                        }
                        echo "</td>";
                        echo "<td align = center><input type = checkbox checked = true name = " . $csId . "[] value = \"" . $row['lessonId'] . "\"></td></tr>";
                    }
                    echo "</table>";
                }
                ?>
            </div>
        </div>
    </form>
</div>
</html>