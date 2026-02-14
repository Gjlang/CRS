<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.User" %>

<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }
%>

<html>
<body>
  <h2>Dashboard</h2>
  <p>Welcome, <b><%= user.getName() %></b> | Role: <%= user.getRole() %></p>

  <a href="<%= request.getContextPath() %>/dbtest">DB Test</a>
</body>
</html>
