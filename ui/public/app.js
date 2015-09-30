'use strict';

var myApp = angular.module('myApp', [
  'ngRoute',
  'ui.bootstrap',
  'myApp.login',
  'myApp.signup',
  'myApp.landing'
])

.config(['$routeProvider','$locationProvider', function($routeProvider, $locationProvider) {
  //$routeProvider.otherwise({
    //redirectTo: '/landing'
  //});
  $locationProvider.html5Mode(true);
}]);
