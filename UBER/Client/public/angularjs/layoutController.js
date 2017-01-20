var uberApp = angular.module('uberApp');
uberApp.controller("layoutController",function($scope,$http,$window,$state, uberService){
  
  uberService.setRideHider(true);
  
  $scope.$watch( function () { 
	    return uberService.getRideHider()
	  }, function ( id ) {
	    $scope.createRideHider = id;
	    
	  }); 
  
})
