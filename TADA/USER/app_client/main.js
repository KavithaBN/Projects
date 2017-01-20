(function () {

  angular.module('meanApp', ['ngRoute']);

  function config ($routeProvider, $locationProvider) {
    $routeProvider
    .when('/', {
      templateUrl: '/profile/profile.view.html',
      controller: 'profileCtrl',
      controllerAs: 'vm'
    })
    .when('/profile', {
      templateUrl: '/profile/profile.view.html',
      controller: 'profileCtrl',
      controllerAs: 'vm'
    })
    .when('/jobs', {
      templateUrl: '/jobs/jobs.view.html',
      controller: 'jobsCtrl',
      controllerAs: 'vm'
    })
    .when('/jobProfile/:id', {
      templateUrl: '/jobProfile/jobProfile.view.html',
      controller: 'jobProfileCtrl',
      controllerAs: 'vm'
    })

    .otherwise({redirectTo: '/'});

    // use the HTML5 History API
    $locationProvider.html5Mode(true);
  }

  angular
  .module('meanApp')
  .config(['$routeProvider', '$locationProvider', config])

})();
