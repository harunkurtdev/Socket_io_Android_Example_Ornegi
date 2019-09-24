var app = require('express')();
var server = require('http').createServer(app);
var io = require('socket.io')(server);

server.listen(4001);

io.on('connection', function(socket){
	console.log("Client connected");

	socket.on('mesaj', function(data){
		console.log(data);
		io.emit('mesajAl',data);
	});


});

