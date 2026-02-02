# 目标

## p0

* 自动订阅国内外主流媒体的每日新闻
* 支持过滤出感兴趣的事件内容
* 横轴分类，纵轴主题，周期聚合扩大信息密度。
  * 支持按照主题进行每周、每月大事件的聚合提炼，总结出关键事件和动态。
* 支持参考检索，避免模型幻觉

## p1

* 支持自动同步社交平台：微信、头条等
* 支持bark手机通知&告警

## p2

* 利用模型来做图文可视化、最终用于C段消费。

# 技术架构

数据采集：RSS、API

RSS：https://juejin.cn/post/7459966392429101067


### 首批上线


需要搜索中国支持RSS的媒体信息。

Search for "中国支持RSS的媒体网站有哪些"
5 Results

Thought

根据搜索结果，中国支持RSS的媒体主要包括以下几类：

### 门户网站类

- 网易新闻 ：RSS地址为 http://www.163.com/rss 2
- 新浪新闻 ：RSS地址为 http://rss.sina.com.cn/ 2
- 腾讯新闻 ：RSS地址为 http://rss.qq.com 2
- 新华网 ：RSS地址为 http://www.xinhuanet.com/rss.htm 2

### 科技类媒体

-
- 钛媒体 ：提供RSS订阅功能 1
- 前端之巅 ：提供RSS订阅功能 1

### 其他媒体

- 果壳网 ：提供RSS订阅功能 1
- 大旗网 ：RSS地址为 http://www.daqi.com/rss/ 2
- JustPod ：RSS地址为 justpodnews.com/rss/ 1

### 注意事项

1. 部分网站的RSS可能只提供摘要而非全文，可以使用FeedSoSo等工具将其转换为全文输出 3
2. 可以使用Feedly、Inoreader、The Old Reader等RSS阅读器来订阅这些媒体 5
3. 开源项目RSSHub可以将各种网站内容转换为RSS格式，扩展订阅范围 5


# 参考项目

* https://github.com/OpenAISpace/ai-trend-publish
* ~~八爪鱼RPA：https://mp.weixin.qq.com/s/lxy_5FDSXAbPbgvZyTN2Hw~~
* ~~https://rpa.bazhuayu.com/~~

---
