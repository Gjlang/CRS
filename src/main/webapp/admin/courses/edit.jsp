<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.Course" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>Edit Course</title>
</head>
<body>

<h2>Edit Course</h2>

<p><a href="<%= request.getContextPath() %>/admin/courses">← Back to Courses</a></p>

<%
  String error = (String) request.getAttribute("error");
  Course c = (Course) request.getAttribute("course");

  if (error != null) {
%>
  <p style="color:red;"><b><%= error %></b></p>
<%
  }

  if (c == null) {
%>
  <p>Course not found.</p>
</body>
</html>
<%
    return;
  }
%>

<form method="post" action="<%= request.getContextPath() %>/admin/courses/edit">
  <table cellpadding="6">
    <tr>
      <td>Course Code</td>
      <td><input type="text" name="courseCode" value="<%= c.getCourseCode() %>" readonly /></td>
    </tr>
    <tr>
      <td>Title</td>
      <td><input type="text" name="title" value="<%= c.getTitle() %>" required /></td>
    </tr>
    <tr>
      <td>Credit Hours</td>
      <td><input type="number" name="creditHours" value="<%= c.getCreditHours() %>" min="1" max="20" required /></td>
    </tr>
  </table>

  <p><button type="submit">Save Changes</button></p>
</form>

</body>
</html>
