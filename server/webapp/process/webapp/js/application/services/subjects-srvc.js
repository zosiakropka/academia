'use strict';

var SubjectsSrvc = Application.Services.factory('SubjectsSrvc', ["$q", "$rootScope", "ApiSrvc", function($q, $rootScope, ApiSrvc) { 

		return {

			get : function(id){
				return ApiSrvc.get("subject", {subject_id: id});
			},
			list : function(id){
				return ApiSrvc.list("subject");
			},

	};
}]);
