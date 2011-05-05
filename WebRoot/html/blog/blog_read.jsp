<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
</head>
<body>
<table width="99%" align="center" cellspacing="2">
    <tr>
        <td align="center" style="font-size:22px;" title='${title}'><b>${blog.title}</b></td>
    </tr>
    <tr>
        <td align="right" valign="middle" class="border_bottom_green" style="height:27px;color:#CCC">
            <c:if test='${!empty tags}'>
                标签:
                <c:forEach items='${tags}' var='tag'>
                    <a href="${contextPath}/search/bcm/?q=${tag}">${tag}</a>&nbsp;
                </c:forEach>
            </c:if>|
            访问:${blog.hits}

            <c:if test='${user.role == "admin"}'>
                <a href="${contextPath}/blog/edit/${blog.id}">
                    [编 辑]
                </a>
            </c:if>
        </td>
    </tr>
    <tr>
        <td align="left" valign="top">
            <div style="padding-left:10px;background-color:#FFF;color:black;">${blog.content}</div>
        </td>
    </tr>
    <tr>
        <form name='${blog.id}' id='${blog.id}' action='${contextPath}/blog/del' method='post'>
            <input type="hidden" name="id" value="${blog.id}"/>
            <td align="right" class="border_bottom_green" style=" color:#999">
                ${blog.author}&nbsp;发布于&nbsp;
                <time dateteime='${blog.create_date}' pubdate>${blog.create_date}</time>
                &nbsp;
                <c:if test='${user.role == "admin"}'>
                    |&nbsp;<a href="#" onClick="onDel(${blog.id});">删除</a>
                </c:if>
                |
                <a href="#top">顶部</a>
            </td>
        </form>
    </tr>
</table>
</body>
</html>
