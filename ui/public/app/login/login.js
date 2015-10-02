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

.controller('LoginController', ['$scope', '$alert', '$auth', function($scope, $alert, $auth) {

    $scope.submit = function() {
      alert("test")
      $auth.setStorage($scope.rememberMe ? 'localStorage' : 'sessionStorage');
      $auth.login({ email: $scope.email, password: $scope.password, rememberMe: $scope.rememberMe })
        .then(function() {
          $alert({
            content: 'You have successfully signed in',
            animation: 'fadeZoomFadeDown',
            type: 'material',
            duration: 3
          });
        })
        .catch(function(response) {
          console.log(response);
          $alert({
            content: response.data.message,
            animation: 'fadeZoomFadeDown',
            type: 'material',
            duration: 3
          });
        });
    };

}]);

})();

