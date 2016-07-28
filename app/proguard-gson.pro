-keep class com.google.gson.** {*;}
-keep class sun.misc.Unsafe { *; }  
-keep class com.google.gson.stream.** { *; }  
-keep class com.google.gson.examples.android.model.** { *; }   
-keep class com.google.** {  
    <fields>;  
    <methods>;  
}
-dontwarn com.google.gson.**