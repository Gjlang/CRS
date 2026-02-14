<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.Student" %>

<!DOCTYPE html>
<html>
<head>
  <title>Edit Student</title>
</head>
<body>

<h2>Edit Student</h2>

<p>
  <a href="<%= request.getContextPath() %>/admin/students">← Back to Students</a>
</p>

<%
  String error = (String) request.getAttribute("error");
  Student s = (Student) request.getAttribute("student");
  if (error != null) {
%>
  <p style="color:red;"><b><%= error %></b></p>
<%
  }
  if (s == null) {
%>
  <p>Student not found.</p>
</body>
</html>
<%
    return;
  }
%>

<form method="post" action="<%= request.getContextPath() %>/admin/students/edit">
  <table cellpadding="6">
    <tr>
      <td>Student ID</td>
      <td>
        <input type="text" name="studentId" value="<%= s.getStudentId() %>" readonly />
      </td>
    </tr>
    <tr>
      <td>Name</td>
      <td><input type="text" name="name" value="<%= s.getName() %>" required /></td>
    </tr>
    <tr>
      <td>Program</td>
      <td><input type="text" name="program" value="<%= s.getProgram() %>" required /></td>
    </tr>
    <tr>
      <td>Year of Study</td>
      <td><input type="number" name="yearOfStudy" value="<%= s.getYearOfStudy() %>" min="1" max="10" required /></td>
    </tr>
    <tr>
      <td>Email</td>
      <td><input type="email" name="email" value="<%= s.getEmail() %>" required /></td>
    </tr>
    <tr>
      <td>Status</td>
      <td>
        <select name="status">
          <option value="ACTIVE" <%= "ACTIVE".equalsIgnoreCase(s.getStatus()) ? "selected" : "" %>>ACTIVE</option>
          <option value="INACTIVE" <%= "INACTIVE".equalsIgnoreCase(s.getStatus()) ? "selected" : "" %>>INACTIVE</option>
        </select>
      </td>
    </tr>
  </table>

  <p>
    <button type="submit">Save Changes</button>
  </p>
</form>

</body>
</html>
