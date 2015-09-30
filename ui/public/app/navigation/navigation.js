(function () {
  'use strict';

angular.module('myApp')


.controller('NavigationController', ['$scope', '$auth', function($scope, $auth) {
  
  $scope.isAuthenticated = function() {
    return $auth.isAuthenticated();
  };

}]);

})();

