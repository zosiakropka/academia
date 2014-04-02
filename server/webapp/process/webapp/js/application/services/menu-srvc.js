'use strict';

var MenuSrvc = Application.Services.factory('MenuSrvc', ["$q", "$rootScope", function($q, $rootScope) { 
		var items = [
			{
				title: "tesciuch",
				controller: SubjectsCtrl,
				tile_partial: ""
			},
			{
				title: "schedule",
				controller: ScheduleCtrl,
				tile_partial: ""
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
