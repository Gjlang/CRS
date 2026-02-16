<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.Student" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>Eligibility Check</title>
</head>
<body>

<h2>Eligibility Check (Academic Officer)</h2>

<%
  String error = (String) request.getAttribute("error");
  if (error != null) {
%>
  <p style="color:red;"><b><%= error %></b></p>
<%
  }

  String searchedStudentId = (String) request.getAttribute("searchedStudentId");
%>

<form method="post" action="<%= request.getContextPath() %>/academic/eligibility">
  Student ID:
  <input type="text" name="student_id" value="<%= searchedStudentId != null ? searchedStudentId : "" %>" required />
  <button type="submit">Check</button>
</form>

<hr/>

<%
  Student student = (Student) request.getAttribute("student");
  if (student != null) {
    Integer failedCount = (Integer) request.getAttribute("failedCount");
    String cgpaText = (String) request.getAttribute("cgpaText");
    Boolean eligible = (Boolean) request.getAttribute("eligible");
    String explanation = (String) request.getAttribute("explanation");
%>

<h3>Student Info</h3>
<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse;">
  <tr><td><b>Student ID</b></td><td><%= student.getStudentId() %></td></tr>
  <tr><td><b>Name</b></td><td><%= student.getName() %></td></tr>
  <tr><td><b>Program</b></td><td><%= student.getProgram() %></td></tr>
  <tr><td><b>Year</b></td><td><%= student.getYearOfStudy() %></td></tr>
  <tr><td><b>Email</b></td><td><%= student.getEmail() %></td></tr>
  <tr><td><b>Status</b></td><td><%= student.getStatus() %></td></tr>
</table>

<br/>

<h3>Computed Results</h3>
<p><b>Total Failed Courses:</b> <%= failedCount != null ? failedCount : 0 %></p>
<p><b>CGPA/GPA:</b> <%= cgpaText != null ? cgpaText : "N/A" %></p>

<h3>Eligibility Decision</h3>
<p style="font-size:18px;">
  <b style="color:<%= (eligible != null && eligible) ? "green" : "red" %>;">
    <%= (eligible != null && eligible) ? "ELIGIBLE" : "NOT ELIGIBLE" %>
  </b>
</p>

<h3>Explanation</h3>
<pre style="background:#f7f7f7; padding:12px; border:1px solid #ddd;"><%= explanation %></pre>

<%
  }
%>

</body>
</html>
