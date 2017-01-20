// load the things we need
var restful = require('node-restful');
var mongoose = require('mongoose');
var bcrypt   = require('bcrypt-nodejs');

// define the schema for our application model
var appSchema = mongoose.Schema({
  jobID                : String,
  teamID               : String,
  position             : String,
  applicantName        : String,
  applicantID          : String,
  applicantGithubID    : String,
  applicantEmail       : String,
  applicantLocation    : String,
  applicationStatus    : String,
  resumeID             : String,
  skillScore           : Number,
  applicantSkills : {
    type: Array
  },
  applicantGithubConnections : {
    type: Array
  },
  applicantGender     : String,
  applicantRace       : String
});
// create the model for application and expose it to our app
module.exports = restful.model('application', appSchema);
