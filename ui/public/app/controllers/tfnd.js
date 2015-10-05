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

.controller('TFNDController', ['$scope', '$http', 'TFNDFactory', '$alert', function($scope, $http, TFNDFactory, $alert) {

  $scope.submit = function() {
    TFNDFactory.post({"tfn" : "nothing"})
      .then(function(data) {
        $alert({
          content: JSON.stringify(data.data),
          animation: 'fadeZoomFadeDown',
          type: 'material',
          duration: 3
        })
      })
    };

}]);

})();

