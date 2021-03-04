var exec = require('cordova/exec');

exports.getDeviceId = function (success, error) {
    exec(success, error, 'CordovaPluginPunchhHelper', 'getDeviceId', []);
};

exports.getUserAgent = function (success, error) {
    exec(success, error, 'CordovaPluginPunchhHelper', 'getUserAgent', []);
};
