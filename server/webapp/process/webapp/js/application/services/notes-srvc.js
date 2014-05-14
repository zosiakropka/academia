'use strict';

var NotesSrvc = Application.Services.factory('NotesSrvc', ["$q", "$rootScope", "ApiSrvc", function($q, $rootScope, ApiSrvc) { 
		return {

			/**
			* Retrieves single Note
			* @param {string} id Note identifier
			* @return {Promise} Resolves to JSON array of menu items.
			*/
			get : function(id){
				return ApiSrvc.get("note", {note_id: id});
			},

			/**
			* Retrieves single Note
			* @param {string} id Note identifier
			* @return {Promise} Resolves to JSON array of menu items.
			*/
			recent : function(count){
				return ApiSrvc.list("note", {per_page: count});
			},
	};
}]);
