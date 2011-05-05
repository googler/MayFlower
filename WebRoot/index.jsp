<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <title>Welcome to MayFlower!</title>
    <%@ include file="/html/inc/init.jsp" %>
</head>
<body>
<a name="top"></a>
<table width="100%" border="0" align="center" cellpadding="0"
       cellspacing="0">
    <tr>
        <td>
            <%@ include file="/html/inc/top.jsp" %>
        </td>
    </tr>
    <tr>
        <td height="300px" valign="top">
            <form action="${contextPath}/search/bcm" method="get">
                <table width="100%" border="0" align="center" cellpadding="2"
                       cellspacing="2">
                    <tr>
                        <td align="center" height="120" valign="bottom">
                            <img src="${contextPath}/images/mayFlower.png"
                                 alt="MayFlower:五月花!" title="MayFlower:五月花!">
                        </td>
                    </tr>
                    <tr>
                        <td height="50" align="center" valign="bottom">
                            <input id="q" name="q" type="search" style="height: 25px;" size="80"
                                   maxlength="200" placeholder='请输入关键字' autofocus>
                            <script>
                                if (!('autofocus' in document.createElement('input'))) {
                                    document.getElementById("q").focus();
                                }
                                if (!Modernizr.webworkers) {
                                    alert('你的浏览器本系统的部分功能，请在页面底部下载Google的Chrome浏览器！');
                                }
                            </script>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">
                            <input name="search" type="submit" value=" 手 气 不 错 "
                                   class="lsb"
                                   style="background-image: url(${contextPath}/images/index_btn_bg.png); height: 33px;">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
    <tr>
        <td align="center">
            <%@ include file="/html/inc/bottom.jsp" %>
        </td>
    </tr>
</table>
</body>
</html>
