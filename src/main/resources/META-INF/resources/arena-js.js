
let whitePositions = [{ x:0, y:1 }, { x:0, y:3 }, { x:0, y:5 }, { x:0, y:7 },
    { x:1, y:0 }, { x:1, y:2 }, { x:1, y:4 }, { x:1, y:6 },
    { x:2, y:1 }, { x:2, y:3 }, { x:2, y:5 }, { x:2, y:7 }];
let blackPositions = [{ x:5, y:0 }, { x:5, y:2 }, { x:5, y:4 }, { x:5, y:6 },
    { x:6, y:1 }, { x:6, y:3 }, { x:6, y:5 }, { x:6, y:7 },
    { x:7, y:0 }, { x:7, y:2 }, { x:7, y:4 }, { x:7, y:6 }];

// Pawn Class
class Pawn{
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
} //-------- Pawn Class

// Declaration of new object Pawn
let whiteList = [], blackList = [];
for(let i=0; i<12; i++){ 
    whiteList.push( new Pawn(whitePositions[i].x, whitePositions[i].y) );
    blackList.push( new Pawn(blackPositions[i].x, blackPositions[i].y) );
}
// Declaration of const px/square on board
const square = 50;
// Declaration of src ob pawns sprites
const srcWhite = "white.png";
const srcBlack = "black.png";

// START
//setPawnsAtStartPosition()

//setInterval( drawPawnsSprites, 1000);
let stompClientWS = Stomp.client("ws://localhost:8080/get/Board1");
stompClientWS.connect({"Access-Control-Allow-Origin":"*"}, function(frame) {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClientWS.subscribe('/get/messages', function(messageOutput) {
        console.log(JSON.parse(messageOutput.body));
    });
});
function drawPawnsSprites(){
    try {
        let request = new XMLHttpRequest();
        request.open("GET", "http://localhost:8080/getBoard?playerName="+"Kamil", false); //Player session name
        request.send(null);
        request.onreadystatechange = updateBoard(request.response);
    } catch (error) {
        console.log(error)
    }

    function updateBoard(data){
        let newBoardJSON = JSON.parse(data);
        console.log(Array.from(newBoardJSON.player1.paws))
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



