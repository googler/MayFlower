<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>搜索 - ${q}</title>
    <style type="text/css">
        ul, li {
            margin: 0;
            padding: 0;
        }

        .unit {
            margin: 0px auto;
            padding: 0px;
        }

        .unit_title {
            height: 30px;
            line-height: 30px;
            margin: 0px;
            padding-left: 3px;
        }

        .u_tab {
            float: left;
            background-color: #930;
            padding: 0 7px;
            line-height: 26px;
            margin-top: 2px;
        }

        .u_tab_no {
            float: left;
            background-color: #930;
            padding: 0 7px;
            line-height: 26px;
            margin-top: 2px;
        }

        .u_tab_hover {
            float: left;
            background-color: #230;
            width: 50px;
            text-align: center;
            line-height: 26px;
            margin-top: 4px;
        }
    </style>
</head>
<body>
<c:choose>
<c:when test='${!(empty blog_list) || !(empty code_list) || !(empty motto_list)}'>
<div class="unit">
<div class="unit_title">
    <div id="tab01">
        <c:if test='${!(empty blog_list)}'>
            <span ${class_blog}><a href="#" title="搜到${total_count_blog}条博文">博 文</a></span>
        </c:if>
        <c:if test='${!(empty code_list)}'>
            <span ${class_code}><a href="#" title="搜到${total_count_code}条代码">代 码</a></span>
        </c:if>
        <c:if test='${!(empty motto_list)}'>
            <span ${class_motto}><a href="#" title="搜到${total_count_motto}条箴言">箴 言</a></span>
        </c:if>
    </div>
</div>
<div id="tabCon01">
<c:if test='${!(empty blog_list)}'>
    <ul class="ul_news" ${style_blog}>
        <table width="100%">
            <c:forEach items='${blog_list}' var='blog'>
                <tr align="left" valign="middle">
                    <form name="${blog.id}" id="${blog.id}" action="${contextPath}/blog/del"
                          method="post">
                        <input type="hidden" name="id" value="${blog.id}"/>
                        <td width="68%" valign="middle" style="color:#FFF; font-size:14px;" nowrap>
                            <img src="${contextPath}/images/default_activity.png" width="16">&nbsp;
                            <a href="${contextPath}/blog/read/${blog.id}/?q=${q}"><b>${blog.title}</b></a>
                        </td>
                        <td width="9%" align="right" nowrap>
                                ${blog.tag}&nbsp;
                        </td>
                        <td width="15%" align="right" nowrap>
                                ${blog.create_date}&nbsp;
                        </td>
                        <c:if test='${user.role == "admin"}'>
                            <td width="4%" align="center" nowrap>
                                <a href="${contextPath}/blog/edit/${blog.id}">编辑</a>
                            </td>
                            <td width="4%" align="center" nowrap>
                                <a href="#" onClick="onDel(${blog.id});">删除</a>
                            </td>
                        </c:if>
                    </form>
                </tr>
                <tr align="left" valign="middle">
                    <td colspan="5" class="border_bottom_green_dotted"
                        style="color:#CCC;">${blog.content}</td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="5">
                    <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
                        <tr>
                            <%
                                //int total_page = Integer.parseInt(request.getAttribute("total_page").toString());
                                int p_start_blog = Integer.parseInt(request.getAttribute("p_start_blog").toString());
                                int p_end_blog = Integer.parseInt(request.getAttribute("p_end_blog").toString());
                            %>
                            <td align=left width="50%" nowrap>
                                <a href='${contextPath}/search/bcm/?q=${q}&p=<%=p_start_blog-10%>'>上一屏</a>
                                /
                                <a href='${contextPath}/search/bcm/?q=${q}&p=<%=p_start_blog+10%>'>下一屏</a>
                            </td>
                            <td width="100%" align="right" nowrap>
                                <a href='${contextPath}/search/bcm/?q=${q}&p=${curr_page_blog-1}'>上一页</a>
                                /
                                <a href='${contextPath}/search/bcm/?q=${q}&p=${curr_page_blog+1}'>下一页</a>
                                |
                                第<%
                                for (int ii = p_start_blog; ii <= p_end_blog - 1; ii++) {%><a
                                    href="${contextPath}/search/bcm/?q=${q}&p=<%=ii%>"><%=ii%>
                            </a>&nbsp;
                                <%}%>
                                <a href="${contextPath}/search/bcm/?q=${q}&p=<%=p_end_blog%>">${p_end_blog}</a>页
                                |
                                当前第${curr_page_blog}页
                                /
                                共${total_page_blog}页
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </ul>
</c:if>
<c:if test='${!(empty code_list)}'>
    <ul class="ul_news" ${style_code}>
        <table width="100%">
            <c:forEach items='${code_list}' var='code'>
                <tr align="left" valign="middle">
                    <form name="${code.id}" id="${code.id}" action="${contextPath}/code/del"
                          method="post">
                        <input type="hidden" name="id" value="${code.id}"/>
                        <td width="68%" valign="middle" style="color:#FFF; font-size:14px;" nowrap>
                            <img src="${contextPath}/images/default_activity.png" width="16">&nbsp;
                            <a href="${contextPath}/code/read/${code.id}/?q=${q}"><b>${code.title}</b></a>
                        </td>
                        <td width="9%" align="right" nowrap>
                                ${code.tag}&nbsp;
                        </td>
                        <td width="15%" align="right" nowrap>
                            <!--${code.create_date}&nbsp;-->
                        </td>
                        <c:if test='${user.role == "admin"}'>
                            <td width="4%" align="center" nowrap>
                                <a href="${contextPath}/code/edit/${code.id}">编辑</a>
                            </td>
                            <td width="4%" align="center" nowrap>
                                <a href="#" onClick="onDel(${code.id});">删除</a>
                            </td>
                        </c:if>
                    </form>
                </tr>
                <tr align="left" valign="middle">
                    <td colspan="5" class="border_bottom_green_dotted"
                        style="color:#CCC;">${code.content}</td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="5">
                    <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
                        <tr>
                            <%
                                //int total_page = Integer.parseInt(request.getAttribute("total_page").toString());
                                int p_start_code = Integer.parseInt(request.getAttribute("p_start_code").toString());
                                int p_end_code = Integer.parseInt(request.getAttribute("p_end_code").toString());
                            %>
                            <td align=left width="50%" nowrap>
                                <a href='${contextPath}/search/bcm/?q=${q}&p=<%=p_start_code-10%>&t=code'>上一屏</a>
                                /
                                <a href='${contextPath}/search/bcm/?q=${q}&p=<%=p_start_code+10%>&t=code'>下一屏</a>
                            </td>
                            <td width="100%" align="right" nowrap>
                                <a href='${contextPath}/search/bcm/?q=${q}&p=${curr_page_code-1}&t=code'>上一页</a>
                                /
                                <a href='${contextPath}/search/bcm/?q=${q}&p=${curr_page_code+1}&t=code'>下一页</a>
                                |
                                第<%
                                for (int ii = p_start_code; ii <= p_end_code - 1; ii++) {%><a
                                    href="${contextPath}/search/bcm/?q=${q}&p=<%=ii%>&t=code"><%=ii%>
                            </a>&nbsp;
                                <%}%>
                                <a href="${contextPath}/search/bcm/?q=${q}&p=<%=p_end_code%>&t=code">${p_end_code}</a>页
                                |
                                当前第${curr_page_code}页
                                /
                                共${total_page_code}页
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </ul>
</c:if>
<c:if test='${!(empty motto_list)}'>
    <ul class="ul_news" ${style_motto}>
        <table width="100%">
            <c:forEach items='${motto_list}' var='motto'>
                <tr align="left" valign="middle">
                    <form name="${motto.id}" id="${motto.id}" action="${contextPath}/motto/del"
                          method="post">
                        <input type="hidden" name="id" value="${motto.id}"/>
                        <td width="68%" valign="middle" style="color:#FFF;">
                            <img src="${contextPath}/images/default_activity.png" width="16">&nbsp;
                                ${motto.content}
                        </td>
                        <td width="26%" align="right" nowrap>
                                ${motto.tag}&nbsp;
                        </td>
                        <c:if test='${user.role == "admin"}'>
                            <td width="3%" align="center" nowrap>
                                <a href="${contextPath}/motto/edit/${motto.id}">编辑</a>
                            </td>
                            <td width="3%" align="center" nowrap>
                                <a href="#" onClick="onDel(${motto.id});">删除</a>
                            </td>
                        </c:if>
                    </form>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="5">
                    <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
                        <tr>
                            <%
                                //int total_page = Integer.parseInt(request.getAttribute("total_page").toString());
                                int p_start_motto = Integer.parseInt(request.getAttribute("p_start_motto").toString());
                                int p_end_motto = Integer.parseInt(request.getAttribute("p_end_motto").toString());
                            %>
                            <td align=left width="50%" nowrap>
                                <a href='${contextPath}/search/bcm/?q=${q}&p=<%=p_start_motto-10%>&t=motto'>上一屏</a>
                                /
                                <a href='${contextPath}/search/bcm/?q=${q}&p=<%=p_start_motto+10%>&t=motto'>下一屏</a>
                            </td>
                            <td width="100%" align="right" nowrap>
                                <a href='${contextPath}/search/bcm/?q=${q}&p=${curr_page_motto-1}&t=motto'>上一页</a>
                                /
                                <a href='${contextPath}/search/bcm/?q=${q}&p=${curr_page_motto+1}&t=motto'>下一页</a>
                                |
                                第<%
                                for (int ii = p_start_motto; ii <= p_end_motto - 1; ii++) {%><a
                                    href="${contextPath}/search/bcm/?q=${q}&p=<%=ii%>&t=code"><%=ii%>
                            </a>&nbsp;
                                <%}%>
                                <a href="${contextPath}/search/bcm/?q=${q}&p=<%=p_end_motto%>&t=code">${p_end_motto}</a>页
                                |
                                当前第${curr_page_motto}页
                                /
                                共${total_page_motto}页
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </ul>
</c:if>
</div>
</div>
</c:when>
<c:otherwise>
    <div><img src="../../images/404_2.jpg" width="473" height="402"></div>
</c:otherwise>
</c:choose>
<script type="text/javascript">
    function tabAction(parNode, parCon) {
        var tabs = document.getElementById(parNode).getElementsByTagName("span");
        var tabLength = tabs.length;
        var tabCon = document.getElementById(parCon).getElementsByTagName("ul");
        var tabConLength = tabCon.length;

        function initTab() {
            for (var i = 0; i < tabLength; i++) {
                tabs[i].className = "u_tab";
            }
            tabs[tabLength - 1].className = "u_tab_no";
        }

        function initTabCon() {
            for (var i = 0; i < tabConLength; i++) {
                tabCon[i].style.display = "none";
            }
        }

        for (var i = 0; i < tabLength; i++) {
            tabs[i].theI = i;
            tabs[i].onclick = function() {
                var theI = this.theI;
                initTab();
                initTabCon();
                this.className = "u_tab_hover";
                tabCon[theI].style.display = "block";
                //判断被点击tab的前一个tab,在非IE中可能为文本节点
                if (this.previousSibling) {
                    var preObj = this.previousSibling;
                    if (this.previousSibling.nodeType == 1) {
                        preObj.className = "u_tab_no";
                    }
                    else if (this.previousSibling.nodeType == 3 && this.previousSibling.previousSibling) {
                        preObj = this.previousSibling.previousSibling;
                        preObj.className = "u_tab_no";
                    }
                }
                this.childNodes[0].blur();//去掉锚点的虚线框
            }
        }
    }
    tabAction("tab01", "tabCon01");
</script>
</body>
</html>
