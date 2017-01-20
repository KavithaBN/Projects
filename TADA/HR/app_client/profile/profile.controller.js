(function() {

  angular
  .module('meanApp')
  .controller('profileCtrl', profileCtrl);

  profileCtrl.$inject = ['$location','$http','$routeParams','$window'];
  function profileCtrl($location,$http,$routeParams,$window) {
    var vm = this;
    vm.user = {};
    var user = $window.localStorage['username'];

    $http.get('/user').success(function(data) {
      vm.user = data;
      $http.get('/application').success(function(data) {
        vm.application = data;
      });
    });
    $('#jobs').removeClass("active");
    $('#dashboard').addClass("active");
  }

})();
