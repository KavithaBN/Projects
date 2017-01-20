// load all the things we need
var LinkedInStrategy = require('passport-linkedin-oauth2').Strategy;
var LocalStrategy    = require('passport-local').Strategy;
// load up the user model
var User       = require('../app/models/user');
// load the auth variables
var configAuth = require('./auth'); // use this one for testing


module.exports = function(passport) {

  // =========================================================================
  // passport session setup ==================================================
  // =========================================================================
  // required for persistent login sessions
  // passport needs ability to serialize and unserialize users out of session

  // used to serialize the user for the session
  passport.serializeUser(function(user, done) {
    done(null, user.id);
  });

  // used to deserialize the user
  passport.deserializeUser(function(id, done) {
    User.findById(id, function(err, user) {
      done(err, user);
    });
  });


  passport.use('local-login', new LocalStrategy({
      usernameField : 'username',
      passwordField : 'password',
      passReqToCallback : true // allows us to pass in the req from our route (lets us check if a user is logged in or not)
  },
  function(req, username, password, done) {

      // asynchronous
      process.nextTick(function() {
          User.findOne({ 'username' : username }, function(err, user) {
              // if there are any errors, return the error
              if (err)
                  return done(err);

              // if no user is found, return the message
              if (!user)
                  return done(null, false, req.flash('loginMessage', 'No user found.'));

              if (user.password != password)
                  return done(null, false, req.flash('loginMessage', 'Oops! Wrong password.'));

              // all is well, return user
              else
                  return done(null, user);
          });
      });

  }));

  // =========================================================================
  // Linkedin ================================================================
  // =========================================================================

  passport.use(new LinkedInStrategy({
    clientID: configAuth.linkedinAuth.clientID,
    clientSecret: configAuth.linkedinAuth.clientSecret,
    callbackURL: configAuth.linkedinAuth.callbackURL,
    profileFields: ['id', 'first-name', 'last-name', 'email-address','public-profile-url','picture-url','location'],
    state:true
  },
  function(req, token, tokenSecret, profile, done) {

    User.findOne({ '_id': profile.id }, function (err, user) {
      if(err) return done(err);

      if(user) {
        done(null, user);
      } else {
        var newUser = new User();

        newUser._id    = profile.id;
        newUser.username = profile.id;
        newUser.token = tokenSecret.access_token;
        newUser.name  = profile.name.givenName;
        newUser.email = profile.emails[0].value;
        newUser.location = profile._json.location.name;
        newUser.picUrl = profile.photos[0].value;

        // TODO: Adding skills manually!! can be fetched automatically once we have API access

        newUser.skills.push("Java");
        newUser.skills.push("HTML");
        newUser.skills.push("CSS");
        newUser.skills.push("JavaScript");
        newUser.isNew = true;

        newUser.save(function(err) {
          if(err) {
            console.log(err);
            throw err;
          }
          done(null, newUser);
        });
      }
    });
  }
));

};
