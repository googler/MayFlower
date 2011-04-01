<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<title>博文欣赏</title>
</head>
<body>
<c:choose>
    <c:when test='${!(empty blogs)}'>
        <table width="100%" border="0" align="left" cellpadding="0" cellspacing="0"> 
            <c:forEach items='${blogs}' var='blog'>
                <tr align="left" valign="middle">
                  <form name="${blog.id}" id="${blog.id}" action="${contextPath}/blog/del" method="post">
                      <input type="hidden" name="id" value="${blog.id}"/>
                      <td width="77%" valign="middle" nowrap class="border_bottom_green_dotted">
                          <img src="${contextPath}/images/dev-java.png" width="16">&nbsp;
                          <a href="${contextPath}/blog/read/${blog.id}">${blog.title}</a>
                      </td> 
                      <td width="15%" align="right" nowrap class="border_bottom_green_dotted">
                          ${blog.create_date}&nbsp;
                      </td> 
                      <c:if test='${user.role == "admin"}'>
                          <td width="4%" align="center" nowrap class="border_bottom_green_dotted">
                              <a href="${contextPath}/blog/edit/${blog.id}">编辑</a>
                          </td> 
                          <td width="4%" align="center" nowrap class="border_bottom_green_dotted">
                              <a href="#" onClick="onDel(${blog.id});">删除</a>
                          </td>
                      </c:if>
                  </form>
              </tr> 
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
		<div align="center"><font color='red' size="+6"><c:out value="no blog!"/></font></div>
    </c:otherwise>
</c:choose>
</body>
</html>
