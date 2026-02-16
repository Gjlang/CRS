<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.myapp.model.StudentResult" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>Student Result Summary</title>
</head>
<body>

<h2>Student Result Summary (Academic)</h2>

<p>
  <a href="<%= request.getContextPath() %>/academic/dashboard.jsp">← Back to Dashboard</a>
</p>

<form method="get" action="<%= request.getContextPath() %>/academic/results">
  Student ID:
  <input type="text" name="student_id" value="<%= request.getAttribute("studentId") != null ? request.getAttribute("studentId") : "" %>" />
  <button type="submit">View</button>
</form>

<br/>

<%
  String error = (String) request.getAttribute("error");
  if (error != null) {
%>
  <p style="color:red;"><b><%= error %></b></p>
<%
  }

  String studentId = (String) request.getAttribute("studentId");
  Integer failCount = (Integer) request.getAttribute("failCount");
  List<StudentResult> results = (List<StudentResult>) request.getAttribute("results");

  if (studentId != null && error == null) {
%>

  <h3>Summary for Student: <%= studentId %></h3>
  <p><b>Total Failed Courses:</b> <%= (failCount != null ? failCount : 0) %></p>

  <table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
    <tr>
      <th>Course Code</th>
      <th>Assessment</th>
      <th>Grade</th>
      <th>Grade Point</th>
      <th>Status</th>
    </tr>

    <%
      if (results == null || results.isEmpty()) {
    %>
      <tr><td colspan="5" style="text-align:center;">No results found for this student.</td></tr>
    <%
      } else {
        for (StudentResult r : results) {
    %>
      <tr>
        <td><%= r.getCourseCode() %></td>
        <td><%= r.getComponentName() %> (#<%= r.getAssessmentId() %>)</td>
        <td><%= r.getGrade() %></td>
        <td><%= r.getGradePoint() %></td>
        <td><%= r.isPassed() ? "PASS" : "FAIL" %></td>
      </tr>
    <%
        }
      }
    %>
  </table>

<%
  }
%>

</body>
</html>
