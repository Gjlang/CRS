<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.myapp.model.Milestone" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head><title>Milestones</title></head>
<body>

<%
  Integer planId = (Integer) request.getAttribute("planId");
  List<Milestone> milestones = (List<Milestone>) request.getAttribute("milestones");
%>

<h2>Milestones for Plan ID: <%= planId %></h2>

<p>
  <a href="<%= request.getContextPath() %>/admin/recovery-plans">← Back to Recovery Plans</a>
</p>

<p>
  <a href="<%= request.getContextPath() %>/admin/milestones/create?plan_id=<%= planId %>">
    <button type="button">+ Add Milestone</button>
  </a>
</p>

<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
  <tr>
    <th>ID</th>
    <th>Week Range</th>
    <th>Task</th>
    <th>Due Date</th>
    <th>Status</th>
    <th>Updated At</th>
    <th>Actions</th>
  </tr>

  <%
    if (milestones == null || milestones.isEmpty()) {
  %>
    <tr><td colspan="7" style="text-align:center;">No milestones yet.</td></tr>
  <%
    } else {
      for (Milestone m : milestones) {
  %>
    <tr>
      <td><%= m.getMilestoneId() %></td>
      <td><%= m.getWeekRange() %></td>
      <td><%= m.getTaskDescription() %></td>
      <td><%= m.getDueDate() %></td>
      <td><%= m.getProgressStatus() %></td>
      <td><%= m.getUpdatedAt() %></td>
      <td>
        <a href="<%= request.getContextPath() %>/admin/milestones/edit?id=<%= m.getMilestoneId() %>">Edit</a>
        |
        <a href="<%= request.getContextPath() %>/admin/milestones/delete?id=<%= m.getMilestoneId() %>"
           onclick="return confirm('Delete this milestone?');">Delete</a>
      </td>
    </tr>
  <%
      }
    }
  %>
</table>

</body>
</html>
