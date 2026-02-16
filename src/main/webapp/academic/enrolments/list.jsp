<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.myapp.model.Enrolment" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>Enrolment Confirmation</title>
</head>
<body>

<h2>Pending Enrolments (Course Recovery)</h2>

<p>
  <a href="<%= request.getContextPath() %>/academic/dashboard.jsp">← Back to Dashboard</a>
</p>

<%
  List<Enrolment> pending = (List<Enrolment>) request.getAttribute("pendingEnrolments");
%>

<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
  <tr>
    <th>Enrolment ID</th>
    <th>Student ID</th>
    <th>Course Code</th>
    <th>Semester</th>
    <th>Academic Year</th>
    <th>Status</th>
    <th>Actions</th>
  </tr>

  <%
    if (pending == null || pending.isEmpty()) {
  %>
    <tr><td colspan="7" style="text-align:center;">No pending enrolments.</td></tr>
  <%
    } else {
      for (Enrolment e : pending) {
  %>
    <tr>
      <td><%= e.getEnrolmentId() %></td>
      <td><%= e.getStudentId() %></td>
      <td><%= e.getCourseCode() %></td>
      <td><%= e.getSemester() %></td>
      <td><%= e.getAcademicYear() %></td>
      <td><%= e.getStatus() %></td>
      <td>
        <a href="<%= request.getContextPath() %>/academic/enrolments/view?id=<%= e.getEnrolmentId() %>">View</a>
        |
        <a href="<%= request.getContextPath() %>/academic/enrolments/approve?id=<%= e.getEnrolmentId() %>"
           onclick="return confirm('Approve this enrolment?');">Approve</a>
        |
        <a href="<%= request.getContextPath() %>/academic/enrolments/reject?id=<%= e.getEnrolmentId() %>"
           onclick="return confirm('Reject this enrolment?');">Reject</a>
      </td>
    </tr>
  <%
      }
    }
  %>
</table>

</body>
</html>
