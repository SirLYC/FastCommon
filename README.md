# FastCommon

日常个人开发/验证测试快速搭建App的常用基础库。

**MinSDK**：16

> 注：纯Kotlin开发，Java可以用，但没有专门针对Java优化API形式，使用Java体验可能不会太好。

## 使用

### 导入

### 初始化

在使用任何API前，务必先初始化：

``` Kotlin
FastCommonLib.init(context)
```

## 模块介绍

### MVVM架构

**位于arch包下**：封装了`LiveData`和`ViewModel`的套件。

### 事件

**位于event包下**：封装了回调函数和简单的事件总线。

### 日志

**位于log包下**：支持日志记录在本地。

### 权限

**位于permission包下**：简化了权限申请流程，通过回调的方式告知权限申请结果。

### 设置项

**位于preference包下**：于LiveData结合，简化设置项的改变、本地记录与回调方法。

### 线程

**位于thread包下**：提供了一些基础的全局线程池和一些线程相关工具。

### Toast

**位于toast包下**：toast简单封装。

### 其他工具

**位于utils包下**：

- `DeviceUtils`：设备宽高、状态栏颜色、震动、宽高、dp/sp/px互转等函数
- `ViewUtils`：改变Drawable颜色、颜色混合、等工具
