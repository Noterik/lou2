function Signal(options){

	var self = {};
	var settings = {};
	$.extend(settings, options);

	self.subscribe = function(observable, subscriber) {
		eddie.putLou('signal', 'subscribe(' + subscriber + ',' + observable + ')', false);
	}

	self.unsubscribe = function(observable, subscriber) {
		eddie.putLou('signal', 'unsubscribe(' + subscriber + ',' + observable + ')', false);
	}

	self.notify = function(source, action){
		eddie.putLou('signal', 'notify(' + source + ',' + action + ')', false);
	}

	self.putMsg = function(msg){
		try{
			var command = [msg.target[0].class];
		}catch(e){
			command = $(msg.currentTarget).attr('class').split(" ");
		}
		var content = msg.content;
		for(i=0;i<command.length;i++){
			switch(command[i]) { 
			}
		}
		console.log('signal.putMsg()');
	}

	return self;
}