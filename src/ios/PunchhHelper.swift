
import Foundation

@objc(PunchhHelper)
class PunchhHelper : CDVPlugin {
    @objc(getDeviceId:)
    func getDeviceId(command : CDVInvokedUrlCommand) {
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: Bundle.main.identifier)
        self.commandDelegate.send(result, callbackId: command.callbackId)
    }

    @objc(getUserAgent:)
    func getUserAgent(command : CDVInvokedUrlCommand) {
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: userAgent())
        self.commandDelegate.send(result, callbackId: command.callbackId)
    }

    func userAgent() -> String {
    String(format: "%@/%@/%@ (%@; %@ %@; Scale/%0.2f)",
           Bundle.main.appName(),
           Bundle.main.versionNumber(),
           Bundle.main.buildNumber(),
           UIDevice.current.model,
           UIDevice.current.systemName,
           UIDevice.current.systemVersion,
           UIScreen.main.scale)
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

    /// Return the short bundle version string (usually application version)
    /// - returns: version number as string
    public func versionNumber() -> String {
        let key = "CFBundleShortVersionString"
        return object(forInfoDictionaryKey: key) as? String ?? ""
    }

    /// Return the bundle version string (usually the application build number)
    /// - returns: build number as string
    public func buildNumber() -> String {
        let key = "CFBundleVersion"
        return object(forInfoDictionaryKey: key) as? String ?? ""
    }

    /// Return the full version including build number
    /// - returns: version and build number as string
    public func versionAndBuild() -> String {
        let version = versionNumber()
        let build = buildNumber()
        return "\(version).\(build)"
    }

    /// App name
    /// - Returns: App name
    func appName() -> String {
        object(forInfoDictionaryKey: "CFBundleName") as? String ?? ""
    }
}
