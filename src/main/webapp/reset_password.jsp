<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Reset Password</title></head>
<body>
<h2>Reset Password</h2>

<% if (request.getAttribute("error") != null) { %>
  <p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>

<form method="post" action="<%= request.getContextPath() %>/reset-password">
  <input type="hidden" name="token" value="<%= request.getAttribute("token") %>" />

  <label>New Password:</label>
  <input type="password" name="password" required />
  <br/>

  <label>Confirm Password:</label>
  <input type="password" name="confirm" required />
  <br/>

  <button type="submit">Reset</button>
</form>

</body>
</html>
