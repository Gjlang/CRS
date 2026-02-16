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

<h2>Admin jaswaasasaaaawa</h2>
<p>Welcome, <b><%= user.getName() %></b> | Role: <%= user.getRole() %></p>

<hr>

<p>
  <a href="<%= request.getContextPath() %>/admin/students">
    <button type="button">Manage Students</button>
  </a>
</p>

<p>
  <a href="<%= request.getContextPath() %>/admin/students/create">
    <button type="button">+ Create Student</button>
  </a>
</p>

<p>
  <a href="<%= request.getContextPath() %>/admin/courses">
    <button type="button">Manage Courses</button>
  </a>
</p>

<a href="<%= request.getContextPath() %>/logout">
    <button>Logout</button>
</a>

<p>
  <a href="<%= request.getContextPath() %>/admin/results">
    <button type="button">Manage Results</button>
  </a>
</p>

<p>
  <a href="<%= request.getContextPath() %>/admin/recovery-plans">
    <button type="button">Recovery Plans</button>
  </a>
</p>


</body>
</html>
