var sys = require("sys")
  , ws = require('./lib/ws/server');

var server = ws.createServer({debug: true});

// Handle WebSocket Requests
server.addListener("connection", function(conn){
  conn.broadcast("[系统消息]\r\n=> 有神秘人士上线，他不说话我们都不知道他是谁！\r\n");
  conn.addListener("message", function(message){
    //conn.broadcast("<"+conn.id+"> "+message);
    conn.broadcast(message);
    if(message == "error"){
      conn.emit("error", "test");
    }
  });
});

server.addListener("error", function(){
  console.log(Array.prototype.join.call(arguments, ", "));
});

server.addListener("disconnected", function(conn){
  server.broadcast("<"+conn.id+"> disconnected");
});

server.listen(8000);
