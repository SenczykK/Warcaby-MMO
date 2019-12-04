
let whitePositions = [{ x:0, y:1 }, { x:0, y:3 }, { x:0, y:5 }, { x:0, y:7 },
    { x:1, y:0 }, { x:1, y:2 }, { x:1, y:4 }, { x:1, y:6 },
    { x:2, y:1 }, { x:2, y:3 }, { x:2, y:5 }, { x:2, y:7 }];
let blackPositions = [{ x:5, y:0 }, { x:5, y:2 }, { x:5, y:4 }, { x:5, y:6 },
    { x:6, y:1 }, { x:6, y:3 }, { x:6, y:5 }, { x:6, y:7 },
    { x:7, y:0 }, { x:7, y:2 }, { x:7, y:4 }, { x:7, y:6 }];

let playerName = window.sessionStorage.getItem("playerSessionName");
let opponentName = window.sessionStorage.getItem("opponent");
let whiteBlack = window.sessionStorage.getItem("whiteBlack");

if( whiteBlack == "white") whiteBlackBool = true;
if( whiteBlack == "black") whiteBlackBool = false;
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
drawPawns()

let stompClientWS = Stomp.client("ws://localhost:8080/movement/websocket");
stompClientWS.connect({"Access-Control-Allow-Origin":"*"}, function(frame) {
	setDropDown();
    stompClientWS.subscribe('/ws/updateGameboard', function(messageOutput) {
    	if(JSON.parse(messageOutput.body).author.name == playerName) return 0;    	
    	updateBoard(messageOutput.body);
    });
});

function updateBoard(data){
    let newBoardJSON = JSON.parse(data);
    
    if(whiteBlack == "white"){
    	whitePositions = Array.from(newBoardJSON.reciver.paws);
    	blackPositions = Array.from(newBoardJSON.author.paws);
    }else if(whiteBlack == "black"){
    	whitePositions = Array.from(newBoardJSON.author.paws);
    	blackPositions = Array.from(newBoardJSON.reciver.paws);
    }
    drawPawns();
    setDropDown();
    
}

function setDropDown(){
    
    //zabezpieczenie przed kilkoma klikami
    let isMouseDown = false;
    let mouse;

    for(let i=0; i<12; i++){
    	if( whiteBlack == "white")
    		setterW(i);
    	if( whiteBlack == "black")
    		setterB(i);
    }

    function setterW(i){
        whiteList[i].DOMelement = document.getElementById("pawnWhite"+i);
        whiteList[i].DOMelement.addEventListener( "mousedown", (e) => {
            whiteList[i].DOMelement.style.backgroundColor = "red";
            isMouseDown = true;
            whiteList[i].DOMelement.style.zIndex = 1000;
            mouse = {x: whiteList[i].DOMelement.offsetLeft - e.clientX,
                y: whiteList[i].DOMelement.offsetTop - e.clientY}; 
        });
        
        whiteList[i].DOMelement.addEventListener( "mousemove", (e) => {
            e.preventDefault();
            if( isMouseDown ){
            	whitePositions[i].x = Math.round((e.clientX + mouse.x)/square);
            	whitePositions[i].y = Math.round((e.clientY + mouse.y)/square);
            	whiteList[i].DOMelement.style.left = e.clientX + mouse.x  +"px";
            	whiteList[i].DOMelement.style.top = e.clientY + mouse.y +"px";
            }
        });
        whiteList[i].DOMelement.addEventListener( "mouseup", (e) => { 
            whiteList[i].DOMelement.style.backgroundColor = "transparent";
            isMouseDown = false;
            whiteList[i].DOMelement.style.zIndex = 1;
            drawPawns();
            stompClientWS.send("/movement", {}, JSON.stringify(
            		{"author": { "name": playerName, 
                				"paws": whitePositions,
                				"lastMove": [{}]
                				},
                	"reciver": { "name": opponentName, 
                				"paws": blackPositions,
                				"lastMove": [{}]
            					}
                	})); 
        });
    }
    function setterB(i){
        blackList[i].DOMelement = document.getElementById("pawnBlack"+i);
        blackList[i].DOMelement.addEventListener( "mousedown", (e) => {
            blackList[i].DOMelement.style.backgroundColor = "red";
            isMouseDown = true;
            blackList[i].DOMelement.style.zIndex = 1000;
            mouse = {x: blackList[i].DOMelement.offsetLeft - e.clientX,
                y: blackList[i].DOMelement.offsetTop - e.clientY};
        });
        blackList[i].DOMelement.addEventListener( "mousemove", (e) => {
            e.preventDefault();
            if( isMouseDown ){
                blackPositions[i].x = Math.round((e.clientX + mouse.x)/square);
                blackPositions[i].y = Math.round((e.clientY + mouse.y)/square);
                blackList[i].DOMelement.style.left = e.clientX + mouse.x  +"px";
                blackList[i].DOMelement.style.top = e.clientY + mouse.y  +"px";
            }
        });
        blackList[i].DOMelement.addEventListener( "mouseup", (e) => {
            blackList[i].DOMelement.style.backgroundColor = "transparent";
            isMouseDown = false;
            blackList[i].DOMelement.style.zIndex = 1;
            drawPawns();
            stompClientWS.send("/movement", {}, JSON.stringify(
            		{"author": { "name": playerName, 
                				"paws": blackPositions,
                				"lastMove": [{}]
                				},
                	"reciver": { "name": opponentName, 
                				"paws": whitePositions,
                				"lastMove": [{}]
            					}
                	}));   
        });
    }
}

function drawPawns(){
    
    let divPawns = document.getElementsByClassName("pawns")[0];
    divPawns.innerHTML="";
    for(let i=0; i<12; i++){//do liczby pionków w grze
        divPawns.innerHTML += "<img class=\"pawnWhite\" id='pawnWhite"+i+"' src=\""+srcWhite+"\" style=\"left:"+whitePositions[i].x*square+"px;top:"+whitePositions[i].y*square+"px;\" />";
        divPawns.innerHTML += "<img class=\"pawnWhite\" id='pawnBlack"+i+"' src=\""+srcBlack+"\" style=\"left:"+blackPositions[i].x*square+"px;top:"+blackPositions[i].y*square+"px;\" />";
    }
}

document.getElementById("cancelGame").addEventListener("click",()=>{
	let request = new XMLHttpRequest();
	request.open("POST", "http://localhost:8080/destroy", false);
	request.send(JSON.stringify({ "author": playerName, "reciver": opponentName, "decision": "cancel"}));
	window.open("/waitingRoom.html", "_top");
});



