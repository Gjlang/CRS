<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.myapp.model.RecoveryPlan" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head><title>Recovery Plans</title></head>
<body>

<h2>Recovery Plans</h2>

<p>
  <a href="<%= request.getContextPath() %>/admin/recovery-plans/create">
    <button type="button">+ Create Recovery Plan</button>
  </a>
</p>

<%
  List<RecoveryPlan> plans = (List<RecoveryPlan>) request.getAttribute("plans");
%>

<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
  <tr>
    <th>Plan ID</th>
    <th>Enrolment ID</th>
    <th>Student ID</th>
    <th>Course Code</th>
    <th>Status</th>
    <th>Created At</th>
    <th>Actions</th>
  </tr>

  <%
    if (plans == null || plans.isEmpty()) {
  %>
    <tr><td colspan="7" style="text-align:center;">No recovery plans found.</td></tr>
  <%
    } else {
      for (RecoveryPlan p : plans) {
  %>
    <tr>
      <td><%= p.getPlanId() %></td>
      <td><%= p.getEnrolmentId() %></td>
      <td><%= p.getStudentId() %></td>
      <td><%= p.getCourseCode() %></td>
      <td><%= p.getStatus() %></td>
      <td><%= p.getCreatedAt() %></td>
      <td>
        <a href="<%= request.getContextPath() %>/admin/recovery-plans/view?id=<%= p.getPlanId() %>">View</a>
        |
        <a href="<%= request.getContextPath() %>/admin/recovery-plans/edit?id=<%= p.getPlanId() %>">Edit</a>
        |
        <a href="<%= request.getContextPath() %>/admin/milestones?plan_id=<%= p.getPlanId() %>">Manage Milestones</a>
      </td>
    </tr>
  <%
      }
    }
  %>
</table>

</body>
</html>
