<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.myapp.model.Course" %>

<jsp:include page="/components/header.jsp" />

<!DOCTYPE html>
<html>
<head>
  <title>Manage Courses</title>
</head>
<body>

<h2>Course Management</h2>

<p>
  <a href="<%= request.getContextPath() %>/admin/courses/create">
    <button type="button">+ Create Course</button>
  </a>
</p>

<%
  List<Course> courses = (List<Course>) request.getAttribute("courses");
%>

<table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse; width:100%;">
  <tr>
    <th>Course Code</th>
    <th>Title</th>
    <th>Credit Hours</th>
    <th>Actions</th>
  </tr>

  <%
    if (courses == null || courses.isEmpty()) {
  %>
    <tr><td colspan="4" style="text-align:center;">No courses found.</td></tr>
  <%
    } else {
      for (Course c : courses) {
  %>
    <tr>
      <td><%= c.getCourseCode() %></td>
      <td><%= c.getTitle() %></td>
      <td><%= c.getCreditHours() %></td>
      <td>
        <a href="<%= request.getContextPath() %>/admin/courses/edit?course_code=<%= c.getCourseCode() %>">Edit</a>
        |
        <a href="<%= request.getContextPath() %>/admin/assessments?course_code=<%= c.getCourseCode() %>">
          Manage Assessments
        </a>
      </td>
    </tr>
  <%
      }
    }
  %>
</table>

</body>
</html>
