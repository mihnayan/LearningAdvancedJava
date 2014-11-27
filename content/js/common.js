/**
 * 
 */

 'use strict'

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
};

var hideAlert = function () {
    var el = $('#alert');
    if (!el.hasClass('hide')) {
        el.removeClass().addClass('hide');
    }
}