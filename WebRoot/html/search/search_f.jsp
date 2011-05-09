<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
<title>搜索服务器文件 - ${q}</title>
</head>
<body>
<c:choose>
    <c:when test='${!(empty file_list)}'>    	
        <table cellspacing="1" width="100%">
        	<tr>
        	  <td colspan="3"><font color='#FFFF00' size="+1">我们搜到${total_count_file}条记录</font></td></tr>
            <c:forEach items='${file_list}' var='file'>
                <tr align="left" valign="middle">
                  <form name="${file.id}" id="${file.id}" action="${contextPath}/file/del" method="post">
                      <input type="hidden" name="id" value="${file.id}"/>
                      <td valign="left" class="border_bottom_green_dotted">
                          <img src="${contextPath}/images/default_activity.png" width="16">&nbsp;
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
            <tr><td colspan="3">
                <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
                        <tr>
                            <%
                                //int total_page = Integer.parseInt(request.getAttribute("total_page").toString());
                                int p_start = Integer.parseInt(request.getAttribute("p_start_file").toString());
                                int p_end = Integer.parseInt(request.getAttribute("p_end_file").toString());
                            %>
                            <td align=left width="50%" nowrap>
                                [<a href='${contextPath}/searchf/?q=${q}&p=1'>第一屏</a>
                                /
                                <a href='${contextPath}/searchf/?q=${q}&p=${total_page_file}'>最后一屏</a>]

                                [<a href='${contextPath}/searchf/?q=${q}&p=<%=p_start-10%>'>上一屏</a>
                                /
                                <a href='${contextPath}/searchf/?q=${q}&p=<%=p_start+10%>'>下一屏</a>]
                            </td>
                            <td width="100%" align="right" nowrap>
                                <a href='${contextPath}/searchf/?q=${q}&p=${curr_page_file-1}'>上一页</a>
                                /
                                <a href='${contextPath}/searchf/?q=${q}&p=${curr_page_file+1}'>下一页</a>
                                |
                                第<%
                                for (int ii = p_start; ii <= p_end - 1; ii++) {%><a
                                    href="${contextPath}/searchf/?q=${q}&p=<%=ii%>"><%=ii%>
                            </a>&nbsp;
                                <%}%>
                                <a href="${contextPath}/searchf/?q=${q}&p=<%=p_end%>">${p_end_file}</a>页
                                |
                                当前第${curr_page_file}页
                                /
                                共${total_page_file}页
                            </td>
                        </tr>
                    </table>

            </td></tr>
         </table>
    </c:when>
    <c:otherwise>
		<div align="left"><font color='#ea0' size="+3"><c:out value="服务器上什么都没有"/></font></div>
    </c:otherwise>
</c:choose>
</body>
</html>
