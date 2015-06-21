var accountSettingsUrl = "/service/account-settings/";

$(document).ready(function() {
  document.getElementById("changePassword").onclick = toggleChangePassword;
  document.getElementById("doChangePassword").onclick = doChangePassword;

  window.setInterval(validateChangePassword, 500);
});

// Onclick callbacks ///////////////////////////////////////////////////////////

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

var toggleChangePassword = function toggleChangePassword() {

  passwordChange = document.getElementById("passwordChange");

  if (passwordChange.className) {
    passwordChange.className = "";
  } else {
    passwordChange.className = "hidden";
  }
}

var doChangePassword = function doChangePassword() {
  $.ajax({
    type : "PUT",
		url : accountSettingsUrl + "password",
    data : document.getElementById("password").value,
    success : function pwChangeSuccess() {
      showMessage("Password updated", 2500);
    }
  });
}

// Helper functions ////////////////////////////////////////////////////////////

function comparePasswords() {
  return document.getElementById("password").value === document.getElementById("confirm").value
}

var clearMessage = function clearMessage() {

  var messageDiv = document.getElementById("messageContainer");
  messageDiv.className = "hidden";

  var message = document.getElementById("messageContainer");
  message.innerHTML = "";
}

function showMessage(message, timeout) {

  toggleChangePassword();

  var message = document.getElementById("messageContainer");
  message.innerHTML = "Password updated";

  var messageDiv = document.getElementById("messageContainer");
  messageDiv.className = "";

  window.setTimeout(clearMessage, timeout);
}
