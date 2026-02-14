<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
  <title>Create Student</title>
</head>
<body>

<h2>Create Student</h2>

<p>
  <a href="<%= request.getContextPath() %>/admin/students">← Back to Students</a>
</p>

<%
  String error = (String) request.getAttribute("error");
  if (error != null) {
%>
  <p style="color:red;"><b><%= error %></b></p>
<%
  }
%>

<form method="post" action="<%= request.getContextPath() %>/admin/students/create">
  <table cellpadding="6">
    <tr>
      <td>Student ID</td>
      <td><input type="text" name="studentId" required /></td>
    </tr>
    <tr>
      <td>Name</td>
      <td><input type="text" name="name" required /></td>
    </tr>
    <tr>
      <td>Program</td>
      <td><input type="text" name="program" required /></td>
    </tr>
    <tr>
      <td>Year of Study</td>
      <td><input type="number" name="yearOfStudy" min="1" max="10" required /></td>
    </tr>
    <tr>
      <td>Email</td>
      <td><input type="email" name="email" required /></td>
    </tr>
  </table>

  <p>
    <button type="submit">Create</button>
  </p>
</form>

</body>
</html>
