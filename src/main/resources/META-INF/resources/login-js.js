
document.getElementById("submit").addEventListener("click", () => {
    let playerName = {
        "name": document.getElementById("inbox").value
    }
    
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8080/loginPl", false);
    request.setRequestHeader( "playerName", playerName);
    request.onreadystatechange = () => {
    	let message = JSON.parse(request.response);
    	if( message == "Logged in..."){
    		window.sessionStorage.setItem("playerSessionName", document.getElementById("inbox").value)
    		window.open("/waitingRoom.html", "_top");
    	} else if( message == "Try different player name"){
    		document.getElementById("info").innerHTML = "Nazwa zajeta. Sproboj ponownie."
    	} else {
    		document.getElementById("info").innerHTML = "Error ........"
    	}
    }
    request.send(JSON.stringify(playerName));
});
