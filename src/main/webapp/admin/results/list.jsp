<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.myapp.model.StudentResult" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>Manage Results</title>
</head>
<body>

<h2>Student Results Management</h2>

<p>
  <a href="<%= request.getContextPath() %>/admin/results/create">
    <button type="button">+ Add Result</button>
  </a>
</p>

<form method="get" action="<%= request.getContextPath() %>/admin/results">
  Filter by Student ID:
  <input type="text" name="student_id" value="<%= request.getAttribute("filterStudentId") != null ? request.getAttribute("filterStudentId") : "" %>" />
  <button type="submit">Filter</button>
  <a href="<%= request.getContextPath() %>/admin/results">Reset</a>
</form>

<br/>

<%
  List<StudentResult> results = (List<StudentResult>) request.getAttribute("results");
%>

<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
  <tr>
    <th>Result ID</th>
    <th>Student ID</th>
    <th>Course Code</th>
    <th>Assessment</th>
    <th>Grade</th>
    <th>Grade Point</th>
    <th>Passed</th>
    <th>Actions</th>
  </tr>

  <%
    if (results == null || results.isEmpty()) {
  %>
    <tr><td colspan="8" style="text-align:center;">No results found.</td></tr>
  <%
    } else {
      for (StudentResult r : results) {
  %>
    <tr>
      <td><%= r.getResultId() %></td>
      <td><%= r.getStudentId() %></td>
      <td><%= r.getCourseCode() %></td>
      <td><%= r.getComponentName() %> (#<%= r.getAssessmentId() %>)</td>
      <td><%= r.getGrade() %></td>
      <td><%= r.getGradePoint() %></td>
      <td><%= r.isPassed() ? "PASS" : "FAIL" %></td>
      <td>
        <a href="<%= request.getContextPath() %>/admin/results/edit?id=<%= r.getResultId() %>">Edit</a>
        |
        <a href="<%= request.getContextPath() %>/admin/results/delete?id=<%= r.getResultId() %>"
           onclick="return confirm('Delete this result?');">Delete</a>
      </td>
    </tr>
  <%
      }
    }
  %>
</table>

</body>
</html>
