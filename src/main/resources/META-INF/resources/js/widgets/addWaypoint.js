var arrested = arrested || {};
arrested.maps = arrested.maps || {};

arrested.maps.AddWaypointMenu = function constructor(map, onClose) {

  var onCloseCallback = onClose;
  var description = undefined;

  // Creates the expanded menu, applying styles and attaching click handlers.
  function createExpandedMenu() {

    expandedMenuUi = document.createElement("div");
    expandedMenuUi.className = "hidden";

    $.get('/ui-elements/addWaypoint.html').then(function(responseData) {
      expandedMenuUi.innerHTML = responseData;
      expandedMenuUi.querySelector("#add").onclick = doSubmit;
      expandedMenuUi.querySelector("#close").onclick = doCancel;

      description = expandedMenuUi.querySelector("#description");
    });

    map.controls[google.maps.ControlPosition.LEFT_BOTTOM].push(expandedMenuUi);
  }

  // Displays the expanded sub menu
  function openMenu() {
    expandedMenuUi.className = "";
  }

  // Hides the expanded sub menu
  function hideMenu() {
    expandedMenuUi.className = "hidden";
  }

  // Submits the new waypoint and closes the sub menu
  function doSubmit() {

    console.log("doSubmit() \"" + description.value + "\"");
    hideMenu();

    if (onCloseCallback) {
      onCloseCallback();
    }
  }

  // Closes the sub menu
  function doCancel() {

    hideMenu();

    if (onCloseCallback) {
      onCloseCallback();
    }
  }

  // Constructor ///////////////////////////////////////////////////////////////
  createExpandedMenu();
  this.openMenu = openMenu;
}
