'use strict';

var SubjectsCtrl = Application.Controllers.controller('SubjectsCtrl', ['$scope', 'SubjectsSrvc', function($scope, SubjectsSrvc){
	var subjects_promise = SubjectsSrvc.get();
	subjects_promise.then(function(subjects) {
		$scope.subjects = subjects;
	});
}]);
