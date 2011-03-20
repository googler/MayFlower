<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
    <title>MayFlower chatroom</title>  
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
          	onClick="window.location=''")>
          </td>
          <td align="left" valign="middle"><table><tr>
            <td align="left">
              <table>
                <tr>
                  <td><input name="msg" type="search" id="msg" title="说句心里话" value=""  size="90" maxlength="2048" placeholder='说句心里话' autofocus onKeyDown="if(event.keyCode==13) sendMsg('${user.nickname}');"/></td>
                  <td nowrap><input id="send" name="send" type="button" style="height:30px;" value="发 送" onClick="sendMsg('${user.nickname}');">(按回车键发送)</td></tr>
              </table>
            </td></tr></table></td> 
        </tr> 
        <tr> 
          <td valign="top" class="border_right_green"><table width="100%"  border="0" cellpadding="0" cellspacing="0">
              <tr>
				<td width="26%" align="center">&nbsp;</td>
				<td width="74%" height="31" align="left"><a href="">博文欣赏</a></td>
              </tr>
              <tr >
              	  <td align="center" class="border_bottom_green">&nbsp;</td>	
                  <td height="31" colspan="2" align="left" class="border_bottom_green"><a href="">代码片段</a></td>
              </tr>
              <tr>
                <td colspan="3" align="center">&nbsp;</td>
              </tr>
            </table></td> 
          <td valign="top" style='padding-left:5px;'>
			<textarea cols="90" rows="25" readonly="readonly" id="redord"></textarea><br>服务器不会保存您的聊天记录，请放心使用！
          </td> 
        </tr> 
    </table></td> 
  </tr> 
  <tr> 
    <td align="center"><%@ include file="/html/inc/bottom.jsp" %></td> 
  </tr> 
</table>
<script type="text/javascript">
	var months_en = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
	var months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'];
	var conn, recvd, connections = 0;
	var redord = document.getElementById("redord");
	// 转换个位数
	var connect = function() {
		if (window["WebSocket"]) {
			recvd = 0;
			//host = (document.location.host != "" ? document.location.host : "localhost:8000");
			host = "172.16.1.12:8000";
			conn = new WebSocket("ws://"+host+"/test");
			conn.onmessage = 
				function(evt) {
					//alert(evt.data)
					log(evt.data);
					  // conn.close();
					  // log(connections++);
					  // connect();
					  // if(recvd == 0){
					  // conn.id = parseInt(evt.data.match(/\: ([0-9]+)/)[1], 10);
					  // }
					  // recvd++;
				};
			conn.onerror = function() {
				log("[系统消息]\r\n=> 出错了", arguments);
			};
			conn.onclose = function() {
				log("[系统消息]\r\n=> 服务器已经关闭，谢谢您的使用！");
			};
			conn.onopen = function() {
				log("[系统消息]\r\n=> 已与服务器建立连接，你不说话大家不会知道你在线:-)\r\n");
			};
		}
	};
	function sendMsg(user) {
		if (document.getElementById('msg').value.trim() == '') {
			alert('不要说空话哦!')
			return false;
		}
		var msg = document.getElementById('msg').value;
		msg = "[" + user + "]\r\n=> " + msg + "\r\n";
		document.getElementById('msg').value = '';
		if (conn) {
			setTimeout(function() {conn.send(msg);log(msg);}, 0);
		}
	}
	function pad(n) {
		return n < 10 ? '0' + n.toString(10) : n.toString(10); 
	}
	// 获取当前时间戳
	function timestamp() {
		var d = new Date();
		return [months[d.getMonth()] + d.getDate()+'日',
						  [ 
							pad(d.getHours()), 
							pad(d.getMinutes()), 
							pad(d.getSeconds())
						  ].join(':')//---
				].join(' ');
	}
	function scrollToBottom() {
		redord.scrollTop = redord.scrollHeight
		//window.scrollBy(0, document.body.scrollHeight - document.body.scrollTop);
	}
		
	function log(data){
		redord.innerHTML += timestamp()+" : " + data + "\r\n";
		scrollToBottom();
	}
	/*document.getElementById("close").addEventListener("click", 
		function(e) {
		  if (conn) {
			conn.close();
			conn = false;
		  }
		  e.preventDefault();
		  return false;
		}, false);
		
	document.getElementById("open").addEventListener("click", 
		function(e) {
			if (!conn) {
				connect();
			}
			e.preventDefault();
			return false;
		}, false);*/
	window.onload = connect;
</script>
</body>
</html>
