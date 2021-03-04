
import Foundation
class PunchhHelper : CDVPlugin {
    @objc(getDeviceId:)
    func getDeviceId(command : CDVInvokedUrlCommand) {
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: Bundle.main.identifier)
        self.commandDelegate.send(result, callbackId: command.callbackId)
    }

    @objc(getUserAgent:)
    func getUserAgent(command : CDVInvokedUrlCommand) {
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: Bundle.main.punchhUserAgent)
        self.commandDelegate.send(result, callbackId: command.callbackId)
    }
}

extension Bundle {
    var identifier: String {
        var systemInfo = utsname()
        uname(&systemInfo)
        let machineMirror = Mirror(reflecting: systemInfo.machine)
        return machineMirror.children.reduce("") { identifier, element in
            guard let value = element.value as? Int8, value != 0 else { return identifier }
            return identifier + String(UnicodeScalar(UInt8(value)))
        }
    }

    /// Punchh UserAgent according to the specs https://developers.punchh.com/mobile-app-intg/user-agent
    var punchhUserAgent: String {
        let appIdentifier = bundleIdentifier?.split(".").last ?? ""
        let iOSVersion = UIDevice.current.systemVersion
        let scale = UIScreen.main.scale
        return "\(appIdentifier)/\(versionNumber())/\(buildNumber())(\(identifier);iOS;\(iOSVersion):\(scale))"
    }
}
