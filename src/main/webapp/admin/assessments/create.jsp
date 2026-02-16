<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.Course" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>Add Assessment</title>
</head>
<body>

<%
  Course course = (Course) request.getAttribute("course");
  String error = (String) request.getAttribute("error");
%>

<h2>Add Assessment for <%= course.getCourseCode() %> - <%= course.getTitle() %></h2>

<p>
  <a href="<%= request.getContextPath() %>/admin/assessments?course_code=<%= course.getCourseCode() %>">← Back</a>
</p>

<%
  if (error != null) {
%>
  <p style="color:red;"><b><%= error %></b></p>
<%
  }
%>

<form method="post" action="<%= request.getContextPath() %>/admin/assessments/create">
  <input type="hidden" name="courseCode" value="<%= course.getCourseCode() %>" />

  <table cellpadding="6">
    <tr>
      <td>Component Name</td>
      <td><input type="text" name="componentName" required /></td>
    </tr>
    <tr>
      <td>Weight (%)</td>
      <td><input type="number" step="0.01" name="weightPercentage" min="0" max="100" required /></td>
    </tr>
  </table>

  <p><button type="submit">Add</button></p>
</form>

</body>
</html>
