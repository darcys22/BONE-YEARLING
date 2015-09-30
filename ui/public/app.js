'use strict';

var myApp = angular.module('myApp', [
  'ngRoute',
  'satellizer',
  'ui.bootstrap',
  'myApp.login',
  'myApp.signup',
  'myApp.landing'
])

.config(['$routeProvider','$locationProvider', '$httpProvider', '$authProvider', function($routeProvider, $locationProvider, $httpProvider, $authProvider) {
  //$routeProvider.otherwise({
    //redirectTo: '/landing'
  //});

  $httpProvider.interceptors.push(function($q, $injector) {
    return {
      request: function(request) {
        // Add auth token for Silhouette if user is authenticated
        var $auth = $injector.get('$auth');
        if ($auth.isAuthenticated()) {
          request.headers['X-Auth-Token'] = $auth.getToken();
        }

        // Add CSRF token for the Play CSRF filter
        //var cookies = $injector.get('$cookies');
        //var token = cookies.get('PLAY_CSRF_TOKEN');
        //if (token) {
          //// Play looks for a token with the name Csrf-Token
          //// https://www.playframework.com/documentation/2.4.x/ScalaCsrf
          //request.headers['Csrf-Token'] = token;
        //}

        return request;
      },

      responseError: function(rejection) {
        if (rejection.status === 401) {
          var $auth = $injector.get('$auth');
          $auth.logout();
          $injector.get('$state').go('signIn');
        }
        return $q.reject(rejection);
      }
    };
  });


  // Auth config
  $authProvider.httpInterceptor = true; // Add Authorization header to HTTP request
  $authProvider.loginOnSignup = true;
  $authProvider.loginRedirect = '/profile';
  $authProvider.logoutRedirect = '/landing';
  $authProvider.signupRedirect = '/profile';
  $authProvider.loginUrl = '/api/signIn';
  $authProvider.signupUrl = '/api/signUp';
  $authProvider.tokenName = 'token';
  $authProvider.tokenPrefix = 'satellizer'; // Local Storage name prefix
  $authProvider.authHeader = 'X-Auth-Token';
  $authProvider.platform = 'browser';
  $authProvider.storage = 'localStorage';

  $locationProvider.html5Mode(true);
}])

.run(function($rootScope) {

  /**
   * The user data.
   *
   * @type {{}}
   */
  $rootScope.user = {};
});

