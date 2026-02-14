<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.myapp.model.User" %>

<%
  User headerUser = (User) session.getAttribute("user");
%>

<div style="padding:12px; border-bottom:1px solid #ccc; margin-bottom:16px;">
  <div style="display:flex; justify-content:space-between; align-items:center;">
    <div>
      <b>CRS</b>
      <span style="margin-left:10px; color:#555;">
        <%
          if (headerUser != null) {
        %>
          User: <b><%= headerUser.getName() %></b> | Role: <b><%= headerUser.getRole() %></b>
        <%
          } else {
        %>
          Not logged in
        <%
          }
        %>
      </span>
    </div>

    <div>
      <a href="<%= request.getContextPath() %>/dashboard">Dashboard</a>
      <span style="margin:0 8px;">|</span>
      <a href="<%= request.getContextPath() %>/logout">Logout</a>
    </div>
  </div>
</div>
