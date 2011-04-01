<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<title>${user.nickname}</title>
</head>
<body>
<form action="${contextPath}/admin/save" method="post">
	<input type="hidden" name="id" value="${user.id}" />
    <table width="100%" align="center" cellspacing="2">
      <tr>
        <td align="left" valign="middle" width=5%>用户名：</td>
        <td align="left" valign="middle" ><input type="text" name="username" id="username" value="${user.username}"/></td>
      </tr>
      <tr>
        <td align="left" valign="middle">昵&nbsp;&nbsp;&nbsp;称：</td>
        <td align="left" valign="middle"><input type="text" name="nickname" id="nickname" value="${user.nickname}"/></td>
      </tr>
      <tr>
        <td align="left" valign="middle">密&nbsp;&nbsp;&nbsp;码：</td>
        <td align="left" valign="middle"><input type="password" name="password" id="password" value="${user.password}"/></td>
      </tr>
      <tr>
        <td align="left" valign="middle">邮&nbsp;&nbsp;&nbsp;箱：</td>
        <td align="left" valign="middle"><input type="text" name="email" id="email" value="${user.email}"/>(登录时使用)</td>
      </tr>
      <tr>
        <td align="left" valign="top">简&nbsp;&nbsp;&nbsp;介：</td>
        <td align="left" valign="middle"><textarea name="profile" cols="45" rows="4">${user.profile}</textarea></td>
      </tr>
      <tr>
        <td align="left" valign="middle">&nbsp;</td>
        <td align="left" valign="middle">
          <input type="submit" value=" 保  存 "/>
          &nbsp;&nbsp;
          <font color="#FA0">${msg}</font></td>
      </tr>
    </table>
</form>
</body>
</html>
