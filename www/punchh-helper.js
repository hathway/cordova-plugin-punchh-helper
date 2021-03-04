// Cordova Format: exec(<successFunction>, <failFunction>, <service>, <action>, [<args>]);
// Simplify to remove <service> and <failFunction>
const exec = (action, args, callback, errCallback) => {
    cordova.exec(
        callback,
        errCallback,
        'PunchhHelper',
        action,
        args
    );
};

var punchhExport = {};

punchhExport.getDeviceId = function (callback, err) {
    exec('getDeviceId', null, callback, err);
}

punchhExport.getUserAgent = function (callback, err) {
    exec('getUserAgent', null, callback, err);
}


module.exports = punchhExport;