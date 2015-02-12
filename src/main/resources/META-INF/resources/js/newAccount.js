$(document).ready(function() {

	$email = $("#email");
	$emailConfirm = $("#emailConfirm");
	$emailNoMatch = $("#emailNoMatch");
	
	$password = $("#password");
	$passwordConfirm = $("#passwordConfirm");
	$passwordNoMatch = $("#passwordNoMatch");
	
	$("#username").change(function() {
		// Check to see if there is an account with this name already.
	});
	
	$("#email").change(validateEmails);
	$("#emailConfirm").change(validateEmails);
	
	$("#password").change(validatePasswords);
	$("#passwordConfirm").change(validatePasswords);
});

function validateEmails() {
		
	var email = $email.val();
	var emailConfirm = $emailConfirm.val();

	if (email && emailConfirm ) {
		if (email === emailConfirm) {
			$emailNoMatch.addClass("hidden");	
		} else {
			$emailNoMatch.removeClass("hidden");
		}
	}
}

function validatePasswords() {
	var password = $password.val();
	var passwordConfirm = $passwordConfirm.val();

	if (password && passwordConfirm ) {
		if (password === passwordConfirm) {
			$passwordNoMatch.addClass("hidden");	
		} else {
			$passwordNoMatch.removeClass("hidden");
		}
	}
}