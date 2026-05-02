# 冰小智 Android 应用

基于 RK3588 开发板的智能冰箱助手 Android 客户端应用。

## 项目结构

```
app/src/main/java/com/example/fridgeai_android/
├── FridgeApplication.kt          # Application 入口
├── MainActivity.kt                # 主 Activity
├── data/                          # 数据层
│   ├── local/                     # 本地数据库
│   │   ├── FridgeDatabase.kt
│   │   ├── IngredientDao.kt
│   │   └── ChatDao.kt
│   ├── model/                     # 数据模型
│   │   ├── Ingredient.kt
│   │   ├── ChatMessage.kt
│   │   └── Recipe.kt
│   ├── remote/                    # 网络 API
│   │   └── FridgeApiService.kt
│   └── repository/                # 数据仓库
│       ├── IngredientRepository.kt
│       ├── ChatRepository.kt
│       └── RecipeRepository.kt
├── di/                            # 依赖注入
│   ├── DatabaseModule.kt
│   └── NetworkModule.kt
├── ui/                            # UI 层
│   ├── components/                # 通用组件
│   │   ├── BottomNavigationBar.kt
│   │   └── IngredientCard.kt
│   ├── navigation/                # 导航
│   │   ├── Screen.kt
│   │   └── FridgeNavGraph.kt
│   ├── screens/                   # 页面
│   │   ├── home/                  # 主页
│   │   ├── inventory/             # 库存管理
│   │   ├── camera/                # 食材识别
│   │   ├── recipe/                # 菜谱推荐
│   │   ├── chat/                  # 语音助手
│   │   ├── expiring/              # 临期提醒
│   │   └── settings/              # 设置
│   └── theme/                     # 主题
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
```

## 功能模块

### 1. 主页 (HomeScreen)
- 快捷功能入口
- 库存统计展示
- 临期食材提醒
- 最近添加的食材列表

### 2. 食材库存 (InventoryScreen)
- 食材列表展示
- 搜索和分类筛选
- 手动添加食材
- 食材详情查看

### 3. 食材识别 (CameraScreen)
- 相机实时预览
- 拍照识别食材
- 识别结果确认
- 自动添加到库存

### 4. 菜谱推荐 (RecipeScreen)
- 基于库存食材推荐菜谱
- 菜谱详情展示
- 烹饪步骤指导
- 本地离线菜谱库

### 5. 语音助手 (ChatScreen)
- 文字对话交互
- 语音输入识别
- 智能问答
- 历史记录管理

### 6. 临期提醒 (ExpiringScreen)
- 已过期食材列表
- 即将过期食材列表
- 过期天数显示

### 7. 设置 (SettingsScreen)
- 设备连接配置
- 通知设置
- 数据管理
- 关于信息

## 技术栈

- **UI 框架**: Jetpack Compose
- **架构**: MVVM + Repository Pattern
- **依赖注入**: Hilt
- **数据库**: Room
- **网络请求**: Retrofit + OkHttp
- **图片加载**: Coil
- **导航**: Navigation Compose
- **权限管理**: Accompanist Permissions

## 数据库设计

### Ingredient 表
- id: 主键
- name: 食材名称
- quantity: 数量
- unit: 单位
- addedAt: 添加时间
- expireDays: 保质期天数
- source: 来源 (camera/manual/voice)
- imageUrl: 图片 URL
- category: 分类

### ChatMessage 表
- id: 主键
- role: 角色 (user/assistant)
- content: 消息内容
- createdAt: 创建时间

## API 接口

### 后端服务地址
默认: `http://192.168.1.100:8000/`

### 主要接口
- `POST /api/vision/recognize` - 食材识别
- `POST /api/recipe/recommend` - 菜谱推荐
- `POST /api/asr/recognize` - 语音识别
- `POST /api/tts/synthesize` - 语音合成
- `POST /api/llm/chat` - 大模型对话
- `GET /api/inventory/status` - 库存状态

## 配置说明

### 修改后端地址
在 `NetworkModule.kt` 中修改 `BASE_URL`:
```kotlin
private const val BASE_URL = "http://YOUR_RK3588_IP:8000/"
```

### 权限配置
应用需要以下权限：
- INTERNET - 网络访问
- CAMERA - 相机识别
- RECORD_AUDIO - 语音输入
- READ/WRITE_EXTERNAL_STORAGE - 文件存储

## 构建运行

### 环境要求
- Android Studio Hedgehog | 2023.1.1+
- JDK 11+
- Android SDK 30+
- Gradle 8.0+

### 构建步骤
```bash
# 克隆项目
git clone https://github.com/your-repo/FridgeAI-Android.git

# 打开 Android Studio
# File -> Open -> 选择项目目录

# 同步 Gradle
# 点击 "Sync Project with Gradle Files"

# 运行应用
# 点击 Run 按钮或按 Shift+F10
```

## 主题配色

### 主色调 - 冰箱蓝色系
- Primary: #0288D1
- Primary Container: #B3E5FC

### 辅助色 - 清新绿色系
- Secondary: #4CAF50
- Secondary Container: #C8E6C9

### 警告色 - 橙色系
- Tertiary: #FF9800
- Tertiary Container: #FFE0B2

### 错误色 - 红色系
- Error: #F44336
- Error Container: #FFCDD2

## 离线降级策略

### 食材识别
- 在线: RK3588 NPU 推理
- 离线: 本地 ONNX 模型

### 菜谱推荐
- 在线: 云端大模型推荐
- 离线: 本地规则匹配（5个基础菜谱）

### 语音对话
- 在线: 云端 ASR + LLM + TTS
- 离线: 本地规则回复

## 开发计划

- [x] 基础架构搭建
- [x] 主页面开发
- [x] 食材库存管理
- [x] 食材识别功能
- [x] 菜谱推荐功能
- [x] 语音助手功能
- [x] 临期提醒功能
- [x] 设置页面
- [ ] CameraX 集成
- [ ] 语音识别集成
- [ ] 推送通知
- [ ] 数据同步
- [ ] 性能优化

## 注意事项

1. **网络配置**: 确保 Android 设备与 RK3588 开发板在同一局域网
2. **权限申请**: 首次运行需要授予相机和麦克风权限
3. **API 兼容**: 最低支持 Android 11 (API 30)
4. **测试数据**: 首次运行数据库为空，可手动添加测试数据

## 许可证

MIT License

## 联系方式

- 项目地址: https://github.com/your-repo/FridgeAI-Android
- 问题反馈: https://github.com/your-repo/FridgeAI-Android/issues
