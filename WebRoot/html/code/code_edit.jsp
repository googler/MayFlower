<!DOCTYPE html>
<html>
<head>
    <title>代码编辑:${code.title}</title>
</head>
<body>
<form name="codeFm" id="codeFm" action="${contextPath}/code/save" method="post">
    <input type="hidden" name="id" value="${code.id}">
    <table width="100%" border="0" cellspacing="0" cellpadding="2">
        <tr>
            <td valign="middle">标题：
                <input style="height:30px; " name="title" type="text" size="60" maxlength="100" value="${code.title}"
                       placeholder='这里写标题'/>*&nbsp;&nbsp;
                标签：
                <input style="height:30px; " name="tag" type="text" size="35" maxlength="60" value="${code.tag}"
                       placeholder='这里写标签'/>*&nbsp;&nbsp;
                语言：
                <select name="language" style="height:30px;width:120px;">
                    <option value="${code.language}">${code.language}</option>
                    <option value="java">java</option>
                    <option value="scala">scala</option>
                    <option value="xml">xml</option>
                    <option value="cpp">c++</option>
                    <option value="python">python</option>
                    <option value="js">javaScript</option>
                </select>*
            </td>
        </tr>
        <tr>
            <td><textarea cols="130" id="content" name="content" rows="16"
                          placeholder='把你得意的代码贴在此处吧'>${code.content}</textarea></td>
        </tr>
        <tr>
            <td valign="middle"><input style="height:30px;width:935px" name="svBtn" type="button" onClick="save()"
                                       value="保      存"></td>
        </tr>
    </table>
</form>
<script>
    function save() {
        var fm = document.codeFm;
        if (fm.title.value == '') {
            alert("Hi, 不要忘记填写标题啊！");
            return false;
        }
        if (fm.content.value == '') {
            alert("oh, 发表的代码怎么是空的啊？");
            return false;
        }
        if (fm.language.value == '') {
            alert("oh, 还是选一个编程语言吧！");
            return false;
        }
        fm.submit();
    }
</script>
</body>
</html>
