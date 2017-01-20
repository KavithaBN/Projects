// load the things we need
var restful = require('node-restful');
var mongoose = require('mongoose');
var bcrypt   = require('bcrypt-nodejs');

// define the schema for our job model
var jobSchema = mongoose.Schema({
  teamID        : String,
  position      : String,
  description   : String,
  skills : {
    type: Array
  },
  salary        : String,
  location      : String
});

// create the model for job and expose it to our app
module.exports = restful.model('Jobs', jobSchema);
