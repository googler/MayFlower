<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
    <title>MayFlower Game</title>  
    <%@ include file="/html/inc/init.jsp" %>  
</head>
<body>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"> 
  <tr> 
    <td><%@ include file="/html/inc/top.jsp" %></td>
  </tr> 
  <tr> 
    <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"> 
        <tr> 
          <td width="150">
          	<img src="${contextPath}/images/mayFlower_small.png" alt="MayFlower:五月花!" title="MayFlower:五月花!" 
          	onClick="window.location=''"/>
          </td>
          <td align="left" valign="middle">测试版本</td> 
        </tr> 
        <tr> 
          <td valign="top" class="border_right_green"><table width="100%"  border="0" cellpadding="0" cellspacing="0">
              <tr>
                  <td height="31" align="center" class="border_bottom_green"><a href="">当前在线</a></td>
              </tr>
            </table></td> 
          <td valign="top" style='padding-left:5px;'>
			
          </td> 
        </tr> 
    </table></td> 
  </tr> 
  <tr> 
    <td align="center"><%@ include file="/html/inc/bottom.jsp" %></td> 
  </tr> 
</table>
</body>
</html>
