function Qrcode(options){
	var self = {};
	var settings = {};
	$.extend(settings, options);
	
	self.putMsg = function(msg){
		try{
			var command = [msg.target[0].class];
		}catch(e){
			command = $(msg.currentTarget).attr('class').split(" ");
		}
		var content = msg.content;
		for(i=0;i<command.length;i++){
			switch(command[i]) { 
				case 'visible':
					handleVisibilityChange(content);
	  				break;
				default:
					alert('unhandled msg in qrcode.html : '+msg); 
			}
		}
	}

	handleVisibilityChange = function(content){
		if (content=="true") {
				$('#qrcode').html("");
				jQuery('#qrcode').qrcode({ text    : document.URL });
                $("#qrcode").css("visibility","visible");
			} else {
                $("#qrcode").css("visibility","hidden");
			}
	}
	
	
	return self;
}