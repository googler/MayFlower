<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% String contextPath = request.getContextPath(); %>
<link rel="shortcut icon" href="${contextPath}/images/paladinIco.png"/>
<link rel="Bookmark" href="${contextPath}/images/paladinIco.png"/>
<link href="${contextPath}/css/border.css" rel="stylesheet">
<link href="${contextPath}/css/jquery-ui-1.8.10.custom.css" rel="stylesheet" />
<!--<meta http-equiv="Content-Type" content="text/html; charset=utf-8">-->
<meta charset="utf-8"/>
<style>
		#gbar,#guser{font-size:13px;padding-top:1px;padding-bottom:7px;}
		#gbar{float:left;height:22px;}
		#guser{text-align:right;}
		.gbh{
			border-top:1px solid #ccccff;
			font-size:1px;
			position:absolute;
			top:24px;
			width:100%;}
		.lineBottom{
			border-top:1px solid #ccccff;
			font-size:1px;a
			position:absolute;
			bottom:2px;
			width:100%;
			color:gray;
			text-align:center;}
		.footer {
			MARGIN-TOP: 10px;
			margin-bottom:10px;
			COLOR: #666;
			TEXT-ALIGN: center;}
</style>
<script src="${contextPath}/js/jquery-1.5.1.js"></script>
<script src="${contextPath}/js/jquery-ui-1.8.10.custom.min.js"></script>
<script src="${contextPath}/js/modernizr-1.7.js"></script>