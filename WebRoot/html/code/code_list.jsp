<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<title>代码分享</title>
</head>
<body>
<c:choose>
    <c:when test='${!(empty codes)}'>
         <table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
            <%int i = 0;%>
            <c:forEach items='${codes}' var='code'>
				<%if(i++ % 2 == 0) {%>
                    <tr align="left" valign="middle">
                      <td valign="middle" nowrap class="border_bottom_green_dotted" width="50%">
                        <img src="${contextPath}/images/ejbEntity.png">&nbsp;
                        <a href="${contextPath}/code/read/${code.id}">${code.title}</a>
                      </td>
                    <%} else {%>         
                      <td width="50%" valign="middle" nowrap class="border_bottom_green_dotted">
                        <img src="${contextPath}/images/ejbEntity.png">&nbsp;
                        <a href="${contextPath}/code/read/${code.id}">${code.title}</a>
                      </td>
                    </tr>
               <%}%>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
		<div align="center"><font color='red' size="+6"><c:out value="no code!"/></font></div>
    </c:otherwise>
</c:choose>
</body>
</html>
