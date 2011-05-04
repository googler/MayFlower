<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <title>箴言抱团</title>
</head>
<body>
<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top">
            <c:choose>
                <c:when test='${!(empty mottos)}'>
                    <table width="100%" border="0" align="center" cellpadding="1" cellspacing="0">
                        <c:forEach items='${mottos}' var='motto'>
                            <form name="${motto.id}" id="${motto.id}" action="${contextPath}/motto/del" method="post">
                            <input type="hidden" name="id" value="${motto.id}">
                            <tr valign="middle">
                                <td align="left" valign="middle" class="border_bottom_green_dotted" width="100%">
                                    <img src="${contextPath}/images/star.png">&nbsp;
                                    <span title="${motto.author}" >${motto.content}</span>
                                </td>
                                <td nowrap>
                                	<a href="${contextPath}/motto/edit/${motto.id}">编辑</a>
                                    |
                                    <a href="#" onClick="onDel(${motto.id});">删除</a>
                                </td>
                            </tr>
                            </form>
                        </c:forEach>
                        <tr>
                            <td colspan="2">
                                <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td width="90%" nowrap>
                                            <%
                                                //int total_page = Integer.parseInt(request.getAttribute("total_page").toString());
                                                int p_start = Integer.parseInt(request.getAttribute("p_start").toString());
                                                int p_end = Integer.parseInt(request.getAttribute("p_end").toString());
                                            %>
                                        </td>
                                        <td width="10%" align="right" nowrap>
                                            <a href='${contextPath}/motto/?p=${curr_page-1}'>上一页</a>
                                            /
                                            <a href='${contextPath}/motto/?p=${curr_page+1}'>下一页</a>
                                            |
                                            <!--<a href='${contextPath}/motto/?p=<%=p_start-10%>'>上一屏</a>-->
                                            第<%
                                            for (int ii = p_start; ii <= p_end - 1; ii++) {%><a
                                                href="${contextPath}/motto/?p=<%=ii%>"><%=ii%>
                                        </a>,
                                            <%}%>
                                            <a href="${contextPath}/motto/?p=<%=p_end%>">${p_end}</a>
                                            页
                                            |
                                            <!--<a href='${contextPath}/motto/?p=<%=p_start+10%>'>下一屏</a>-->
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
                        <c:out value="no motto!"/>
                    </font></div>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>

</body>
</html>
