'use strict';

SubjectsCtrl = Application.Controllers.controller('SubjectsCtrl', ['$scope', 'SubjectsSrvc', function($scope, SubjectsSrvc){
	$scope.subjects = SubjectsSrvc.get();
	alert($scope.subjects);
}]);
