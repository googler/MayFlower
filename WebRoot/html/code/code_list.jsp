<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
<title>代码分享</title>
</head>
<body>
<c:choose>
    <c:when test='${!(empty codes)}'>
         <table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
            <c:forEach items='${codes}' var='code'>
                <tr align="left" valign="middle">
                  <form name="${code.id}" id="${code.id}" action="${contextPath}/code/del" method="post">
                      <input type="hidden" name="id" value="${code.id}"/>
                      <td width="77%" valign="middle" nowrap class="border_bottom_green_dotted">
                          <img src="${contextPath}/images/code-context.png">&nbsp;
                          <a href="${contextPath}/code/read/${code.id}">${code.title}</a>
                </td> 
                      <td width="15%" align="right" nowrap class="border_bottom_green_dotted">
                          ${code.create_date}&nbsp;
                      </td>
                      <c:if test='${user.role == "admin"}'>
                          <td width="4%" align="center" nowrap class="border_bottom_green_dotted">
                              <a href="${contextPath}/code/edit/${code.id}">编辑</a>
                          </td> 
                          <td width="4%" align="center" nowrap class="border_bottom_green_dotted">
                              <a href="#" onClick="onDel(${code.id});">删除</a>
                          </td>
                      </c:if>
                  </form>
              </tr> 
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
		<div align="center"><font color='red' size="+6"><c:out value="no code!"/></font></div>
    </c:otherwise>
</c:choose>
</body>
</html>
