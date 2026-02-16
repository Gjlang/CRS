<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.myapp.model.Assessment" %>
<%@ page import="com.myapp.model.Course" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>Manage Assessments</title>
</head>
<body>

<%
  Course course = (Course) request.getAttribute("course");
  List<Assessment> assessments = (List<Assessment>) request.getAttribute("assessments");
%>

<h2>Assessments for Course: <%= course.getCourseCode() %> - <%= course.getTitle() %></h2>

<p>
  <a href="<%= request.getContextPath() %>/admin/courses">← Back to Courses</a>
</p>

<p>
  <a href="<%= request.getContextPath() %>/admin/assessments/create?course_code=<%= course.getCourseCode() %>">
    <button type="button">+ Add Assessment</button>
  </a>
</p>

<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
  <tr>
    <th>ID</th>
    <th>Component Name</th>
    <th>Weight (%)</th>
    <th>Actions</th>
  </tr>

  <%
    if (assessments == null || assessments.isEmpty()) {
  %>
    <tr><td colspan="4" style="text-align:center;">No assessments found.</td></tr>
  <%
    } else {
      for (Assessment a : assessments) {
  %>
    <tr>
      <td><%= a.getAssessmentId() %></td>
      <td><%= a.getComponentName() %></td>
      <td><%= a.getWeightPercentage() %></td>
      <td>
        <a href="<%= request.getContextPath() %>/admin/assessments/edit?id=<%= a.getAssessmentId() %>">Edit</a>
        |
        <a href="<%= request.getContextPath() %>/admin/assessments/delete?id=<%= a.getAssessmentId() %>&course_code=<%= course.getCourseCode() %>"
           onclick="return confirm('Delete this assessment?');">Delete</a>
      </td>
    </tr>
  <%
      }
    }
  %>
</table>

</body>
</html>
