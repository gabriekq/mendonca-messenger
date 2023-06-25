/**
 * 
 */

const baseUrl = 'http://192.168.0.147:8081/';
var userName ;

function loadMainPage() {
	validateMenu();
	loadUserName();
	loadUsersAvailable();
    loadConversation();
}

function sendMenssage() {
	var chatTextElement = document.getElementById('chat6');
	var menssage = chatTextElement.value;

	sendRequest(menssage);
	updateScrem('You -> '+menssage);
	cleanTextFieldUser();
}


function updateScrem(menssage) {
	var chatScreem = document.getElementById('chat5');
	chatScreem.append(menssage);
	chatScreem.append('\n');
}

function cleanTextFieldUser() {
	var chatTextElement = document.getElementById('chat6');
	chatTextElement.value = '';
}

function sendRequest(menssage) {
    var selectDestination = document.getElementById('user-talk');
	var payloadMessage = JSON.stringify({ "sender": userName, "addressee":  selectDestination.value, "messageText": menssage });
	var xhttp = new XMLHttpRequest();
	var finalURl = baseUrl + 'message/send';

	xhttp.open('POST', finalURl, false);
	xhttp.setRequestHeader("Content-Type", "application/json");
	xhttp.send(payloadMessage);
}

function loadUserName() {
	var xhttp = new XMLHttpRequest();
	var finalURl = baseUrl + 'message/currentUser';

	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var elemTitle = document.getElementById('userName');
			userName = xhttp.responseText;
			elemTitle.append(' : ' + xhttp.responseText);
		}
	};
	xhttp.open('GET', finalURl, false);
	xhttp.send();
}

function loadUsersAvailable() {
	var xhttp = new XMLHttpRequest();
	var finalURl = baseUrl + 'message/usersAvailable/'+userName;

	xhttp.onreadystatechange = function() {

		if (this.readyState == 4 && this.status == 200) {
			arrNames = JSON.parse(xhttp.responseText);
			arrNames.forEach(addMenu)

		}
	}
	xhttp.open('GET', finalURl, false);
	xhttp.send();
}

function addMenu(item, index) {
	  var selectDestination = document.getElementById('user-talk');
	  var option = document.createElement("option");
	  option.text = item;
	  option.index = index;
	  selectDestination.add(option);
}

function validateMenu(){
 var selectDestination = document.getElementById('user-talk');
 var button = document.getElementById('btn-Send-message');
 
    if(selectDestination.value==='default'){
	  button.disabled = true
    }else{
	  button.disabled = false;
    }
    
     var heightForMenssage =  (window.screen.availHeight / 2)*1.20;
     var elemMessageView = document.getElementById('chat5');
     
     elemMessageView.style.height=heightForMenssage+'px';
}

function loadConversation(){

    var worker = new Worker("/javascript/workers.js?userName="+userName);
    var menssage;
    
        worker.onmessage = function(event) {
	    if(event.data===401){
		  location.reload();
	    }
	
         menssage = JSON.parse(event.data);
         menssage.forEach(addMessagesRetrieve);
        };
        
}

function addMessagesRetrieve(item, index){    
    var finalMenssageChat = item.sender+" -> "+item.messageText;
    updateScrem(finalMenssageChat);
}



