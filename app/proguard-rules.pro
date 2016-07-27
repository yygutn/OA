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

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-dontwarn org.htmlcleaner.HtmlCleanerForAnt
-dontwarn org.htmlcleaner.JDomSerializer
-dontwarn org.**
-dontwarn com.hyphenate.**
-dontwarn com.fsck.k9.**
-dontwarn com.baidu.**
-dontwarn android.webkit.WebView
-dontwarn android.webkit.WebViewClient
#-dontwarn android.**
#-dontwarn com.**
-dontwarn com.helger.**
-dontwarn com.parse.**
-dontwarn com.lhh.ptrrv.library.PullToRefreshRecyclerView$InterOnScrollListener
-dontwarn

-dontnote org.apache.**
-dontnote internal.org.apache.**
-dontnote com.hyphenate.**
-dontnote com.baidu.**
#-dontnote com.fsck.k9.**
-dontnote org.openintents.**
-dontnote com.android.**
-dontnote android.net.http.**
-dontnote com.shizhefei.**
-dontnote bolts.**
-dontnote com.parse.**
-dontnote com.larswerkman.**
-dontnote com.fsck.k9.**
-dontnote android.net.compatibility.WebAddress
-dontnote vi.com.gdi.bgl.android.java.EnvDrawText
-dontnote vi.com.gdi.bgl.android.java.a
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class com.android.vending.licensing.ILicensingService
-keep public class org.**{*;}
-keep public class com.hyphenate.**{*;}
#-keep public class com.fsck.k9.**{*;}
-keep public class com.baidu.**{*;}
-keep public class com.helger.**{*;}
-keep public class com.parse.**{*;}
-keep public class com.nineoldandroids.view.ViewHelper
#-keep public class com.**{*;}
#-keep public class android.**{*;}
-keep public class android.net.http.SslCertificate
-keep public class android.net.http.SslError
-keep public class org.apache.http.params.HttpParams
#but not the descriptor class

##如果有引用v4包可以添加下面这行
#-keep class android.support.v4.** { *; }
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.app.Fragment
#
#
##如果引用了v4或者v7包，可以忽略警告，因为用不到android.support
#-dontwarn android.support.**

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

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

##混淆保护自己项目的部分代码以及引用的第三方jar包library（想混淆去掉"#"）
-libraryjars ../k9mail/libs/HoloColorPicker-debug.aar
-libraryjars ../k9mail/libs/openpgp-api-debug.aar
-libraryjars ../k9mail/libs/k9mail-library-debug.aar
-libraryjars ../easeUI/libs/BaiduLBS_Android.jar
-libraryjars ../easeUI/libs/hyphenatechat_3.1.3.jar
-libraryjars ../easeUI/libs/org.apache.http.legacy.jar
-libraryjars ../JumyFramework/libs/ViewpagerIndicator_1.0.6.jar
-libraryjars ../easeUIDemo/libs/bolts-tasks-1.4.0.jar
-libraryjars ../easeUIDemo/libs/parse-android-1.13.0.jar

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

