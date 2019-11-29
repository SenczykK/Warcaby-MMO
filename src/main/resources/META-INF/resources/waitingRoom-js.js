
document.getElementById("answer").style.display = "none";
document.getElementById("askPlayer").style.display = "none";
let playerList = [];

let playerName = window.sessionStorage.getItem("playerSessionName");
document.getElementById("playerSessionName").innerHTML = playerName;

let dateNow = 0;

let stompClientWS = Stomp.client("ws://localhost:8080/getPlayers/websocket");
stompClientWS.connect(
		{"Access-Control-Allow-Origin":"*"}, 
		function(frame) {
			stompClientWS.subscribe('/ws/getPlayers', function(messageOutput) {
					updatePlayerList(messageOutput.body);
			});
			stompClientWS.subscribe('/ws/askPlayer', function(messageOutput) {
				
				//console.log(JSON.parse(messageOutput.body));
			});
			// Listener do decyzji do nowej gry
			stompClientWS.subscribe('/ws/listener', function(messageOutput) {
				let players = JSON.parse(messageOutput.body);
				console.log("Dane /listener "+players[0].name+" "+players[1].name);
				if( (players[1].name == "reject" && players[0].name == playerName)
					|| (players[1].name == playerName && players[0].name == "reject")){
					console.log("zapytanie odrzucone");
					document.getElementById("askPlayer").style.display = "none";
					document.getElementById("askPlayer").style.display = "none";
					document.getElementById("listOfPlayers").style.display = "block";
	                document.getElementById("answer").style.display = "none"; 
	                // TODO subscribe /ws/askPlayer
					return 0;
				}
				if( playerName == players[1].name ){
					console.log("chcesz zagrac z "+players[0].name+" ?");
					document.getElementById("askPlayer").style.display = "block";
					document.getElementById("opponent").innerHTML = players[0].name;
					document.getElementById("opponent").style.weight = "bold";
				}
			});
		}
);

let refreshPlayers = setInterval( () => {
	if( document.getElementById("askPlayer").style.display == "block") return 0;
	if( stompClientWS.connect ){
		stompClientWS.send("/getPlayers", {"playerName":playerName}, "");
	}
}, 5000); 

	function updatePlayerList(json){
        playerList = JSON.parse(json);
        document.getElementById("avaliable").innerHTML = "";

        for(let i=0; i<playerList.length; i++){
            if( playerList[i] == playerName) continue;
            document.getElementById("avaliable").innerHTML += "<p id=\"playerPlace"+i+"\" class=\"playerList\">"+playerList[i]+"</p>";
        }
        listenerBuilder();
    }

let dottedAnim;
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
                // TODO unsubscribe /ws/askPlayer
                dottedAnim = setInterval( dottedAnimation, 2000);
                // wyslij do serwera, aby odpytac gracza
                stompClientWS.send("/askPlayer",
                    	{}, JSON.stringify([
                    		{"name" : playerName},
                    		{"name" : playerList[i]}
                    	])
                );
            }, true);
       });
    }
    for( p of playerListeners){
        p();
    }
    
}

// Listenery decyzji YES?NO gracza
document.getElementById("opponentYes").addEventListener("click", () => {
	stompClientWS.send("/answer", 
			{},
			JSON.stringify([
				{"name": playerName}, {"name":document.getElementById("opponent").value}
			]));
});
document.getElementById("opponentNo").addEventListener("click", () => {
	stompClientWS.send("/answer", 
			{},
			JSON.stringify([
				{"name": playerName}, {"name": "reject"}
			]));
	document.getElementById("askPlayer").style.display = "none";
});

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
        refreshPlayers = setInterval( getPlayerList, 2000);
    }
}
