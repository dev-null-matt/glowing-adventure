var arrested = arrested || {};
arrested.maps = arrested.maps || {};

// The main game menu
arrested.maps.GameMenu = function constructor(map) {

  var accountSettingsUrl = "/service/account-settings/";
  var isEmailVerified = false;

  var collapsedMenuUi = undefined;
  var expandedMenuUi = undefined;

  // Sub menu
  var addWaypointMenu = undefined;

  var logoutCallback = undefined;

  // Click handler functions ///////////////////////////////////////////////////

  // Hides the expanded menu
  function closeMenu() {
    expandedMenuUi.className = "hidden";
    applyCallbackToCollapsedUi(openMenu);
  }

  // Displays the add waypoint sub-menu
  function doAddWaypoint() {
    expandedMenuUi.className = "hidden";
    applyCallbackToCollapsedUi(false);
    addWaypointMenu.openMenu();
  }

  // Logs the currently logged in character out
  function logout() {

    if (logoutCallback) {
      logoutCallback();
    }

    location = "/pages/characterSelect.html";
  }

  // Displays the expanded menu
  function openMenu() {
    expandedMenuUi.className = "";
    applyCallbackToCollapsedUi(closeMenu);

    if (!isEmailVerified) {
      $.ajax({
        type : "GET",
        url : accountSettingsUrl + "isVerified",
        success : function emailVerifiedSuccess(data) {
          if (data) {
            expandedMenuUi.querySelector("#addWaypoint").parentElement.classList.remove("hidden");
            isEmailVerified = true;
          }
        }
      });
    }
  }

  // Constructor helper functions //////////////////////////////////////////////

  // Creates the collapsed menu, applying styles and attaching click handlers.
  function createCollapsedMenu() {

    collapsedMenuUi = document.createElement("div");
    collapsedMenuUi.className = "inputField";
    collapsedMenuUi.style.zIndex = 100;
    collapsedMenuUi.innerHTML = "<div class='controlContainer'><button id='openMenu'>Menu</button></div>";

    map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(collapsedMenuUi);

    applyCallbackToCollapsedUi(openMenu);
  }

  // Creates the expanded menu, applying styles and attaching click handlers.
  function createExpandedMenu() {

    expandedMenuUi = document.createElement("div");
    expandedMenuUi.className = "hidden";

    $.get('/ui-elements/gameMenu.html').then(function(responseData) {
      expandedMenuUi.innerHTML = responseData;
      expandedMenuUi.querySelector("#addWaypoint").onclick = doAddWaypoint;
      expandedMenuUi.querySelector("#logout").onclick = logout;
      expandedMenuUi.querySelector("#close").onclick = closeMenu;
    });

    map.controls[google.maps.ControlPosition.LEFT_BOTTOM].push(expandedMenuUi);
  }

  // Setter functions //////////////////////////////////////////////////////////
  function setLogoutCallback(callback) {
    logoutCallback = callback;
  }

  // Generic helper functions //////////////////////////////////////////////////
  function applyCallbackToCollapsedUi(callback) {
    if (callback) {
      collapsedMenuUi.querySelector("#openMenu").onclick = callback.bind(this);
    } else {
      collapsedMenuUi.querySelector("#openMenu").onclick = undefined;
    }
  }

  // Constructor ///////////////////////////////////////////////////////////////
  createExpandedMenu();
  createCollapsedMenu();

  this.setLogoutCallback = setLogoutCallback;

  addWaypointMenu = new arrested.maps.AddWaypointMenu(map, openMenu);
}
