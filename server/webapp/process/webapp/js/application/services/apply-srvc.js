'use strict';

var ApplySrvc = Application.Services.factory('ApplySrvc', ["$rootScope", function($rootScope) { 

		return {
			apply: function(callback) {
				var phase = $rootScope.$$phase;
				if(phase == '$apply' || phase == '$digest') {
					if(callback && (typeof(callback) === 'function')) {
						callback();
					}
				} else {
					$rootScope.$apply(callback);
				}
			}
		};
}]);
