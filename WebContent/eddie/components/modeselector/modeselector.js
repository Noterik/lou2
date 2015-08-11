var Modeselector = function(options){
	var self = {};
	var settings = {
		modeactive: null,
		inanim: null
	};
	
	$.extend(settings, options);
	
	self.swipeRight = function(event){
		if(settings.inanim == true) {
			return;
		}
	
		if(settings.modeactive == 'related'){
			settings.inanim = true;
			$('#controller').css("left","-1024px");
			$('#relatedview').animate({left:'1024px'},"slow");	
			$('#controller').animate({left:'25px'},"slow",function(){ 
				animDone('controller'); 
			});	
		} else if (settings.modeactive=='controller'){
			$('#controller').css("left","25px");
			$('#overview').css("left","-1024px");
			$('#overview').css("visibility","visible");
			$('#overview').animate({left:'25px'},"slow");	
			$('#controller').animate({left: '1024px'}, "slow", function(){ 
				animDone('overview'); 
			});
		}
	}
	
	self.swipeLeft = function(event){
		if(settings.modeactive == 'controller'){
			$('#controller').animate({left: '-1024px'}, "slow");	
			$('#relatedview').css("left", "1024px");
			$('#relatedview').css("visibility", "visible");
			$('#relatedview').animate({left:'0px'}, "slow", function(){ 
				animDone('related'); 
			});
			components.relatedview.drawOverview();
		} else if (window.modeactive=='overview') {
			$('#overview').animate({left:'-1024px'},"slow");    
			$('#controller').animate({left:'25px'},"slow",function(){
				animDone('controller'); 
			});
		}
	}
	
	var animDone = function(newmode){
		settings.inanim = false;
		settings.modeactive = newmode;
	}
	
	/*
	$('#modeselector_gesture_catcher').css('height', $(window).height() + 'px');
	$('#modeselector_gesture_catcher').css('position', 'fixed');
	$('#modeselector_gesture_catcher').css('left', 0);
	$('#modeselector_gesture_catcher').css('top', 0);
	*/
	
	$('#modeselector').hammer({
		prevent_default: true,
		drag_vertical: true,
		drag_min_distance: 20
	}).bind('swipe', function(event){
		if(event.direction=='left'){
			self.swipeLeft(event);
		}else if(event.direction=='right'){
			self.swipeRight(event);
		}
	});
	settings.modeactive = 'controller';
	settings.inanim = false;
	
	return self;
}