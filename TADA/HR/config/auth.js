// config/auth.js

// expose our config directly to our application using module.exports
module.exports = {
	'linkedinAuth' : {
		'clientID' 		: '78umi3azfouqit', // your App ID
		'clientSecret' 	: 'xol3PbKwp9kFivJF', // your App Secret
		'callbackURL' 	: 'http://localhost:8080/auth/linkedin/callback'
	}
};
