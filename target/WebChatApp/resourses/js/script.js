"use strict";
var theMessage = function (text, username) {
    
	return {
		textMessage: text,
		user: username,
		id: 0
	};
};
var appState = {
                mainUrl : 'http://localhost:8080/chat',
                token : 'TN11EN' };
var editingMessage;
var messageList = [];
var nameUser="Unknown";

function run()
{
    
    restoreFromServer();
    
    var appContainer=document.getElementsByClassName('log_in')[0];
    appContainer.addEventListener("click", delegateUsername);
    appContainer = document.getElementsByClassName('username')[0];
    appContainer.addEventListener("keydown", delegateEnterPushUs); 
    appContainer = document.getElementsByClassName('sendBut')[0];
	appContainer.addEventListener("click", delegateEventClick);
    appContainer = document.getElementsByClassName('bottomRect')[0];
    appContainer.addEventListener("keydown", delegateEnterPush);

}
function delegateEnterPushUs(evtObj)
{
    if(evtObj.keyCode==13)
        initUsername(evtObj);
}
function delegateUsername(evtObj)
{
    if(evtObj.type=='click')
        initUsername(evtObj);
}
function delegateEnterPush(evtObj)
{
    if(evtObj.keyCode==13)
        onAddButtonClick(evtObj);
}
function delegateEventClick(evtObj) {
	if(evtObj.type === 'click'){
		onAddButtonClick(evtObj);
	}
	       
}
function onAddButtonClick(){
	var mesText = document.getElementsByClassName('bottomRect')[0];
	if(!mesText.innerText)
        return;
    addMessage(theMessage(mesText.innerText,nameUser));
    
	mesText.innerHTML = '';          
}
function addMessage(message) {
    if (!message.textMessage) {
		return;
	}
    
    Post(appState.mainUrl, JSON.stringify(message), function(){}, 	function(message) {
																		defaultErrorHandler(message);
																		wait();
																	});
}

function createAllMessages(allMessages) {
    
    var i;
    
    if (allMessages && allMessages.length) {
       // nameUser=allMessages[allMessages.length-1].user;
        
        
        for (i = 0; i < allMessages.length; i++) {
            messageList.push(allMessages[i]);
            restoreMessage(allMessages[i]);
        
    }}
    return;
}



function createMessage(username, textMessage)
{
    var text=document.createElement('span');
    text.setAttribute('class',"text");
    
    
    var user=document.createElement('span');
    user.setAttribute("class","user");
    user.appendChild(document.createTextNode(username));
    
    text.appendChild(document.createTextNode(": "+textMessage));
                        
    var deleteMessageButton = document.createElement("img");
    deleteMessageButton.setAttribute("id","deleteMessageButton");
    deleteMessageButton.setAttribute("src", "delete2.png");
    
    var editMessageButton=document.createElement("img");
    editMessageButton.setAttribute("id","editMessageButton");
    editMessageButton.setAttribute("src","edit.png");
    
    var oneMessage=document.createElement("div");
    oneMessage.setAttribute("id","oneMessage");
    
    oneMessage.appendChild(user);
    oneMessage.appendChild(text);
    oneMessage.appendChild(editMessageButton);
    oneMessage.appendChild(deleteMessageButton);
    
    
    var messages=document.getElementsByClassName('leftRect')[0];
	messages.appendChild(oneMessage);
    return oneMessage;
    
}
function restoreMessage(message) {
 
    if (!message.textMessage) {

		return;
	}
    var newMessage = createMessage(message.user, message.textMessage),
        messages = document.getElementsByClassName("leftRect")[0];

    newMessage.id = message.id;
    
	messages.appendChild(newMessage);
    //messageList.push(message);
    
    return;
}
function updateMessageList(newMessage, messageList) {
 
    messageList.textMessage = newMessage;
    
    return;
}
function initUsername()
{
    var nameText=document.getElementsByClassName('username')[0];
    if(nameText.innerText)
    {
       nameUser=nameText.innerText;
       nameText.innerHTML = ' ';
    }
}
function restoreFromServer() {
	var url = appState.mainUrl + "?token=" + appState.token;
	
    Get(url, 	function(responseText) {
            console.assert(responseText != null);
            var response = JSON.parse(responseText);
					appState.token = response.token;
					createAllMessages(response.messages);
        		}, 	
				function(message) {
					defaultErrorHandler(message);
					wait();
				});

	setTimeout(function() {
					restoreFromServer();
				}, 1000);
}

function Get(url, continueWith, continueWithError) {
	ajax('GET', url, null, continueWith, continueWithError);
}

function Post(url, data, continueWith, continueWithError) {
	ajax('POST', url, data, continueWith, continueWithError);	
}


function ajax(method, url, data, continueWith, continueWithError) {
	var xhr = new XMLHttpRequest();
	xhr.open(method, url, true);
	xhr.onload = function () {
		if (xhr.readyState !== 4) {
			indicatorOff();
			return;
		}

		if(xhr.status != 200) {
			continueWithError('Error on the server side, response ' + xhr.status);
			indicatorOff();
			return;
		}

		if(isError(xhr.responseText)) {
			continueWithError('Error on the server side, response ' + xhr.responseText);
			indicatorOff();
			return;
		}

		indicatorOn();
		continueWith(xhr.responseText);
	};    

    xhr.ontimeout = function () {
		indicatorOff();
    	continueWithError('very.cute.cat.controller.Server timed out !');
    }

    xhr.onerror = function (e) {
    	var errMsg = 'very.cute.cat.controller.Server connection error !\n'+
    	'\n' +
    	'Check if \n'+
    	'- server is active\n'+
    	'- server sends header "Access-Control-Allow-Origin:*"';

		indicatorOff();
        continueWithError(errMsg);
    };

    xhr.send(data);
}
function indicatorOn() {	
    var serverStatus=document.getElementsByClassName('showServerStatus')[0];
    if(serverStatus.innerHTML)
    {
        serverStatus.removeChild(serverStatus.firstChild);
    }
    
    var divItem=document.createElement('div');
    divItem.setAttribute('class','serverStatus');
    
    var text=document.createElement('div');
    text.setAttribute('class','statusText');
    text.appendChild(document.createTextNode('Server Status: On '));
                         
    /*var indicatorOnImg = document.createElement("img");
    indicatorOnImg.setAttribute("class","indicatorOnImg");
    indicatorOnImg.setAttribute("src", "images/apply1794.png");*/
   // alert('on');
    divItem.appendChild(text);
   // divItem.appendChild(indicatorOnImg);
    serverStatus.appendChild(divItem);
    
}

function indicatorOff() {
	var serverStatus=document.getElementsByClassName('showServerStatus')[0];
    if(serverStatus.innerHTML)
    {
        serverStatus.removeChild(serverStatus.firstChild);
    }
    
    var divItem=document.createElement('div');
    divItem.setAttribute('class','serverStatus');
        
    var text=document.createElement('div');
    text.setAttribute('class','statusText');
    text.appendChild(document.createTextNode('Server Status: Off '));
                         
    /*var indicatorOffImg = document.createElement("img");
    indicatorOffImg.setAttribute("class","indicatorOffImg");
    indicatorOffImg.setAttribute("src", "images/no7705.png");*/
  //  alert('off');
    divItem.appendChild(text);
    //divItem.appendChild(indicatorOffImg);
    serverStatus.appendChild(divItem);
    
}
function isError(text) {
	if(text == "")
		return false;
	
	try {
		var obj = JSON.parse(text);
	} catch(ex) {
		return true;
	}

	return !!obj.error;
}