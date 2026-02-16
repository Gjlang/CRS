<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.User" %>

<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }
  if (!"ACADEMIC".equals(user.getRole())) {
    response.sendRedirect(request.getContextPath() + "/dashboard");
    return;
  }
%>

<!DOCTYPE html>
<html>
<head><title>Academic Officer Dashboard</title></head>
<body>
  <h2>Academic Officer Dashboard</h2>
  <p>Welcome, <b><%= user.getName() %></b> | Role: <%= user.getRole() %></p>

  <ul>
    <li><a href="<%= request.getContextPath() %>/eligibility">Check Eligibility</a></li>
    <li><a href="<%= request.getContextPath() %>/enrolments">Confirm Enrolment</a></li>
    <li><a href="<%= request.getContextPath() %>/reports">View Reports</a></li>
    <a href="<%= request.getContextPath() %>/logout">
    <button>Logout</button>
</a>

<p>
  <a href="<%= request.getContextPath() %>/academic/results">
    <button type="button">View Results</button>
  </a>
</p>


  </ul>
</body>
</html>
