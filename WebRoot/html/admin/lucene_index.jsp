<%--
  Created by IntelliJ IDEA.
  User: Erhu
  Date: 11-8-2
  Time: 下午1:38
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <title>索引管理</title>
</head>
<body>
<form name="form_index" id="form_index" action="${contextPath}/admin/updateIndex" method="post">
    <input type='hidden' name="operation"/>
    <input type='hidden' name="table"/>

    <h3 style="color:#FF0">索引管理</h3>
    <table align="left" cellspacing="2">
        <tr>
            <td align="left" valign="middle" nowrap>博文表：</td>
            <td align="left" valign="middle">
                <input type="button" name="createIndex_Blog" id="createIndex_Blog"
                       value="重建索引" onClick="rebuild('blog')"/>
                <input type="button" name="updateIndex_Blog" id="updateIndex_Blog"
                       value="更新索引" onClick="update('blog')"/>
            </td>
            <td rowspan="3" align="left" valign="middle">
                <input type="button" name="createIndex_All" id="createIndex_All"
                       value="全部重建" style="height: 81px;" onClick="rebuild('blog,code,motto')"/>
            </td>
            <td rowspan="3" align="left" valign="middle">
                <input type="button" name="updateIndex_All" id="updateIndex_All"
                       value="全部更新" style="height: 81px;" onClick="update('blog,code,motto')"/>
            </td>
        </tr>
        <tr>
            <td align="left" valign="middle">代码表：</td>
            <td align="left" valign="middle">
                <input type="button" name="createIndex_Code" id="createIndex_Code"
                       value="重建索引" onClick="rebuild('code')"/>
                <input type="button" name="updateIndex_Code" id="updateIndex_Code"
                       value="更新索引" onClick="update('code')"/>
            </td>
        </tr>
        <tr>
            <td align="left" valign="middle">箴言表：</td>
            <td align="left" valign="middle">
                <input type="button" name="createIndex_Motto" id="createIndex_Motto"
                       value="重建索引" onClick="rebuild('motto')"/>
                <input type="button" name="updateIndex_Motto" id="updateIndex_Motto"
                       value="更新索引" onClick="update('motto')"/>
        </tr>
    </table>
</form>
<script language="javascript">

    function rebuild(t_table) {
        var fm = document.form_index
        fm.operation.value = "rebuild"
        fm.table.value = t_table
        fm.submit()
    }

    function update(t_table) {
        var fm = document.form_index
        fm.operation.value = "update"
        fm.table.value = t_table
        fm.submit()
    }
</script>
</body>
</html>
