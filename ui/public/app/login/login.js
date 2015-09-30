(function () {
  'use strict';

angular.module('myApp.login', ['ngRoute'])

// Declared route
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/login', {
    templateUrl: 'partials/login.html',
    controller: 'LoginController'
  });
}])

.controller('LoginController', ['$scope', '$http', function($scope, $http) {
  $scope.message = '';

}]);

})();

