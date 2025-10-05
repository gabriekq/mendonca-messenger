/**
 *  
 */

const baseUrl = 'https://192.168.0.225:8443/';
var jwtUser = localStorage.jwt;
var userName;
const usersLogged = new Map();
let audioChunks = [];
var audioDataBase64;
var xhttpRequest = new XMLHttpRequest();
var messageReceived;

function loadMainPage() {
	validateMenu();
	loadUserName();
	loadUsersAvailable();
	ajustScreenSize();

	loadConversation();
}

function sendMenssage() {

	if (validToSend()) {
		var chatTextElement = document.getElementById('chat6');
		var menssage = chatTextElement.value;
		sendRequest(menssage);
		updateScrem(menssage);
	}
	cleanTextFieldUser();
	validateButton();
}


function updateScrem(menssage) {
	var chatScreem = document.getElementById('chat5');

	if (validateChatTextField()) {
		chatScreem.append('You -> ' + menssage);
		chatScreem.append('\n');
	}

	if (validateAudioChunksSend()) {

		var selectDestination = document.getElementById('user-talk');
		let item = { "sender": selectDestination.value, "audioData": audioDataBase64 };
		createAudioScreen(item, '#FFD700');
	}
	chat5.scrollTop = chat5.scrollHeight;
}

function cleanTextFieldUser() {
	var chatTextElement = document.getElementById('chat6');
	chatTextElement.value = '';
	audioChunks = [];
	audioDataBase64 = null;
}


function sendRequest(menssage) {

	var selectDestination = document.getElementById('user-talk');
	var payloadMessage = JSON.stringify({ "sender": userName, "addressee": selectDestination.value, "messageText": menssage, "audioData": audioDataBase64 });

	var finalURl = baseUrl + 'message/send';

	xhttpRequest.open('POST', finalURl, false);
	xhttpRequest.setRequestHeader("Content-Type", "application/json");
	xhttpRequest.setRequestHeader("Authorization", "Bearer " + jwtUser);
	xhttpRequest.send(payloadMessage);
}

function loadUserName() {
	
	var finalURl = baseUrl + 'message/currentUser';

	xhttpRequest.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var elemTitle = document.getElementById('userName');
			userName = xhttpRequest.responseText;
			elemTitle.append(' : ' + xhttpRequest.responseText);
		}
	};
	xhttpRequest.open('GET', finalURl, false);
	xhttpRequest.setRequestHeader("Authorization", "Bearer " + jwtUser);
	xhttpRequest.send();
}

function loadUsersAvailable() {

	var finalURl = baseUrl + 'message/usersAvailable/' + userName;

	xhttpRequest.onreadystatechange = function() {

		if (this.readyState == 4 && this.status == 200) {
			
			arrNames = JSON.parse(xhttpRequest.responseText);
			arrNames.forEach(addMenu);
		}
	}
	xhttpRequest.open('GET', finalURl, false);
	xhttpRequest.setRequestHeader("Authorization", "Bearer " + jwtUser);
	xhttpRequest.send();
}

function addMenu(item, index) {
	var selectDestination = document.getElementById('user-talk');
	var option = document.createElement("option");
	option.text = item;
	option.index = index;
	selectDestination.add(option);

	var elemTextArea = document.createElement('div');
	elemTextArea.id = 'chat5'
	elemTextArea.className = 'chat1'
	elemTextArea.style.width = '80%';
	elemTextArea.style.height = '40em';
	elemTextArea.style.backgroundColor = "#e6f3fb";
	elemTextArea.disabled = 'disabled';

	usersLogged.set(item, elemTextArea);
}

function validateMenu() {
	var selectDestination = document.getElementById('user-talk');
	validateButton();
	if (selectDestination.selectedIndex !== -1) {
		var elemtTextArea = document.getElementById('chat5');
		elemtTextArea.replaceWith(usersLogged.get(selectDestination.value));
		selectDestination.options[selectDestination.selectedIndex].style.fontWeight = 'normal';
	}

}

function loadConversation() {

	var worker = new Worker("/javascript/workers.js?userName=" + userName + "?jwt=" + jwtUser);
	
	worker.onmessage = function(event) {

		console.log('Receve message' + event.data);
		if (event.data === 401) {
			window.location.href = window.location.origin + '/myLogin';
		}
        
        statusReceved=event.data; 
        if(statusReceved==1){
	    downloadMessage();
	    messageReceived=JSON.parse(messageReceived);
	    messageReceived.forEach(addMessagesRetrieve);
		}

	};

}

function downloadMessage(){
	var finalURl = baseUrl + 'message/retrieveMessages/';
	xhttpRequest.onreadystatechange = function() {
		
		if (this.readyState==4 && this.status==200 ) {
			console.log('Receve message' + xhttpRequest.responseText);			
			messageReceived= xhttpRequest.responseText;  
		}
		
	};
	
	xhttpRequest.open('GET', finalURl, false);
	xhttpRequest.setRequestHeader("Authorization", "Bearer "+jwtUser);
	xhttpRequest.send();
}


function addMessagesRetrieve(item,index) {
	var textAreaTarget = usersLogged.get(item.sender);
	console.log('item ->>' + item.sender);
	highlightUserUnselect(item.sender);

	if (item.messageText.length > 0) {
		textAreaTarget.append(item.sender + " -> " + item.messageText);
		textAreaTarget.append('\n');
	}

	if ((item.audioData != null) && (item.audioData.length > 0)) {
		createAudioScreen(item, '#9acd32');
	}

}

function sendMenssageArea(event) {
	var code = event.code;
	validateButton();

	if (code == "Enter") {
		sendMenssage();
		event.preventDefault();
	}

}

function validToSend() {
	if ((validateSelectDestination()) && ((validateChatTextField()) || (validateAudioChunksSend()))) {
		return true;
	} else {
		return false;
	}
}

function validateButton() {
	var button = document.getElementById('btn-Send-message');

	setTimeout(() => {
		if (validToSend()) {
			button.disabled = false;
		} else {
			button.disabled = true;
		}

	}, 100);
}

function ajustScreenSize() {
	var heightForMenssage = (window.screen.availHeight / 2) * 1.20;
	var elemMessageView = document.getElementById('chat5');
	elemMessageView.style.height = heightForMenssage + 'px';
}


navigator.mediaDevices.getUserMedia({ audio: true }).then(stream => {

	var button = document.getElementById('btn-voice-message');

	audioRecorder = new MediaRecorder(stream);

	audioRecorder.ondataavailable = event => {
		audioChunks.push(event.data);
	}

	audioRecorder.onstop = event => {
		convertBobToBase64();
		validateButton();
	}

	button.addEventListener('click', function(event) {

		if (button.textContent === 'speak') {
			console.log('record has started');
			audioChunks = [];
			audioRecorder.start();
			button.innerText = 'stop';
		} else {
			audioRecorder.stop();
			button.innerText = 'speak';
		}
	});


}).catch(error => {
	console.log('error has occurred ' + error);
});


function validateSelectDestination() {
	var selectDestination = document.getElementById('user-talk');
	if (selectDestination.selectedIndex !== -1) {
		return true;
	} else {
		return false;
	}
}

function validateChatTextField() {
	var textElem = document.getElementById('chat6');
	if (textElem.value.trim().length > 0) {
		return true;
	} else {
		return false;
	}
}

function validateAudioChunksSend() {

	if (audioChunks.length > 0) {
		return true;
	} else {
		return false;
	}

}

function convertBobToBase64() {
	const blobObj = new Blob(audioChunks, { type: 'audio/ogg; codecs=opus' });
	var reader = new FileReader();
	reader.readAsDataURL(blobObj);

	reader.onload = function() {
		audioDataBase64 = reader.result;
		return;
	}
}

function createAudioScreen(item, color) {

	var screenMenssage = usersLogged.get(item.sender);

	var divElement = document.createElement('div');
	divElement.style.backgroundColor = color;
	divElement.style.width = '430px';
	divElement.style.fontWeight = 'bold';
	divElement.id = 'audioWrapper'

	var audioElement = document.createElement('audio');
	audioElement.id = 'audioItem';
	audioElement.controls = true;
	audioElement.type = 'audio/ogg';
	audioElement.src = item.audioData;

	if (color === '#FFD700') {
		divElement.append('You -> ');
	} else {
		divElement.append(item.sender + " -> ");
	}

	divElement.append(audioElement);
	screenMenssage.append(divElement);
	screenMenssage.append('\n');
}

function highlightUserUnselect(userName) {

	var selectDestination = document.getElementById('user-talk');


	for (var index = 0; index < selectDestination.options.length; index++) {

		if (selectDestination.options[index].text === userName && selectDestination.options[index].selected == false) {
			selectDestination.options[index].style.fontWeight = 'bold';
			break;
		}

	}

}

function logOut() {

	var finalURl = baseUrl + 'exit';
	
	xhttpRequest.onreadystatechange = function() {

		if (this.readyState == 4 && this.status == 401) {
			  localStorage.jwt=null;
              jwtUser=null;
              
	        window.location.href = window.location.origin + '/myLogin';   
		}

	}
	xhttpRequest.open('POST', finalURl, false);
	xhttpRequest.setRequestHeader("Authorization", "Bearer " + jwtUser);
	xhttpRequest.send();
}









