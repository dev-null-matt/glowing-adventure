$(document).ready(function() {

	baseUrl = "/service/account-creation/";

	$login = $("#username");
	$loginTaken = $("#loginTaken");

	$email = $("#email");
	$emailConfirm = $("#emailConfirm");
	$emailNoMatch = $("#emailNoMatch");
	$emailTaken = $("#emailTaken");

	$password = $("#password");
	$passwordConfirm = $("#passwordConfirm");
	$passwordNoMatch = $("#passwordNoMatch");

	window.setInterval(validateLogin, 500);
	window.setInterval(validateEmails, 500);
	window.setInterval(validatePasswords, 250);

	$("#newUser").click(submit);
});

var lastSubmittedLogin = "";
var lastSubmittedEmail = "";

function validateLogin() {

	var login = $login.val();

	if (login) {

		if (login === lastSubmittedLogin) {
			// Nothing changed, don't validate
			return;
		}

		$.ajax({
			type : "GET",
			url : baseUrl + "isLoginRegistered/" + login + "/",
			success : function (data) {
				if (data) {
					$loginTaken.removeClass("hidden");
				} else {
					$loginTaken.addClass("hidden");
				}

				lastSubmittedLogin = login;
			}
		});
	} else {
		$loginTaken.addClass("hidden");
	}
}

function validateEmails() {

	var email = $email.val();
	var emailConfirm = $emailConfirm.val();

	if (email && emailConfirm) {
		if (email === emailConfirm) {

			if (email === lastSubmittedEmail) {
				// Nothing changed, don't validate
				return;
			}

			$emailNoMatch.addClass("hidden");

			$.ajax({
				type : "GET",
				url : baseUrl +  "isEmailRegistered/" + email + "/",
				success : function (data) {
					if (data) {
						$emailTaken.removeClass("hidden");
					} else {
						$emailTaken.addClass("hidden");
					}

					lastSubmittedEmail = email;
				}
			});
		} else {
			$emailNoMatch.removeClass("hidden");
			$emailTaken.addClass("hidden");
		}
	} else if (email === "" && emailConfirm === "") {
		$emailNoMatch.addClass("hidden");
		$emailTaken.addClass("hidden");
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

function submit() {
	$.ajax({
		type : "PUT",
		url : baseUrl + "create/" + $login.val() + "/" + $email.val() + "/",
		data : $password.val(),
		success : function (data) {
			if (data) {
				alert("Account creation successful");
			} else {
				alert("There was a problem creating your account");
			}
 		}
	});
}
