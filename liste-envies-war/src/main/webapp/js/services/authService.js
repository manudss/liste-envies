angular
	.module('service', [])
    .service('AuthService', AuthService);

AuthService.$inject = ['$http'];
    function AuthService($http){
        var obj = {
        	user: null
        };
        
        obj.refresh = function() {
	        $http.get('/user')
		        .then(function(data) {
		            obj.user = eval(data);
		        })
	        	.catch(function(error) {
	        		obj.user = null;
	        	});
    	};
        
        obj.isAuthenticated = function() {
        	return obj.user != null;
        };

        obj.isAdmin = function() {
            return obj.user != null ? obj.user.isAdmin : false;
        };
        
        obj.getUser = function() {
        	return obj.user;
        };
        
        return obj;    
    }