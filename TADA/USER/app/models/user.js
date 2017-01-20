// load the things we need
var restful = require('node-restful');
var mongoose = require('mongoose');
var bcrypt   = require('bcrypt-nodejs');

// define the schema for our user model
var userSchema = mongoose.Schema({
  _id          : { type: String, unique: true },
  token        : String,
  name         : String,
  email        : String,
  picUrl       : String,
  location     : String,
  username   : String,
  skills : {
    type: Array
  }
});
// create the model for users and expose it to our app
module.exports = restful.model('users', userSchema);
