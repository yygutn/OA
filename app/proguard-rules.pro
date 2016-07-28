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
# keep住源文件以及行号
-keepattributes SourceFile,LineNumberTable
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#四大组件&&相关
-keep class * extends android.app.Application
-keep class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider
-keep class * extends android.app.backup.BackupAgentHelper
-keep class * extends android.preference.Preference
-keep class com.android.vending.licensing.ILicensingService
#but not the descriptor class


-dontnote org.apache.**
-dontnote internal.org.apache.**
-dontnote com.hyphenate.**
-dontnote com.baidu.**
-dontnote com.fsck.k9.**
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

#如果有引用v4&&v7包可以添加下面这行
-keep class android.support.v4.** { *; }
-keep class * extends android.support.v4.**
-keep class * extends android.app.Fragment
-keep class * extends android.app.Activity


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

###----------环信
-keep class com.hyphenate.**{*;}
-dontwarn com.hyphenate.**

###---自己的代码
-keep class cn.edu.jumy.oa.bean.**{*;}
-keep class cn.edu.jumy.oa.widget.**{*;}

###---org
-keep class org.apache.http.** {*;}
-dontwarn org.apache.http.**

###---parse
-keep class com.parse.**{*;}
-dontwarn com.parse.**

###---百度地图
-keep class com.baidu.**{*;}
-dontwarn com.baidu.**

###---k9-mail
-keep class com.fsck.k9.**{*;}
-dontwarn com.fsck.k9.**

###---拉动刷新的RecyclerView
#-keep com.lhh.ptrrv.**{*;}
#-dontwarn com.lhh.ptrrv.**

###---com.helper
-keep class com.helger.**{*;}
-dontwarn com.helger.**

###---ssl
-keep class android.net.http.SslCertificate
-keep class android.net.http.SslError

###---webview
-dontwarn android.webkit.WebView
-dontwarn android.webkit.WebViewClient
-dontwarn org.htmlcleaner.HtmlCleanerForAnt
-dontwarn org.htmlcleaner.JDomSerializer

###---AA注解
-keep class org.androidannotations.** {*;}
-dontwarn org.androidannotations.**

###---org.jdom2 --xml解析
-keep class org.jdom2.**{*;}
-dontwarn org.jdom2.**

###---nineoldandroid --因为不用，所以忽略提醒
-dontwarn com.nineoldandroids.view.ViewHelper

###---