<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.StudentResult" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>Edit Result</title>
</head>
<body>

<h2>Edit Student Result</h2>

<p><a href="<%= request.getContextPath() %>/admin/results">← Back to Results</a></p>

<%
  String error = (String) request.getAttribute("error");
  StudentResult r = (StudentResult) request.getAttribute("result");

  if (error != null) {
%>
  <p style="color:red;"><b><%= error %></b></p>
<%
  }

  if (r == null) {
%>
  <p>Result not found.</p>
</body>
</html>
<%
    return;
  }
%>

<form method="post" action="<%= request.getContextPath() %>/admin/results/edit">
  <input type="hidden" name="resultId" value="<%= r.getResultId() %>" />

  <table cellpadding="6">
    <tr>
      <td>Result ID</td>
      <td><input type="text" value="<%= r.getResultId() %>" readonly /></td>
    </tr>
    <tr>
      <td>Enrolment ID</td>
      <td><input type="text" value="<%= r.getEnrolmentId() %>" readonly /></td>
    </tr>
    <tr>
      <td>Assessment ID</td>
      <td><input type="text" value="<%= r.getAssessmentId() %>" readonly /></td>
    </tr>
    <tr>
      <td>Grade</td>
      <td><input type="text" name="grade" value="<%= r.getGrade() %>" required /></td>
    </tr>
    <tr>
      <td>Grade Point</td>
      <td><input type="number" step="0.01" name="gradePoint" value="<%= r.getGradePoint() %>" required /></td>
    </tr>
    <tr>
      <td>Passed</td>
      <td>
        <select name="passed" required>
          <option value="true" <%= r.isPassed() ? "selected" : "" %>>PASS</option>
          <option value="false" <%= !r.isPassed() ? "selected" : "" %>>FAIL</option>
        </select>
      </td>
    </tr>
  </table>

  <p><button type="submit">Save Changes</button></p>
</form>

</body>
</html>
