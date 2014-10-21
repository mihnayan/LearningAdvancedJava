/**
 * 
 */

 'use strict'

 var requestUsersData = function (callback) {
    $.ajax({
        url: "gameData",
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

 var loadUsers = function (data) {
    console.log(data);
    var playerName = data.player.userName;

    var showPlayerInfo = function () {
        console.log("ok");
    };

    playerName && playerName.trim() !== '' ?
        showPlayerInfo() : 
        showAlert('warning', 'Sorry, you are not authenticated! '
            + 'You can go to <a href=\"http://localhost:8080\">the authentication page</a>');

 };

 $(document).ready(function () { 
    console.log('ok');
    requestUsersData(loadUsers);
});