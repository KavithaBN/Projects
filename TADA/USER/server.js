// server.js

// set up ======================================================================
// get all the tools we need
var express = require('express'),
cookieParser = require('cookie-parser'),
expressSession = require('express-session'),
bodyParser = require('body-parser'),
app = express();

var port     = process.env.PORT || 8081;
var mongoose = require('mongoose');
var passport = require('passport');
var flash    = require('connect-flash');
var path = require('path');


var Jobs = require('./app/models/jobs');
var User = require('./app/models/user');
var Application = require('./app/models/application');
var configDB = require('./config/database.js');

// configuration ===============================================================
mongoose.connect(configDB.url); // connect to our database

require('./config/passport')(passport); // pass passport for configuration

var routesApi = require('./app/routes.js');

// set up our express application
// app.use(express.logger('dev')); // log every request to the console
app.use(cookieParser()); // read cookies (needed for auth)
app.use(bodyParser.urlencoded({ extended: true}));
app.use(bodyParser({defer: true}));
app.use(express.static(path.join(__dirname, 'public')));
// // [SH] Set the app_client folder to serve static resources
app.use(express.static(path.join(__dirname, 'app_client')));
app.set('view engine', 'ejs');
// app.set('view engine', 'jade'); // set up ejs for templating
// required for passport
app.use(expressSession({ secret: 'ilovescotchscotchyscotchscotch' })); // session secret
app.use(passport.initialize());
app.use(passport.session()); // persistent login sessions
app.use(flash()); // use connect-flash for flash messages stored in session
app.use('/', routesApi);

// routes ======================================================================

// show the home page (will also have our login links)
app.get('/', function(req, res) {
  res.render('./app_client/index.html');
});

// PROFILE SECTION =========================
app.get('/profile', isLoggedIn, function(req, res) {
  var user = { name: req.user.name, email : req.user.email, picUrl: req.user.picUrl, _id : req.user._id };
  res.render('../app_client/main.ejs', user);
});

app.get('/jobs', isLoggedIn, function(req, res) {
  var user = { name: req.user.name, email : req.user.email, picUrl: req.user.picUrl, _id : req.user._id};
  res.render('../app_client/main.ejs',user);
});

app.get('/jobProfile/:id', isLoggedIn, function(req, res) {
  var user = { name: req.user.name, email : req.user.email, picUrl: req.user.picUrl, _id : req.user._id};
  res.sendfile('../app_client/main.ejs');
});

// LOGOUT ==============================
app.get('/logout', function(req, res) {
  req.logout();
  res.redirect('/');
});

// linkedin =====================================

// send to linkedin to do the authentication
app.get('/auth/linkedin', passport.authenticate('linkedin',{ scope: ['r_basicprofile', 'r_emailaddress'] }));

// handle the callback after linkedin has authenticated the user
app.get('/auth/linkedin/callback',
passport.authenticate('linkedin', {
  successRedirect : '/profile',
  failureRedirect : '/'
}));

// route middleware to ensure user is logged in
function isLoggedIn(req, res, next) {
  if (req.isAuthenticated())
  return next();
  res.redirect('/');
}

// launch ======================================================================
app.listen(port);
console.log('The magic happens on port ' + port);

module.exports = app;
