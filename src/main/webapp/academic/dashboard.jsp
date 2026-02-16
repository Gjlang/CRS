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

  <p>
    <a href="<%= request.getContextPath() %>/academic/eligibility">
      <button type="button">Eligibility Check</button>
    </a>
  </p>

  <p>
    <a href="<%= request.getContextPath() %>/academic/results">
      <button type="button">View Results</button>
    </a>
  </p>
<p>
  <a href="<%= request.getContextPath() %>/academic/enrolments">
    <button type="button">Confirm Enrolment</button>
  </a>
</p>
<p>
  <a href="<%= request.getContextPath() %>/academic/progress">
    <button type="button">Progress Tracking</button>
  </a>
</p>
<p>
  <a href="<%= request.getContextPath() %>/academic/notifications">
    <button type="button">View Notifications</button>
  </a>
</p>


  <p>
    <a href="<%= request.getContextPath() %>/logout">
      <button type="button">Logout</button>
    </a>
  </p>

</body>
</html>
