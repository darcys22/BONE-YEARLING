(function () {
  'use strict';

angular.module('myApp.tfnd', ['ngRoute'])

// Declared route
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/tfnd', {
    templateUrl: 'partials/tfnd.html',
    controller: 'TFNDController'
  });
}])

.controller('TFNDController', ['$scope', '$http', function($scope, $http) {
  $scope.test = 'test';

}]);

})();

