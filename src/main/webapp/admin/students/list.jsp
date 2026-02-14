<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.myapp.model.Student" %>

<!DOCTYPE html>
<html>
<head>
  <title>Admin - Students</title>
</head>
<body>

<h2>Student Management</h2>

<%
  List<Student> students = (List<Student>) request.getAttribute("students");
%>

<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
  <tr>
    <th>Student ID</th>
    <th>Name</th>
    <th>Program</th>
    <th>Year</th>
    <th>Email</th>
    <th>Status</th>
    <th>Actions</th>
  </tr>

  <%
    if (students == null || students.isEmpty()) {
  %>
    <tr><td colspan="7" style="text-align:center;">No students found.</td></tr>
  <%
    } else {
      for (Student s : students) {
  %>
    <tr>
      <td><%= s.getStudentId() %></td>
      <td><%= s.getName() %></td>
      <td><%= s.getProgram() %></td>
      <td><%= s.getYearOfStudy() %></td>
      <td><%= s.getEmail() %></td>
      <td><%= s.getStatus() %></td>
      <td>
        <a href="<%= request.getContextPath() %>/admin/students/edit?id=<%= s.getStudentId() %>">Edit</a>

        |
        <a href="<%= request.getContextPath() %>/admin/students/deactivate?id=<%= s.getStudentId() %>"
           onclick="return confirm('Deactivate this student?');">Deactivate</a>
      </td>
    </tr>
  <%
      }
    }
  %>
</table>

</body>
</html>
