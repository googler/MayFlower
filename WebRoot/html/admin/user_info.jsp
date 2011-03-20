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
        <td width="12%"></td>
        <td width="88%"></td>
      </tr>
      <tr>
        <td align="left" valign="middle" >用户名：</td>
        <td align="left" valign="middle" ><input type="text" name="username" id="username" value="${user.username}"/></td>
      </tr>
      <tr>
        <td align="left" valign="middle">昵称：</td>
        <td align="left" valign="middle"><input type="text" name="nickname" id="nickname" value="${user.nickname}"/></td>
      </tr>
      <tr>
        <td align="left" valign="middle">密码：</td>
        <td align="left" valign="middle"><input type="password" name="password" id="password" value="${user.password}"/></td>
      </tr>
      <tr>
        <td align="left" valign="middle">邮箱：</td>
        <td align="left" valign="middle"><input type="text" name="email" id="email" value="${user.email}"/></td>
      </tr>
      <tr>
        <td align="left" valign="middle">简介：</td>
        <td align="left" valign="middle"><textarea name="profile" cols="45" rows="4">${user.profile}</textarea></td>
      </tr>
      <tr>
        <td align="left" valign="middle">&nbsp;</td>
        <td align="left" valign="middle">
          <input type="submit" value=" 保  存 "/>
          &nbsp;&nbsp;
          <input type="button" onClick="history.go(-1)" value=" 取  消 "/>
          <font color="#FA0">${msg}</font></td>
      </tr>
    </table>
</form>
</body>
</html>
