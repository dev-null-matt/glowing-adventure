$(document).ready(function() {

	$login = $("#username");
	$loginTaken = $("#loginTaken");

	$email = $("#email");
	$emailConfirm = $("#emailConfirm");
	$emailNoMatch = $("#emailNoMatch");
	$emailTaken = $("#emailTaken");
	
	$password = $("#password");
	$passwordConfirm = $("#passwordConfirm");
	$passwordNoMatch = $("#passwordNoMatch");
	
	$login.change(validateLogin);
	
	$email.change(validateEmails);
	$emailConfirm.change(validateEmails);
	
	$password.change(validatePasswords);
	$passwordConfirm.change(validatePasswords);
});

function validateLogin() {
	
	var login = $login.val();
	
	if (login) {
		$.ajax({
			type : "GET",
			url : "/service/accountCreation/isLoginRegistered/" + login + "/",
			success : function (data) {
				if (data) {
					$loginTaken.removeClass("hidden");
				} else {
					$loginTaken.addClass("hidden");
				}
			}
		});
	}
}

function validateEmails() {
		
	var email = $email.val();
	var emailConfirm = $emailConfirm.val();

	if (email && emailConfirm ) {
		if (email === emailConfirm) {
			$emailNoMatch.addClass("hidden");
			
			$.ajax({
				type : "GET",
				url : "/service/accountCreation/isEmailRegistered/" + email + "/",
				success : function (data) {
					if (data) {
						$emailTaken.removeClass("hidden");
					} else {
						$emailTaken.addClass("hidden");
					}
				}
			});
		} else {
			$emailNoMatch.removeClass("hidden");
			$emailTaken.addClass("hidden");
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