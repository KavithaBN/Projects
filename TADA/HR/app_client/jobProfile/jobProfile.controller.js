(function() {

  angular
  .module('meanApp')
  .controller('jobProfileCtrl', jobProfileCtrl);

  jobProfileCtrl.$inject = ['$location','$routeParams','$http','$scope','$window'];
  function jobProfileCtrl($location,$routeParams,$http,$scope,$window) {
    var vm = this;
    $http.get('/job/' + $routeParams.id).success(function(data) {
      vm.job = data;
    });
  }
})();
