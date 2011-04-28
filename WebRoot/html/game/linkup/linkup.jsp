<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Game - 连连看</title>
    <%@ include file="/html/inc/init.jsp" %>
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
                                    <form action="#" method="get">
                                        <table>
                                            <tr>
                                                <td><input name="q" type="search" style="height:25px;"
                                                           placeholder='请输入要找的游戏...' title="请输入要找的游戏..." size="70"
                                                           maxlength="2048" value="${q}"/></td>
                                                <td><input type="submit" style="height:30px;" id='Sbtn'
                                                           value="   找 游 戏   "></td>
                                            </tr>
                                        </table>
                                    </form>
                                </td>
                                <td align="left">&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td valign="top" class="border_right_green">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td width="26%" align="center">&nbsp;</td>
                                <td width="74%" height="31" align="left"><a href="${contextPath}/game">连连看</a>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" class="border_bottom_green">&nbsp;</td>
                                <td height="31" colspan="2" align="left" class="border_bottom_green">&nbsp;</td>
                            </tr>
                            <tr>
                                <td colspan="3" align="center">&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                    <td align="center" valign="top" style='padding-left:5px;'><iframe style="border:none; width:100%; height:550px;" src="${contextPath}/html/game/linkup/linkup.html"/></td>
                </tr>
            </table>
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






