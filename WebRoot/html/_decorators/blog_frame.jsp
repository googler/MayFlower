<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<html>
<head>
    <title>MayFlower - <decorator:title default="Welcome to MayFlower!"/></title>
    <%@ include file="/html/inc/init.jsp" %>
    <decorator:head/>
</head>
<body><a name="top"></a>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <%@ include file="/html/inc/top.jsp" %>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="150">
                        <img src="${contextPath}/images/mayFlower_small.png" alt="MayFlower:五月花!" title="MayFlower:五月花!"
                             onClick="window.location='${contextPath}/blog'">
                    </td>
                    <td align="left" valign="middle">
                        <table>
                            <tr>
                                <td align="left">
                                    <form action="${contextPath}/search/b" method="get">
                                        <table>
                                            <tr>
                                                <td><input name="q" type="search" style="height:25px;"
                                                           placeholder='请输入要查询的博文、代码...' title="请输入要查询的博文、代码..."
                                                           size="70" maxlength="2048" value="${q}"/></td>
                                                <td><input type="submit" style="height:30px;" id='Sbtn'
                                                           value="找文章"></td>
                                            </tr>
                                        </table>
                                    </form>
                                </td>
                                <td align="left">
                                    
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td valign="top" class="border_right_green">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td width="26%" align="center" class="">&nbsp;</td>
                                <td width="74%" height="31" align="left" class=""><a href="${contextPath}/blog/toAdd">发布博文</a></td>
                            </tr>
                            <tr>
                                <td width="26%" align="center" class="border_bottom_green">&nbsp;</td>
                                <td width="74%" height="31" align="left" class="border_bottom_green"><a href="${contextPath}/code/toAdd">分享代码</a></td>
                            </tr>
                            <tr>
                                <td colspan="3" align="center">&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                    <td valign="top" style='padding-left:5px;'><decorator:body/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td align="center">
            <!--<%@ include file="/html/inc/bottom.jsp" %>-->
        </td>
    </tr>
</table>
<script language="javascript1.2">
    function onDel(t_id) {
        var flag = window.confirm("删除?")
        if (flag == false) {
            return;
        } else {
            var t_form = document.getElementById(t_id);
            t_form.submit();
        }
    }
</script>
</body>
</html>
