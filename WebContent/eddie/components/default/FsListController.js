var FsListController = function(options) {}; // needed for detection



FsListController.update = function(vars, data){
	// get out targetid from our local vars
	var tid = data["targetid"]; 
	var mouseovercss = vars["controller/mouseovercss"];

	// render the new html using mustache and the data from the server and show it
	var parsed = Mustache.render(vars["template"],data);

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
     
     // add the mouseover class when in target area
     $('#item_'+tid+'_addbutton').bind('mouseup',function(event){
                var map = {};
                eddie.sendEvent(tid,"itemadd",map);
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