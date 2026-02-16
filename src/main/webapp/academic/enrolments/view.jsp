<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.Enrolment" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>View Enrolment</title>
</head>
<body>

<h2>Enrolment Details</h2>

<p>
  <a href="<%= request.getContextPath() %>/academic/enrolments">← Back to Pending List</a>
</p>

<%
  Enrolment e = (Enrolment) request.getAttribute("enrolment");
  if (e == null) {
%>
  <p>Enrolment not found.</p>
</body>
</html>
<%
    return;
  }
%>

<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse;">
  <tr><td><b>Enrolment ID</b></td><td><%= e.getEnrolmentId() %></td></tr>
  <tr><td><b>Student ID</b></td><td><%= e.getStudentId() %></td></tr>
  <tr><td><b>Course Code</b></td><td><%= e.getCourseCode() %></td></tr>
  <tr><td><b>Semester</b></td><td><%= e.getSemester() %></td></tr>
  <tr><td><b>Academic Year</b></td><td><%= e.getAcademicYear() %></td></tr>
  <tr><td><b>Attempt Number</b></td><td><%= e.getAttemptNumber() %></td></tr>
  <tr><td><b>Status</b></td><td><%= e.getStatus() %></td></tr>
</table>

<br/>

<p>
  <a href="<%= request.getContextPath() %>/academic/enrolments/approve?id=<%= e.getEnrolmentId() %>">
    <button type="button" onclick="return confirm('Approve this enrolment?');">Approve</button>
  </a>

  <a href="<%= request.getContextPath() %>/academic/enrolments/reject?id=<%= e.getEnrolmentId() %>">
    <button type="button" onclick="return confirm('Reject this enrolment?');">Reject</button>
  </a>
  <p>
  <a href="<%= request.getContextPath() %>/academic/enrolments">
    <button type="button">Confirm Enrolment</button>
  </a>
</p>
  
</p>

</body>
</html>
