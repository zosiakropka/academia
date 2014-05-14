'use strict';

var ApiSrvc = Application.Services.factory('ApiSrvc', ["$q", "$rootScope", "$http", "$timeout", function($q, $rootScope, $http, $timeout) { 

		function parse_params(params) {
			var query = "?";
			for (var key in params) {
				query += (key + "=" + params[key] + "&");
			}
			return query;
		}

		function combine(resource, action, params) {
			return "api/" + resource + "/" + action + "/" + parse_params(params);
		}

		function request(resource, action, params, method) {
			var path = combine (resource, action, params);
			var deferred = $q.defer();
			$http[method](path).
			  success(function(data, status, headers, config) {
				deferred.resolve(data);
			  }).
			  error(function(data, status, headers, config) {				  
			    console.error(data, status, headers, config);
			    deferred.reject(data);
			  });
			return deferred.promise;
		}

		function get(resource, params) {
			return request(resource, "get", params, 'get');
		}
		function list(resource, params) {
			return request(resource, "list", params, 'get');
		}
		function create(resource, params) {
			return request(resource, "create", params, 'post');
		}
		function remove(resource, params) {
			return request(resource, "remove", params, 'post');
		}
		function update(resource, params) {
			return request(resource, "update", params, 'post');
		}

		return {
			get : get,
			list : list,
			create : create,
			remove : remove,
			update : update,
	};
}]);
