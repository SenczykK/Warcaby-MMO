
document.getElementById("answer").style.display = "none";
document.getElementById("askPlayer").style.display = "none";
document.getElementById("reject").style.display = "none";
let playerList = [];

let playerName = window.sessionStorage.getItem("playerSessionName");
document.getElementById("playerSessionName").innerHTML = playerName;

let dateNow = 0;
let opponentName;
// Deklaracja WebSocket'u
let stompClientWS = Stomp.client("ws://localhost:8080/getPlayers/websocket");
stompClientWS.connect(
		{"Access-Control-Allow-Origin":"*"}, 
		function(frame) {
			// pobieram listę graczy z servera
			stompClientWS.subscribe('/ws/getPlayers', function(messageOutput) {
					updatePlayerList(messageOutput.body);
			});
			
			// Listener do decyzji do nowej gry
			stompClientWS.subscribe('/ws/listener', function(messageOutput) {
				let message = JSON.parse(messageOutput.body);
				console.log("zapytanie"+message+" "+opponentName)
				if(message.reciver == playerName && message.author == opponentName && message.option == "accept" ){
					console.log("Accepted game")
					window.sessionStorage.setItem("opponent", message.author);
					window.sessionStorage.setItem("whiteBlack", "black");
					window.open("/arena.html", "_top");
				} else
				if(message.reciver == playerName  && message.option == "reject") {
					document.getElementById("reject").style.display = "block";
					document.getElementById("askPlayer").style.display = "none";
					document.getElementById("askPlayer").style.display = "none";
					document.getElementById("listOfPlayers").style.display = "block";
	                document.getElementById("answer").style.display = "none"; 
				} else
				if(message.reciver == playerName && message.option == "question"){
					document.getElementById("reject").style.display = "none";
					document.getElementById("askPlayer").style.display = "block";
					document.getElementById("listOfPlayers").style.display = "none";
					opponentName = message.author;
					document.getElementById("opponent").value = message.author;
					document.getElementById("opponent").innerHTML = message.author;
					document.getElementById("opponent").style.weight = "bold";
				}
			}); // ---------- end of /ws/listener
		}
);

//Listenery decyzji YES?NO gracza
document.getElementById("opponentYes").addEventListener("click", () => {
	stompClientWS.send("/answer", 
			{},
			JSON.stringify(
				{"author": playerName, "reciver":document.getElementById("opponent").value, "option": "accept"}
			));
	window.sessionStorage.setItem("opponent", document.getElementById("opponent").value);
	window.sessionStorage.setItem("whiteBlack", "white");
	window.open("/arena.html", "_top");
});
document.getElementById("opponentNo").addEventListener("click", () => {
	stompClientWS.send("/answer", 
			{},
			JSON.stringify(
				{"author": playerName, "reciver": document.getElementById("opponent").value, "option": "reject"}
			));
	document.getElementById("askPlayer").style.display = "none";
	document.getElementById("listOfPlayers").style.display = "block";
});


let refreshPlayers = setInterval( () => {
	if( document.getElementById("askPlayer").style.display == "block") return 0;
	if( stompClientWS.connect ){
		stompClientWS.send("/getPlayers", {"playerName":playerName}, "");
	}
}, 5000); 


let dottedAnim;
let playerListeners = [];
function updatePlayerList(json){
    playerList = JSON.parse(json);
    document.getElementById("avaliable").innerHTML = "";

    for(let i=0; i<playerList.length; i++){
        if( playerList[i] == playerName) continue;
        document.getElementById("avaliable").innerHTML += "<p id=\"playerPlace"+i+"\" class=\"playerList\">"+playerList[i]+"</p>";
    }
    listenerBuilder();
    
    function listenerBuilder(){
        playerListeners = [];
        for(let i=0; i<playerList.length; i++){
            if( playerList[i] == playerName) continue;
            playerListeners.push( function(){
                document.getElementById("playerPlace"+i).addEventListener("click", ()=>{ 
                    document.getElementById("listOfPlayers").style.display = "none";
                    document.getElementById("answer").style.display = "block"; 
                    clearTimeout(refreshPlayers);
                    opponentName = playerList[i];
                    dottedAnim = setInterval( dottedAnimation, 2000);
                    stompClientWS.send("/askPlayer", {}, JSON.stringify({
                        		"author" : playerName,
                        		"reciver" : playerList[i],
                        		"option" : "question"
                        	}));
                }, true);
           });
        }
        for( p of playerListeners){
            p();
        }
        
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
        refreshPlayers = setInterval( () => {
        	if( document.getElementById("askPlayer").style.display == "block") return 0;
        	if( stompClientWS.connect ){
        		stompClientWS.send("/getPlayers", {"playerName":playerName}, "");
        	}
        }, 5000); 
    }
}
