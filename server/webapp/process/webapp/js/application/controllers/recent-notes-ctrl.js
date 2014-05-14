var RecentNotesCtrl = Application.Controllers.controller('RecentNotesCtrl', ['$scope', 'ApplySrvc', 'NotesSrvc', function($scope, ApplySrvc, NotesSrvc){
	var recent_notes_promise = NotesSrvc.recent(10);
	recent_notes_promise.then(function(recent_notes) {
		ApplySrvc.apply(function() {
			$scope.recent_notes = recent_notes;
		});
	});
}]);
