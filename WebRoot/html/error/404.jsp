<%@ page import="java.util.Random" %>
<%--
  Created by IntelliJ IDEA.
  User: Erhu
  Date: 11-8-3
  Time: 上午10:37
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html>
<head>
    <title>404 错误</title>
</head>
<body>
<%
    java.util.Random rand = new Random();
    int i = rand.nextInt(4);
%>
<table border="0">
    <tr>
        <td align="left" valign="middle"><img src="${contextPath}/images/404_<%=i%>.jpg" width="504" height="376"></td>
    </tr>
</table>
</body>
</html>
