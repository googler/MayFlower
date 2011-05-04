﻿
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <title>代码分享</title>
</head>
<body>
<c:choose>
    <c:when test='${!(empty codes)}'>
        <table width="99%" border="0" align="left" cellpadding="0" cellspacing="0">
            <%int i = 0;%>
            <c:forEach items='${codes}' var='code'>
                <%if (i++ % 2 == 0) {%>
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
            <tr>
                <td colspan="2" nowrap>
                    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>
                                <img src="${contextPath}/images/label.png">
                                <%
                                    List<String> list = (List<String>) request.getAttribute("hotTag");
                                    int total_page = Integer.parseInt(request.getAttribute("total_page").toString());
                                    int p_start = Integer.parseInt(request.getAttribute("p_start").toString());
                                    int p_end = Integer.parseInt(request.getAttribute("p_end").toString());
                                    for (String str : list) {
                                        String[] arr = str.split(":=:");
                                %>
                                <a href="${contextPath}/search/c/?q=<%=arr[0]%>" title="<%=arr[1]%>">
                                    <%=arr[0]%>
                                </a>&nbsp;
                                <%}%>
                            </td>
                            <td width="10%" align="right" nowrap>
                                <a href='${contextPath}/code/?p=${curr_page-1}'>上一页</a>
                                /
                                <a href='${contextPath}/code/?p=${curr_page+1}'>下一页</a>
                                |
                                <!--<a href='${contextPath}/code/?p=<%=p_start-10%>'>上一屏</a>-->
                                第<%
                                for (int ii = p_start; ii <= p_end - 1; ii++) {%><a
                                    href="${contextPath}/code/?p=<%=ii%>"><%=ii%>
                            </a>,
                                <%}%>
                                <a href="${contextPath}/code/?p=<%=p_end%>">${p_end}</a>页
                                |
                                <!--<a href='${contextPath}/code/?p=<%=p_start+10%>'>下一屏</a>-->
                                当前第${curr_page}页/共${total_page}页
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </c:when>
    <c:otherwise>
        <div align="center"><font color='red' size="+6"><c:out value="no code!"/></font></div>
    </c:otherwise>
</c:choose>
</body>
</html>
