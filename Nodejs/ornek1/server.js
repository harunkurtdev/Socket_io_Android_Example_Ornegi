var app = require('express')();
var server = require('http').createServer(app);
var io = require('socket.io')(server);

server.listen(3001);

io.on('connection', function(socket){

	console.log("istemci baðlandý");
	socket.on('mesaj', function(data){
		console.log(data);
		io.emit('mesajAl',data);
	});

});