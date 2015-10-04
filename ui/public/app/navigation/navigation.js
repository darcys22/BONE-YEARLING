(function () {
  'use strict';

angular.module('myApp')


.controller('NavigationController', ['$rootScope', '$scope', '$auth', 'UserFactory', function($rootScope, $scope, $auth, UserFactory) {

  //UserFactory.get()
      //.success(function(data) {
        //$rootScope.user = data;
      //})

  $scope.name = "BusinessName"
  $scope.isAuthenticated = function() {
    return $auth.isAuthenticated();
  };

  $scope.logout = function() {
    $auth.logout();
  }

}]);

})();

