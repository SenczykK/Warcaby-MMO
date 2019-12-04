
let whitePositions = [{ x:0, y:1, alive:true }, { x:0, y:3, alive:true }, { x:0, y:5, alive:true }, { x:0, y:7, alive:true },
    { x:1, y:0, alive:true }, { x:1, y:2, alive:true }, { x:1, y:4, alive:true }, { x:1, y:6, alive:true },
    { x:2, y:1, alive:true }, { x:2, y:3, alive:true }, { x:2, y:5, alive:true }, { x:2, y:7, alive:true }];
let blackPositions = [{ x:5, y:0, alive:true }, { x:5, y:2, alive:true }, { x:5, y:4, alive:true }, { x:5, y:6, alive:true },
    { x:6, y:1, alive:true }, { x:6, y:3, alive:true }, { x:6, y:5, alive:true }, { x:6, y:7, alive:true },
    { x:7, y:0, alive:true }, { x:7, y:2, alive:true }, { x:7, y:4, alive:true }, { x:7, y:6, alive:true }];

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
    	let data = JSON.parse(messageOutput.body);
    	console.log("A:"+data.author.name+" R:"+data.reciver.name+" Q:"+data.queue)
    	if(data.author.name == playerName && data.queue == opponentName) return 0;
    	
    	updateBoard(messageOutput.body);
    });
});

function updateBoard(data){
    let newBoardJSON = JSON.parse(data);
    
    let oldWhitePositions = whitePositions;
    let oldBlackPositions = blackPositions;
    
    whitePositions = Array.from(newBoardJSON.author.paws);
	blackPositions = Array.from(newBoardJSON.reciver.paws);
	
    	for(let i=0; i<12; i++){
    		if(oldWhitePositions[i].x != whitePositions[i].x && whitePositions[i].alive){
    			if(oldWhitePositions[i].y != whitePositions[i].y)
    				
    				if( Math.abs(oldWhitePositions[i].x-whitePositions[i].x) >=2){
    					let x_average = Math.abs(oldWhitePositions[i].x+whitePositions[i].x)/2;
    					let y_average = Math.abs(oldWhitePositions[i].y+whitePositions[i].y)/2;
    					//zbito czarnego
    					for(let j=0; j<12; j++){
        					if(blackPositions[j].x == x_average && blackPositions[j].y == y_average){
        						blackPositions[j].alive = false;
        						
        						sendGameboard("white");
        						drawPawns();
        					}
        				}
    				}
    		} else
    		if(oldBlackPositions[i].x != blackPositions[i].x && blackPositions[i].alive){
    			if(oldBlackPositions[i].y != blackPositions[i].y){
    				
    				if( Math.abs(oldBlackPositions[i].x-blackPositions[i].x) >=2){
    					let x_average = Math.abs(oldBlackPositions[i].x+blackPositions[i].x)/2;
    					let y_average = Math.abs(oldBlackPositions[i].y+blackPositions[i].y)/2;
    					//zbito bialego
    					for(let j=0; j<12; j++){
        					if(whitePositions[j].x == x_average && whitePositions[j].y == y_average){
        						whitePositions[j].alive = false;
        						
        						sendGameboard("black");
        						drawPawns(); 
        					}
    						
        				}
    				}
    			}
    				
    		}
    	}
    drawPawns();
    victory();
    if(newBoardJSON.queue == playerName)
    setDropDown();
    victory();
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
        if(whitePositions[i].alive == true){
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
            	sendGameboard("black");            
        });
        }
    }
    function setterB(i){
        blackList[i].DOMelement = document.getElementById("pawnBlack"+i);
        if(blackPositions[i].alive == true){
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
            	sendGameboard("white");             
        });
    }
    }
}
function victory(){
	let counter = 12;
	for( w of whitePositions){
		if(!w.alive){
			counter--;
		}
	}
	if( counter == 0 && whiteBlack == "white") {
		window.alert("Wygral "+opponentName);
		return 0;
	}
	counter = 12;
	for( b of blackPositions){
		if(!b.alive){
			counter--;
		}
	}
	if( counter == 0 && whiteBlack == "black") {
		window.alert("Wygral "+playerName);
		return 0;
	}
}

function sendGameboard(whiteBlackTemp){
	if(whiteBlackTemp == "white" && whiteBlack == "white"){
		stompClientWS.send("/movement", {}, JSON.stringify(
				{"author": { "name": playerName, 
					"paws": whitePositions
				},
				"reciver": { "name": opponentName, 
					"paws": blackPositions
				},
				"queue": playerName
				}));
	}else if(whiteBlackTemp == "black" && whiteBlack == "black"){
		stompClientWS.send("/movement", {}, JSON.stringify(
				{"author": { "name": playerName, 
					"paws": whitePositions
				},
				"reciver": { "name": opponentName, 
					"paws": blackPositions
				},
				"queue": playerName
				}));
	}else {
		stompClientWS.send("/movement", {}, JSON.stringify(
				{"author": { "name": playerName, 
					"paws": whitePositions
				},
				"reciver": { "name": opponentName, 
					"paws": blackPositions
				},
				"queue": opponentName
				}));
	}
}

function drawPawns(){
    
    let divPawns = document.getElementsByClassName("pawns")[0];
    divPawns.innerHTML="";
    for(let i=0; i<12; i++){//do liczby pionków w grze
    	if(whitePositions[i].alive == true)
        divPawns.innerHTML += "<img class=\"pawnWhite\" id='pawnWhite"+i+"' src=\""+srcWhite+"\" style=\"left:"+whitePositions[i].x*square+"px;top:"+whitePositions[i].y*square+"px;\" />";
    	if(blackPositions[i].alive == true)
        divPawns.innerHTML += "<img class=\"pawnWhite\" id='pawnBlack"+i+"' src=\""+srcBlack+"\" style=\"left:"+blackPositions[i].x*square+"px;top:"+blackPositions[i].y*square+"px;\" />";
    }
}

document.getElementById("cancelGame").addEventListener("click",()=>{
	let request = new XMLHttpRequest();
	request.open("POST", "http://localhost:8080/destroy", false);
	request.send(JSON.stringify({ "author": playerName, "reciver": opponentName, "decision": "cancel"}));
	window.open("/waitingRoom.html", "_top");
});



