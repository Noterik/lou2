function Gestureoverlay(options){
	var self = {};
	var settings = {
		's': null,
		'm': null,
		'h': null,
		'dragstartpos': null
	};
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
				case "imgholder":
					handleImgHolder(msg);
			}
		}
	}

	handleImgHolder = function(event){
		if(event.direction=='up' && event.type=='swipe'){
			self.swipeUp(event);
		}else if(event.direction=='down' && event.type=='swipe'){
			self.swipeDown(event);
		}else if(event.direction=='left' && event.type!='swipe'){
			self.swipeLeft(event);
		}else if(event.direction=='right' && event.type!='swipe'){
    		self.swipeRight(event);
    	}
	}

	self.swipeDown = function(event){
		var curtime = eval(components.controller.getCurtime()) + 1;
	   	settings.s = curtime % 60;
	    settings.m = Math.floor(curtime / 60) % 60;
	    settings.h = Math.floor(settings.m / 60) % 60;
		$("#screenshot").css("visibility", "visible");
	   	document.getElementById('screenshot').src ='http://images1.noterik.com' + components.controller.getVideoSrc() + '/shots/1/h/' + settings.h + '/m/' + settings.m + '/sec' + settings.s + '.jpg';
	    var mypos = document.getElementById("mysliderpos2");
		var pos = settings.s + (settings.m * 60) + (settings.h * 3600);
		$('title').html("p=" + pos);
	    mypos.style.left = 100 * (pos / components.controller.getDuration() ) + "%";
		mypos.style.visibility = 'visible';
	}
	
	self.swipeUp = function(event){
		var pos = settings.s + (settings.m * 60) + (settings.h * 3600);
		if(pos > 0 && pos < components.controller.getDuration()) 
			eddie.putLou('video', 'seek(' + pos + ')');
	}
	
	self.swipeRight = function(event){
		if (event.type=='dragstart') {
			settings.dragstartpos = settings.s + (settings.m * 60) + (settings.h * 3600);
		}else if (event.type=='dragend') {
		}else {
			var pos = settings.dragstartpos + Math.floor(event.distanceX / 10);
			if (pos > components.controller.getDuration()) 
				pos = components.controller.getDuration()
			settings.s = pos % 60;
			settings.m = Math.floor(pos / 60) % 60;
			settings.h = Math.floor(settings.m / 60) % 60;
	        var mypos = document.getElementById("mysliderpos2");
	        mypos.style.left = 100 * (pos / components.controller.getDuration()) + "%";
			mypos.style.visibility = 'visible';
	   		var purl = 'http://images1.noterik.com' + components.controller.getVideoSrc() + '/shots/1/h/' + settings.h + '/m/' + settings.m + '/sec' + settings.s + '.jpg';
			$('title').html(purl);
			if (document.getElementById('screenshot').src != purl)
	   			document.getElementById('screenshot').src = purl; 
		}
	}
	
	self.swipeLeft = function(event){
		if (event.type=='dragstart') {
			settings.dragstartpos = settings.s + (settings.m * 60) + (settings.h * 3600);
		} else if (event.type=='dragend') {
		} else {
			var pos = settings.dragstartpos + Math.floor(event.distanceX / 10);
			if (pos<0) 
				pos = 0;
			settings.s = pos % 60;
			settings.m = Math.floor(pos / 60) % 60;
			settings.h = Math.floor(settings.m / 60) % 60;
	        var mypos = document.getElementById("mysliderpos2");
			mypos.style.visibility = 'visible';
	        mypos.style.left = 100 * (pos / components.controller.getDuration()) + "%";
	   		var purl = 'http://images1.noterik.com' + components.controller.getVideoSrc() + '/shots/1/h/' + settings.h + '/m/' + settings.m + '/sec' + settings.s + '.jpg';
			if (document.getElementById('screenshot').src != purl)
	   			document.getElementById('screenshot').src = purl; 
		}
	}
	
	return self;
}