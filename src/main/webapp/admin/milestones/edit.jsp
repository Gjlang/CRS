<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.Milestone" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head><title>Edit Milestone</title></head>
<body>

<%
  Milestone m = (Milestone) request.getAttribute("milestone");
  if (m == null) { %>
    <p>Milestone not found.</p>
</body></html>
<% return; } %>

<h2>Edit Milestone (ID: <%= m.getMilestoneId() %>)</h2>

<p>
  <a href="<%= request.getContextPath() %>/admin/milestones?plan_id=<%= m.getPlanId() %>">← Back to Milestones</a>
</p>

<form method="post" action="<%= request.getContextPath() %>/admin/milestones/edit">
  <input type="hidden" name="milestoneId" value="<%= m.getMilestoneId() %>" />
  <input type="hidden" name="planId" value="<%= m.getPlanId() %>" />

  <table cellpadding="6">
    <tr>
      <td>Week Range</td>
      <td><input type="text" name="weekRange" value="<%= m.getWeekRange() %>" required /></td>
    </tr>
    <tr>
      <td>Task Description</td>
      <td><input type="text" name="taskDescription" maxlength="300" value="<%= m.getTaskDescription() %>" required style="width:380px;" /></td>
    </tr>
    <tr>
      <td>Due Date</td>
      <td><input type="date" name="dueDate" value="<%= (m.getDueDate()!=null ? m.getDueDate().toString() : "") %>" /></td>
    </tr>
    <tr>
      <td>Progress Status</td>
      <td>
        <select name="progressStatus">
          <option value="PENDING" <%= "PENDING".equalsIgnoreCase(m.getProgressStatus()) ? "selected" : "" %>>PENDING</option>
          <option value="IN_PROGRESS" <%= "IN_PROGRESS".equalsIgnoreCase(m.getProgressStatus()) ? "selected" : "" %>>IN_PROGRESS</option>
          <option value="COMPLETE" <%= "COMPLETE".equalsIgnoreCase(m.getProgressStatus()) ? "selected" : "" %>>COMPLETE</option>
        </select>
      </td>
    </tr>
  </table>

  <p><button type="submit">Save Changes</button></p>
</form>

</body>
</html>
