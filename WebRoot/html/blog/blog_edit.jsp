<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>博文编辑:${blog.title}</title>
    <script type="text/javascript" charset="utf-8" src="${contextPath}/kindEditor/kindeditor.js"></script>
    <script type="text/javascript">
        KE.show({
            id : 'content',
            imageUploadJson : '../../jsp/upload_json.jsp',
            fileManagerJson : '../../jsp/file_manager_json.jsp',
            allowFileManager : true,
            afterCreate : function(id) {
                KE.event.ctrl(document, 13, function() {
                    KE.util.setData(id);
                    document.forms['blogForm'].submit();
                });
                KE.event.ctrl(KE.g[id].iframeDoc, 13, function() {
                    KE.util.setData(id);
                    document.forms['blogForm'].submit();
                });
            }
        });
    </script>
</head>
<body>
<form name="blogForm" id="blogForm" action="${contextPath}/blog/save" method="post">
    <input type="hidden" name="id" value="${blog.id}">
    <table width="100%" border="0" cellspacing="0" cellpadding="2">
        <tr>
            <td><textarea id="content" name="content" cols="130" rows="8" style="height:450px;"
                          placeholder='这里面输入内容哦!'>${blog.content}</textarea></td>
        </tr>
        <tr>
            <td align="left" valign="middle">
                标题：
                <input style="height:30px; " name="title" type="text" size="50" maxlength="100" value="${blog.title}"
                       placeholder='这里写标题'/>*&nbsp;&nbsp;
                标签：
                <input style="height:30px; " name="tag" type="text" size="30" maxlength="60" value="${blog.tag}"
                       placeholder='这里写标签'/>*&nbsp;&nbsp;
                置顶：<c:choose><c:when test='${blog.top == 1}'><input name='top' type='checkbox' checked/></c:when>
                <c:otherwise>
                    <input name='top' type='checkbox'/>
                </c:otherwise>
            </c:choose>
                <input style="height:30px;" name="svBtn" type="button" onClick="doSave()"
                       value="     发                          布     "></td>
        </tr>
    </table>
</form>
<script>
    function doSave() {
        var fm = document.blogForm;
        if (fm.title.value == '') {
            alert("Hi, you can't submit a blog without title:)");
            return false;
        }
        if (fm.content.value == '') {
            alert("oh, you can't submit a blog without content:)");
            return false;
        }
        if (fm.top.checked == true)
            fm.top.value = '1';
        else
            fm.top.value = '0';
        fm.submit();
    }
</script>
</body>
</html>
