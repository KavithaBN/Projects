var express = require('express');
var router = express.Router();
var restful = require('node-restful');
var formidable = require('formidable');
var fs = require('fs');
var path = require('path');
var extract = require('pdf-text-extract');

var Jobs = require('../app/models/jobs');
var User = require('./models/user');
var Application = require('./models/application');

var gridfs = require('mongoose-gridfs')({
	collection:'attachments',
	model:'Attachment'
});
Attachment = gridfs.model;

Jobs.methods(['get', 'put', 'post', 'delete']);
Jobs.register(router, '/job');

Application.methods(['get', 'put', 'post', 'delete']);
Application.register(router, '/application');

router.post('/apply', function(req, res) {
	var application = new Application();
	application.jobID = req.body.jobID;
	application.position = req.body.position;
	application.teamID = req.body.teamID;
	application.applicantName = req.body.applicantName;
	application.applicantID = req.body.applicantID;
	application.applicantGithubID = req.body.applicantGithubID;
	application.applicantEmail = req.body.applicantEmail;
	application.applicantLocation = req.body.applicantLocation;

	application.applicationStatus = req.body.applicationStatus;
	application.resumeID					= req.body.resumeID;
	application.applicantGithubConnections			= req.body.applicantGithubConnections;
	application.applicantGender	 	= req.body.applicantGender;
	application.applicantRace			= req.body.applicantRace;

	var filePath = path.join(__dirname, '../uploads/'+req.body.fileName);

	extract(filePath, { splitPages: false }, function (err, text) {
		if (err) {
			console.dir(err)
			return
		}
		var skillScore = 0;
		var applicantSkills = [];
		var totalSkills = req.body.requiredSkills.length;
		for(var i in req.body.requiredSkills){
			if(text.toString().toLowerCase().includes(req.body.requiredSkills[i].toLowerCase())){
				applicantSkills.push(req.body.requiredSkills[i]);
				skillScore++;
			}
		}
		application.applicantSkills = applicantSkills;
		application.skillScore = skillScore/totalSkills;
		application.save(function(err) {
			if(err) {
				console.log(err);
				throw err;
			}
			console.log('New Application: ' + application + ' created!');
			res.send("Your application is submited!");
		});
	});
});

router.post('/upload/:id', function(req,res){
	// create an incoming form object
	Application.findOne({'teamID': req.params.id, 'applicantID' : req.user.username },function(err, application) {
		if(application){
			res.send("We already have your application for this position or other position in the same team!");
		}
		else{
			var form = new formidable.IncomingForm();
			var fileName = "";
			var applicant = req.user.username;
			var job = req.params.id;

			// specify that we want to allow the user to upload multiple files in a single request
			form.multiples = true;

			// store all uploads in the /uploads directory
			form.uploadDir = path.join(__dirname, '../uploads');

			// every time a file has been uploaded successfully,
			// rename it to it's orignal name
			form.on('file', function(field, file) {
				fs.rename(file.path, path.join(form.uploadDir, file.name));
				fileName = file.name;
			});

			// log any errors that occur
			form.on('error', function(err) {
				console.log('An error has occured: \n' + err);
			});

			// once all the files have been uploaded, send a response to the client and store reusme in mongo grid
			form.on('end', function() {
				Attachment.write({
					filename:fileName,
					contentType:'application/pdf',
					metadata: {"applicantID" : applicant, "jobID": job }
				},
				fs.createReadStream(path.join(__dirname, '../uploads/'+fileName)),
				function(error, savedAttachment){
					res.send({"id": savedAttachment._id,"fileName": fileName});
				});
			});
			// parse the incoming request containing the form data
			form.parse(req);
		}
	});

});

router.get('/user', function(req, res) {
	User.findOne({'_id': req.user._id },function(err, userProfile) {
		res.send(userProfile);
	});
});

module.exports = router;
