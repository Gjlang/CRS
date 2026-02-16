<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.myapp.model.Notification" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head><title>Notifications</title></head>
<body>

<h2>Notifications</h2>

<form method="get" action="<%= request.getContextPath() %>/academic/notifications">
  <label>Student ID:</label>
  <input type="text" name="student_id" />
  <span style="margin:0 10px;">OR</span>
  <label>Plan ID:</label>
  <input type="number" name="plan_id" />
  <button type="submit">Search</button>
</form>

<hr/>

<%
  List<Notification> list = (List<Notification>) request.getAttribute("notifications");
%>

<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
  <tr>
    <th>Title</th>
    <th>Message</th>
    <th>Priority</th>
    <th>Status</th>
    <th>Created At</th>
  </tr>

  <%
    if (list == null) {
  %>
    <tr><td colspan="5" style="text-align:center;">Search by Student ID or Plan ID.</td></tr>
  <%
    } else if (list.isEmpty()) {
  %>
    <tr><td colspan="5" style="text-align:center;">No notifications found.</td></tr>
  <%
    } else {
      for (Notification n : list) {
  %>
    <tr>
      <td><%= n.getTitle() %></td>
      <td><%= n.getMessage() %></td>
      <td><%= n.getPriority() %></td>
      <td><%= n.getStatus() %></td>
      <td><%= n.getCreatedAt() %></td>
    </tr>
  <%
      }
    }
  %>
</table>

</body>
</html>
