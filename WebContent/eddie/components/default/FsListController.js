var FsListController = function(options) {}; // needed for detection



FsListController.update = function(vars, data){
	// get out targetid from our local vars
	var tid = vars.get('targetid'); 
	var mouseovercss = vars.get("controller/mouseovercss");
	console.log("MO="+mouseovercss);
	
	// render the new html using mustache and the data from the server and show it
	var parsed = Mustache.render(vars.get("template"),data);
    $('#'+tid).html(parsed);
    
     // send the selected action to our server part
     $('.item_'+tid).bind('mouseup',function(event){
     	var type = event.target.id;
   		var pos = type.lastIndexOf("_");
   		if (pos!=-1) {
   			var item = type.substring(pos+1);
   			var map = {};
   			map['itemid'] = item;
   			eddie.sendEvent(tid,"itemselected",map);
   		}
     });
    
    
    if (mouseovercss!=null) { 
     	// add the mouseover class when in target area
     	$('.item_'+tid).bind('mouseover',function(event){
      		$(event.target).addClass(mouseovercss);
     	});
     
     	// remove the mouseover class when not in target area
     	$('.item_'+tid).bind('mouseout',function(event){
        	$(event.target).removeClass(mouseovercss);
     	});
     }


};