<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>Add Result</title>
</head>
<body>

<h2>Add Student Result</h2>

<p><a href="<%= request.getContextPath() %>/admin/results">← Back to Results</a></p>

<%
  String error = (String) request.getAttribute("error");
  if (error != null) {
%>
  <p style="color:red;"><b><%= error %></b></p>
<%
  }
%>

<form method="post" action="<%= request.getContextPath() %>/admin/results/create">
  <table cellpadding="6">
    <tr>
      <td>Enrolment ID</td>
      <td><input type="number" name="enrolmentId" required /></td>
    </tr>
    <tr>
      <td>Assessment ID</td>
      <td><input type="number" name="assessmentId" required /></td>
    </tr>
    <tr>
      <td>Grade</td>
      <td><input type="text" name="grade" required /></td>
    </tr>
    <tr>
      <td>Grade Point</td>
      <td><input type="number" step="0.01" name="gradePoint" required /></td>
    </tr>
    <tr>
      <td>Passed</td>
      <td>
        <select name="passed" required>
          <option value="true">PASS</option>
          <option value="false">FAIL</option>
        </select>
      </td>
    </tr>
  </table>

  <p><button type="submit">Create</button></p>
</form>

</body>
</html>
