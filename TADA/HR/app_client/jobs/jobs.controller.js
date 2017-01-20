(function() {

  angular
  .module('meanApp')
  .controller('jobsCtrl', jobsCtrl);

  jobsCtrl.$inject = ['$location', '$http'];
  function jobsCtrl($location, $http) {
    var vm = this;
    $('#dashboard').removeClass("active");
    $('#jobs').addClass("active");
    $http.get('/job').success(function(data) {
      vm.jobs = data;
    });
  }
})();
