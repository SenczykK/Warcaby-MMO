//change url to xml file
document.getElementById("submit").addEventListener("click", () => {
    let playerName = {
        "name": document.getElementById("inbox").value
    }
    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8080/loginPl", false);
    console.log(request)
    //request.setRequestHeader( "playerName", playerName);
    request.send(JSON.stringify(playerName));
    // tymczasowo
    window.sessionStorage.setItem("playerSessionName", document.getElementById("inbox").value)
    // --------------------- <-----------
    let cookie = document.cookie;
    console.log(cookie);
});
