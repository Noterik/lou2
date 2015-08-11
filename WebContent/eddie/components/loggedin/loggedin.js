var Loggedin = function(options){
        var self = {};
        var settings = {
        }

        jQuery('#login').html('Logged in as : '+components.login.getLoginName());

        return self;
}