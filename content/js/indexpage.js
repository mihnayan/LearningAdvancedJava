'use strict';

/**
 * requests user authorization
 * @param success callback function
 */
var loginRequest = function (callback) {
	$.ajax({
		url: "login",
		type: "GET",
		dataType: "json",
		success: function (json) {
			callback(json);
		},
		error: function (xhr, status, errorThrown) {
			 console.log("Error: " + errorThrown);
			 console.log("Status: " + status);
			 console.dir(xhr);
		}
	});
};

var showLoginForm = function (yes) {
	typeof yes === 'undefined' || yes ? 
			$('#login-form').removeClass("hide").trigger('reset') : 
				$('#login-form').addClass("hide");
};

var showWelcome = function (userData) {
	if (typeof userData !== 'object') return;
	console.log(userData);
	var userId = userData.userId;
	var userName = userData.userName;
	var sessionId = userData.sessionId;
	
	$('span#session-id-text').text(sessionId);
	
	var showUserLoginInfo = function () {
		$('span#user-name-text').text(userName);
		$('span#user-id-text').text(userId);
		$('#user-login-info p').removeClass('hide');
		$('#game-link').removeClass('hide');
	};
	
	var handleLoginStatus = {
		'NEW': function () {
			showLoginForm();
		},
		'WAITING': function () {
			showAlert('info', 'Wait for authorization, please...');
			setTimeout(function () { loginRequest(showWelcome) }, 1000);
		},
		'LOGGED': function () {
			showAlert('success', 'You were successfully authenticated :-)', true);
			showUserLoginInfo();
		},
		'FAILED': function () {
			showAlert('warning', 'Sorry, you can not be authenticated with username <strong> \"'
					+ userName + '\"</strong>! '
					+ 'You can <a href=\"http://localhost:8080\">try to sign in again</a>');
		}
	};
	
	console.log(userData.loginStatus);
	handleLoginStatus[userData.loginStatus]();
}; //showWelcome

$(document).ready(function () { 
	var login = function () {
		$.post('login', $('#login-form').serializeArray(), showWelcome); 
		showLoginForm(false);
	};
	
	$('#submit-button').on('click', login);
	$('#login-form').on("keypress", function(e) {
		var code = e.keyCode || e.which; 
		if (code == 13) {               
			e.preventDefault();
		    login();
		}
	});
	
	loginRequest(showWelcome);
});