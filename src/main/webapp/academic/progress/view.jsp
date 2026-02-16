<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.myapp.model.Milestone" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head><title>Progress Tracking</title></head>
<body>

<h2>Progress Tracking</h2>

<%
  Integer planId = (Integer) request.getAttribute("planId");
  List<Milestone> milestones = (List<Milestone>) request.getAttribute("milestones");
%>

<form method="get" action="<%= request.getContextPath() %>/academic/progress">
  <label>Plan ID:</label>
  <input type="number" name="plan_id" value="<%= (planId != null ? planId : "") %>" required />
  <button type="submit">View</button>
</form>

<hr/>

<%
  if (planId == null) {
%>
  <p>Enter a Plan ID to view milestones.</p>
</body></html>
<%
    return;
  }
%>

<h3>Milestones for Plan ID: <%= planId %></h3>

<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
  <tr>
    <th>ID</th>
    <th>Week Range</th>
    <th>Task</th>
    <th>Due Date</th>
    <th>Status</th>
    <th>Updated At</th>
    <th>Update</th>
  </tr>

  <%
    if (milestones == null || milestones.isEmpty()) {
  %>
    <tr><td colspan="7" style="text-align:center;">No milestones found for this plan.</td></tr>
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
        <form method="post" action="<%= request.getContextPath() %>/academic/progress/update">
          <input type="hidden" name="planId" value="<%= planId %>" />
          <input type="hidden" name="milestoneId" value="<%= m.getMilestoneId() %>" />

          <select name="progressStatus">
            <option value="PENDING" <%= "PENDING".equalsIgnoreCase(m.getProgressStatus()) ? "selected" : "" %>>PENDING</option>
            <option value="IN_PROGRESS" <%= "IN_PROGRESS".equalsIgnoreCase(m.getProgressStatus()) ? "selected" : "" %>>IN_PROGRESS</option>
            <option value="COMPLETE" <%= "COMPLETE".equalsIgnoreCase(m.getProgressStatus()) ? "selected" : "" %>>COMPLETE</option>
          </select>

          <button type="submit">Save</button>
        </form>
      </td>
    </tr>
  <%
      }
    }
  %>
</table>

</body>
</html>
