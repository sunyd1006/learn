// processors.go
// 实现新闻处理器
package processor

import (
	"context"
	"log"
	"strings"
	"sync"

	"big_sun.com/auto_crawler/pkg/common"
)

// DefaultProcessor 默认的新闻处理器实现
type DefaultProcessor struct {
	name string
	sync.Mutex
}

// NewDefaultProcessor 创建新的默认处理器
func NewDefaultProcessor() *DefaultProcessor {
	return &DefaultProcessor{
		name: "default_processor",
	}
}

func (p *DefaultProcessor) Name() string {
	return p.name
}

func (p *DefaultProcessor) Process(ctx context.Context, result *common.CrawlerResult) (*common.ProcessorResult, error) {
	if result == nil || len(result.News) == 0 {
		return &common.ProcessorResult{
			UniqueNews:     []common.News{},
			ClassifiedNews: make(map[string][]common.News),
		}, nil
	}

	log.Printf("[%s] 开始处理 %d 条新闻", p.name, len(result.News))

	// 首先进行去重
	uniqueNews := p.Deduplicate(result.News)
	log.Printf("[%s] 去重后剩余 %d 条新闻", p.name, len(uniqueNews))

	// 然后进行分类
	classifiedNews := p.Classify(uniqueNews)
	log.Printf("[%s] 分类完成，共 %d 个分类", p.name, len(classifiedNews))

	return &common.ProcessorResult{
		UniqueNews:     uniqueNews,
		ClassifiedNews: classifiedNews,
	}, nil
}

// 定义一些简单的关键词分类规则
var categoryKeywords = map[string][]string{
	"tech":          {"科技", "技术", "AI", "人工智能", "5G", "互联网", "数字", "软件", "硬件", "计算机", "编程", "developer", "tech", "technology", "digital", "software", "hardware", "AI", "artificial intelligence"},
	"business":      {"商业", "财经", "金融", "经济", "市场", "公司", "企业", "投资", "股票", "证券", "business", "finance", "economy", "market", "company", "investment", "stock"},
	"politics":      {"政治", "政府", "政策", "选举", "领导人", "国家", "国际", "外交", "politics", "government", "policy", "election", "leader", "state", "international", "diplomacy"},
	"sports":        {"体育", "足球", "篮球", "奥运会", "比赛", "运动", "sports", "football", "basketball", "olympics", "game", "match"},
	"health":        {"健康", "医疗", "疾病", "疫情", "病毒", "医院", "医生", "health", "medical", "disease", "pandemic", "virus", "hospital", "doctor"},
	"entertainment": {"娱乐", "电影", "音乐", "明星", "演唱会", "综艺", "entertainment", "movie", "music", "star", "concert", "show"},
	"education":     {"教育", "学校", "学生", "考试", "大学", "学习", "education", "school", "student", "exam", "university", "learning"},
	"science":       {"科学", "研究", "发现", "实验", "科学家", "太空", "科学", "science", "research", "discovery", "experiment", "scientist", "space"},
	"environment":   {"环境", "气候", "环保", "污染", "全球变暖", "environment", "climate", "green", "pollution", "global warming"},
	"culture":       {"文化", "历史", "艺术", "文学", "传统", "culture", "history", "art", "literature", "tradition"},
}

func (p *DefaultProcessor) Classify(news []common.News) map[string][]common.News {
	classifiedNews := make(map[string][]common.News)
	p.Lock()
	defer p.Unlock()

	for _, item := range news {
		// 如果新闻已经有分类，使用已有分类
		if item.Category != "" {
			classifiedNews[item.Category] = append(classifiedNews[item.Category], item)
			continue
		}

		// 基于标题和摘要进行分类
		content := strings.ToLower(item.Title + " " + item.Summary)
		classified := false

		// 检查每个分类的关键词
		for category, keywords := range categoryKeywords {
			for _, keyword := range keywords {
				if strings.Contains(content, strings.ToLower(keyword)) {
					classifiedNews[category] = append(classifiedNews[category], item)
					classified = true
					break
				}
			}
			if classified {
				break
			}
		}

		// 如果没有匹配的分类，放入"other"类别
		if !classified {
			classifiedNews["other"] = append(classifiedNews["other"], item)
		}
	}

	return classifiedNews
}

func (p *DefaultProcessor) Deduplicate(news []common.News) []common.News {
	p.Lock()
	defer p.Unlock()

	// 使用标题作为去重的键
	seen := make(map[string]bool)
	var uniqueNews []common.News

	for _, item := range news {
		// 简单的去重逻辑：基于标题的唯一性
		// 可以进一步优化为基于内容相似度等更复杂的算法
		titleKey := strings.TrimSpace(strings.ToLower(item.Title))
		if !seen[titleKey] {
			seen[titleKey] = true
			uniqueNews = append(uniqueNews, item)
		} else {
			log.Printf("[%s] 发现重复新闻: %s", p.name, item.Title)
		}
	}

	return uniqueNews
}

// AdvancedProcessor 高级处理器实现（可以扩展更复杂的处理逻辑）
type AdvancedProcessor struct {
	DefaultProcessor
	similarityThreshold float64
}

// NewAdvancedProcessor 创建新的高级处理器
func NewAdvancedProcessor(threshold float64) *AdvancedProcessor {
	return &AdvancedProcessor{
		DefaultProcessor:    DefaultProcessor{name: "advanced_processor"},
		similarityThreshold: threshold,
	}
}

// Process 重写Process方法，添加更高级的处理逻辑
func (p *AdvancedProcessor) Process(ctx context.Context, result *common.CrawlerResult) (*common.ProcessorResult, error) {
	// 调用父类的Process方法
	processedResult, err := p.DefaultProcessor.Process(ctx, result)
	if err != nil {
		return nil, err
	}

	// 这里可以添加更高级的处理逻辑
	// 例如：内容摘要生成、情感分析、关键词提取等

	return processedResult, nil
}

// Deduplicate 重写去重方法，使用更高级的算法
func (p *AdvancedProcessor) Deduplicate(news []common.News) []common.News {
	// 这里可以实现更高级的去重算法
	// 例如基于内容相似度的去重
	// 目前暂时使用简单的标题去重
	return p.DefaultProcessor.Deduplicate(news)
}
