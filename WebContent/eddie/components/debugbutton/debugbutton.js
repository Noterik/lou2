function Debugbutton(options){
	self = {};
	var settings = {};
	$.extend(settings, options);

	self.putMsg = function(msg){
		try{
			var command = [msg.target[0].class];
		}catch(e){
			command = $(msg.target).attr('class').split(" ");
		}
		var content = msg.content;
		for(i=0;i<command.length;i++){
			switch(command[i]) { 
				case "debugbutton":
					eddie.putLou('','load(debug,debugmode)');
				break;
			}
		}
	}

	return self;
}