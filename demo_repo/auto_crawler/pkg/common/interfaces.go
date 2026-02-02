// interfaces.go
// 定义系统的核心接口
package common

import (
	"context"
)

// Crawler 采集器接口
type Crawler interface {
	// Name 返回采集器名称
	Name() string

	// Description 返回采集器描述
	Description() string

	// SupportedProtocols 返回支持的协议列表
	SupportedProtocols() []string

	// Configure 配置采集器
	Configure(config map[string]interface{}) error

	// Fetch 采集新闻
	Fetch(ctx context.Context, params map[string]interface{}) (*CrawlerResult, error)

	// Close 关闭采集器资源
	Close() error
}

// Processor 处理器接口
type Processor interface {
	// Name 返回处理器名称
	Name() string

	// Process 处理新闻数据
	Process(ctx context.Context, result *CrawlerResult) (*ProcessorResult, error)

	// Classify 分类新闻
	Classify(news []News) map[string][]News

	// Deduplicate 去重新闻
	Deduplicate(news []News) []News
}

// Publisher 发布器接口
type Publisher interface {
	// Name 返回发布器名称
	Name() string

	// Description 返回发布器描述
	Description() string

	// SupportedPlatforms 返回支持的平台列表
	SupportedPlatforms() []string

	// Configure 配置发布器
	Configure(config map[string]interface{}) error

	// Publish 发布新闻到指定平台
	Publish(ctx context.Context, platform string, data *ProcessorResult) (*PublishResult, error)

	// Close 关闭发布器资源
	Close() error
}

// CrawlerFactory 采集器工厂接口
type CrawlerFactory interface {
	// Create 创建采集器实例
	Create(crawlerType string) (Crawler, error)

	// SupportedTypes 返回支持的采集器类型
	SupportedTypes() []string
}

// PublisherFactory 发布器工厂接口
type PublisherFactory interface {
	// Create 创建发布器实例
	Create(publisherType string) (Publisher, error)

	// SupportedTypes 返回支持的发布器类型
	SupportedTypes() []string
}

// NewsSystem 新闻系统接口
type NewsSystem interface {
	// RegisterCrawler 注册采集器
	RegisterCrawler(crawler Crawler)

	// RegisterPublisher 注册发布器
	RegisterPublisher(publisher Publisher)

	// SetProcessor 设置处理器
	SetProcessor(processor Processor)

	// Run 运行整个系统
	Run(ctx context.Context, config map[string]interface{}) ([]*PublishResult, error)
}
