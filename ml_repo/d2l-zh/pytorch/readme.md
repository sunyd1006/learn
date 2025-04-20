# 相关资料

bilibili 李沐在线课程：https://www.bilibili.com/video/BV1kq4y1H7sw

作业答案在线网址：https://datawhalechina.github.io/d2l-ai-solutions-manual/

作业答案：https://github.com/datawhalechina/d2l-ai-solutions-manual/blob/master/docs/ch08/ch08.md

吴恩达视频课程：https://www.bilibili.com/video/BV16r4y1Y7jv?spm_id_from=333.788.videopod.episodes&vd_source=680a93e4d47198f05ee1b3ed6929a3a9&p=104

# 自定义文件

* note.ipynb：是对python, pytorch的主要总结，默认笔记位置。
* node_model.pynb：是对模型的主要总结
* /Users/sunyindong/opt/anaconda3/envs/d2l/lib/python3.9/site-packages/d2l/torch.py  概念了try_gpu让其能跑在mac的环境上
  * 但是实验证明，在rnn_scratch, rnn_consice 里面如果有mps 会抛出异常，即抛出print(**"inputs max:"**, inputs.max()) > vocabSize的情况，
