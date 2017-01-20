(function() {

  angular
  .module('meanApp')
  .controller('jobProfileCtrl', jobProfileCtrl);

  jobProfileCtrl.$inject = ['$location','$routeParams','$http','$scope','$window','$filter'];
  function jobProfileCtrl($location,$routeParams,$http,$scope,$window,$filter) {
    var vm = this;
    var user = $window.localStorage['username'];

    $scope.email = "";

    $http.get('/job/' + $routeParams.id).success(function(data) {
      vm.job = data;
    });

    $http.get('/user').success(function(data) {
      vm.user = data;
    });

    $scope.setFiles = function(element) {
      $scope.$apply(function($scope) {
        // Turn the FileList object into an Array
        $scope.files = []
        for (var i = 0; i < element.files.length; i++) {
          $scope.files.push(element.files[i])
        }
        $scope.progressVisible = false
      });
    }
    vm.onSubmit = function(){

      var splits = vm.user.github.split('https://github.com/', 2);
      var finalConstributers = [];
      $.ajax({
        url: 'https://api.github.com/users/'+ splits[1] + '/repos',
        type: 'GET',
        success: function(data){
          for(var i=0; i<data.length;i++){
            $.ajax({
              url: data[i].contributors_url,
              type: 'GET',
              success: function(contributers){
                var result = $filter('filter')(contributers, {login: splits[1]});
                if(result.length == 1){
                  for(var j=0; j<contributers.length; j++){
                    if(finalConstributers.indexOf(contributers[j].login) == -1){
                      finalConstributers.push(contributers[j].login);
                    }
                  }
                }
              }});
            }
          }});

          var fd = new FormData();
          for (var i in $scope.files) {
            fd.append("uploads", $scope.files[i]);
          }
          setTimeout(function() {
            var application = {
              jobID                : vm.job._id,
              position             : vm.job.position,
              teamID               : vm.job.teamID,
              requiredSkills       : vm.job.skills,
              applicantName        : vm.user.name,
              applicantID          : vm.user._id,
              applicantEmail       : vm.user.email,
              applicantGithubID    : splits[1],
              applicantLocation    : vm.user.location,
              applicantSkills      : vm.user.skills,
              applicationStatus    : "Submitted",
              applicantGithubConnections : finalConstributers,
              applicantGender      : vm.user.gender,
              applicantRace        : vm.user.race
            };
            $.ajax({
              url: '/upload/' + vm.job.teamID,
              type: 'POST',
              data: fd,
              processData: false,
              contentType: false,
              success: function(data){
                if(data == "We already have your application for this position or other position in the same team!"){
                  alert(data);
                }
                else{
                  application.resumeID = data.id;
                  application.fileName = data.fileName;
                  $http.post('/apply', application).success(function(data) {
                    alert(data);
                  });
                }
              }});
            }, 3000);
          }
        }
      })();
