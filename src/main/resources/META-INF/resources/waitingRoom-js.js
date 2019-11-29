
document.getElementById("answer").style.display = "none";
let playerList = [];

let playerName = window.sessionStorage.getItem("playerSessionName");
document.getElementById("playerSessionName").innerHTML = playerName;

let dateNow = 0;
//setInterval( dottedAnimation, 2000);
// ustawiam pobieranie listy graczy dostepnych


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
			stompClientWS.subscribe('/ws/listener', function(messageOutput) {
				let players = JSON.parse(messageOutput.body);
				console.log(players)
				if( playerName == players[0] ){
					console.log("chcesz zagrac z "+players[1]+" ?");
				}
				//console.log(JSON.parse(messageOutput.body));
			});
		}
);

let refreshPlayers = setInterval( () => {
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
                stompClientWS.send("/askPlayer",
                    	{}, JSON.stringify([
                    		{"name" : playerName},
                    		{"name" : playerList[i]}
                    	])
                );
                    

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
