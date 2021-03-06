﻿<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shCore.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushBash.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushCpp.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushCSharp.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushCss.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushDelphi.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushDiff.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushGroovy.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushJava.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushJScript.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushPhp.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushPlain.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushPython.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushRuby.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushScala.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushSql.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushVb.js"></script>
	<script type="text/javascript" src="${contextPath}/syntaxhighlighter/scripts/shBrushXml.js"></script>
	<link type="text/css" rel="stylesheet" href="${contextPath}/syntaxhighlighter/styles/shCore.css"/>
	<link type="text/css" rel="stylesheet" href="${contextPath}/syntaxhighlighter/styles/shThemeDefault.css"/>
	<script type="text/javascript">
		SyntaxHighlighter.config.clipboardSwf = '${contextPath}/syntaxhighlighter/scripts/clipboard.swf';
		SyntaxHighlighter.all();
	</script>
</head>
<body>
<table width="99%" align='center'>
    <tr> 
        <td align="center" style="font-size:22px;" title='${title}'><b>${code.title}</b></td>
    </tr>
    <tr> 
        <td align="right" valign="middle" class="border_bottom_green" style="height:27px;color:#CCC">
            <c:if test='${!empty tags}'>
                标签:
                <c:forEach items='${tags}' var='tag'>
                    <a href="${contextPath}/search/bcm/?q=${tag}" class="tag">${tag}</a>&nbsp;
                </c:forEach>
            </c:if>|
            访问:${code.hits}
            <c:if test='${user.role == "admin"}'>
                <a href="${contextPath}/code/edit/${code.id}">
                    [编 辑]
                </a>
            </c:if>
        </td> 
    </tr>
    <tr> 
        <td align="left"><pre class="brush: ${code.language};">${code.content}</pre></td> 
    </tr>
    <tr>
        <form action="${contextPath}/code/del" method='post' name='${code.id}' id='${code.id}'>
            <input type="hidden" name="id" value="${code.id}"/>
            <td align="right" class="border_bottom_green" style=" color:#999">
            <a href="#">${code.author}</a>&nbsp;发布于&nbsp;<time dateteime='${code.create_date}' pubdate>${code.create_date}</time>&nbsp;
            	<c:if test='${user.role == "admin"}'>
    	           	 |&nbsp;<a href="#" onClick="onDel(${code.id});">删除</a>
                </c:if>
                |
                <a href="#top">顶部</a>
            </td>
        </form>
    </tr>
</table> 
</body>
</html>
