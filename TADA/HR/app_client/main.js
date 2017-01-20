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
    .when('/teams', {
      templateUrl: '/teams/teams.view.html',
      controller: 'teamsCtrl',
      controllerAs: 'vm'
    })
    .when('/jobProfile/:id', {
      templateUrl: '/jobProfile/jobProfile.view.html',
      controller: 'jobProfileCtrl',
      controllerAs: 'vm'
    })
    .when('/postJob', {
      templateUrl: '/postJob/postJob.view.html',
      controller: 'postJobCtrl',
      controllerAs: 'vm'
    })
    .when('/candidateProfiles', {
      templateUrl: '/candidateProfiles/candidateProfiles.view.html',
      controller: 'candidateProfilesCtrl',
      controllerAs: 'vm'
    })

    .otherwise({redirectTo: '/'});

    $locationProvider.html5Mode(true);
  }

  angular
  .module('meanApp')
  .config(['$routeProvider', '$locationProvider', config])

})();
