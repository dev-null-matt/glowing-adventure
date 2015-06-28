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

	$create = $("#newUser");

	window.setInterval(validateLogin, 500);
	window.setInterval(validateEmails, 500);
	window.setInterval(validatePasswords, 250);
	window.setInterval(conditionallyEnableSubmit, 100);

	document.getElementById("cancel").onclick = function cancel() {location = "/"};

	$create.click(submit);
});

var lastSubmittedLogin = "";
var lastSubmittedEmail = "";

var loginValid = false;
var emailValid = false;
var passwordValid = false;

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
				loginPassedValidation(!data);
				lastSubmittedLogin = login;
			}
		});
	} else {
		$loginTaken.addClass("hidden");
		loginValid = false;
	}
}

function loginPassedValidation(toggle) {
	loginValid = toggle;

	if (toggle) {
		$loginTaken.addClass("hidden");
	} else {
		$loginTaken.removeClass("hidden");
	}
}

function validateEmails() {

	var email = $email.val();
	var emailConfirm = $emailConfirm.val();

	if (email && emailConfirm) {
		if (email === emailConfirm) {

			$emailNoMatch.addClass("hidden");

			if (email === lastSubmittedEmail) {
				// Nothing changed, don't validate
				return;
			}

			$.ajax({
				type : "GET",
				url : baseUrl +  "isEmailRegistered/" + email + "/",
				success : function (data) {
					emailTaken(data);
					lastSubmittedEmail = email;
				}
			});
		} else {
			lastSubmittedEmail = "";
			emailNoMatch(true);
		}
	} else if (email === "" || emailConfirm === "") {
		emailValid = false;
		$emailNoMatch.addClass("hidden");
		$emailTaken.addClass("hidden");
	}
}

function emailTaken(toggle) {
	emailValid = !toggle;

	if (toggle) {
		$emailTaken.removeClass("hidden");
	} else {
		$emailTaken.addClass("hidden");
	}
}

function emailNoMatch(toggle) {
	emailValid = !toggle;

	if (toggle) {
		$emailNoMatch.removeClass("hidden");
		$emailTaken.addClass("hidden");
	} else {
		$emailNoMatch.addClass("hidden");
	}
}

function validatePasswords() {
	var password = $password.val();
	var passwordConfirm = $passwordConfirm.val();
	passwordValid = false;

	if (password && passwordConfirm ) {
		if (password === passwordConfirm) {
			$passwordNoMatch.addClass("hidden");
			passwordValid = true;
		} else {
			$passwordNoMatch.removeClass("hidden");
		}
	} else {
		$passwordNoMatch.addClass("hidden");
	}
}

function conditionallyEnableSubmit() {
	if (loginValid && emailValid && passwordValid) {
		$create.removeClass("inactive");
		$create.removeAttr("disabled");
	} else {
		$create.addClass("inactive");
		$create.attr("disabled", "disabled");
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
