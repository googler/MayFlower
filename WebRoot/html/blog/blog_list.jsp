<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<title>博文欣赏</title>
</head>
<body>
<c:choose>
  <c:when test='${!(empty blogs)}'>
    <table width="100%" border="0" align="center" cellpadding="1" cellspacing="0">
      <%int i = 0;%>
      <c:forEach items='${blogs}' var='blog'>
      	<%if(i++ % 2 == 0) {%>
        <tr align="left" valign="middle">
          <td valign="middle" nowrap class="border_bottom_green_dotted" width="50%">
            <c:if test='${blog.top == 1}'>
            	<img src="${contextPath}/images/events.png">&nbsp;
            </c:if>
            <c:if test='${blog.top != 1}'>
            	<img src="${contextPath}/images/union1.png">&nbsp;
            </c:if>
            <a href="${contextPath}/blog/read/${blog.id}">${blog.title}</a>
          </td>
        <%} else {%>
          <td width="50%" valign="middle" nowrap class="border_bottom_green_dotted">
            <c:if test='${blog.top == 1}'>
            	<img src="${contextPath}/images/events.png">&nbsp;
            </c:if>
            <c:if test='${blog.top != 1}'>
            	<img src="${contextPath}/images/union1.png">&nbsp;
            </c:if>
            <a href="${contextPath}/blog/read/${blog.id}">${blog.title}</a>
          </td>
        </tr>
		<%}%>
      </c:forEach>
      <tr>
        <%
            int total_page = Integer.parseInt(request.getAttribute("total_page").toString());
            int p_start = Integer.parseInt(request.getAttribute("p_start").toString());
            int p_end = Integer.parseInt(request.getAttribute("p_end").toString());
         %>
        <td width='10%' align="left" style="color:lightgray;">
            [<a href='${contextPath}/blog/?p=<%=p_start-10%>'>上一屏</a>
            第<%
                for(int ii = p_start; ii <= p_end - 1; ii ++) {%><a href="${contextPath}/blog/?p=<%=ii%>"><%=ii%></a>,&nbsp;
                <%}%>
            <a href="${contextPath}/blog/?p=<%=p_end%>">${p_end}</a>页&nbsp;
            <a href='${contextPath}/blog/?p=<%=p_start+10%>'>下一屏</a>]
        </td>
        <td align="left" style="color:lightgray;">
            [第${curr_page}页/共${total_page}页]
        </td>
      </tr>
    </table>
  </c:when>
  <c:otherwise>
    <div align="center"><font color='red' size="+6">
      <c:out value="no blog!"/>
      </font></div>
  </c:otherwise>
</c:choose>
</body>
</html>
