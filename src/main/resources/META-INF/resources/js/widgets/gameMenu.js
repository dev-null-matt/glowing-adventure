var arrested = arrested || {};
arrested.maps = arrested.maps || {};

// The main game menu
arrested.maps.GameMenu = function constructor(map) {

  var collapsedMenuUi = undefined;
  var expandedMenuUi = undefined;

  // Click handler functions ///////////////////////////////////////////////////

  // Displays the expanded menu when the collapsed menu is clicked.
  var openMenu = function openMenu() {
    expandedMenuUi.className = "";
    applyCallbackToCollapsedUi(closeMenu);
  }

  var closeMenu = function closeMenu() {
    expandedMenuUi.className = "hidden";
    applyCallbackToCollapsedUi(openMenu);
  }

  // Constructor helper functions //////////////////////////////////////////////

  // Creates the collapsed menu, applying styles and attaching click handlers.
  var createCollapsedMenu = function createCollapsedMenu() {

    collapsedMenuUi = document.createElement("div");
    collapsedMenuUi.className = "inputField";
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
      expandedMenuUi.querySelector("#logout");
      expandedMenuUi.querySelector("#close").onclick = closeMenu.bind(this);
    });

    map.controls[google.maps.ControlPosition.TOP_CENTER].push(expandedMenuUi);
  }

  // Generic helper functions //////////////////////////////////////////////////
  var applyCallbackToCollapsedUi = function applyCallbackToCollapsedUi(callback) {
    collapsedMenuUi.querySelector("#openMenu").onclick = callback.bind(this);
  }

  createExpandedMenu();
  createCollapsedMenu();
}
