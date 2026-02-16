<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.RecoveryPlan" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head><title>Edit Recovery Plan</title></head>
<body>

<h2>Edit Recovery Plan</h2>
<p><a href="<%= request.getContextPath() %>/admin/recovery-plans">← Back to List</a></p>

<%
  String error = (String) request.getAttribute("error");
  if (error != null) {
%>
  <p style="color:red;"><b><%= error %></b></p>
<%
  }

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

<form method="post" action="<%= request.getContextPath() %>/admin/recovery-plans/edit">
  <input type="hidden" name="planId" value="<%= plan.getPlanId() %>" />

  <table cellpadding="6">
    <tr>
      <td>Enrolment ID</td>
      <td><input type="number" name="enrolmentId" value="<%= plan.getEnrolmentId() %>" required /></td>
    </tr>
    <tr>
      <td>Status</td>
      <td>
        <select name="status">
          <option value="DRAFT" <%= "DRAFT".equalsIgnoreCase(plan.getStatus()) ? "selected" : "" %>>DRAFT</option>
          <option value="ACTIVE" <%= "ACTIVE".equalsIgnoreCase(plan.getStatus()) ? "selected" : "" %>>ACTIVE</option>
          <option value="INACTIVE" <%= "INACTIVE".equalsIgnoreCase(plan.getStatus()) ? "selected" : "" %>>INACTIVE</option>
        </select>
      </td>
    </tr>
    <tr>
      <td>Remarks</td>
      <td><textarea name="remarks" rows="4" cols="50"><%= plan.getRemarks() != null ? plan.getRemarks() : "" %></textarea></td>
    </tr>
  </table>

  <p><button type="submit">Save Changes</button></p>
</form>

</body>
</html>
