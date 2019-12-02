
let whitePositions = [{ x:0, y:1 }, { x:0, y:3 }, { x:0, y:5 }, { x:0, y:7 },
    { x:1, y:0 }, { x:1, y:2 }, { x:1, y:4 }, { x:1, y:6 },
    { x:2, y:1 }, { x:2, y:3 }, { x:2, y:5 }, { x:2, y:7 }];
let blackPositions = [{ x:5, y:0 }, { x:5, y:2 }, { x:5, y:4 }, { x:5, y:6 },
    { x:6, y:1 }, { x:6, y:3 }, { x:6, y:5 }, { x:6, y:7 },
    { x:7, y:0 }, { x:7, y:2 }, { x:7, y:4 }, { x:7, y:6 }];

// Pawn Class
/*class Pawn{
    x; y;
    zbity;

    constructor(x, y){
        this.zbity = false;
        this.x = x;
        this.y = y;
    }
    get _srcWhite(){
        return this._srcWhite;
    }
    get _srcBlack(){
        return this._srcBlack;
    }
}*/ //-------- Pawn Class
let playerName = window.sessionStorage.getItem("playerSessionName");
let opponentName = window.sessionStorage.getItem("opponent");
let whiteBlack = window.sessionStorage.getItem("whiteBlack");

document.getElementById("players").innerHTML = playerName+ " "+opponentName;
// Declaration of new object Pawn
let whiteList = [], blackList = [];
for(let i=0; i<12; i++){ 
    whiteList.push( {"x":whitePositions[i].x, "y":whitePositions[i].y} );	
    blackList.push( {"x":blackPositions[i].x, "y":blackPositions[i].y} );
}
// Declaration of const px/square on board
const square = 50;
// Declaration of src ob pawns sprites
const srcWhite = "white.png";
const srcBlack = "black.png";

// START
setPawnsAtStartPosition()

setInterval( drawPawnsSprites, 1000);
let stompClientWS = Stomp.client("ws://localhost:8080/getGame/websocket");
stompClientWS.connect({"Access-Control-Allow-Origin":"*"}, function(frame) {
    stompClientWS.subscribe('/ws/getGame', function(messageOutput) {
    	
//    	for(let i=0; i<12; i++){ 
//    	    whiteList[i] = {"x":whitePositions[i].x, "y":whitePositions[i].y};	
//    	    blackList[i] = {"x":blackPositions[i].x, "y":blackPositions[i].y};
//    	}
    	console.log(JSON.parse(messageOutput.body));
    });
});

function drawPawnsSprites(){
	stompClientWS.send("/getGame", {}, JSON.stringify([{"name": playerName}, {"name": opponentName}]));

    function updateBoard(data){
        let newBoardJSON = JSON.parse(data);
        
        whitePositions = Array.from(newBoardJSON.player1.paws);
        blackPositions = Array.from(newBoardJSON.player2.paws);
        
        let divPawns = document.getElementsByClassName("pawns")[0];
        divPawns.innerHTML="";
        for(let i=0; i<12; i++){//do liczby pionków w grze
            divPawns.innerHTML += "<img class=\"pawnWhite\" src=\""+srcWhite+"\" style=\"left:"+whiteList[i].x*square+"px;top:"+whiteList[i].y*square+"px;\" />";
            divPawns.innerHTML += "<img class=\"pawnWhite\" src=\""+srcBlack+"\" style=\"left:"+blackList[i].x*square+"px;top:"+blackList[i].y*square+"px;\" />";
        }
    }
}

function setPawnsAtStartPosition(){
    
    let divPawns = document.getElementsByClassName("pawns")[0];
    divPawns.innerHTML="";
    for(let i=0; i<12; i++){//do liczby pionków w grze
        divPawns.innerHTML += "<img class=\"pawnWhite\" src=\""+srcWhite+"\" style=\"left:"+whiteList[i].x*square+"px;top:"+whiteList[i].y*square+"px;\" />";
        divPawns.innerHTML += "<img class=\"pawnWhite\" src=\""+srcBlack+"\" style=\"left:"+blackList[i].x*square+"px;top:"+blackList[i].y*square+"px;\" />";
    }
    
}



