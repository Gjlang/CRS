<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Forgot Password</title></head>
<body>
<h2>Forgot Password</h2>

<% if (request.getAttribute("message") != null) { %>
  <p style="color:green;"><%= request.getAttribute("message") %></p>
<% } %>

<form method="post" action="<%= request.getContextPath() %>/forgot-password">
  <label>Email:</label>
  <input type="email" name="email" required />
  <button type="submit">Send Reset Link</button>
</form>

<p><a href="<%= request.getContextPath() %>/login">Back to Login</a></p>
</body>
</html>
