<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<html>
<head>
<title>MayFlower - <decorator:title default="Welcome to MayFlower!" /></title>
<%@ include file="/html/inc/init.jsp" %>
<decorator:head />
</head>
<body><a name="top"></a>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"> 
  <tr> 
    <td><%@ include file="/html/inc/top.jsp" %></td>
  </tr> 
  <tr> 
    <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"> 
        <tr> 
          <td width="150">
          	<img src="${contextPath}/images/mayFlower_small.png" alt="MayFlower:五月花!" title="MayFlower:五月花!" 
          	onClick="window.location='${contextPath}/blog'")>
          </td>
          <td align="left" valign="middle"><table><tr>
            <td align="left"><form action="" method="get">
              <table>
                <tr>
                  <td><input name="q" type="search" style="height:25px;"
                  		placeholder='请输入要查询的人名(Email,昵称)..' title="请输入要查询的人名(Email,昵称).."  size="70" maxlength="70" value="${q}" /></td>
                  <td><input type="submit" style="height:30px;" id='Sbtn' value="   找 朋 友   "></td>
                 </tr>
              </table>
            </form></td></tr></table></td> 
        </tr> 
        <tr> 
          <td valign="top" class="border_right_green"><table width="100%"  border="0" cellpadding="0" cellspacing="0">
              <c:if test='${user.role == "admin"}'>
                  <tr>
                      <td align="center">&nbsp;</td>	
                      <td height="31" colspan="2" align="left">
                          <a href="${contextPath}/admin/sysinfo">系统信息</a>
                      </td>   
                  </tr>
              </c:if>
              <tr>
				<td width="26%" align="center" class="border_bottom_green">&nbsp;</td>
				<td width="74%" height="31" align="left" class="border_bottom_green"><a href="${contextPath}/admin">帐户信息</a></td>
              </tr>
              <tr>
                <td colspan="3" align="center">&nbsp;</td>
              </tr>
            </table></td> 
          <td valign="top" style='padding-left:5px;'><decorator:body /></td> 
        </tr> 
    </table></td> 
  </tr> 
  <tr> 
    <td align="center"><%@ include file="/html/inc/bottom.jsp" %></td> 
  </tr> 
</table>
</body>
</html>
