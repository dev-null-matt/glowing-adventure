var arrested = arrested || {};
arrested.maps = arrested.maps || {};

// The main game menu
arrested.maps.GameMenu = function constructor(map) {

  var collapsedMenuUi = undefined;
  var expandedMenuUi = undefined;

  var logoutCallback = undefined;

  // Click handler functions ///////////////////////////////////////////////////

  // Hides the expanded menu
  var closeMenu = function closeMenu() {
    expandedMenuUi.className = "hidden";
    applyCallbackToCollapsedUi(openMenu);
  }

  // Logs the currently logged in character out
  var logout = function logout() {
    if (logoutCallback) {
      logoutCallback();
    }

    location = "/pages/characterSelect.html";
  }

  // Displays the expanded menu
  var openMenu = function openMenu() {
    expandedMenuUi.className = "";
    applyCallbackToCollapsedUi(closeMenu);
  }

  // Constructor helper functions //////////////////////////////////////////////

  // Creates the collapsed menu, applying styles and attaching click handlers.
  var createCollapsedMenu = function createCollapsedMenu() {

    collapsedMenuUi = document.createElement("div");
    collapsedMenuUi.className = "inputField";
    collapsedMenuUi.style.zIndex = 100;
    collapsedMenuUi.innerHTML = "<div class='controlContainer'><button id='openMenu'>Menu</button></div>";

    map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(collapsedMenuUi);

    applyCallbackToCollapsedUi(openMenu);
  }

  // Creates the expanded menu, applying styles and attaching click handlers.
  var createExpandedMenu = function createExpandedMenu() {

    expandedMenuUi = document.createElement("div");
    expandedMenuUi.className = "hidden";

    $.get('/ui-elements/gameMenu.html').then(function(responseData) {
      expandedMenuUi.innerHTML = responseData;
      expandedMenuUi.querySelector("#logout").onclick = logout;
      expandedMenuUi.querySelector("#close").onclick = closeMenu.bind(this);
    });

    map.controls[google.maps.ControlPosition.LEFT_BOTTOM].push(expandedMenuUi);
  }

  // Setter functions //////////////////////////////////////////////////////////
  this.setLogoutCallback = function setLogoutCallback() {

  }

  // Generic helper functions //////////////////////////////////////////////////
  var applyCallbackToCollapsedUi = function applyCallbackToCollapsedUi(callback) {
    collapsedMenuUi.querySelector("#openMenu").onclick = callback.bind(this);
  }

  // Constructor ///////////////////////////////////////////////////////////////
  createExpandedMenu();
  createCollapsedMenu();
}
