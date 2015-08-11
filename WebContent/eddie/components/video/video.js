var Video = function(options){
	var self = {};
	var settings = {
		qrcode: false,
		myPlayer: document.getElementById("example_video_1"),
		video: null,
		src: null,
		duration: null
	};
	$.extend(settings, options);
	
	self.putMsg = function(msg){
		var myPlayer = settings.myPlayer;
		try{
			var command = [msg.target[0].class];
		}catch(e){
			command = $(msg.currentTarget).attr('class').split(" ");
		}
		var content = msg.content;
		for(i=0;i<command.length;i++){
			switch(command[i]) { 
				case 'play':
					myPlayer.volume=1;
	                myPlayer.playbackRate=1;
	                myPlayer.play();
					eddie.putLou('notification','show(play)');
	  				break;
				case 'pause':
	                myPlayer.pause();
					eddie.putLou('notification','show(pause)');
	  				break;
				case 'seek':
					var time = eval(content);
					if(time > 1){
						time = time - 1;
					}
	            	myPlayer.currentTime = time;
					eddie.putLou('notification','show(seek ' + Math.floor(time) + ')');
	  				break;
				case 'src':
					settings.src = content.replace("src(", "").replace(")", "");
					settings.video = settings.src.substring(settings.src.indexOf('/domain'), settings.src.indexOf('/rawvideo/'));
					eddie.putLou('controller', 'videosrc(' + settings.video + ')');
					eddie.putLou('notification','show(' + settings.video + ')');
					document.getElementById("src1").setAttribute("src", settings.src);
					document.getElementById("src1").setAttribute("type", "video/mp4");
					document.getElementById("src2").setAttribute("src", settings.src.replace(".mp4", ".webm"));
					document.getElementById("src2").setAttribute("type", "video/webm");
					document.getElementById("src3").setAttribute("src", settings.src.replace(".mp4", ".ogv"));
					document.getElementById("src3").setAttribute("type", "video/ogg");
					myPlayer.load();
					myPlayer.volume=1;
	                myPlayer.playbackRate=1;
					myPlayer.play();
	  				break;
				case 'mute':
					myPlayer.volume=0;
					break;
				case 'volumeup':
					myPlayer.volume+=0.1;
					break;
	            case 'volumedown':
	                    myPlayer.volume-=0.1;
	                    break;
	            case 'volume':
	                    myPlayer.volume=eval(contemt);
	                    break;
				case 'speed':
					myPlayer.volume=0;
					if (content=="" || content=="1") {
						content = "1";
						myPlayer.volume=1;
					}
	            	myPlayer.playbackRate=eval(content);
	            	break;
				case 'request_videosrc':
					eddie.putLou('controller','videosrc(' + settings.video + ')');
					break;
				case 'qrcode':
					console.log('toggle QR');
					toggleQRCode()
					break;
				case 'notify':
					break;
				case 'buttonClicked':
					console.log('buttonClicked');
					handleButtonClick(content);
					break;
				default:
					console.log('unhandled msg in video.html : '+command[i]); 
			}
		}
	}
	
	var toggleQRCode = function(){
		if (settings.qrcode != 'true') {
			eddie.putLou('qrcode','visible(true)'); 
			settings.qrcode='true';
		} else {
			eddie.putLou('qrcode','visible(false)'); 
			settings.qrcode='false';
		}
	}

	handleButtonClick = function(content){
			console.log("action: " + content);
			switch (content){
				case '1': 
					eddie.putLou("video", "url(avro)");
					break;
				case '2':
					eddie.putLou("video", "url(rbb)");
					break;
				case '3':
					eddie.putLou("video", "url(t1)");
					break;
				case '4':
					eddie.putLou("video", "url(t2)");
					break;
				case '5':
					eddie.putLou("video", "url(remix)");
				case 'pause':
					eddie.putLou("video", "pause()");
					break;
				case 'play':
					eddie.putLou("video", "play()");
					break;
				case 'stop':
					eddie.putLou("video", "pause()");
					eddie.putLou("video", "seek(0)")
					break;
				case 'volumeup':
					eddie.putLou("video", "volumeup()");
					break;
				case 'volumedown':
					eddie.putLou("video", "volumedown()");
					break;
				case 'qrcode':
					eddie.putLou("video", "qrcode(toggle)");
					break;
				case 'reverse':
					eddie.putLou("video", "speed(-0.5)");
					break;
				case 'forward':
					eddie.putLou("video", "speed(2)");
					break;
				case 'eject':
					eddie.putLou("video", "qrcode(toggle)");
					break;
		}
	}
	
	setInterval(function(){
		if(settings.myPlayer.duration != settings.duration){
			settings.duration = settings.myPlayer.duration;
		}
		eddie.putLou('controller', 'timeupdate(' + Math.floor(settings.myPlayer.currentTime) + ':' + Math.floor(settings.duration)+')');
	}, 1000);
	
	eddie.putLou('controller', 'videosrc(' + settings.video + ')');
	
	return self;
}