(function () {
  'use strict';

angular.module('myApp.signup', ['ngRoute'])

// Declared route
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/signup', {
    templateUrl: 'partials/signup.html',
    controller: 'SignupController'
  });
}])

.controller('SignupController', ['$scope', '$http', function($scope, $http) {
  $scope.message = "";

}]);

})();

