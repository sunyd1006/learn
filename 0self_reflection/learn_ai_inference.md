下面给出两条可落地的学习路线：
A.「从 0 到上线」——面向“先跑通、再优化”的工程师；
B.「由浅入深」——面向“想读论文 + 读源码”的研究/算法同学。


每条路线都分 4 个阶段，每阶段给出：目标、必学概念、官方文档/代码入口、动手任务（2～4h 可完成），以及“踩坑预警”。按顺序执行即可。




A.「从 0 到上线」工程师路线（3～4 周可交付生产）
阶段 1：10 行代码把 LLM 跑起来
目标：理解“模型 → 引擎 → 服务”三层结构。
必学：HF transformers 推理、显存/KV-Cache、REST/gRPC 概念。
官方入口：
transformers quickstart
vLLM Quickstart（https://docs.vllm.ai/en/latest/getting_started/quickstart.html）
动手：
① pip install vllm，跑 python -m vllm.entrypoints.openai.api_server --model lmsys/vicuna-7b-v1.5
② curl 本地 8000 端口，验证 OpenAI-compatible 接口返回。
踩坑：GPU 驱动 ≥525；如显存不足加 --max-model-len 2048。


阶段 2：用 Triton 把服务“包一层”
目标：掌握 Triton 的 model repository、backend、config.pbtxt。
官方入口：
Triton Server r23.10 文档
vLLM Triton backend example（https://github.com/triton-inference-server/vllm_backend）
动手：
① docker run --gpus all -p8000:8000 nvcr.io/nvidia/tritonserver:23.10-vllm-py3
② 把阶段 1 的模型目录挂进去，写一个 config.pbtxt，重跑测试。
踩坑：注意 tensor_parallel_size 与 instance_group 对齐。


阶段 3：换引擎 → TRT-LLM 拿极致性能
目标：会“编译 + 运行”TensorRT 引擎；理解 weight-only INT8、in-flight batching。
官方入口：
TRT-LLM examples/gpt（https://github.com/NVIDIA/TensorRT-LLM/tree/main/examples/gpt）
动手：
① 取阶段 1 同模型，跑一次 python convert_checkpoint.py + trtllm-build
② docker run TRT-LLM Triton backend，对比首 token 延迟/吞吐 vs vLLM。
踩坑：编译时 GPU arch 要写对（sm_80/sm_89）；多卡需 NCCL。


阶段 4：复杂业务 → SGLang 优化
目标：用 SGLang DSL 写多轮对话/Agent，并回落到 vLLM/TRT-LLM。
官方入口：
SGLang README 里的 chatbot/agent 示例
动手：
① pip install sglang，写一个 3-round ReAct Agent，对比纯 vLLM 的 TTFT（time-to-first-token）。
② 把 SGLang 嵌入 Triton：写一个自定义 backend（参考 vllm_backend 源码），CI/CD 上线。
踩坑：SGLang 0.2.x 还在快速迭代，锁定 commit id。
里程碑：
第 1 周：阶段 1 完成，本地可 curl 通。
第 2 周：阶段 2 完成，docker-compose 一键部署。
第 3 周：阶段 3 完成，QPS↑2×，P99 延迟↓30%。
第 4 周：阶段 4 完成，复杂提示场景延迟↓50%，上线。


B.「由浅入深」研究/算法路线（4～6 周）
阶段 1：读 vLLM 论文＋源码
目标：吃透 PagedAttention 的内存管理。
必读：
vLLM paper（OSDI’23）
vllm/vllm/core/scheduler.py & worker/cache_engine.py
动手：
① 用 nvidia-smi + py-spy 抓一张“KV Cache 碎片”火焰图。
② 在 scheduler.py 里把 block_size=1/16 分别跑一遍，记录吞吐变化。


阶段 2：TRT-LLM 编译流程 & CUDA kernel
目标：理解 fused attention、weight-only quant、in-flight batching。
必读：
TRT-LLM docs: “Developer Guide → Kernel Fusion”
cpp/tensorrt_llm/kernels/attention.cpp
动手：
① 把 GPT-2 124M 模型手工改成 INT4 AWQ，跑一次 benchmarks/cpp/gptSessionBenchmark。
② 用 Nsight Compute 抓 kernel，对比原始 PyTorch 实现。


阶段 3：SGLang 前端 & RadixAttention
目标：掌握 Radix Tree 缓存共享、约束采样（FSM-guided decoding）。
必读：
SGLang arXiv 24 paper（https://arxiv.org/abs/2401.12963）
python/sglang/lang/interpreter.py
动手：
① 写一个正则表达式约束 JSON 输出，对比 naive vLLM 的 rejection-sampling 速度。
② 在 radix_tree.py 加 LRU eviction，跑稳定性测试。


阶段 4：Triton 自定义 backend & 生产监控
目标：把研究成果封装成高可用服务。
必读：
Triton Backend API（triton-inference-server/backend）
Prometheus metrics exporter
动手：
① fork vllm_backend，新增自定义 metric sglang_cache_hit_rate。
② 用 Grafana 画板，压测 200 并发 1h，观察内存泄漏。
学习资源汇总（按阶段索引）
官方文档
vLLM: https://docs.vllm.ai
TRT-LLM: https://nvidia.github.io/TensorRT-LLM/
SGLang: https://github.com/sgl-project/sglang
Triton: https://github.com/triton-inference-server/server
速查表
Docker 镜像
nvcr.io/nvidia/tritonserver:23.10-vllm-py3
nvcr.io/nvidia/tritonserver:24.03-trtllm-python-py3
常用命令
vLLM: python -m vllm.entrypoints.openai.api_server ...
TRT-LLM build: trtllm-build --checkpoint_dir ./ckpt --output_dir ./engine --gpt_attention_plugin float16
性能测试
vLLM: benchmarks/benchmark_throughput.py
TRT-LLM: benchmarks/cpp/gptSessionBenchmark
社区/论文
vLLM OSDI’23: https://www.usenix.org/conference/osdi23/presentation/kwon
SGLang arXiv: https://arxiv.org/abs/2401.12963
Discord/Slack：vllm-project 和 triton-inference-server 频道


一句话总结
工程师路线：先 vLLM → 包 Triton → 换 TRT-LLM → 加 SGLang；
研究路线：读 vLLM 论文 → 钻 TRT-LLM CUDA kernel → 玩 SGLang RadixAttention → 自己写 Triton backend。
按上面 4 阶段 checklist 打卡，就能把“sglang / vllm / trt-llm / triton”彻底串起来。
复制
重试
分享
搜索一下


阶段 1 需要多长时间完成？

TRT-LLM 编译流程有哪些关键步骤？
