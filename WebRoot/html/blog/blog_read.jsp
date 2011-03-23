<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
</head>
<body> 
<table width="100%" cellspacing="2">
	<tr>
    	<td align="center" style="font-size:22px; height:34px;"><b>${blog.title}</b></td> 
	</tr>
	<tr> 
    	<td align="right" valign="middle" class="border_bottom_green" style="color:#CCC" height="20">
    		※&nbsp;<a href="#">${blog.author}</a>&nbsp;发布于&nbsp;<time dateteime='${blog.create_date}' pubdate>${blog.create_date}</time>&nbsp;※&nbsp;
     		<c:forEach items='${tags}' var='tag'> 
     			<a href="#">${tag}</a>,
     		</c:forEach>&nbsp;※
            <c:if test='${user.role == "admin"}'>
            	<a href="${contextPath}/blog/edit/${blog.id}">编辑</a>&nbsp;※
            </c:if>
			[${blog.hits}]
    	</td> 
	</tr>
	<tr>
    	<td align="left" valign="top"><div style="padding-left:10px;background-color:#FFF;color:black;">${blog.content}</div></td> 
	</tr>
    <tr>
        <form name='${blog.id}' id='${blog.id}' action='${contextPath}/blog/del' method='post'>
            <input type="hidden" name="id" value="${blog.id}"/>
            <td align="right" class="border_bottom_green" style=" color:#999">
            	<c:if test='${user.role == "admin"}'>
    	            ※&nbsp;<a href="#" onClick="onDel(${blog.id});">删除</a>&nbsp;
                </c:if>
                ※&nbsp;<a href="#top">顶部</a>&nbsp;※
            </td>
		</form>
	</tr>
</table>
</body>
</html>
