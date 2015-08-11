var Notification = function(options){
	var self = {};
	var settings = {
		
	}
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
				case 'show':
					self.show(content);
	  				break;
	  			case 'showlong':
	  				showLong(content);
	  				break;
	  			case 'closelong':
	  				closeLong();
	  				break;
				case 'login':
					$('#notificationshort').html(content+" logged in");
	    			$('#notificationshort').animate({top:'10px'},400,function() { self.animDone('in'); });
	  				break;
				case 'logout':
					$('#notificationshort').html(content+" logged out ");
	    			$('#notificationshort').animate({top:'10px'},400,function() { self.animDone('in'); });
	  				break;
				case 'sound':
					self.makesound(content);
	  				break;
				default:
					alert('unhandled msg in notification.html : '+msg); 
			}
		}
	}
	
	self.show = function(line) {
		$('#notificationshort').html(line);
        $('#notificationshort').animate({top:'10px'},400,function() { self.animDone('in'); });
	}

	showLong = function(line) {
		console.log("showing persistent notification");
		$('#notificationlong').html(line);
        $('#notificationlong').animate({top:'50px'},400,function() { });
	}

	closeLong = function(){
		$('#notificationLong').animate({top:'-40px'},400,function() {});
	}
	
	self.animDone = function(step) {
		if (step=='in') {
        	$('#notificationshort').animate({top:'10px'},1000,function() { self.animDone('out'); });
		} else if (step=='out') {
        	$('#notificationshort').animate({top:'-40px'},400,function() { self.animDone('done'); });
		}
	}
	
	self.makesound = function(sound) {
   		var audio = $('<audio />', {
   			autoPlay : 'autoplay'
   		});
    	$('<source>').attr('src', '/eddie/sounds/'+sound+'.mp3').appendTo(audio);
     	audio.appendTo('body');    
	}
	
	$('#notificationshort').css('z-index', 9999);
	$('#notificationlong').css('z-index', 9999);
	eddie.putLou('notification','show(new screen '+ eddie.getScreenId() + ')');
	
	return self;
}