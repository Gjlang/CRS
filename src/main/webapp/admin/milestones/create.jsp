<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head><title>Create Milestone</title></head>
<body>

<%
  Object planObj = request.getAttribute("planId");
  String planId = (planObj == null) ? request.getParameter("plan_id") : String.valueOf(planObj);
  String error = (String) request.getAttribute("error");
%>

<h2>Create Milestone (Plan ID: <%= planId %>)</h2>

<p>
  <a href="<%= request.getContextPath() %>/admin/milestones?plan_id=<%= planId %>">← Back to Milestones</a>
</p>

<% if (error != null) { %>
  <p style="color:red;"><b><%= error %></b></p>
<% } %>

<form method="post" action="<%= request.getContextPath() %>/admin/milestones/create">
  <input type="hidden" name="planId" value="<%= planId %>" />

  <table cellpadding="6">
    <tr>
      <td>Week Range</td>
      <td><input type="text" name="weekRange" placeholder="Week 1-2" required /></td>
    </tr>
    <tr>
      <td>Task Description</td>
      <td><input type="text" name="taskDescription" maxlength="300" required style="width:380px;" /></td>
    </tr>
    <tr>
      <td>Due Date</td>
      <td><input type="date" name="dueDate" /></td>
    </tr>
    <tr>
      <td>Progress Status</td>
      <td>
        <select name="progressStatus">
          <option value="PENDING">PENDING</option>
          <option value="IN_PROGRESS">IN_PROGRESS</option>
          <option value="COMPLETE">COMPLETE</option>
        </select>
      </td>
    </tr>
  </table>

  <p><button type="submit">Save</button></p>
</form>

</body>
</html>
