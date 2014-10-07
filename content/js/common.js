'use strict';

/**
 * requests user authorization
 * @param serializevalue user data
 * @param success callback function
 * @param error callback function
 */
var loginRequest = function (userData, callback, err_callback) {
	
	$.ajax({
		url: "login",
		
		data: userData,
		
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
			$('#login-form').removeClass("hide") : $('#login-form').addClass("hide");
};

var showWelcome = function (userData) {
	if (typeof userData !== 'object') return;
	
	var userId = userData.userLoginData.userId;
	var userName = userData.userLoginData.userName;
	var sessionId = userData.userLoginData.sessionId;
	
	$('span#session-id-text').text(sessionId);
	
	var showUserLoginInfo = function () {
		$('span#user-name-text').text(userName);
		$('span#user-id-text').text(userId);
		$('#user-login-info p').removeClass('hide');
	};
	
	/**
	 * Shows alert in style of Foundation framework.
	 * @param type of alert: 'success' || 'warning' || 'info' || 'alert' || 'secondary'
	 * @param alert message
	 * @param if true shows close button in alert message
	 */
	var showAlert = function (type, message, close) {
		if (close) 
			message += '<a href="#" class="close">&times;</a>';
		$('#alert').removeClass().addClass('alert-box ' + type).html(message);
	}
	
	var showStatus = {
		'newSession': function () {
			showLoginForm();
		},
		'waiting': function () {
			showAlert('info', 'Wait for authorization, please...');
		},
		'logged': function () {
			showAlert('success', 'You were successfully authenticated :-)', true);
			showUserLoginInfo();
		},
		'notLogged': function () {
			showAlert('warning', 'Sorry, you can not be authenticated! '
					+ 'You can <a href=\"http://localhost:8080\">try to sign in again</a>');
		}
	};
	
	var currentStatus = '';
	
	if (userName === '')
		currentStatus = 'newSession';
	else currentStatus = (userId === '') ? 'notLogged' : 
			(userId === '0') ? 'waiting' : 'logged';
		
	if (currentStatus === 'waiting') 
		setTimeout(function () { loginRequest("", showWelcome) }, 1000);
	
	showStatus[currentStatus]();
}; //showWelcome

var login = function () {
	showLoginForm(false);
	loginRequest($('#login-form').serializeArray(), showWelcome);
	$('#login-form').trigger('reset');
};

$(document).ready(function () { 
	$('#submit-button').on('click', login);
	$('#login-form').on("keypress", function(e) {
		var code = e.keyCode || e.which; 
		if (code == 13) {               
			e.preventDefault();
		    login();
		}
	});
	
	loginRequest("", showWelcome);
});