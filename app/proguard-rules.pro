# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Jumy/SDK/android-sdk-macosx/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.tencent.**{*;}
-dontwarn com.tencent.**

-keep class tencent.**{*;}
-dontwarn tencent.**

-keep class qalsdk.**{*;}
-dontwarn qalsdk.**

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-keepattributes InnerClasses
-dontoptimize
-dontwarn org.htmlcleaner.HtmlCleanerForAnt
-dontwarn org.htmlcleaner.JDomSerializer
-dontwarn android.webkit.WebView
-dontwarn org.apache.http.params.HttpConnectionParams
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#如果有引用v4包可以添加下面这行
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment


#如果引用了v4或者v7包，可以忽略警告，因为用不到android.support
-dontwarn android.support.**

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}


#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}


#okio
-dontwarn okio.**
-keep class okio.**{*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义组件不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void init*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

##混淆保护自己项目的部分代码以及引用的第三方jar包library（想混淆去掉"#"）
-libraryjars libs/HoloColorPicker-debug.aar
-libraryjars libs/openpgp-api-debug.aar
-libraryjars libs/k9mail-library-debug.aar
#-libraryjars libs/BaiduLBS_Android.jar
#-libraryjars libs/hyphenatechat_3.1.3.jar
#-libraryjars libs/MiPush_SDK_Client_2_2_19.jar
#-libraryjars libs/org.apache.http.legacy.jar
#-libraryjars libs/parse-android-1.13.0.jar
#-libraryjars libs/umeng-analytics-v5.2.4.jar
#-libraryjars libs/umeng-update-v2.6.0.1.jar
#-libraryjars libs/ViewpagerIndicator_1.0.6.jar
#-libraryjars libs/com.umeng.message_v2.8.1.jar

###-------- pulltorefresh 相关的混淆配置---------
-dontwarn com.handmark.pulltorefresh.library.**
-keep class com.handmark.pulltorefresh.library.** { *;}
-dontwarn com.handmark.pulltorefresh.library.extras.**
-keep class com.handmark.pulltorefresh.library.extras.** { *;}
-dontwarn com.handmark.pulltorefresh.library.internal.**
-keep class com.handmark.pulltorefresh.library.internal.** { *;}

###--------------umeng 相关的混淆配置-----------
-keep class com.umeng.** { *; }
-keep class com.umeng.analytics.** { *; }
-keep class com.umeng.common.** { *; }
-keep class com.umeng.newxp.** { *; }
