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
android{
  compileOptions {
        sourceCompatibility 1.8
        targetCompatibility 1.8
    }
}
dependencies {
   implementation 'com.github.bingoloves:common:1.0.5'
}
```
