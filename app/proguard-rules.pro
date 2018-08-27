# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/louis/Library/Android/sdk/tools/proguard/proguard-android.txt
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
#基本指令
-optimizationpasses 5  #指定代码的压缩级别 0 - 7
-dontskipnonpubliclibraryclassmembers  #如果应用程序引入的有jar包，并且想混淆jar包里面的class
-printmapping proguardMapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  #混淆时采用的算法
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature  #过滤泛型（不写可能会出现类型转换错误，一般情况把这个加上就是了）
-dontpreverify  #预校验

-dontwarn okio.**
-dontwarn java.nio.**

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.smartstudy.xxd.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-ignorewarnings

-keep class * {
    public private *;
}
-keep class * {
    public protected *;
}
#jpush
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#Router
-keep class com.smartstudy.router.** {*;}
-keep class * implements com.smartstudy.router.RouteInterceptor {*;}


-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep class com.umeng.commonsdk.** {*;}
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}

-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.umeng.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keepattributes Signature

#growingio
-keep class com.growingio.android.sdk.** {
 *;
}
-dontwarn com.growingio.android.sdk.**
-keepnames class * extends android.view.View

-keep class * extends android.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
-keep class android.support.v4.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
-keep class * extends android.support.v4.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

# Config need by TinkerPatch
-keep class com.tinkerpatch.sdk.TinkerPatch { *; }
-keep class com.tinkerpatch.sdk.BuildConfig { *; }

-keep class com.tinkerpatch.sdk.TinkerPatch$Builder { *; }
-keep class com.tinkerpatch.sdk.server.RequestLoader { *; }
-keep class com.tinkerpatch.sdk.util.ContentLengthInputStream { *; }
-keep interface com.tinkerpatch.sdk.server.model.DataFetcher { *; }
-keep interface com.tinkerpatch.sdk.server.model.DataFetcher$DataCallback { *; }
-keep class com.tinkerpatch.sdk.server.model.TinkerClientUrl { *; }
-keep class com.tinkerpatch.sdk.server.callback.** { *; }
-keep class com.tinkerpatch.sdk.tinker.callback.** { *; }
-keep public class * extends android.app.Application
-keep class com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike { *; }
-keep class com.tencent.tinker.** { *; }

# Config from tinker
-dontwarn com.tencent.tinker.anno.AnnotationProcessor
-keep @com.tencent.tinker.anno.DefaultLifeCycle public class *
-keep public class * extends android.app.Application {
    *;
}

-keep public class com.tencent.tinker.loader.app.ApplicationLifeCycle {
    *;
}
-keep public class * implements com.tencent.tinker.loader.app.ApplicationLifeCycle {
    *;
}

-keep public class com.tencent.tinker.loader.TinkerLoader {
    *;
}
-keep public class * extends com.tencent.tinker.loader.TinkerLoader {
    *;
}
-keep public class com.tencent.tinker.loader.TinkerTestDexLoad {
    *;
}
-keep public class com.tencent.tinker.loader.TinkerTestAndroidNClassLoader {
    *;
}

# RongCloud SDK
-keep class io.rong.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent {*;}
-dontwarn io.rong.push.**
-dontnote com.xiaomi.**
-keep public class com.google.firebase.* {*;}
-dontnote com.google.android.gms.gcm.**
-dontnote io.rong.**
-keep class com.smartstudy.xxd.receiver.RongNotificationReceiver {*;}

#华为 push
-ignorewarning -keepattributes *Annotation*
-keepattributes Exceptions -keepattributes InnerClasses -keepattributes Signature
# hmscore-support: remote transport
-keep class * extends com.huawei.hms.core.aidl.IMessageEntity { *; }
# hmscore-support: remote transport
-keepclasseswithmembers class * implements com.huawei.hms.support.api.transport.DatagramTransport {
<init>(...); }
# manifest: provider for updates
-keep public class com.huawei.hms.update.provider.UpdateProvider { public *; protected *; }
-ignorewarning
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

-dontwarn com.xiaomi.mipush.sdk.**
-keep public class com.xiaomi.mipush.sdk.* {*; }
-keep public class com.google.firebase.* {*;}