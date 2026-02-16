<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.myapp.model.Notification" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head><title>Notification History</title></head>
<body>

<h2>Notification History</h2>

<p>
  <a href="<%= request.getContextPath() %>/admin/notifications/send">
    <button type="button">+ Send New Notification</button>
  </a>
</p>

<%
  List<Notification> list = (List<Notification>) request.getAttribute("notifications");
%>

<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
  <tr>
    <th>ID</th>
    <th>Student</th>
    <th>Plan</th>
    <th>Title</th>
    <th>Message</th>
    <th>Priority</th>
    <th>Status</th>
    <th>Created By</th>
    <th>Created At</th>
  </tr>

  <%
    if (list == null || list.isEmpty()) {
  %>
    <tr><td colspan="9" style="text-align:center;">No notifications found.</td></tr>
  <%
    } else {
      for (Notification n : list) {
  %>
    <tr>
      <td><%= n.getNotificationId() %></td>
      <td><%= n.getStudentId() %></td>
      <td><%= n.getPlanId() %></td>
      <td><%= n.getTitle() %></td>
      <td><%= n.getMessage() %></td>
      <td><%= n.getPriority() %></td>
      <td><%= n.getStatus() %></td>
      <td><%= n.getCreatedBy() %></td>
      <td><%= n.getCreatedAt() %></td>
    </tr>
  <%
      }
    }
  %>
</table>

</body>
</html>
