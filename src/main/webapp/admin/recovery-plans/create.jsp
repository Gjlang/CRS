<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head><title>Create Recovery Plan</title></head>
<body>

<h2>Create Recovery Plan</h2>
<p><a href="<%= request.getContextPath() %>/admin/recovery-plans">← Back to List</a></p>

<%
  String error = (String) request.getAttribute("error");
  if (error != null) {
%>
  <p style="color:red;"><b><%= error %></b></p>
<%
  }
%>

<form method="post" action="<%= request.getContextPath() %>/admin/recovery-plans/create">
  <table cellpadding="6">
    <tr>
      <td>Enrolment ID</td>
      <td><input type="number" name="enrolmentId" required /></td>
    </tr>
    <tr>
      <td>Created By (users.user_id)</td>
      <td><input type="number" name="createdBy" required /></td>
    </tr>
    <tr>
      <td>Status</td>
      <td>
        <select name="status">
          <option value="DRAFT">DRAFT</option>
          <option value="ACTIVE">ACTIVE</option>
          <option value="INACTIVE">INACTIVE</option>
        </select>
      </td>
    </tr>
    <tr>
      <td>Remarks</td>
      <td><textarea name="remarks" rows="4" cols="50"></textarea></td>
    </tr>
  </table>

  <p><button type="submit">Save</button></p>
</form>

<p style="color:#555;">
  Tip: enrolment_id must exist in <b>enrollments</b>, createdBy must exist in <b>users</b>.
</p>

</body>
</html>
