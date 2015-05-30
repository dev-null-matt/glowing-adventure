arrested = {maps : {}};

// A simple toast window for use with google maps.
arrested.maps.ToastWindow = function constructor(map) {

  var messages = [];

  var displayDiv = document.createElement('div');
  var messageDiv = document.createElement('div');

  displayDiv.title = "Click to close notification";
  displayDiv.style.display = "none";

  displayDiv.className = "toastWindow-displayDiv";
  messageDiv.className = "toastWindow-messageDiv";

  displayDiv.appendChild(messageDiv);

  map.controls[google.maps.ControlPosition.BOTTOM_CENTER].push(displayDiv);

  // Adds a new message to the queue of messages to display
  this.queueMessage = function(message) {

    messages.push(message);

    if (!messageDiv.innerHTML) {
      toastWindow.paint();
    }
  }

  // If there are no messages left in the queue, hides the toast window,
  // otherwise, displays the first message in the queue and removes that
  // message from the queue.  A timer is set to recall this function after
  // either action.
  this.paint = function paint() {

    if (messages.length) {
      displayDiv.style.display = "block";
      messageDiv.innerHTML = messages.shift();
      window.setTimeout(this.paint.bind(this), 1500);
    } else {
      displayDiv.style.display = "none";
      messageDiv.innerHTML = "";
    }
  }
}
