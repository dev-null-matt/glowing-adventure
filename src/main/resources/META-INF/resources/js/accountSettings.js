$(document).ready(function() {
  document.getElementById("changePassword").onclick = toggleChangePassword;
  window.setInterval(validateChangePassword, 500);
});

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

function comparePasswords() {
  return document.getElementById("password").value === document.getElementById("confirm").value
}

var toggleChangePassword = function toggleChangePassword() {

  passwordChange = document.getElementById("passwordChange");

  if (passwordChange.className) {
    passwordChange.className = "";
  } else {
    passwordChange.className = "hidden";
  }
}
