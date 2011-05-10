<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <title>我爱记单词</title>
</head>
<body>
<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top">
            <c:choose>
                <c:when test='${!(empty words)}'>
                    <table width="100%" border="0" align="center" cellpadding="1" cellspacing="0">
                        <c:forEach items='${words}' var='word'>
                            <form name="${word.id}" id="${word.id}" action="${contextPath}/word/del" method="post">
                                <input type="hidden" name="id" value="${word.id}">
                                <tr valign="middle">
                                    <td width="9%" align="left" valign="middle" nowrap
                                        class="border_bottom_green_dotted">
                                        <img src="${contextPath}/images/star.png">
                                        <span title="${word.spelling}">${word.spelling}</span></td>
                                    <!--<td width="14%" align="left" valign="middle" nowrap class="border_bottom_green_dotted">
                                    <span title="${word.phonetic_symbol}" >${word.phonetic_symbol}</span>
                                </td>-->
                                    <td width="78%" align="left" valign="middle" class="border_bottom_green_dotted">
                                        <span title="${word.meaning}">${word.meaning}</span>
                                    </td>
                                    <td width="10%" nowrap>
                                        <a href="${contextPath}/word/edit/${word.id}">编辑</a>
                                        |
                                        <a href="#" onClick="onDel(${word.id});">删除</a>
                                    </td>
                                </tr>
                            </form>
                        </c:forEach>
                        <tr>
                            <td colspan="5">&nbsp;</td>
                        </tr>
                    </table>
                </c:when>
                <c:otherwise>
                    <div align="center"><font color='red' size="+3">
                        <c:out value="一个单词都没有!"/>
                    </font></div>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>

</body>
</html>
