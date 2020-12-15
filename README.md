# common
JitPack系列之工具库封装

Step 1. Add the JitPack repository to your build file
```gradle
 allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
 }
```

Step 2. Add the dependency
```gradle
dependencies {
   implementation 'com.github.bingoloves:common:1.0.3'
}
```