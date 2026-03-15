# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.rahees.unicalc.data.remote.** { *; }
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Gson
-keepattributes Signature
-keep class com.google.gson.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# Glance
-keep class androidx.glance.** { *; }
