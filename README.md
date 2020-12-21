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
   implementation 'com.github.bingoloves:common:1.0.7'
}
```
#### 更新日志记录

`2020-12-21 14:22:27` **v1.0.7**
* 优化网络请求HttpConfig,使其可配置 
* 新增请求响应统一拦截接口
* 修复子线程网络请求bug