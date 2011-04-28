<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>箴言编辑:${motto.id}</title>
</head>
<body>

<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
    <form name="mottoForm" id="mottoForm" action="${contextPath}/motto/save" method="post">
        <input type="hidden" name="id" value="${motto.id}">
        <tr>
            <td align="left" valign="middle"></td>
        </tr>
        <tr>
            <td><textarea id="content" name="content" cols="80" rows="4"
                          placeholder='内容'>${motto.content}</textarea>
            </td>
        </tr>
        <tr>
            <td><input style="height:30px; " name="tag" type="text" size="50" maxlength="60" value="${motto.tag}"
                       placeholder='标签'/>
                标签&nbsp;&nbsp;&nbsp;&nbsp;<input style="height:30px;" name="svBtn" type="button" onClick="doSave()"
                                                 value=" 发   布                                  "></td>
        </tr>
    </form>
</table>
<script>
    function doSave() {
        var fm = document.mottoForm;
        if (fm.content.value == '') {
            alert("Hi, 内容不可不填哦:)");
            return false;
        }
        if (fm.tag.value == '') {
            alert("OH, 标签是不可以不填的啊:)");
            return false;
        }
        fm.submit();
    }
</script>
</body>
</html>
