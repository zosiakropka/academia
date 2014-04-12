'use strict';

var MenuSrvc = Application.Services.factory('MenuSrvc', ["$q", "$rootScope", function($q, $rootScope) { 
	var items = [
		{
			partial: "miniatures/subjects",
			slug: "subjects"
		},
		{
			partial: "miniatures/schedule",
			slug: "schedule"
		},
		{
			partial: "miniatures/recent-notes",
			slug: "recent-notes"
		},
	];
	return {

		/**
		* Retrieves menu Items
		* @return {Object} Array of menu items.
		*/
		get : function(){
			return items;
		},
	};
}]);
