<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.RecoveryPlan" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head><title>View Recovery Plan</title></head>
<body>

<%
  RecoveryPlan plan = (RecoveryPlan) request.getAttribute("plan");
  if (plan == null) {
%>
  <p>Plan not found.</p>
</body>
</html>
<%
    return;
  }
%>

<h2>Recovery Plan Details</h2>
<p><a href="<%= request.getContextPath() %>/admin/recovery-plans">← Back to List</a></p>

<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse;">
  <tr><td><b>Plan ID</b></td><td><%= plan.getPlanId() %></td></tr>
  <tr><td><b>Enrolment ID</b></td><td><%= plan.getEnrolmentId() %></td></tr>
  <tr><td><b>Student ID</b></td><td><%= plan.getStudentId() %></td></tr>
  <tr><td><b>Course Code</b></td><td><%= plan.getCourseCode() %></td></tr>
  <tr><td><b>Status</b></td><td><%= plan.getStatus() %></td></tr>
  <tr><td><b>Created By</b></td><td><%= plan.getCreatedBy() %></td></tr>
  <tr><td><b>Created At</b></td><td><%= plan.getCreatedAt() %></td></tr>
  <tr><td><b>Remarks</b></td><td><%= plan.getRemarks() %></td></tr>
</table>

<br/>

<p>
  <a href="<%= request.getContextPath() %>/admin/recovery-plans/edit?id=<%= plan.getPlanId() %>">
    <button type="button">Edit</button>
  </a>

  <a href="<%= request.getContextPath() %>/admin/milestones?plan_id=<%= plan.getPlanId() %>">
    <button type="button">Manage Milestones</button>
  </a>
</p>

</body>
</html>
