/**
 * 
 */

'use strict'

var getFormatedTime = function (ms) {
    var allSeconds = Math.floor(ms / 1000);
    var hours = Math.floor(allSeconds / 3600);
    var allSecondsInHours = hours * 3600;
    var mins = Math.floor((allSeconds - allSecondsInHours) / 60)
    var allSecondsInMins = mins * 60;
    var secs = allSeconds - allSecondsInHours - allSecondsInMins;

    var leadZero = function (val) {
        return (val > 9) ? val : "0" + val;
    };

    return leadZero(hours) + ":" + leadZero(mins) + ":" + leadZero(secs);
};

var processGame = new function () {

    var self = this;
    var wasLogged;
    var wasStartedGame;

    var requestGameData = function (callback) {
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

    var handleGameData = function (data) {
        console.log(data);
        var isLogged = data.loginStatus === 'LOGGED';

        if (wasLogged !== isLogged) {
            if (isLogged) {
                if (typeof self.onlogin === 'function') self.onlogin(data.player);
            } else {
                if (typeof self.onlogout === 'function') self.onlogout();
            }
            wasLogged = isLogged;
        }

        if (isLogged) {

            var isGameStarted = data.gameState === 'GAMEPLAY';

            if (wasStartedGame !== isGameStarted) {
                if (isGameStarted) {
                    if (typeof self.ongamestart === 'function') self.ongamestart();
                } else {
                    if (typeof self.ongameend === 'function') self.ongameend();
                }
                wasStartedGame = isGameStarted;
            }

            if (isGameStarted) {
                if (typeof self.ongamedata === 'function') self.ongamedata(data.gameData);
            }
        }

        process();
    };

    var process = function () {
        setTimeout(function () { requestGameData(handleGameData); }, 200);
    };

    this.onlogout = function () {};

    this.onlogin = function (player) {};

    this.ongamestart = function () {};

    this.ongameend = function () {};

    this.ongamedata = function (gameData) {};

    this.start = function () {
        process();
    };

};

 $(document).ready(function () { 
    processGame.onlogout = function () { 
        showAlert('warning', 'Sorry, you have not been authenticated! '
            + 'You can authenticate at the <a href=\"http://localhost:8080\">login page</a>');  
    };

    processGame.onlogin = function (player) {
        showAlert('info', 'The game has not yet begun. Please wait for the start of the game.');

        $('#player-name').text(player.userName);
        $('#player-id').text(player.userId);
    };

    processGame.ongamestart = function () {
        hideAlert();
    }

    processGame.ongamedata = function (gameData) {
        $('#elapsed-time').text(getFormatedTime(gameData.elapsedTime));
        console.log(gameData);
        var opps = gameData.opponents;
        console.log(opps);

        $('div#opponents').html(createOpponentList(opps));
    }

    processGame.start();
});

 function createOpponentList(opponentArray) {
    var frag = document.createDocumentFragment();
    var list = document.createElement('ul');
    
    if (Object.prototype.toString.call(opponentArray) === '[object Array]') {
        for (var i = 0; i < opponentArray.length; i++) {
            var item = document.createElement('li');
            item.innerHTML = opponentArray[i];
            list.appendChild(item);
        }
    }

    frag.appendChild(list);
    return frag;
 }