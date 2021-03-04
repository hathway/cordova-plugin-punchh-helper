package com.wearehathway.cordova.punchh;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * This class echoes a string called from JavaScript.
 */
public class PunchhHelper extends CordovaPlugin {

  private PunchhDeviceIdHelper deviceIdHelper;
  private Activity cordovaActivity;
  private Context applicationContext;
  private String userAgent;

  @Override
  public void pluginInitialize() {
    this.deviceIdHelper = new PunchhDeviceIdHelper();
    this.cordovaActivity = this.cordova.getActivity();
    this.applicationContext = cordovaActivity.getApplicationContext();
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("getUserAgent")) {
      String message = args.getString(0);
      String userAgentValue = this.getUserAgent();
      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, userAgentValue);
      callbackContext.sendPluginResult(pluginResult);
      return true;
    } else if (action.equals("getDeviceId")) {
      String deviceId = this.deviceIdHelper.generateDeviceId(applicationContext);
      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, deviceId);
      callbackContext.sendPluginResult(pluginResult);
      return true;
    }
    return false;
  }

  private String getUserAgent() {
    if (userAgent == null) {
      try {
        PackageInfo pInfo = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0);
        String appId = pInfo.packageName;
        String version = pInfo.versionName;
        String verCode = String.valueOf(pInfo.versionCode);
        String deviceManufacturer = android.os.Build.MANUFACTURER;
        String modelNumber = android.os.Build.MODEL;
        String androidVersion = android.os.Build.VERSION.RELEASE;
        String ScreenDensity = getDensityName();
        userAgent = appId + "/" + version + "/" + verCode + "(Android;" + deviceManufacturer + ";" + modelNumber + ";" + androidVersion + ";" + ScreenDensity + ")";
      } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
      }
    }

    return userAgent;
  }

  private String getDensityName()
  {
    float density = this.applicationContext.getResources().getDisplayMetrics().density;
    if (density >= 4.0)
    {
      return "xxxhdpi";
    }
    if (density >= 3.0)
    {
      return "xxhdpi";
    }
    if (density >= 2.0)
    {
      return "xhdpi";
    }
    if (density >= 1.5)
    {
      return "hdpi";
    }
    if (density >= 1.0)
    {
      return "mdpi";
    }
    return "ldpi";
  }
}


class PunchhDeviceIdHelper {

  private static String INSTALLATION = "CONSTANT_INSTALLATION_APP_GENERATED_ID";
  private static String DEVICE_ID = "DEVICE_ID";

  private static String PREFS_NAME = "PUNCHH_PREFS";

  private String randomId() {
    return String.valueOf(new Random().nextLong());
  }

  private SharedPreferences getSharedPreferences(Context context) {
    return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
  }

  public String generateDeviceId(Context context) {
    SharedPreferences preferences = getSharedPreferences(context);
    String deviceId = preferences.getString(DEVICE_ID, "");

    if (deviceId == null || deviceId.isEmpty()) {
      File installationFile = new File(context.getFilesDir(), INSTALLATION);
      if (!installationFile.exists()) {
        UUID deviceUuid = new UUID(randomId().hashCode(), context.getApplicationContext().getPackageName().hashCode());
        deviceId = deviceUuid.toString();
        try {
          writeInstallationFile(installationFile, deviceId);
        } catch (IOException e) {
          e.printStackTrace();
        }
        preferences.edit().putString(DEVICE_ID, deviceId).apply();
        return deviceId;
      }
      try {
        deviceId = readInstallationFile(installationFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
      preferences.edit().putString(DEVICE_ID, deviceId).apply();
    }
    return deviceId;
  }

  private String readInstallationFile(File installation) throws IOException {
    RandomAccessFile f = new RandomAccessFile(installation, "r");
    byte[] bytes = new byte[(int) f.length()];
    f.readFully(bytes);
    f.close();
    return new String(bytes);
  }

  private void writeInstallationFile(File installation, String id) throws IOException {
    FileOutputStream output = new FileOutputStream(installation);
    output.write(id.getBytes());
  }
}
