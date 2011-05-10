<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>我爱编辑单词 ${word.spelling}</title>
</head>
<body>
<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
    <form name="wordForm" id="wordForm" action="${contextPath}/word/save" method="post">
        <input type="hidden" name="id" value="${word.id}">
        <tr>
            <td align="left" valign="middle"></td>
        </tr>
        <tr>
            <td>拼写：<input type="text" id="spelling" name="spelling" cols="80" placeholder='拼写' value='${word.spelling}'
                          style="height:30px;"/>&nbsp;&nbsp;<!--音标：<input type="text" id="phonetic_symbol"
                                                                      name="phonetic_symbol" cols="80" placeholder='音标'
                                                                      value='${word.phonetic_symbol}'
                                                                      style="height:30px;"/>&nbsp;&nbsp;-->
                <c:choose>
                    <c:when test='${word.remembed}'><input name='remembed' type='checkbox' checked/></c:when>
                    <c:otherwise>
                        <input name='remembed' type='checkbox'/>
                    </c:otherwise>
                </c:choose>记住了？
            <input style="height:30px; width:335px;" name="svBtn" type="button" onClick="doSave()"
                       value=" 发 布 "></td>
        </tr>
        <tr>
            <td>
            </td>
        </tr>
        <tr>
            <td valign="top">释义：<textarea id="meaning" name="meaning" cols="80" rows="6" placeholder='释义'
                    >${word.meaning}</textarea>
            </td>
        </tr>
        <tr>
            <td>

            </td>
        </tr>
        <tr>

            <td>&nbsp;</td>
        </tr>
    </form>
</table>
<script>
    function doSave() {
        var fm = document.wordForm;
        if (fm.spelling.value == '') {
            alert("Hi, 单词是由字母组成的哦:)");
            return false;
        }
        if (fm.meaning.value == '') {
            alert("Hello, 单词都是有意义的哦:)");
            return false;
        }
        fm.submit();
    }
</script>
</body>
</html>