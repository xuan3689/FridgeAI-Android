# 冰小智 · 智能冰箱家庭助手

> 基于 RK3588 开发板的外挂式 AI 冰箱助手，端云混合架构，摄像头识别食材 + 语音交互 + 菜谱推荐。

---

## 目录

- [项目简介](#项目简介)
- [硬件清单](#硬件清单)
- [系统环境](#系统环境)
- [代码结构](#代码结构)
- [快速启动](#快速启动)
- [模块说明](#模块说明)
- [数据库结构](#数据库结构)
- [Prompt 模板](#prompt-模板)
- [技术选型](#技术选型)
- [开发计划](#开发计划)
- [参赛演示脚本](#参赛演示脚本)
- [附录](#附录)

---

## 项目简介

冰小智将普通冰箱升级为具备**感知 → 推理 → 主动交互**能力的家庭 AI 终端，无需改造冰箱本身，开发板外挂即用。

**三个核心亮点（参赛展示重点）：**

- **实用性**：食材自动识别入库 + 菜谱智能推荐 + 临期预警
- **情感化**：放入食材时主动语音建议，模拟家庭伙伴陪伴感
- **技术性**：RK3588 NPU 端侧推理 + 云端大模型混合架构，离线可用

---

## 硬件清单

| 硬件 | 型号 / 规格 | 用途 | 备注 |
|------|------------|------|------|
| 主板 | RK3588（Orange Pi 5 / Rock 5B） | 主控 + NPU 推理 | NPU 6 TOPS |
| 摄像头 | USB 摄像头，分辨率 ≥1080p | 拍摄食材图像 | 建议 USB 免驱 |
| 麦克风 | USB 麦克风或 3.5mm 话筒 | 语音采集 | 建议有指向性 |
| 扬声器 | USB 音箱或 3.5mm 喇叭 | TTS 语音播报 | 功率 3W 以上 |
| 存储 | TF 卡 ≥32GB Class 10 或 eMMC | 系统 + 数据 | 建议 64GB |
| 电源 | PD 65W 适配器 | 稳定供电 | 满载约 15-20W |

**摄像头安装位置：** 固定在冰箱顶部，朝向门把手区域，高度 160-180cm，俯角约 30-45 度。

---

## 系统环境

- **OS**：Ubuntu 22.04 ARM64
- **Python**：3.10+
- **推理框架**：RKNN-Toolkit2（NPU）+ ONNXRuntime（CPU fallback）
- **音频**：PyAudio + ALSA
- **数据库**：SQLite 3

### 安装依赖

```bash
# 系统依赖
sudo apt update && sudo apt install -y \
  python3-pip python3-venv ffmpeg portaudio19-dev sqlite3

# Python 依赖
pip install rknn-toolkit2 opencv-python-headless pyaudio requests
pip install openai SpeechRecognition gTTS playsound vosk
```

---

## 代码结构

```
bingxiaozhi/
├── main.py                   # 主入口，启动所有模块
├── config.py                 # 全局配置（API Key、阈值、路径）
├── modules/
│   ├── vision.py             # 视觉识别（YOLOv8 + RKNN）
│   ├── wakeword.py           # 语音唤醒（Porcupine）
│   ├── asr.py                # 语音识别（讯飞 + Vosk 降级）
│   ├── tts.py                # 语音合成（阿里云 + pyttsx3 降级）
│   ├── llm.py                # 大模型调用（通义千问 + 本地降级）
│   ├── inventory.py          # 食材库存管理（SQLite CRUD）
│   └── scheduler.py          # 事件调度与主动提醒
├── models/
│   ├── food_yolov8n.rknn     # RKNN 量化食材识别模型
│   └── vosk-model-cn/        # 本地 ASR 模型目录
├── data/
│   └── bingxiaozhi.db        # SQLite 数据库文件
├── recipes/
│   └── base_recipes.json     # 离线菜谱规则库（约 200 条）
└── requirements.txt
```

---

## 快速启动

```bash
# 1. 克隆仓库
git clone https://github.com/your-repo/bingxiaozhi.git
cd bingxiaozhi

# 2. 复制并填写配置
cp config.example.py config.py
# 编辑 config.py，填入 API Key

# 3. 初始化数据库
python3 -c "from modules.inventory import init_db; init_db()"

# 4. 启动
python3 main.py
```

---

## 模块说明

### 架构概览

```
摄像头 ──► vision.py ──► inventory.py ──► scheduler.py
                                               │
麦克风 ──► wakeword.py ──► asr.py ──► llm.py ──► tts.py ──► 扬声器
```

**本地运行（RK3588 NPU）：** 视觉识别、语音唤醒、库存管理、事件调度

**云端 API（联网时）：** ASR 语音识别、大模型对话、TTS 语音合成

**离线降级（断网时）：** Vosk 本地 ASR → 本地 Qwen2-1.5B → pyttsx3 TTS

---

### 食材识别流程

1. 摄像头 5fps 持续捕帧
2. 帧差法检测画面变化，超过阈值触发识别
3. RKNN 推理获取食材类别与置信度
4. 置信度 > 0.75 → 直接写库 + 触发主动提醒
5. 置信度 0.5-0.75 → 连续 3 帧取众数后写库
6. 置信度 < 0.5 → 丢弃

### 语音交互流程

1. Porcupine 检测唤醒词「冰小智」
2. TTS 播报「在的，请说」，开始录音（最长 8 秒）
3. 云端 ASR 转写文字
4. 构造 Prompt（含库存快照）→ 大模型 API
5. TTS 合成回复 → 扬声器播报
6. 5 秒后回到监听状态

### 主动提醒流程

1. 食材写库后，scheduler 收到「新食材入库」事件
2. 构造主动建议 Prompt（新食材 + 当前库存）
3. 大模型生成一句话建议，直接 TTS 播报

---

### 关键接口

**inventory.py**
```python
def add_ingredient(name, quantity=1, unit='个', source='camera') -> int
def get_all_ingredients() -> list[dict]
def get_expiring_soon(days=1) -> list[dict]
def remove_ingredient(ingredient_id) -> bool
```

**vision.py**
```python
def start_camera_loop(callback: Callable[[str, float], None]) -> None
# callback(food_name, confidence) 识别到食材时回调
```

**llm.py**
```python
def generate_recipe(ingredients: list[str]) -> str
def chat(user_message: str, context: list[dict]) -> str
```

**tts.py / asr.py**
```python
def speak(text: str) -> None      # TTS 播报
def listen(timeout=8) -> str      # 录音并返回识别文字
```

---

## 数据库结构

```sql
CREATE TABLE ingredients (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT    NOT NULL,
    quantity    REAL    DEFAULT 1,
    unit        TEXT    DEFAULT '个',
    added_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    expire_days INTEGER DEFAULT 7,
    source      TEXT    DEFAULT 'camera'
);

CREATE TABLE chat_history (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    role       TEXT NOT NULL,          -- user / assistant
    content    TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE events (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    event_type TEXT NOT NULL,          -- ingredient_added / recipe_suggested / reminder_sent
    payload    TEXT,                   -- JSON
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

## Prompt 模板

### 菜谱推荐

```
系统角色：你是「冰小智」，一个家庭冰箱 AI 助手，说话简洁亲切，像家人一样。

用户消息：
冰箱里现在有以下食材：{食材列表，逗号分隔}
请根据这些食材推荐 2-3 道适合今天做的菜，
每道菜包含：菜名、所需食材、简要做法（3 步以内）。
回复要口语化，像在和家人聊天。
```

### 主动提醒

```
刚刚检测到冰箱放入了「{新增食材}」。
冰箱目前还有：{其他库存食材}。
请用一句话主动建议一道今晚可以做的菜，语气要自然温暖。
```

### 临期预警

```
以下食材快过期了：{临期食材列表，含剩余天数}。
请用一句温馨的提醒告诉主人，并简单建议怎么尽快用掉它们。
```

### 通用对话

```
系统角色：你是「冰小智」，家庭冰箱 AI 助手。
只回答与食物、烹饪、食材、健康饮食相关的问题，其他话题礼貌拒绝。
回复控制在 100 字以内，适合 TTS 播报。
```

---

## 技术选型

| 模块 | 选用方案 | 离线降级 |
|------|---------|---------|
| 视觉识别 | YOLOv8n（RKNN 量化） | CPU + ONNX |
| 语音唤醒 | Picovoice Porcupine | — |
| ASR | 讯飞开放平台 | Vosk 本地模型 |
| 大模型 | 阿里云通义千问 API | Qwen2-1.5B RKNN |
| TTS | 阿里云 TTS / 讯飞 TTS | pyttsx3 |
| 数据库 | SQLite 3 | —（本地无需降级）|

---

## 开发计划

| 阶段 | 时间 | 目标 | 验收标准 |
|------|------|------|---------|
| 第一周 | Day 1-7 | 环境搭建 + 视觉识别跑通 | 识别 10 种食材并写入 SQLite |
| 第二周 | Day 8-14 | 语音链路全线跑通 | 唤醒 → 对话 → 播报完整流程 |
| 第三周 | Day 15-21 | 主动交互 + 菜谱推荐 | 放入食材自动触发语音推荐 |
| 第四周 | Day 22-28 | 离线降级 + 整合测试 | 断网仍可用，全流程稳定 |
| 冲刺 | Day 29-30 | 参赛演示准备 | 演示视频 + Demo 脚本就绪 |

### 风险应对

| 风险 | 应对方案 |
|------|---------|
| RKNN 模型转换报错 | 改用 CPU + ONNX 推理 |
| 云端 API 延迟高 | 3 秒超时后切换本地降级 |
| 唤醒词误触发率高 | 调高 Porcupine 阈值 + 增加确认音 |
| 食材识别准确率不达标 | 低置信度类别发图给大模型辅助识别 |

---

## 参赛演示脚本

> 总时长 5 分钟，覆盖三个核心亮点。

| 时间 | 演示动作 | 展示亮点 |
|------|---------|---------|
| 0:00-0:30 | 介绍冰小智，展示开发板实物和安装位置 | 硬件方案 |
| 0:30-1:30 | 放入番茄、鸡蛋，摄像头自动识别入库，语音主动播报菜谱建议 | 实用性 + 情感化 |
| 1:30-2:30 | 说「冰小智，帮我推荐今晚的晚饭」，演示完整语音对话 | 技术性 |
| 2:30-3:30 | 展示临期食材提醒（提前设置过期数据） | 实用性 + 情感化 |
| 3:30-4:30 | 拔掉网线，展示离线识别 + 规则菜谱降级依然可用 | 技术性（端云混合）|
| 4:30-5:00 | 总结亮点，说明未来扩展方向 | 产品愿景 |

**演示注意事项：**
- 提前在数据库写入 5-8 种食材作为已有库存
- 现场光线与测试环境保持一致
- 准备备用热点防止网络不稳定
- 录制完整演示视频作为保底

---

## 附录

### API 申请

- 通义千问（阿里云）：https://dashscope.aliyuncs.com
- 讯飞开放平台（ASR + TTS）：https://console.xfyun.cn
- Picovoice Porcupine（唤醒词）：https://picovoice.ai

### 参考资料

- RKNN-Toolkit2：https://github.com/rockchip-linux/rknn-toolkit2
- YOLOv8 文档：https://docs.ultralytics.com
- Vosk 离线 ASR：https://alphacephei.com/vosk
- Picovoice Porcupine：https://github.com/Picovoice/porcupine

---

> 文档版本：v1.0 | 最后更新：见 git log
