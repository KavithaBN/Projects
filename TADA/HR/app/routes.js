var express = require('express');
var router = express.Router();
var restful = require('node-restful');
var formidable = require('formidable');
var fs = require('fs');

var Jobs       = require('../app/models/jobs');
var User       = require('./models/user');
var Application       = require('./models/application');
var Team       = require('./models/team');


Jobs.methods(['get', 'put', 'post', 'delete']);
Jobs.register(router, '/job');

Application.methods(['get', 'put', 'post', 'delete']);
Application.register(router, '/application');

User.methods(['get', 'put', 'post', 'delete']);
User.register(router, '/users');

router.post('/team', function(req, res){
	Team.findOne({'teamID': req.body.teamID},function(err, team) {
		if(team){
			Team.update({'teamID': req.body.teamID}, { $addToSet: {positions: req.body.position } }, function(err){
				if(err) {
					console.log(err);
					throw err;
				}
				res.send("Team is updated!");
			});
		}
		else{
			var team = new Team();

			team.teamID = req.body.teamID;
			team.positions = req.body.position;
			team.save(function(err) {
				if(err) {
					console.log(err);
					throw err;
				}
				console.log('New Application');
				res.send("Team is saved!");
			});

		}
	});
});

router.get('/recom/:id/:locS/:gitS/:divS/:gdivS', function(req, res){
	var gitS = req.params.gitS;
	var locS = req.params.locS;
	var divS = req.params.divS;
	var gdivS = req.params.gdivS;

	Team.findOne({"teamID": req.params.id },function(err,team){

		var allArrays = [];
		var finlResult = [];
		var numberOfApplications = [];
		for(var i = 0; i < team.positions.length; i++){
			var pos = [];

			Application.find({"position":team.positions[i],"teamID": req.params.id,"skillScore":1},function(err,applications){
				pos = applications;
				allArrays.push(pos);
				numberOfApplications.push(pos.length);
			});
		}
		setTimeout(function(){
			allArrays.push([{}]);
			allArrays.push([{}]);
			var flag = false;

			for(var j in numberOfApplications){
				if(numberOfApplications[j] < 2){
					flag = true;
				}
			}

			if(flag){
				res.send("Not enough information!");
			}
			else{
				var results = allPossibleCases(allArrays);

				function allPossibleCases(arr) {
					if (arr.length == 1) {
						return arr[0];
					} else {
						var result = [];
						var allCasesOfRest = allPossibleCases(arr.slice(1));  // recur with the rest of array
						for (var i = 0; i < allCasesOfRest.length; i++) {
							for (var j = 0; j < arr[0].length; j++) {
								result.push('/' + JSON.stringify(arr[0][j]) + allCasesOfRest[i]);
							}
						}
						return result;
					}
				}

				for (var i=0 ; i< results.length; i ++){
					var string = results[i].split('/');
					var final = string.slice(1,string.length - 1);
					rankTeamBasedOnUserDetails(final);
				}

				function rankTeamBasedOnUserDetails(arr){
					var locationWeight = 0;
					var gitWeight = 0;
					var diversityWeight = 0;
					var genderDiversityWeight = 0;

					for(var i = 0; i< arr.length; i++){
						for (var j = i + 1; j < arr.length; j++){
							var obj1 = JSON.parse(arr[i]);
							var obj2 = JSON.parse(arr[j]);
							if(obj1.applicantLocation == obj2.applicantLocation){
								locationWeight = locationWeight + parseInt(locS) ;
							}
							if(obj2.applicantGithubConnections.indexOf(obj1.applicantGithubID) >= 0){
								gitWeight = gitWeight + parseInt(gitS);
							}
						}
					}
					var map = new Object();
					var gmap = new Object();

					for (var h = 0; h< arr.length; h++){
						var object = JSON.parse(arr[h]);
						if(map[object.applicantRace]){
							map[object.applicantRace]++;
						}
						else{
							map[object.applicantRace] = 1;
						}
						if(gmap[object.applicantGender]){
							gmap[object.applicantGender]++;
						}
						else{
							gmap[object.applicantGender] = 1;
						}
					}
					for(var val in map){
						diversityWeight = diversityWeight + parseInt(divS);
					}

					for(var val in gmap){
						genderDiversityWeight = genderDiversityWeight + parseInt(gdivS);
					}

					var loc = "{\"locationWeight\":"+ locationWeight +"}";
					var git = "{\"gitWeight\":"+ gitWeight +"}";
					var diversity = "{\"diversityWeight\":"+ diversityWeight +"}";
					var gdiversity = "{\"genderDiversityWeight\":"+ genderDiversityWeight +"}";
					var totalWeight = locationWeight + gitWeight + diversityWeight + genderDiversityWeight;
					var total = "{\"totalWeight\":"+ totalWeight +"}";

					arr.push(loc);
					arr.push(git);
					arr.push(total);
					var jsonObj = [];
					for(var i = 0; i< arr.length; i++){
						jsonObj.push(JSON.parse(arr[i]));
					}
					finlResult.push(jsonObj);
				}
				finlResult.sort(sortResult(finlResult[0].length,'totalWeight'));

				var teamsData = { "name" : req.params.id, "children": []};
				for(var i=0; i< finlResult.length/3; i++){
					var membesData = { "name" : "Team Weight: " +finlResult[i][finlResult[i].length - 1].totalWeight, "children": []};
					for(var j=0; j< finlResult[i].length - 3; j++){
						var membesData2 = { "name" : finlResult[i][j].applicantName, "children": []};
						membesData2.children.push({"name": finlResult[i][j].position});
						membesData2.children.push({"name": finlResult[i][j].applicantLocation});
						membesData2.children.push({"name": "skills", "children": []});
						for(var k=0; k< finlResult[i][j].applicantSkills.length;k++){
							membesData2.children[2].children.push({"name":finlResult[i][j].applicantSkills[k]});
						}
						membesData.children.push(membesData2);
					}
					teamsData.children.push(membesData);
				}
				res.send(teamsData);
			}
		},3000);

	});
});

function sortResult(len,prop){
	return function(a,b){
		if( a[len - 1][prop] > b[len - 1][prop]){
			return -1;
		}else if( a[len - 1][prop] < b[len - 1][prop] ){
			return 1;
		}
		return 0;
	}
}

router.get('/user', function(req, res) {
	User.findOne({'_id': req.user._id },function(err, userProfile) {
		res.send(userProfile);
	});
});

module.exports = router;
