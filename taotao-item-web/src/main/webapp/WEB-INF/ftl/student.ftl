<html>
<head>
<title>测试页面</title>
</head>
<body>
学生信息：<br>
学号：{student.id}<br>
姓名：{student.name}<br>
年龄：{student.age}<br>
地址：{student.address}<br>
 <table border="1">
 <tr>
     <th>序号</th>
     <th>学号</th>
     <th>姓名</th>
     <th>年龄</th>
     <th>家庭住址</th>
     <#list studentList as student> 
     </tr>
     <tr>
     <td>${student_index}</td>
     <td>${student.id}</td>
     <td>${student.name}</td>
     <td>${student.age}</td>
      </tr>

 </table>

</body>

</html>