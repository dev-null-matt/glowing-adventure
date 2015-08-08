var accountSettingsUrl = "/service/account-settings/";

$(document).ready(function() {
  document.getElementById("changePassword").onclick = toggleChangePassword;
  document.getElementById("changeEmail").onclick = toggleChangeEmail;
  document.getElementById("doChangePassword").onclick = doChangePassword;
  document.getElementById("doChangeEmail").onclick = doChangeEmail;
  document.getElementById("confirmEmail").onclick = doConfirmEmail;
  document.getElementById("back").onclick = goCharacterSelect;

  window.setInterval(validateChangeEmail, 500);
  window.setInterval(validateChangePassword, 500);
});

// Onclick callbacks ///////////////////////////////////////////////////////////

var validateChangeEmail = function validateChangeEmail() {
  if (document.getElementById("email").value && compareEmails()) {
    var button = document.getElementById("doChangeEmail");
    button.disabled = undefined;
    button.className = "";
  } else {
    var button = document.getElementById("doChangeEmail");
    button.disabled = "disabled";
    button.className = "inactive";
  }
}

var validateChangePassword = function validateChangePassword() {
  if (document.getElementById("password").value && comparePasswords()) {
    var button = document.getElementById("doChangePassword");
    button.disabled = undefined;
    button.className = "";
  } else {
    var button = document.getElementById("doChangePassword");
    button.disabled = "disabled";
    button.className = "inactive";
  }
}

var toggleChangeEmail = function toggleChangeEmail() {

  emailChange = document.getElementById("emailChange");

  if (emailChange.className) {
    emailChange.className = "";
    document.getElementById("passwordChange").className = "hidden";
  } else {
    emailChange.className = "hidden";
  }
}

var toggleChangePassword = function toggleChangePassword() {

  passwordChange = document.getElementById("passwordChange");

  if (passwordChange.className) {
    passwordChange.className = "";
    document.getElementById("emailChange").className = "hidden";
  } else {
    passwordChange.className = "hidden";
  }
}

var doChangeEmail = function doChangeEmail() {
  $.ajax({
    type : "PUT",
    url : accountSettingsUrl + "email",
    data : document.getElementById("email").value,
    success : function emailChangeSuccess() {
      toggleChangeEmail();
      showMessage("Email updated", 2500);
    }
  });
}

var doChangePassword = function doChangePassword() {
  $.ajax({
    type : "PUT",
		url : accountSettingsUrl + "password",
    data : document.getElementById("password").value,
    success : function pwChangeSuccess() {
      toggleChangePassword();
      showMessage("Password updated", 2500);
    }
  });
}

var doConfirmEmail = function doConfirmEmail() {
  $.ajax({
    type : "POST",
		url : accountSettingsUrl + "requestConfirmEmail",
    success : function emailConfirmSuccess() {
      showMessage("Confirmation email sent", 2500);
    }
  });
}

var goCharacterSelect = function goCharacterSelect() {
  location = "/pages/characterSelect.html";
}

// Helper functions ////////////////////////////////////////////////////////////
function compareEmails() {
  return document.getElementById("email").value === document.getElementById("emailConfirm").value;
}

function comparePasswords() {
  return document.getElementById("password").value === document.getElementById("confirm").value;
}

var clearMessage = function clearMessage() {
  var messageDiv = document.getElementById("messageContainer");
  messageDiv.className = "hidden";
  messageDiv.innerHTML = "";
}

function showMessage(message, timeout) {
  var messageDiv = document.getElementById("messageContainer");
  messageDiv.innerHTML = message;
  messageDiv.className = "";
  window.setTimeout(clearMessage, timeout);
}
