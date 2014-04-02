'use strict';

MenuCtrl = Application.Controllers.controller('MenuCtrl', ['$scope', "MenuSrvc", function($scope, MenuSrvc){
	var context = $scope.context;
	$scope.items = MenuSrvc.get();
}]);

console.log(MenuCtrl.controller);
