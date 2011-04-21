<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>搜索 - ${q}</title>
</head>
<body>
<c:choose>
    <c:when test='${!(empty blog_list)}'>
        <table width="100%">
            <c:forEach items='${blog_list}' var='blog'>
                <tr align="left" valign="middle">
                    <form name="${blog.id}" id="${blog.id}" action="${contextPath}/blog/del" method="post">
                        <input type="hidden" name="id" value="${blog.id}"/>
                        <td width="68%" valign="middle" style="color:#FFF; font-size:16px;" nowrap>
                            <img src="${contextPath}/images/default_activity.png" width="16">&nbsp;
                            <a href="${contextPath}/blog/read/${blog.id}/?q=${q}"><b>${blog.title}</b></a>
                        </td>
                        <td width="9%" align="right" nowrap class="">
                                ${blog.tag}&nbsp;
                        </td>
                        <td width="15%" align="right" nowrap>
                                ${blog.create_date}&nbsp;
                        </td>
                        <c:if test='${user.role == "admin"}'>
                            <td width="4%" align="center" nowrap class="">
                                <a href="${contextPath}/blog/edit/${blog.id}">编辑</a>
                            </td>
                            <td width="4%" align="center" nowrap class="">
                                <a href="#" onClick="onDel(${blog.id});">删除</a>
                            </td>
                        </c:if>
                    </form>
                </tr>
                <tr align="left" valign="middle">
                    <td colspan="5" class="border_bottom_green_dotted" style="color:#CCC;">${blog.content}</td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
        <div align="center"><font color='#FFFF00' size="+4"><c:out value="我们没有搜索到任何博文和代码:("/></font></div>
    </c:otherwise>
</c:choose>
</body>
</html>