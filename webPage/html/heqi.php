<!DoCTYPE html>
<html>
<h1 align = center>和气气最可爱了！是不是？</h1>
<table align = center>
    <tr>
        <td><form action = "" method = post>
                <input type = "hidden" value = "1" name = "ch">
                <input type = "submit" value = "当然了，难道还有更可爱的吗！">
            </form>
        </td>
        <td>
        <form action = "" method = post>
                <input type = "hidden" value = "2" name = "ch">
                <input type = "submit" value = "哦，是吗？">
            </form>
        </td>
    </tr>
</table>

<?php
    if($_POST['ch'] == "1"){
         echo"<h2 align = center>那当然，恭喜你答对了这个送命题！</h2>";
    }else if($_POST['ch'] == "2"){
        echo"<h2 align = center>你竟然这样不坚定。。。你完了！！！！</h2>";
    }else{
        echo"<p align = center>你有三秒时间来回答这个问题。。</p>";
    }
?>
    <p align = center>友情赞助：esz</p>
</html>