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

 var playersInfo = function (data) {
    console.log(data);
    var player = data.player;
    var opponent = data.opponent;

    var showPlayer = function () {
        $('#player-name').text(player.userName);
        $('#player-id').text(player.userId);
    };

    playerName && playerName.trim() !== '' ?
        showPlayer() : 
        showAlert('warning', 'Sorry, you are not authenticated! '
            + 'You can go to <a href=\"http://localhost:8080\">the authentication page</a>');

 };

 $(document).ready(function () { 
    requestUsersData(playersInfo);
});