<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<title>服务器信息</title>
</head>
<body>
<h3 style="color:#FF0">虚拟机信息</h3>
<table width="100%" border="0">
  <tr>
    <th width="8%" align="left" valign="middle" nowrap>Key</th>
    <th width="92%" align="left" valign="middle">Value</th>
  </tr>
  <c:forEach items="${requestScope.sysProperties}" var="item">
    <tr>
      <td align="left" valign="top" nowrap>${item.key}</td>
      <td align="left" valign="middle">${item.value}</td>
    </tr>
  </c:forEach>
</table>
<h3 style="color:#FF0">OS环境变量</h3>
<table width="100%" border="0">
  <tr>
    <th width="8%" align="left" valign="middle" nowrap>Key</th>
    <th width="92%" align="left" valign="middle">Value</th>
  </tr>
  <c:forEach items="${requestScope.env}" var="item">
    <tr>
      <td align="left" valign="top" nowrap>${item.key}</td>
      <td align="left" valign="middle">${item.value}</td>
    </tr>
  </c:forEach>
</table>
<h3 style="color:#FF0">运行配置</h3>
<table width="100%" border="0">
  <tr>
    <th width="15%" align="left" valign="middle">Key</th>
    <th width="85%" align="left" valign="middle">Value</th>
  </tr>
  <tr>
    <td align="left" valign="top" nowrap>Number of processors</td>
    <td align="left" valign="middle">${requestScope.processorNum}</td>
  </tr>
  <tr>
    <td align="left" valign="top" nowrap>Free memory</td>
    <td align="left" valign="middle">${requestScope.freeMemory}</td>
  </tr>
  <tr>
    <td align="left" valign="top" nowrap>Max memory</td>
    <td align="left" valign="middle">${requestScope.maxMemory}</td>
  </tr>
</table>
</body>
</html>
