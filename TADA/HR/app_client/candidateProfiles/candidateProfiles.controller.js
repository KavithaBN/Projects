(function() {

  angular
  .module('meanApp')
  .controller('candidateProfilesCtrl', candidateProfilesCtrl);

  candidateProfilesCtrl.$inject = ['$location','$routeParams','$http','$scope','$window'];
  function candidateProfilesCtrl($location,$routeParams,$http,$scope,$window) {
    var vm = this;
    $http.get('/users').success(function(data) {
      vm.profiles = data;
      console.log(vm);
    });
  }
})();
