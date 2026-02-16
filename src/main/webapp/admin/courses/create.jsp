<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>Create Course</title>
</head>
<body>

<h2>Create Course</h2>

<p><a href="<%= request.getContextPath() %>/admin/courses">← Back to Courses</a></p>

<%
  String error = (String) request.getAttribute("error");
  if (error != null) {
%>
  <p style="color:red;"><b><%= error %></b></p>
<%
  }
%>

<form method="post" action="<%= request.getContextPath() %>/admin/courses/create">
  <table cellpadding="6">
    <tr>
      <td>Course Code</td>
      <td><input type="text" name="courseCode" required /></td>
    </tr>
    <tr>
      <td>Title</td>
      <td><input type="text" name="title" required /></td>
    </tr>
    <tr>
      <td>Credit Hours</td>
      <td><input type="number" name="creditHours" min="1" max="20" required /></td>
    </tr>
  </table>

  <p><button type="submit">Create</button></p>
</form>

</body>
</html>
