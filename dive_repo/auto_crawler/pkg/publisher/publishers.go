// publishers.go
// 实现新闻发布器
package publisher

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"strings"
	"time"

	"big_sun.com/auto_crawler/pkg/common"
)

// BasePublisher 基础发布器实现
type BasePublisher struct {
	name        string
	description string
	config      map[string]interface{}
}

// NewBasePublisher 创建新的基础发布器
func NewBasePublisher(name, description string) *BasePublisher {
	return &BasePublisher{
		name:        name,
		description: description,
		config:      make(map[string]interface{}),
	}
}

func (p *BasePublisher) Name() string {
	return p.name
}

func (p *BasePublisher) Description() string {
	return p.description
}

func (p *BasePublisher) Configure(config map[string]interface{}) error {
	for k, v := range config {
		p.config[k] = v
	}
	return nil
}

func (p *BasePublisher) Close() error {
	// 基础发布器没有需要关闭的资源
	return nil
}

// JSONPublisher JSON文件发布器实现
type JSONPublisher struct {
	BasePublisher
}

// NewJSONPublisher 创建新的JSON发布器
func NewJSONPublisher() *JSONPublisher {
	return &JSONPublisher{
		BasePublisher: *NewBasePublisher("json_publisher", "将新闻保存为JSON文件的发布器"),
	}
}

func (p *JSONPublisher) SupportedPlatforms() []string {
	return []string{"json", "file", "local"}
}

func (p *JSONPublisher) Publish(ctx context.Context, platform string, data *common.ProcessorResult) (*common.PublishResult, error) {
	if data == nil || len(data.UniqueNews) == 0 {
		return &common.PublishResult{
			Platform: platform,
			Success:  true,
			Count:    0,
			Message:  "没有新闻数据需要发布",
		}, nil
	}

	log.Printf("[%s] 开始发布 %d 条新闻到 %s 平台", p.name, len(data.UniqueNews), platform)

	// 生成文件名
	timestamp := time.Now().Format("20060102_150405")
	filename := fmt.Sprintf("news_publish_%s_%s.json", platform, timestamp)

	// 检查是否有自定义的保存路径
	if savePath, ok := p.config["save_path"].(string); ok && savePath != "" {
		// 确保目录存在
		if err := os.MkdirAll(savePath, 0755); err != nil {
			return nil, fmt.Errorf("创建保存目录失败: %v", err)
		}
		filename = savePath + "/" + filename
	}

	// 将数据保存为JSON
	fileData, err := json.MarshalIndent(data, "", "  ")
	if err != nil {
		return nil, fmt.Errorf("JSON编码失败: %v", err)
	}

	// 写入文件
	err = os.WriteFile(filename, fileData, 0644)
	if err != nil {
		return nil, fmt.Errorf("写入文件失败: %v", err)
	}

	log.Printf("[%s] 成功发布 %d 条新闻到文件: %s", p.name, len(data.UniqueNews), filename)

	return &common.PublishResult{
		Platform: platform,
		Success:  true,
		Count:    len(data.UniqueNews),
		Message:  fmt.Sprintf("成功保存到文件: %s", filename),
	}, nil
}

// ToutiaoPublisher 今日头条发布器实现
type ToutiaoPublisher struct {
	BasePublisher
}

// NewToutiaoPublisher 创建新的今日头条发布器
func NewToutiaoPublisher() *ToutiaoPublisher {
	return &ToutiaoPublisher{
		BasePublisher: *NewBasePublisher("toutiao_publisher", "发布新闻到今日头条平台的发布器"),
	}
}

func (p *ToutiaoPublisher) SupportedPlatforms() []string {
	return []string{"toutiao", "今日头条"}
}

func (p *ToutiaoPublisher) Publish(ctx context.Context, platform string, data *common.ProcessorResult) (*common.PublishResult, error) {
	if data == nil || len(data.UniqueNews) == 0 {
		return &common.PublishResult{
			Platform: platform,
			Success:  true,
			Count:    0,
			Message:  "没有新闻数据需要发布",
		}, nil
	}

	// 检查必要的配置
	_, hasAppID := p.config["app_id"].(string)
	_, hasAppSecret := p.config["app_secret"].(string)

	if !hasAppID || !hasAppSecret {
		log.Printf("[%s] 今日头条发布器缺少必要配置，使用模拟发布", p.name)
		// 如果缺少配置，使用模拟发布
		return p.mockPublish(data)
	}

	log.Printf("[%s] 开始发布 %d 条新闻到今日头条平台", p.name, len(data.UniqueNews))

	// TODO: 实现真实的今日头条API发布逻辑
	// 这里需要调用今日头条的开放平台API
	// 由于API调用需要真实的认证信息和接口文档，这里暂时使用模拟发布
	return p.mockPublish(data)
}

// mockPublish 模拟发布到今日头条
func (p *ToutiaoPublisher) mockPublish(data *common.ProcessorResult) (*common.PublishResult, error) {
	// 生成模拟发布结果文件
	timestamp := time.Now().Format("20060102_150405")
	filename := fmt.Sprintf("toutiao_publish_mock_%s.json", timestamp)

	// 准备发布内容
	publishContent := map[string]interface{}{
		"platform":     "toutiao",
		"publish_time": time.Now(),
		"total_count":  len(data.UniqueNews),
		"categories":   make(map[string]int),
		"sample_news":  []common.News{},
	}

	// 添加分类统计
	for category, newsList := range data.ClassifiedNews {
		publishContent["categories"].(map[string]int)[category] = len(newsList)
	}

	// 添加样本新闻（最多5条）
	sampleCount := 5
	if len(data.UniqueNews) < sampleCount {
		sampleCount = len(data.UniqueNews)
	}
	publishContent["sample_news"] = data.UniqueNews[:sampleCount]

	// 保存模拟发布结果
	fileData, err := json.MarshalIndent(publishContent, "", "  ")
	if err != nil {
		return nil, fmt.Errorf("JSON编码失败: %v", err)
	}

	err = os.WriteFile(filename, fileData, 0644)
	if err != nil {
		return nil, fmt.Errorf("写入模拟发布文件失败: %v", err)
	}

	log.Printf("[%s] 模拟发布 %d 条新闻到今日头条平台，详细信息请查看: %s", p.name, len(data.UniqueNews), filename)

	return &common.PublishResult{
		Platform: "toutiao",
		Success:  true,
		Count:    len(data.UniqueNews),
		Message:  fmt.Sprintf("模拟发布成功，详细信息请查看文件: %s", filename),
	}, nil
}

// MarkdownPublisher Markdown发布器实现
type MarkdownPublisher struct {
	BasePublisher
}

// NewMarkdownPublisher 创建新的Markdown发布器
func NewMarkdownPublisher() *MarkdownPublisher {
	return &MarkdownPublisher{
		BasePublisher: *NewBasePublisher("markdown_publisher", "将新闻生成Markdown格式的发布器"),
	}
}

func (p *MarkdownPublisher) SupportedPlatforms() []string {
	return []string{"markdown", "md"}
}

func (p *MarkdownPublisher) Publish(ctx context.Context, platform string, data *common.ProcessorResult) (*common.PublishResult, error) {
	if data == nil || len(data.UniqueNews) == 0 {
		return &common.PublishResult{
			Platform: platform,
			Success:  true,
			Count:    0,
			Message:  "没有新闻数据需要发布",
		}, nil
	}

	log.Printf("[%s] 开始生成Markdown内容，共 %d 条新闻", p.name, len(data.UniqueNews))

	// 生成Markdown内容
	var mdContent strings.Builder

	// 添加标题和生成时间
	mdContent.WriteString(fmt.Sprintf("# 新闻汇总报告\n\n"))
	mdContent.WriteString(fmt.Sprintf("*生成时间: %s*\n\n", time.Now().Format("2006-01-02 15:04:05")))
	mdContent.WriteString(fmt.Sprintf("## 概览\n\n"))
	mdContent.WriteString(fmt.Sprintf("- 总新闻数量: %d\n", len(data.UniqueNews)))
	mdContent.WriteString(fmt.Sprintf("- 分类数量: %d\n\n", len(data.ClassifiedNews)))

	// 添加分类统计
	mdContent.WriteString("## 分类统计\n\n")
	for category, newsList := range data.ClassifiedNews {
		mdContent.WriteString(fmt.Sprintf("- **%s**: %d 条\n", category, len(newsList)))
	}

	// 按分类添加新闻详情
	mdContent.WriteString("\n## 新闻详情\n\n")
	for category, newsList := range data.ClassifiedNews {
		mdContent.WriteString(fmt.Sprintf("### %s (%d条)\n\n", category, len(newsList)))

		for _, news := range newsList {
			mdContent.WriteString(fmt.Sprintf("#### [%s](%s)\n\n", news.Title, news.URL))
			mdContent.WriteString(fmt.Sprintf("- **来源**: %s\n", news.Source))
			mdContent.WriteString(fmt.Sprintf("- **发布时间**: %s\n", news.Published.Format("2006-01-02 15:04")))
			if news.Summary != "" {
				mdContent.WriteString(fmt.Sprintf("- **摘要**: %s\n", news.Summary))
			}
			mdContent.WriteString("\n---\n\n")
		}
	}

	// 生成文件名
	timestamp := time.Now().Format("20060102_150405")
	filename := fmt.Sprintf("news_report_%s.md", timestamp)

	// 检查是否有自定义的保存路径
	if savePath, ok := p.config["save_path"].(string); ok && savePath != "" {
		// 确保目录存在
		if err := os.MkdirAll(savePath, 0755); err != nil {
			return nil, fmt.Errorf("创建保存目录失败: %v", err)
		}
		filename = savePath + "/" + filename
	}

	// 写入文件
	err := os.WriteFile(filename, []byte(mdContent.String()), 0644)
	if err != nil {
		return nil, fmt.Errorf("写入Markdown文件失败: %v", err)
	}

	log.Printf("[%s] 成功生成Markdown报告: %s", p.name, filename)

	return &common.PublishResult{
		Platform: platform,
		Success:  true,
		Count:    len(data.UniqueNews),
		Message:  fmt.Sprintf("成功生成Markdown报告: %s", filename),
	}, nil
}

// SimplePublisherFactory 简单的发布器工厂实现
type SimplePublisherFactory struct{}

// NewSimplePublisherFactory 创建新的发布器工厂
func NewSimplePublisherFactory() *SimplePublisherFactory {
	return &SimplePublisherFactory{}
}

func (f *SimplePublisherFactory) Create(publisherType string) (common.Publisher, error) {
	switch strings.ToLower(publisherType) {
	case "json", "file":
		return NewJSONPublisher(), nil
	case "toutiao", "今日头条":
		return NewToutiaoPublisher(), nil
	case "markdown", "md":
		return NewMarkdownPublisher(), nil
	default:
		return nil, fmt.Errorf("不支持的发布器类型: %s", publisherType)
	}
}

func (f *SimplePublisherFactory) SupportedTypes() []string {
	return []string{"json", "toutiao", "markdown", "file", "md", "今日头条"}
}
