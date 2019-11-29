
document.getElementById("answer").style.display = "none";
let playerList = [];

let playerName = window.sessionStorage.getItem("playerSessionName");
document.getElementById("playerSessionName").innerHTML = playerName;

let dateNow = 0;
//setInterval( dottedAnimation, 2000);
// ustawiam pobieranie listy graczy dostepnych
let refreshPlayers ;// = setInterval( getPlayerList, 2000);

let stompClientWS = Stomp.client("ws://localhost:8080/get/Board1");
stompClientWS.connect({"Access-Control-Allow-Origin":"*"}, function(frame) {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClientWS.subscribe('/get/messages', function(messageOutput) {
        console.log(JSON.parse(messageOutput.body));
    });
});



function getPlayerList(){
    try {
        let request = new XMLHttpRequest();
        request.open("GET", "http://localhost:8080/getPlayerNameList", false);
        request.send(null);
        request.onreadystatechange = updatePlayerList(request.response);
    } catch (error) {
        console.log(error)
    }

    function updatePlayerList(json){
        playerList = JSON.parse(json);
        document.getElementById("avaliable").innerHTML = "";

        for(let i=0; i<playerList.length; i++){
            if( playerList[i] == playerName) continue;
            document.getElementById("avaliable").innerHTML += "<p id=\"playerPlace"+i+"\" class=\"playerList\">"+playerList[i]+"</p>";
        }
        listenerBuilder();
    }
}

let playerListeners = [];
function listenerBuilder(){
    playerListeners = [];
    for(let i=0; i<playerList.length; i++){
        if( playerList[i] == playerName) continue;
        playerListeners.push( function(){
            document.getElementById("playerPlace"+i).addEventListener("click", ()=>{ 
                // zmien widok na oczekiwanie na akceptacje
                document.getElementById("listOfPlayers").style.display = "none";
                document.getElementById("answer").style.display = "block"; 
                // zatrzymaj pobieranie graczy
                clearTimeout(refreshPlayers);
                // wyslij do serwera, aby odpytac gracza
                try{
                    let request = new XMLHttpRequest();
                    request.open("POST", "http://localhost:8080/askPlayer", false);
                    let players = [
                        {
                            "name": playerName
                        },
                        {
                            "name" : playerList[i]
                        }
                    ]
                    request.send(JSON.stringify(players));
                    request.onreadystatechange = checkServerAnswer(request.response); //check server answeer
                }catch(e){
                    console.log("Error when ask player to play")
                }
                // wyswietl odpowiedz
                function checkServerAnswer(response){
                    if(response == "null"){
                        console.log("player2 reject invitation");
                        document.getElementById("listOfPlayers").style.display = "block";
                        document.getElementById("answer").style.display = "none"; 
                        //refreshPlayers = setInterval( getPlayerList, 2000);
                    }else{
                        console.log("player2 accept invitation");
                        // zmien widok
                    }
                }
            }, true);
            });
    }
    for( p of playerListeners){
        p();
    }
    
}


function dottedAnimation(){
    dateNow++;
    if( document.getElementById("answer").style.display == "block" ){
            let dotted = "";
            for(let i=0; i<dateNow; i++){
                dotted+=".";
            }
            if(dateNow > 3){
                dateNow = 0;
            }
        document.getElementById("dottedWait").innerHTML = dotted;
    }else{
        //refreshPlayers = setInterval( getPlayerList, 2000);
    }
}
