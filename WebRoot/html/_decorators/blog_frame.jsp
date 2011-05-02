<%@ page import="com.paladin.action.MottoAction" %>
<%@ page import="com.paladin.bean.Motto" %>
<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<html>
<head>
    <title>MayFlower - <decorator:title default="Welcome to MayFlower!"/></title>
    <%@ include file="/html/inc/init.jsp" %>
    <%
        Motto r_motto = MottoAction.getRandomMotto();// 提取一条箴言
        request.setAttribute("r_motto", r_motto);
    %>
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
                    <td width="160">
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
                                                           value="找 文 章    "></td>
                                            </tr>
                                        </table>
                                    </form>
                                </td>
                                <td align="center" valign="middle">
                                    <c:if test='${!(empty user)}'>
                                        <a href="${contextPath}/blog/toAdd">发布博文</a>
                                        |
                                        <a href="${contextPath}/code/toAdd">分享代码</a>
                                        <c:if test='${user.role == "admin"}'>
                                            |
                                            <a href="${contextPath}/motto/toAdd">发布箴言</a>
                                        </c:if>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td valign="top" class="border_right_green">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td width="26%" align="center" >&nbsp;</td>
                                <td width="74%" height="31" align="left" >
                                    <a href="${contextPath}/blog"
                                       class=gb1>博文欣赏</a>
                                </td>
                            </tr>
                            <tr>
                                <td width="26%" align="center" class="border_bottom_green">&nbsp;</td>
                                <td width="74%" height="31" align="left" class="border_bottom_green"><a
                                        href="${contextPath}/code" class=gb1>代码集锦</a></td>
                            </tr>
                            <tr>
                                <td colspan="3" align="left" style="padding:5px 5px 0px 10px;color:#FF0;"
                                    title="'${r_motto.author}'">${r_motto.content}</td>
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
        var flag = window.confirm("确定删除?")
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
