var MenuCtrl = Application.Controllers.controller('MenuCtrl', ['$scope', "MenuSrvc", function($scope, MenuSrvc){
	var context = $scope.context;
	$scope.items = MenuSrvc.get();

	$scope.open = function(item) {
		console.log(item);
	};
}]);
