<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.Assessment" %>
<%@ page import="com.myapp.model.Course" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>Edit Assessment</title>
</head>
<body>

<%
  Assessment a = (Assessment) request.getAttribute("assessment");
  Course course = (Course) request.getAttribute("course");
  String error = (String) request.getAttribute("error");

  if (a == null) {
%>
  <p>Assessment not found.</p>
</body>
</html>
<%
    return;
  }
%>

<h2>Edit Assessment (ID: <%= a.getAssessmentId() %>)</h2>

<p>
  <a href="<%= request.getContextPath() %>/admin/assessments?course_code=<%= a.getCourseCode() %>">← Back</a>
</p>

<%
  if (error != null) {
%>
  <p style="color:red;"><b><%= error %></b></p>
<%
  }
%>

<form method="post" action="<%= request.getContextPath() %>/admin/assessments/edit">
  <input type="hidden" name="assessmentId" value="<%= a.getAssessmentId() %>" />
  <input type="hidden" name="courseCode" value="<%= a.getCourseCode() %>" />

  <table cellpadding="6">
    <tr>
      <td>Course</td>
      <td><input type="text" value="<%= (course != null ? course.getCourseCode() : a.getCourseCode()) %>" readonly /></td>
    </tr>
    <tr>
      <td>Component Name</td>
      <td><input type="text" name="componentName" value="<%= a.getComponentName() %>" required /></td>
    </tr>
    <tr>
      <td>Weight (%)</td>
      <td><input type="number" step="0.01" name="weightPercentage" value="<%= a.getWeightPercentage() %>" min="0" max="100" required /></td>
    </tr>
  </table>

  <p><button type="submit">Save Changes</button></p>
</form>

</body>
</html>
