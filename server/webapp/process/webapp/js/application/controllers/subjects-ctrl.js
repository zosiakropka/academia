'use strict';

var SubjectsCtrl = Application.Controllers.controller('SubjectsCtrl', ['$scope', 'ApplySrvc', 'SubjectsSrvc', function($scope, ApplySrvc, SubjectsSrvc){
	var subjects_promise = SubjectsSrvc.list();
	subjects_promise.then(function(subjects) {
		ApplySrvc.apply(function () {
			$scope.subjects = subjects;
		});
	});
}]);
