// model.go
// 定义系统中共享的数据模型
package common

import "time"

// News 新闻基础数据结构
type News struct {
	Title     string    `json:"title"`
	Content   string    `json:"content,omitempty"`
	Source    string    `json:"source"`
	URL       string    `json:"url"`
	Published time.Time `json:"published"`
	Category  string    `json:"category,omitempty"`
	Summary   string    `json:"summary,omitempty"`
}

// CrawlerResult 采集器返回结果
type CrawlerResult struct {
	News   []News `json:"news"`
	Source string `json:"source"`
}

// ProcessorResult 处理器返回结果
type ProcessorResult struct {
	UniqueNews     []News            `json:"unique_news"`
	ClassifiedNews map[string][]News `json:"classified_news"`
}

// PublishResult 发布器返回结果
type PublishResult struct {
	Platform string `json:"platform"`
	Success  bool   `json:"success"`
	Count    int    `json:"count"`
	Message  string `json:"message,omitempty"`
}
