<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <title>博文欣赏</title>
</head>
<body>
<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top"><c:choose>
            <c:when test='${!(empty blogs)}'>
                <table width="100%" border="0" align="center" cellpadding="1" cellspacing="0">
                    <%int i = -1;%>
                    <c:forEach items='${blogs}' var='blog'>
                        <%
                            i++;
                            if (i % 2 == 0) {
                        %>
                        <tr align="left" valign="middle">
                            <%}%>
                            <td valign="middle" class="border_bottom_green_dotted" width="50%" nowrap>
                                <c:if test='${blog.top == 1}'>
                                    <img src="${contextPath}/images/events.png">&nbsp;
                                </c:if>
                                <c:if test='${blog.top != 1}'>
                                    <img src="${contextPath}/images/union1.png">&nbsp;
                                </c:if>
                                <a href="${contextPath}/blog/read/${blog.id}">${blog.title}</a>
                            </td>
                            <%if (i % 2 == 1) {%>
                        </tr>
                        <%}%>
                    </c:forEach>
                    <tr>
                        <td colspan="3">
                            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="90%" nowrap>
                                        <img src="${contextPath}/images/label.png">
                                        <%
                                            List<String> list = (List<String>) request.getAttribute("hotTag");
                                            int total_page = Integer.parseInt(request.getAttribute("total_page").toString());
                                            int p_start = Integer.parseInt(request.getAttribute("p_start").toString());
                                            int p_end = Integer.parseInt(request.getAttribute("p_end").toString());
                                            for (String str : list) {
                                                String[] arr = str.split(":=:");
                                        %>
                                        <a href="${contextPath}/search/bc/?q=<%=arr[0]%>" title="<%=arr[1]%>">
                                            <%=arr[0]%>
                                        </a>&nbsp;
                                        <%}%>
                                    </td>
                                    <td width="10%" align="right" nowrap>
                                        <a href='${contextPath}/blog/?p=${curr_page-1}'>上一页</a>
                                        /
                                        <a href='${contextPath}/blog/?p=${curr_page+1}'>下一页</a>
                                        |
                                        <!--<a href='${contextPath}/blog/?p=<%=p_start-10%>'>上一屏</a>-->
                                        第<%
                                        for (int ii = p_start; ii <= p_end - 1; ii++) {%><a
                                            href="${contextPath}/blog/?p=<%=ii%>"><%=ii%>
                                    </a>,
                                        <%}%>
                                        <a href="${contextPath}/blog/?p=<%=p_end%>">${p_end}</a>页
                                        |
                                        <!--<a href='${contextPath}/blog/?p=<%=p_start+10%>'>下一屏</a>-->
                                        当前第${curr_page}页/共${total_page}页
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </c:when>
            <c:otherwise>
                <div align="center"><font color='red' size="+6">
                    <c:out value="No blog!"/>
                </font></div>
            </c:otherwise>
        </c:choose></td>
    </tr>
</table>

</body>
</html>
