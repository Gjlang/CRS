<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>CRS Login</title>
</head>
<body>
    <h2>Login</h2>
    <%
  String err = (String) request.getAttribute("error");
  if (err != null) {
%>
  <p style="color:red;"><%= err %></p>
<%
  }
%>
    

    <form method="post" action="login">
        Email:<br/>
        <input type="text" name="email" /><br/><br/>

        Password:<br/>
        <input type="password" name="password" /><br/><br/>

        <button type="submit">Login</button>
    </form>
</body>
</html>
