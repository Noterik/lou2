var Relatedview = function(options){
	var self = {};
	var settings = {
		layerdata_related: null,
		loaderlog: null
	};
	$.extend(settings, options);
	
	self.load = function(url){
		$(document).ready(function() {
  		$.ajax({
	   		type: "GET",
	    		url: "http://bart1.noterik.com/bart/domain/linkedtv/user/rbb/presentation/92",
	    		dataType: "xml",
	    		success: parseXml
	  		});
		});
		self.drawOverview();
	}
	
	self.getLayerDataRelated = function(){
		return settings.layerdata_related
	}
	
	self.drawRelated = function(){
		var content = "";
		for(i = 0; i < settings.layerdata_related.length; i++) {
			var related = settings.layerdata_related[i];
			var starttime = related[0];
			var duration = related[1];
			var thumbnail = related[2];
			content +="<img src=\"" + thumbnail + "\" width=\"120\" height=\"80\" onclick=\"JavaScript:eddie.putLou('video','seek(" + Math.floor(starttime / 1000) + ")')\" ontouchstart=\"JavaScript:eddie.putLou('video','seek(" + Math.floor(starttime / 1000) + ")')\"/>";
		}
        $('#relatedview_content').html(content);
	}
	
	self.drawOverview = function(){
		var content = "";
		var count = components.controller.getDuration();
		var step = count / 40;
		var j = 0; 
		for (i=0; i < 40; i++){
			var cur = Math.floor(j);
        	var s = cur % 60;
        	var m = Math.floor(cur / 60) % 60;
        	var h = Math.floor(m / 60) % 60;
			var thumbnail = 'http://images1.noterik.com' + components.controller.getVideoSrc() + '/shots/1/h/' + h + '/m/' + m + '/sec' + s + '.jpg' 
			content +="<img src=\"" + thumbnail + "\" width=\"120\" height=\"80\" onclick=\"JavaScript:eddie.putLou('video','seek(" + Math.round(j - 1) + ")')\" ontouchstart=\"JavaScript:eddie.putLou('video','seek(" + Math.round(j - 1) + ")')\"/>";
			j += step;
		}
        $('#relatedview_content').html(content);
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
				case 'visible':
	  				break;
				case 'select':
					if (content=="related") {
						self.drawRelated();
					} else if (content=="overview") {
						self.drawOverview();
					}
	  				break;
				default:
					alert('unhandled msg in relatedview.html : ' + msg); 
			}
		}
	}
	
	self.log = function(line) {
		if (settings.loaderlog==undefined) {
			settings.loaderlog = "";
		}
		settings.loaderlog += line + "\n";
		$('#loaderlogwindow').html(settings.loaderlog);
	}
	
	var parseXml = function(xml){
		var starttime = 0;
		var duration = 0;
		var thumbnail = "";
		settings.layerdata_related = new Array();
	  	$(xml).find("related").each(function() {
	    	var id = $(this).attr("id");
   			var related = new Array();
		  	$(this).find("starttime").each(function() {
				starttime = $(this).text();
		  	});
		  	$(this).find("duration").each(function() {
				duration = $(this).text();
		   	});
		   	$(this).find("thumbnail").each(function() {
				thumbnail = $(this).text();
		   	});
		   	related[0] = starttime;
		   	related[1] = duration;
		   	related[2] = thumbnail;
		   	settings.layerdata_related.push(related);
	   	});
	}
	
	return self;
}