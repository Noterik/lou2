function Login(options){
	var self = {};
	var settings = {
		loginname: null,
		louticket: null
	}
	
	$.extend(settings, options);
	
	self.change = function(){
		eddie.putLou("login", "setProperties(" + $('#login_account').val() + "," + $('#login_password').val() + ")");
	}
	
	self.putMsg = function(msg) {
		try{
			var command = [msg.target[0].class];
		}catch(e){
			command = $(msg.currentTarget).attr('class').split(" ");
		}
		var content = msg.content;
		for(i=0;i<command.length;i++){
			switch(command[i]) { 
				case 'login':
					var ar = content.split(',');
					alert("problem login made it !!! a="+ar[0]+" p=******");
	  				break;
	        	case 'ticket':
					if (content!="-1") {
						var ar = content.split(',');
						settings.louticket = ar[0];
						settings.loginname = ar[1];
						
						eddie.putLou('','load(login,loggedin)');
					} else {
						eddie.putLou('notification','show(' + $('#login_account').value() + ' forgot password)');
					}
	                break;
				default:
					alert('unhandled msg in login.html : ' + msg); 
			}
		}
	}
	
	$('#login_form').submit(function(e) {
		e.preventDefault();
		e.stopPropagation();
		eddie.putLou("login", "login(" + $('#login_account').val() + "," + $('#login_password').val() + ")");
		return false;
	});
	
	self.getLoginName = function() {
		return settings.loginname;
	}

	return self;
}