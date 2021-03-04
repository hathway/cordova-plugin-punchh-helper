import { exec } from 'cordova';

var punchhExport = {};

punchhExport.getDeviceId = function (success, error) {
    exec(success, error, 'PunchhHelper', 'getDeviceId', []);
};

punchhExport.getUserAgent = function (success, error) {
    exec(success, error, 'PunchhHelper', 'getUserAgent', []);
};

export default punchhExport;
