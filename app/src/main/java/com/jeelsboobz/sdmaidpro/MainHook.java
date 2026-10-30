package com.jeelsboobz.sdmaidpro;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    private static final String TARGET_PKG = "eu.darken.sdmse";
    private static final String INFO_CLASS =
            "eu.darken.sdmse.common.upgrade.core.UpgradeRepoFoss$Info";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!TARGET_PKG.equals(lpparam.packageName)) return;

        try {
            Class<?> infoClass = XposedHelpers.findClass(INFO_CLASS, lpparam.classLoader);

            XposedBridge.hookAllConstructors(infoClass, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    try {
                        XposedHelpers.setBooleanField(param.thisObject, "isPro", true);
                    } catch (Throwable t) {
                        XposedBridge.log("[Hook] setBooleanField gagal: " + t);
                    }
                }
            });

            XposedBridge.log("[Hook] Hook terpasang pada " + INFO_CLASS);
        } catch (Throwable t) {
            XposedBridge.log("[Hook] " + t);
        }
    }
}