# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in X:\Android\sdk/tools/proguard/proguard-android.txt
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

###Various

-dontwarn com.github.mikephil.charting.**
-dontwarn com.squareup.picasso.**
-dontwarn okio.**

# Google Support
-keep class android.support.** { *; }
-keep interface android.support.** { *; }

# Retrofit Config

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# CPORM CONFIGURATION

-keepattributes *Annotation*
-keepclassmembers class ** {
    @za.co.cporm.model.annotation.* *;
}

-keep class za.co.omnia.gis.domain.** { *; }
-keep class za.co.cporm.** { *; }
-dontwarn za.co.cporm.**

# BUTTEFKNIFE CONFIGURATION

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# OTTO CONFIGURATION

-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

#  MP CHARTS
-keep class com.github.mikephil.charting.** { *; }

# Feather
-keepattributes *Annotation*
-keepclassmembers class ** {
    @javax.inject.Inject public *;
    @javax.inject.Singleton public *;
    @org.codejargon.feather.Provides public *;
}
# JTS
# -dontwarn com.vividsolutions.** { *; }
-dontwarn com.vividsolutions.jts.**
# Spotlight Showcase

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

