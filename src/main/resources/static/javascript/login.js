



function authenticate(){

	var username = document.getElementById('user').value;
	var password = document.getElementById('password').value;
	var payloadAuth = JSON.stringify({"username":username,"password":password});

	console.log('payloadAuth: -> '+payloadAuth);
	
	var finalUrl = window.location.origin+'/authenticate';

	var xhttp = new XMLHttpRequest();
	xhttp.open('POST',finalUrl,false);
	xhttp.setRequestHeader("Content-Type", "application/json");
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			console.log(xhttp.responseText);
			localStorage.jwt = xhttp.responseText;
			window.location.href = window.location.origin+'/chat';
		}
	};
	
	console.log('username:'+username+'password'+password);
	xhttp.send(payloadAuth);
		
	}


