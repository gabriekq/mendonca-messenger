
const baseUrl = 'https://192.168.1.239:8443/';
var menssage;

setInterval(function() {
    console.log('get menssages');
    
    var parameters = {}
    location.search.slice(1).split("&").forEach( function(key_value) {
	 var kv = key_value.split("=");
	  parameters[kv[0]] = kv[1];
	  parameters['jwt']=kv[2];
     })

    var userName = parameters['userName'];
    var userJwt = parameters['jwt'];
    
    var xhttp = new XMLHttpRequest();
	var finalURl = baseUrl + 'message/retrieveMessages/'+userName;

	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {			
			menssage = xhttp.responseText;
            postMessage(menssage);
              
		}else{
			postMessage(this.status);
		}
			
	};
	xhttp.open('GET', finalURl, false);
	xhttp.setRequestHeader("Authorization", "Bearer "+userJwt);
	xhttp.send();
    
}, 600);
