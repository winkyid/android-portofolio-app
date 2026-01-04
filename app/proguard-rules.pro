# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep data classes
-keep class com.slimmy.portoapps.data.** { *; }

# Keep iText PDF
-keep class com.itextpdf.** { *; }
-dontwarn com.itextpdf.**
