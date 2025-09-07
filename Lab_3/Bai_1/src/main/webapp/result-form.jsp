<%@ page import="iuh.fit.se.bai_1.Student" %><%--
  Created by IntelliJ IDEA.
  User: Student
  Date: 9/7/2025
  Time: 10:52 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        Student student = new Student();
        student = (Student) request.getAttribute("student");
        out.println(
                "First name:" + student.getFirstName()
                + "<br/> LastName" + student.getLastName()
                + "<br/> Birthday" + student.getDateOfBirth()
                + "<br/> Email" + student.getEmail()

        );
    %>
</body>
</html>
