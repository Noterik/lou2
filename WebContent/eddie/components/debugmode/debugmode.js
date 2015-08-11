function Debugmode(options){
	var self = {};
	var settings = {};
	$.extend(settings, options);
	
	$('#debugmode_form').submit(function(e) {
		e.preventDefault();
		e.stopPropagation();
		eddie.putLou($('#debugmode_formtarget').val(), $('#debugmode_formvalue').val());
		return false;
	})

	return self;
}