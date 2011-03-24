<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
<title>搜索服务器文件 - ${q}</title>
</head>
<body>
<c:choose>
    <c:when test='${!(empty file_list)}'>    	
        <table cellspacing="1"> 
        	<tr>
        	  <td colspan="3"><font color='#FFFF00' size="+1">我们搜到以下文件</font></td></tr>
            <c:forEach items='${file_list}' var='file'>
                <tr align="left" valign="middle">
                  <form name="${file.id}" id="${file.id}" action="${contextPath}/file/del" method="post">
                      <input type="hidden" name="id" value="${file.id}"/>
                      <td valign="left" class="border_bottom_green_dotted">
                          <img src="${contextPath}/images/dev-java.png" width="16">&nbsp;
                          ${file.fileName}
                      </td> 
                      <td align="left" class="border_bottom_green_dotted">
                          ${file.filePath}
                      </td> 
                      <c:if test='${user.role == "admin"}'>
                          <td align="center" nowrap class="border_bottom_green_dotted">
                            <a href="#">删除</a>
                          </td> 
                      </c:if>
                  </form>
              </tr>
            </c:forEach>
         </table>
    </c:when>
    <c:otherwise>
		<div align="center"><font color='#FFFF00' size="+4"><c:out value="可怜的孩子！服务器没有你要的文件哦！"/></font></div>
    </c:otherwise>
</c:choose>
</body>
</html>
