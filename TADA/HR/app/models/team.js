// load the things we need
var restful = require('node-restful');
var mongoose = require('mongoose');
var bcrypt   = require('bcrypt-nodejs');

var teamSchema = mongoose.Schema({
  teamID        : String,
  positions : {
    type: Array
  }
});

module.exports = restful.model('Team', teamSchema);
