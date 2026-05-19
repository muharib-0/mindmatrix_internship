# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *

# ML Kit
-keep class com.google.mlkit.** { *; }

# Navigation Safe Args
-keep class * implements androidx.navigation.NavArgs { *; }

# Kotlin Coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule { *; }
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
