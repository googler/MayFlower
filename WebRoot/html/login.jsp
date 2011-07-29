<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>登录 | 五月花</title>
    <%@ include file="/html/inc/init.jsp" %>
</head>
<body>
<%@ include file="/html/inc/top.jsp" %>
<table width="100%" border="0" align="center">
    <tr>
        <td valign="top" class="border_bottom_green">
            <TABLE width="95%" border=0 align="center" cellPadding='5'>
                <tr>
                    <td vAlign='top' width="75%">
                        <img
                                src="${contextPath}/images/mayFlower_small.png"
                                alt="登录 MayFlower 帐户给您带来个性化的 MayFlower 体验。"
                                title="登录 MayFlower 帐户给您带来个性化的 MayFlower 体验。"><BR>
                        在您登录到 MayFlower 帐户后，MayFlower 可提供更多服务。您可以自定义页面、查看建议并获得相关性更高的搜索结果。
                        <BR>
                        在右侧进行登录，或仅使用电子邮件地址和您所选择的密码
                        <A href="#">免费创建一个帐户</A>。
                        <BR>
                        <TABLE cellPadding=4 border=0>
                            <TBODY>
                            <tr>
                                <td>
                                    <IMG alt=Gmail
                                         src="${contextPath}/images/mail.gif">
                                    </IMG>
                                </td>
                                <td>
                                    <A href="#">Pmail</A>开始使用减少了垃圾邮件的全新电子信箱(正在建设中)。
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <IMG alt=iMayFlower src="${contextPath}/images/ig.gif">
                                    </IMG>
                                </td>
                                <td>
                                    <A href="#">iMayFlower </A>在MayFlower 首页中添加资讯、游戏和其他更多产品(正在建设中)。
                                </td>
                            </tr>
                            </TBODY>
                        </TABLE>
                    </td>
                    <td style="PADDING-LEFT: 10px" vAlign=middle align=middle>
                        <form id="loginFm" name="loginFm" action="${contextPath}/login/doLogin" method="post">
                            <input type="hidden" name="r" value="${r}"/>
                            <TABLE cellSpacing=0 cellPadding=1 align=center
                                   border=0>
                                <tr>
                                    <td align=center>&nbsp;</td>
                                    <td align=center><font size=+0><b>用户登录</b></font></td>
                                </tr>
                                <tr>
                                    <td align=middle colSpan=2></td>
                                </tr>
                                <tr id=email-row>
                                    <td height="25" align="right" noWrap>电子邮件：</td>
                                    <td height="25" align="left">
                                        <input type="email" class="val" id="email" size="30" name="email"
                                               value="${user.username}" autofocus>
                                    </td>
                                </tr>
                                <tr class=enabled id=password-row>
                                    <td height="25" align=right noWrap>密码：</td>
                                    <td height="25" align="left">
                                        <input class="val" id="pwd" type="password" size="30" name="pwd"
                                               value="${user.password}">
                                    </td>
                                </tr>
                                <!--<tr>
                                    <td height="25" align=right vAlign=top>
                                        <input id=PersistentCookie type=checkbox CHECKED value=yes
                                            name=PersistentCookie>
                                        <input type=hidden value=1 name=rmShown>
                                    </td>
                                    <td height="25" align="left">保持登录状态</td>
                                </tr>
                                -->
                                <tr>
                                    <td height="25"></td>
                                    <td height="25" align="left">
                                        <input id="signIn" type="submit" value=" 登  录 " style="width: 45%;height: 30px;"
                                               name="signIn"/>&nbsp;&nbsp;
                                        <input name="reSetBtn" type="button" value=" 清  空 "
                                               style="width: 45%;height: 30px;" onClick='resetFm()'/>
                                    </td>
                                </tr>
                                <!--
                                <tr>
                                    <td height="25" colSpan=2 align=middle>
                                        <A href="#" target=_top>无法访问您的帐户？</A>&nbsp;&nbsp;&nbsp;&nbsp;
                                        <A href="#">创建新帐户</A>
                                    </td>
                                </tr>
                                -->
                                <tr>
                                    <td height="25" colSpan=2 align="center"><font color="#FA0">${msg}</font>
                                    </td>
                                </tr>
                            </TABLE>
                        </form>
                    </td>
                </tr>
            </TABLE>
        </td>
    </tr>
    <tr>
        <td align="center">
            <%@ include file="/html/inc/bottom.jsp" %>
        </td>
    </tr>
</table>
<script>
    function login() {
        var fm = document.loginFm
        if (fm.email.value == '' || fm.pwd.value == '') {
            alert("Oops!Email and password can't be null:(")
            return false;
        } else
            fm.submit()
    }
    function resetFm() {
        var fm = document.loginFm
        fm.email.value = ''
        fm.pwd.value = ''
    }
</script>
</body>
</html>
