customerDAO = require('../db_services/customerDAO');

exports.handle_request = function(message, callback)	{
	if(message.reqType === "getCustomerProfile"){ 
		console.log("Received message");
		getCustomerProfile(message, callback);
	}
	if(message.reqType === "updateCustomerDetails"){ 
		updateCustomerDetails(message, callback);
	}
	if(message.reqType == "getRideStatus"){
		getRideStatus(message,callback);
	}
};


function getCustomerProfile(message, callback){
	console.log("In server getCustomerProfile");
	var ssn=message.ssn;
	var res={};
	console.log(ssn);
	customerDAO.getCustomerProfileDetails(ssn,function(results) {
		if(results){
			res.code=200;
			res.value=results;
			callback(res);
		}
		else{
			res.code=401;
			res.value="No records found";
			callback(res);
		}
	});
}
function updateCustomerDetails(message, callback){
  	console.log("In server Update Profile");
	var ssn=message.ssn;
	var firstName=message.data[0];
	var lastName=message.data[1];
	var address=message.data[2];
	var city=message.data[3];
	var state=message.data[4];
	var zip_primary=message.data[5];
	var zip_secondary=message.data[6];
	var phone=message.data[7];
	var email=message.data[8];
	var res={};
	customerDAO.getCustomerDetails('',phone, function(results){
		if(results){
			if(phone==results[0].PHONE_NUMBER){
				res.code = "401";
				res.value="Phone exists";
				callback(res);
			}
		}
		else{
			customerDAO.getCustomerCredentialsDetails(email, function(results) {
				if(results){
					if(email==results[0].EMAIL){
					res.code = "401";
					res.value="Email exists";
					callback(res);
					}
				}
				else{
					customerDAO.updateCustomerDetails(ssn,firstName,lastName,address,city,state,zip_primary,zip_secondary,phone,email,function(results) {
						if(results){
							res.code=200;
							res.value="Successfully Updated";
							callback(res);
						}
						else{
							res.code=401;
							res.value="No records found";
							callback(res);
						}
					});
				}
			})
		}
	});
}

function getRideStatus(message,callback){
	var ssn=message.ssn;
	var res={};
	customerDAO.getRideStatus(ssn,function(results) {
		if(results){
			res.code=200;
			console.log("results received from db"+results);
			res.value=results[0];
			callback(res);
		}
		else{
			res.code=401;
			res.value="No records found";
			callback(res);
		}
	});
}