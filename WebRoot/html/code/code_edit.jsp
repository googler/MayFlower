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
            	<input style="height:30px; " name="title" type="text" size="50" maxlength="100" value="${code.title}" placeholder='这里写标题哦'/>*&nbsp;&nbsp;
                标签：
                <input style="height:30px; " name="tag" type="text" size="30" maxlength="60" value="${code.tag}"  placeholder='这里写标签哈'/>*&nbsp;&nbsp;
                语言：
                <select name="language" style="height:25px; background-color:#CCC; width:120px;">
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
        	<td><textarea cols="150" id="content" name="content" rows="16" placeholder='把你得意的代码贴在此处吧'>${code.content}</textarea><br>请粘贴你的代码</td> 
        </tr>
        <tr> 
	        <td valign="middle"><input style="height:30px;" name="svBtn" type="button" onClick="save()" value="保 存"></td> 
        </tr>
    </table> 
</form>
<script>
	function save() {
		var fm = document.codeFm;
		if(fm.title.value == '') {
			alert("Hi, you can't submit a blog without title:)");
			return false;
		}
		if(fm.content.value == '') {
			alert("oh, you can't submit a blog without content:)");
			return false;
		}
		if(fm.language.value == '') {
			alert("oh, you can't submit a blog without language:)");
			return false;
		}
		fm.submit();
	}
</script>
</body>
</html>
