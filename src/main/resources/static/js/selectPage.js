//根据查询到的课程实体填充表格
function fillCourseList(result) {
    $('#findByTime').css('display','none');
    $('#findByPlan').css('display','');
    $('#btn_byTime').removeClass('active');
    $('#btn_byPlan').addClass('active');
    var tableBody=$("#courseListForChoose");
    var hiddenResult=$("#hiddenResult");
    tableBody.empty();
    hiddenResult.empty();
    if(result.length === 0){
        tableBody.append(
            "                <tr class='text-center'>\n" +
            "                    <td colspan='5'>\n" +
            "                        <div class='alert alert-warning'>\n" +
            "                            根据条件未检索到课程。\n" +
            "                        </div>\n" +
            "                    </td>\n" +
            "                </tr>\n");
    }
    else {
        for (var i in result) {
            hiddenResult.append("<tr>" +
                "<td>" + result[i]['courseId'] + "</td>" +
                "<td>" + result[i]['courseName'] + "</td>" +
                "<td>" + result[i]['credit'] + "</td>" +
                "<td>" + result[i]['school'] + "</td>" +
                "<td><input type='button' class='btn btn-success btn-xs' onclick='addOneCourse(\"" + result[i]['courseId'] + "\",\"" + result[i]['courseName'] + "\")' value='点击选择'/></td>" +
                "</tr>"
            )
        }
        var pageBar=$("#Pagination");
        var resultRows=$('#hiddenResult tr');
        if(resultRows.length>10) {
            pageBar.css('display','');
            var num_entries = resultRows.length;
            // 创建分页
            pageBar.pagination(num_entries, {
                num_edge_entries: 1, //边缘页数
                num_display_entries: 4, //主体页数
                prev_text: '上一页',
                next_text: '下一页',
                callback: pageSelectCallback,
                items_per_page: 10 //每页显示10项
            });
        }else{
            pageBar.css('display','none');
            tableBody.append(resultRows);
        }
    }
}
//分页回调函数
function pageSelectCallback(page_index, jq){
    var tableBody=$("#courseListForChoose");
    tableBody.empty();
    var maxElem = $("#hiddenResult tr").length;
    for(var i=page_index*10;i<10+page_index*10;++i){
        if(i<maxElem){
            tableBody.append($("#hiddenResult tr:eq("+i+")").clone())
        }else{
            var lineHei=$('#courseListForChoose tr:eq(0)').css('height');
            tableBody.append("<tr><td colspan='5' style='height: "+lineHei+";'></td></tr>");
        }
    }
    return false;
}
function getMajorList(gradeId){
    var majors=$("#majorList");
    majors.empty();

    if(gradeId===""){
        majors.append('<option value="">请先选择年级</option>');
        majors.attr("disabled","disabled");
    }else{
        majors.removeAttr('disabled');
        $.getJSON("majors/"+gradeId,function (result) {
            for(var i in result){
                if(result[i].hasOwnProperty('majorId') && result[i].hasOwnProperty('majorName')) {
                    majors.append('<option value=' + result[i]['majorId'] + '>' + result[i]['majorName']  + '</option>');
                }
            }
        })
    }
}
function deleteOneCourse(courseId){
    var chosenCourseTable = document.getElementById("chosenCourses");
    for(var r = 0;r<chosenCourseTable.rows.length;++r)
        if(courseId === chosenCourseTable.rows[r].cells[0].innerHTML){
            chosenCourseTable.deleteRow(r);
            if(chosenCourseTable.rows.length === 0){
                document.getElementById("next").setAttribute("disabled","disabled");
            }
            return;
        }
}
function addOneCourse(courseId,courseName) {
    var chosenCourseTable = document.getElementById('chosenCourses');
    document.getElementById("next").removeAttribute("disabled");
    for(var r = 0;r<chosenCourseTable.rows.length;++r)
        if(courseId === chosenCourseTable.rows[r].cells[0].innerHTML)return;
    var newRow = chosenCourseTable.insertRow();
    newRow.innerHTML='<td>'+courseId+'</td><td>'+courseName+'</td><td><input type="hidden" name="courseId" value="'+courseId+'"><input type = button class="btn btn-danger btn-xs" value = "删除" onclick = deleteOneCourse("'+courseId+'")></td>';
    newRow.setAttribute('class','success');
}
function getCoursesByName(name){
    var inputName = $('#courseNameDiv');
    if(name.length === 0){
        inputName.addClass("has-error");
        $('#inputCourseName').attr("placeholder","课程名称不能为空");
    }else{
        inputName.removeClass("has-error");
        inputName.addClass("has-success");
        $.getJSON("courses/name="+name,function (result) {
            fillCourseList(result);
        })
    }
}
function getCoursesByPlan(grade,major,semester){
    var tableBody=$("#courseListForChoose");
    if(grade==='' || major===''){
        tableBody.empty();
        tableBody.append(
            "                <tr class='text-center'>\n" +
            "                    <td colspan='5'>\n" +
            "                        <div class='alert alert-warning'>\n" +
            "                            年级和专业序号不能为空。\n" +
            "                        </div>\n" +
            "                    </td>\n" +
            "                </tr>\n");
    }else{
        $.getJSON("courses/plan/"+grade+"/"+major+"/"+semester,function (result) {
            fillCourseList(result);
        })
    }
}
function getCourseByTime(time) {
    var type=$('#type').val();
    $.getJSON("courses/type/"+type+"/"+time,function (result) {
        fillCourseList(result);
    })
}