//This is a Javascript class equal. Basically the entire thing between the curly braces is the constructor. Any arguments you want to pass to a class can be passed to the function which contains the class.

//Static functions are defined as: this.<name> = function(){}
//Private functions are defined as: var <name> = function(){}
//Public functions are defined as: self.<name> = function(){}
var Controller = springfield_function(options){
	//Self contains the public functions for this object. Gets returned at the end of this class (or function) 
	var self = {};
	
	//Contains default settings for this object, gets overwritten by the options argument passed to the constructor (var Controller = function(options))
	var settings = {
		curtime: null,
		duration: null,
		videosrc: null,
		test: null,
	}
	//$.extend overwrites the values in settings by the values given in the options. See http://api.jquery.com/jQuery.extend/
	$.extend(settings, options);

	//Public function sliderclick, add it to the self object so that it can be returned as a public function!
	self.sliderclick = function(event){
		// console.log('Controller.sliderclick()');
		var mx = event.offsetX;
		var swidth = document.getElementById("myslider_bg").width;
		var sperc = (mx / swidth);
		var pos = settings.duration * sperc;
		eddie.putLou('video','seek(' + pos + ')');
	}
	
	//Public function putMsg
	self.putMsg = function(msg){
		try{
			var command = [msg.target[0].class];
		}catch(e){
			command = $(msg.currentTarget).attr('class').split(" ");
		}
		var content = msg.content;
		for(i=0;i<command.length;i++){
			switch(command[i]) { 
				case 'timeupdate':
					if (settings.videosrc == null) {
						eddie.putLou('video','request_videosrc()')
					}
					var curtime = content.substring(0, content.indexOf(":"));
					var duration = content.substring(content.indexOf(":") + 1);
	       			var mypos = document.getElementById("mysliderpos");
					mypos.style.left = 100 * (curtime / duration) + "%";
					settings.curtime = curtime;
					settings.duration = duration;
	  				break;
				case 'videosrc':
					settings.videosrc = content;
					if(document.getElementById('screenshot') != null){
				    	document.getElementById('screenshot').src ='http://images1.noterik.com/domain/webtv/user/daniel/video/151/shots/1/h/0/m/0/sec0';
				    }
					document.getElementById("mysliderpos2").style.visibility = 'hidden';
					if(components.relatedview){
						components.relatedview.load(content);
					}
					break;
				case 'setdata':
					//$('#test').html(content.substring(content.indexOf(",")+1));
					self.setProperty(content.substring(0, content.indexOf(",")), content.substring(content.indexOf(",")+1));
					//console.log(settings[content.substring(0, content.indexOf(","))]);
					break;
				case 'notify':
					var args = content.split(',');
					console.log('component :' + args[0] + ' triggered action: ' + args[1]);
					break;
				case 'clickable':
					handleClickEvent(event);
					break;
				default:
					alert('unhandled msg in controller.html : ' + msg); 
			}
		}
	}

	self.setProperty = function(pname, pval){
		settings[pname] = pval;
		$("#"+pname).html(pval);
	}

	self.getProperty = function(pname){
		return settings[pname];
	}
	
	self.getCurtime = function(){
		return settings.curtime;
	}
	
	self.getDuration = function(){
		return settings.duration;
	}
	
	self.getVideoSrc = function(){
		return settings.videosrc;
	}
	
	self.setVideoSrc = function(src){
		settings.videosrc = src;
	}

	self.testget = function(){
		eddie.putLou('controller','getdata(test)');
	}

	handleClickEvent = function(event){
		var cl = $(event.currentTarget).attr('id');
		switch(cl){
			case "play":
				eddie.putLou('video', 'play()');
				break;
			case "pause":
				eddie.putLou('video', 'pause()');
				break;
			case "mute":
				eddie.putLou('video', 'mute()');
				break;
			case "volup":
				eddie.putLou('video', 'volumeup()');
				break;
			case "voldown":
				eddie.putLou('video', 'volumedown()');
				break;
			case "avro":
				eddie.putLou('video','url(avro)');
				break;
			case "rbb":
				eddie.putLou('video','url(rbb)');
				break;
			case "t1":
				eddie.putLou('video','url(t1)');
				break;
			case "t2":
				eddie.putLou('video','url(t2)');
				break;
			case "qrcode ":
				eddie.putLou('video','qrcode(toggle)');
				break;
			case "s-2":
				eddie.putLou('video','speed(-2)');
				break;
			case "s-1":
				eddie.putLou('video','speed(-1)');
				break;
			case "s":
				eddie.putLou('video','speed(1)');
				break;
			case "s+2":
				eddie.putLou('video','speed(2)');
				break;
			case "s+3":
				eddie.putLou('video','speed(3)');
				break;
			case "s-02":
				eddie.putLou('video','speed(-0.2)');
				break;
			case "s-05":
				eddie.putLou('video','speed(-0.5)');
				break;
			case "s+02":
				eddie.putLou('video','speed(0.2)');
				break;
			case "s+05":
				eddie.putLou('video','speed(0.5)');
				break;
			case "style1":
				eddie.putLou('','setstyle(1)');
				break;
			case "style2":
				eddie.putLou('','setstyle(2)');
				break;
			case "style3":
				eddie.putLou('','setstyle(3)');
				break;
			case "stylespec":
				eddie.putLou('','setstyle(4)');
				break;
			case "removespec":
				eddie.putLou('','removestyle(specific)');
				break;
			case "subscribe":
				components.signal.subscribe('controller', 'controller');
				break;
			case "signal":
				components.signal.notify('controller', 'notify');
				break;
			default:
			console.log("handler for this image not implemented: "+ cl);
		}
		components.signal.notify('controller', cl);

	}

	$("input#data").bind('keyup', function(){
		var value = $(this).val();
		console.log(value);
		eddie.putLou('controller','setdata(test,'+value+')');
	});

	$('#myslider_bg').click(function() {
		components.controller.sliderclick(event);
	});
	
	//Return the self object which contains the public functions for this class. The functions contained in self can call private functions.
	return self;
}