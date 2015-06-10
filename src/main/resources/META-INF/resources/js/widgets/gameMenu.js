var arrested = arrested || {};
arrested.maps = arrested.maps || {};

// The main game menu
arrested.maps.GameMenu = function constructor(map) {

  var collapsedMenuUi = undefined;
  var expandedMenuUi = undefined;

  // Click handler functions //////////////////////////////////////////////////

  // Displays the expanded menu when the collapsed menu is clicked.
  var openMenu = function openMenu() {
    expandedMenuUi.className = "";
  }

  // Constructor helper functions /////////////////////////////////////////////

  // Creates the collapsed menu, applying styles and attaching click handlers.
  var createCollapsedMenu = function createCollapsedMenu() {

    collapsedMenuUi = document.createElement("div");
    collapsedMenuUi.className = "inputField";
    collapsedMenuUi.innerHTML = "<button id='openMenu'>Menu</button>";

    map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(collapsedMenuUi);

    collapsedMenuUi.querySelector("#openMenu").onclick = openMenu.bind(this);
  }

  // Creates the expanded menu, applying styles and attaching click handlers.
  var createExpandedMenu = function createExpandedMenu() {

    expandedMenuUi = document.createElement("div");
    expandedMenuUi.className = "hidden";

    $.get('/ui-elements/gameMenu.html').then(function(responseData) {
      expandedMenuUi.innerHTML = responseData;
    });

    map.controls[google.maps.ControlPosition.BOTTOM_CENTER].push(expandedMenuUi);

    expandedMenuUi.querySelector("#logout");
    expandedMenuUi.querySelector("#close");
  }

  createExpandedMenu();
  createCollapsedMenu();
}
