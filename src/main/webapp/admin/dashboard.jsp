<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.User" %>

<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"ADMIN".equals(user.getRole())) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }
%>

<!DOCTYPE html>
<html>
<head>
<title>Admin jawaaa</title>
</head>
<body>

<h2>Admin jaswawa</h2>
<p>Welcome, <b><%= user.getName() %></b> | Role: <%= user.getRole() %></p>

<hr>

<a href="<%= request.getContextPath() %>/logout">
    <button>Logout</button>
</a>

</body>
</html>
