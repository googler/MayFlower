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
    	<td align="right" valign="middle" class="border_bottom_green" style="color:#CCC">
            <c:if test='${!empty tags}'><img src="${contextPath}/images/label.png"/>
            <c:forEach items='${tags}' var='tag'> 
     			<a href="${contextPath}/search/b/?q=${tag}">${tag}</a>&nbsp;
     		</c:forEach>
            </c:if>
            <c:if test='${user.role == "admin"}'>
            	&nbsp;<a href="${contextPath}/blog/edit/${blog.id}"><img title='编辑' src='${contextPath}/images/emblem-development.png'/></a>&nbsp;
            </c:if>
            V_${blog.hits}&nbsp;
    	</td> 
	</tr>
	<tr>
    	<td align="left" valign="top"><div style="padding-left:10px;background-color:#FFF;color:black;" >${blog.content}</div></td>
	</tr>
    <tr>
        <form name='${blog.id}' id='${blog.id}' action='${contextPath}/blog/del' method='post'>
            <input type="hidden" name="id" value="${blog.id}"/>
            <td align="right" class="border_bottom_green" style=" color:#999">
            	<a href="#">${blog.author}</a>&nbsp;发布于&nbsp;<time dateteime='${blog.create_date}' pubdate>${blog.create_date}</time>&nbsp;
            	<c:if test='${user.role == "admin"}'>
    	           	 |&nbsp;<a href="#" onClick="onDel(${blog.id});">删除</a>
                </c:if>
                |&nbsp;<a href="#top">Top</a>&nbsp;
            </td>
		</form>
	</tr>
</table>
</body>
</html>
