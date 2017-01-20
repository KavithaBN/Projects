(function() {

  angular
  .module('meanApp')
  .controller('postJobCtrl', postJobCtrl);

  postJobCtrl.$inject = ['$location','$routeParams','$http','$scope','$window'];
  function postJobCtrl($location,$routeParams,$http,$scope,$window) {
    var vm = this;

    $scope.team = "";
    $scope.position = "";
    $scope.description = "";
    $scope.location = "";
    $scope.salary = "";
    $scope.skills = "";

    vm.onSubmit = function(){
      var job = {
        teamID               : $scope.team,
        position             : $scope.position,
        description          : $scope.description,
        salary               : $scope.salary,
        location             : $scope.location,
        skills               : $("#tags2").tagsinput('items')
      };

      var team = {
        teamID               : $scope.team,
        position            : $scope.position
      };

      $http.post('/job', job).success(function(data) {
        alert("Job Posted!");
      });
      $http.post('/team', team).success(function(data) {
        console.log("Team stored!");
      });
    }
  }
})();
