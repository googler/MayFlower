<!DOCTYPE html>
<%@ page import="com.paladin.action.WordAction" %>
<%@ page import="com.paladin.bean.Word" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<html>
<head>
    <title>五月花 - <decorator:title default="Welcome to MayFlower!"/></title>
    <%@ include file="/html/inc/init.jsp" %>
    <%
        Word r_word = WordAction.getRandomWord();// 提取一条箴言
        request.setAttribute("r_word", r_word);
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
                    <td width="100%" align="left" valign="middle">
                        <table>
                            <tr>
                                <td align="left">
                                    <form action="${contextPath}/word/s" method="get">
                                        <table>
                                            <tr>
                                                <td><input name="q" type="search" style="height:25px;"
                                                           placeholder='你要找什么词?' title="把单词写在这里吧"
                                                           size="70" maxlength="2048" value="${q}" autofocus/></td>
                                                <td><input type="submit" style="height:30px;" id='Sbtn'
                                                           value="我爱找单词" title="Life is good(-:"></td>
                                            </tr>
                                        </table>
                                    </form>
                                </td>
                                <td align="center" valign="middle">
                                    <c:if test='${!(empty user)}'>
                                        <a href="${contextPath}/word/toAdd">加新词</a>
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
                                    <a href="${contextPath}/word"
                                       class=gb1>所有单词</a>
                                </td>
                            </tr>
                            <tr>
                                <td width="26%" align="center" class="border_bottom_green">&nbsp;</td>
                                <td width="74%" height="31" align="left" class="border_bottom_green"><a
                                        href="${contextPath}/word" class=gb1>高频词</a></td>
                            </tr>
                            <tr>
                                <td colspan="2" align="left" style="padding:5px 5px 0px 10px;color:#FF0;"
                                    title="${r_world.spelling}">${r_motto.spelling}:<br>${r_motto.meaning}</td>
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
