<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.myapp.model.Student" %>
<%@ page import="com.myapp.model.RecoveryPlan" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head><title>Send Notification</title></head>
<body>

<h2>Send Notification</h2>

<%
  String error = (String) request.getAttribute("error");
  List<Student> students = (List<Student>) request.getAttribute("students");
  List<RecoveryPlan> plans = (List<RecoveryPlan>) request.getAttribute("plans");
%>

<% if (error != null) { %>
  <p style="color:red;"><b><%= error %></b></p>
<% } %>

<form method="post" action="<%= request.getContextPath() %>/admin/notifications/send">
  <table cellpadding="6">
    <tr>
      <td>Student (optional)</td>
      <td>
        <select name="studentId">
          <option value="">-- choose student --</option>
          <%
            if (students != null) {
              for (Student s : students) {
          %>
            <option value="<%= s.getStudentId() %>"><%= s.getStudentId() %> - <%= s.getName() %></option>
          <%
              }
            }
          %>
        </select>
      </td>
    </tr>

    <tr>
      <td>Recovery Plan (optional)</td>
      <td>
        <select name="planId">
          <option value="">-- choose plan --</option>
          <%
            if (plans != null) {
              for (RecoveryPlan p : plans) {
          %>
            <option value="<%= p.getPlanId() %>">
              Plan <%= p.getPlanId() %> (Student: <%= p.getStudentId() %>, Course: <%= p.getCourseCode() %>)
            </option>
          <%
              }
            }
          %>
        </select>
      </td>
    </tr>

    <tr>
      <td>Title</td>
      <td><input type="text" name="title" required style="width:380px;" /></td>
    </tr>

    <tr>
      <td>Message</td>
      <td><textarea name="message" rows="5" cols="60" required></textarea></td>
    </tr>

    <tr>
      <td>Priority</td>
      <td>
        <select name="priority">
          <option value="NORMAL">NORMAL</option>
          <option value="HIGH">HIGH</option>
        </select>
      </td>
    </tr>

    <tr>
      <td>Status</td>
      <td>
        <select name="status">
          <option value="SENT">SENT</option>
          <option value="NEW">NEW</option>
        </select>
      </td>
    </tr>
  </table>

  <p>
    <button type="submit">Send</button>
    <a href="<%= request.getContextPath() %>/admin/notifications/history">View History</a>
  </p>
</form>

</body>
</html>
