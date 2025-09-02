// system.go
// 实现新闻系统的核心逻辑
package service

import (
	"context"
	"fmt"
	"log"
	"sync"

	"big_sun.com/auto_crawler/pkg/common"
)

// SimpleNewsSystem 简单的新闻系统实现
type SimpleNewsSystem struct {
	crawlers   []common.Crawler
	processors []common.Processor
	publishers []common.Publisher
	mutex      sync.RWMutex
}

// NewSimpleNewsSystem 创建新的新闻系统
func NewSimpleNewsSystem() *SimpleNewsSystem {
	return &SimpleNewsSystem{
		crawlers:   make([]common.Crawler, 0),
		processors: make([]common.Processor, 0),
		publishers: make([]common.Publisher, 0),
	}
}

// RegisterCrawler 注册采集器
func (s *SimpleNewsSystem) RegisterCrawler(crawler common.Crawler) {
	s.mutex.Lock()
	defer s.mutex.Unlock()

	// 检查是否已经注册了相同名称的采集器
	for _, c := range s.crawlers {
		if c.Name() == crawler.Name() {
			log.Printf("[System] 采集器 %s 已经注册，跳过注册", crawler.Name())
			return
		}
	}

	s.crawlers = append(s.crawlers, crawler)
	log.Printf("[System] 成功注册采集器: %s (%s)", crawler.Name(), crawler.Description())
}

// RegisterPublisher 注册发布器
func (s *SimpleNewsSystem) RegisterPublisher(publisher common.Publisher) {
	s.mutex.Lock()
	defer s.mutex.Unlock()

	// 检查是否已经注册了相同名称的发布器
	for _, p := range s.publishers {
		if p.Name() == publisher.Name() {
			log.Printf("[System] 发布器 %s 已经注册，跳过注册", publisher.Name())
			return
		}
	}

	s.publishers = append(s.publishers, publisher)
	log.Printf("[System] 成功注册发布器: %s (%s)", publisher.Name(), publisher.Description())
}

// SetProcessor 设置处理器
func (s *SimpleNewsSystem) SetProcessor(processor common.Processor) {
	s.mutex.Lock()
	defer s.mutex.Unlock()

	// 检查是否已经注册了相同名称的处理器
	for i, p := range s.processors {
		if p.Name() == processor.Name() {
			log.Printf("[System] 更新处理器: %s", processor.Name())
			s.processors[i] = processor
			return
		}
	}

	s.processors = append(s.processors, processor)
	log.Printf("[System] 成功设置处理器: %s", processor.Name())
}

// Run 运行整个系统
func (s *SimpleNewsSystem) Run(ctx context.Context, config map[string]interface{}) ([]*common.PublishResult, error) {
	log.Println("[System] 开始运行新闻采集系统")

	// 检查必要的组件
	s.mutex.RLock()
	hasCrawlers := len(s.crawlers) > 0
	hasProcessors := len(s.processors) > 0
	hasPublishers := len(s.publishers) > 0
	s.mutex.RUnlock()

	if !hasCrawlers {
		return nil, fmt.Errorf("没有注册任何采集器")
	}

	if !hasProcessors {
		return nil, fmt.Errorf("没有设置任何处理器")
	}

	if !hasPublishers {
		return nil, fmt.Errorf("没有注册任何发布器")
	}

	// 1. 采集新闻
	crawlerResults, err := s.collectNews(ctx, config)
	if err != nil {
		return nil, fmt.Errorf("采集新闻失败: %v", err)
	}

	if len(crawlerResults) == 0 {
		log.Println("[System] 没有采集到任何新闻数据")
		return []*common.PublishResult{}, nil
	}

	// 2. 处理新闻
	processorResults, err := s.processNews(ctx, crawlerResults)
	if err != nil {
		return nil, fmt.Errorf("处理新闻失败: %v", err)
	}

	// 3. 发布新闻
	publishResults, err := s.publishNews(ctx, processorResults, config)
	if err != nil {
		return nil, fmt.Errorf("发布新闻失败: %v", err)
	}

	log.Println("[System] 新闻采集系统运行完成")
	return publishResults, nil
}

// collectNews 采集新闻
func (s *SimpleNewsSystem) collectNews(ctx context.Context, config map[string]interface{}) ([]*common.CrawlerResult, error) {
	log.Println("[System] 开始采集新闻")

	var results []*common.CrawlerResult
	var wg sync.WaitGroup
	var mu sync.Mutex
	errors := make(chan error, 10) // 调整通道大小

	// 获取采集器配置
	crawlersConfig, ok := config["crawlers"].(map[string]interface{})
	if !ok {
		crawlersConfig = make(map[string]interface{})
	}

	// 创建采集器名称到实例的映射
	s.mutex.RLock()
	crawlerMap := make(map[string]common.Crawler)
	for _, crawler := range s.crawlers {
		crawlerMap[crawler.Name()] = crawler
	}
	s.mutex.RUnlock()

	// 遍历所有配置的采集任务
	for configName, configValue := range crawlersConfig {
		crawlerConfig, ok := configValue.(map[string]interface{})
		if !ok {
			log.Printf("[System] 采集器配置 %s 格式错误，跳过", configName)
			continue
		}

		// 获取crawler_type，确定使用哪个采集器实例
		crawlerType, ok := crawlerConfig["crawler_type"].(string)
		if !ok {
			// 如果没有指定type，尝试使用配置名称作为采集器名称
			crawlerType = configName
			log.Fatalf("[System] 采集器配置 %s 未指定crawler_type，使用配置名称作为默认值", configName)
		}

		// 查找对应的采集器实例
		crawler, exists := crawlerMap[crawlerType]
		if !exists {
			log.Printf("[System] 未找到类型为 %s 的采集器实例，跳过配置 %s", crawlerType, configName)
			continue
		}

		wg.Add(1)
		go func(c common.Crawler, cfg map[string]interface{}, name string) {
			defer wg.Done()

			// 配置采集器
			if err := c.Configure(cfg); err != nil {
				log.Printf("[System] 配置采集器 %s (配置: %s) 失败: %v", c.Name(), name, err)
				errors <- err
				return
			}

			// 获取采集参数
			fetchParams, ok := cfg["fetch_params"].(map[string]interface{})
			if !ok {
				fetchParams = make(map[string]interface{})
			}

			// 采集新闻
			result, err := c.Fetch(ctx, fetchParams)
			if err != nil {
				log.Printf("[System] 采集器 %s (配置: %s) 执行失败: %v", c.Name(), name, err)
				errors <- err
				return
			}

			if result != nil && len(result.News) > 0 {
				mu.Lock()
				results = append(results, result)
				mu.Unlock()
				log.Printf("[System] 采集器 %s (配置: %s) 成功采集 %d 条新闻", c.Name(), name, len(result.News))
			}
		}(crawler, crawlerConfig, configName)
	}

	// 等待所有采集任务完成
	wg.Wait()
	close(errors)

	// 检查错误
	var lastError error
	for err := range errors {
		lastError = err
	}

	// 如果有错误但也有成功的结果，仍然继续处理
	if lastError != nil && len(results) == 0 {
		return nil, lastError
	}

	log.Printf("[System] 新闻采集完成，共 %d 个采集任务返回有效数据", len(results))
	return results, nil
}

// processNews 处理新闻
func (s *SimpleNewsSystem) processNews(ctx context.Context, results []*common.CrawlerResult) ([]*common.ProcessorResult, error) {
	log.Println("[System] 开始处理新闻")

	var processorResults []*common.ProcessorResult

	// 使用第一个处理器处理所有采集结果
	if len(s.processors) > 0 {
		processor := s.processors[0]

		for _, crawlerResult := range results {
			result, err := processor.Process(ctx, crawlerResult)
			if err != nil {
				log.Printf("[System] 处理器 %s 处理失败: %v", processor.Name(), err)
				continue
			}

			processorResults = append(processorResults, result)
			log.Printf("[System] 处理器 %s 成功处理 %d 条新闻", processor.Name(), len(result.UniqueNews))
		}
	}

	log.Printf("[System] 新闻处理完成，共处理 %d 个采集结果", len(processorResults))
	return processorResults, nil
}

// publishNews 发布新闻
func (s *SimpleNewsSystem) publishNews(ctx context.Context, results []*common.ProcessorResult, config map[string]interface{}) ([]*common.PublishResult, error) {
	log.Println("[System] 开始发布新闻")

	var publishResults []*common.PublishResult
	var wg sync.WaitGroup
	var mu sync.Mutex

	// 获取发布器配置
	publishersConfig, ok := config["publishers"].(map[string]interface{})
	if !ok {
		publishersConfig = make(map[string]interface{})
	}

	// 创建发布器名称到实例的映射
	s.mutex.RLock()
	publisherMap := make(map[string]common.Publisher)
	for _, publisher := range s.publishers {
		publisherMap[publisher.Name()] = publisher
	}
	s.mutex.RUnlock()

	// 遍历所有配置的发布任务
	for configName, configValue := range publishersConfig {
		publisherConfig, ok := configValue.(map[string]interface{})
		if !ok {
			log.Printf("[System] 发布器配置 %s 格式错误，跳过", configName)
			continue
		}

		// 获取publisher_type，确定使用哪个发布器实例
		publisherType, ok := publisherConfig["publisher_type"].(string)
		if !ok {
			// 如果没有指定type，尝试使用配置名称作为发布器名称
			publisherType = configName
			log.Printf("[System] 发布器配置 %s 未指定publisher_type，使用配置名称作为默认值", configName)
		}

		// 查找对应的发布器实例
		publisher, exists := publisherMap[publisherType]
		if !exists {
			log.Printf("[System] 未找到类型为 %s 的发布器实例，跳过配置 %s", publisherType, configName)
			continue
		}

		wg.Add(1)
		go func(p common.Publisher, cfg map[string]interface{}, name string) {
			defer wg.Done()

			// 配置发布器
			if err := p.Configure(cfg); err != nil {
				log.Printf("[System] 配置发布器 %s (配置: %s) 失败: %v", p.Name(), name, err)
				return
			}

			// 获取要发布的平台
			platforms, ok := cfg["platforms"].([]string)
			if !ok || len(platforms) == 0 {
				// 默认使用第一个支持的平台
				supportedPlatforms := p.SupportedPlatforms()
				if len(supportedPlatforms) > 0 {
					platforms = []string{supportedPlatforms[0]}
				} else {
					log.Printf("[System] 发布器 %s (配置: %s) 不支持任何平台", p.Name(), name)
					return
				}
			}

			// 发布到每个平台
			for _, platform := range platforms {
				for _, processorResult := range results {
					publishResult, err := p.Publish(ctx, platform, processorResult)
					if err != nil {
						log.Printf("[System] 发布器 %s (配置: %s) 发布到 %s 平台失败: %v", p.Name(), name, platform, err)
						continue
					}

					mu.Lock()
					publishResults = append(publishResults, publishResult)
					mu.Unlock()
				}
			}
		}(publisher, publisherConfig, configName)
	}

	// 等待所有发布任务完成
	wg.Wait()

	log.Printf("[System] 新闻发布完成，共发布 %d 个结果", len(publishResults))
	return publishResults, nil
}

// Close 关闭系统，释放资源
func (s *SimpleNewsSystem) Close() error {
	log.Println("[System] 关闭新闻采集系统")

	// 关闭所有采集器
	s.mutex.RLock()
	defer s.mutex.RUnlock()

	for _, crawler := range s.crawlers {
		if err := crawler.Close(); err != nil {
			log.Printf("[System] 关闭采集器 %s 失败: %v", crawler.Name(), err)
		}
	}

	// 关闭所有发布器
	for _, publisher := range s.publishers {
		if err := publisher.Close(); err != nil {
			log.Printf("[System] 关闭发布器 %s 失败: %v", publisher.Name(), err)
		}
	}

	return nil
}

// GetCrawlers 获取所有注册的采集器
func (s *SimpleNewsSystem) GetCrawlers() []common.Crawler {
	s.mutex.RLock()
	defer s.mutex.RUnlock()

	// 返回副本以避免并发问题
	crawlers := make([]common.Crawler, len(s.crawlers))
	copy(crawlers, s.crawlers)
	return crawlers
}

// GetPublishers 获取所有注册的发布器
func (s *SimpleNewsSystem) GetPublishers() []common.Publisher {
	s.mutex.RLock()
	defer s.mutex.RUnlock()

	// 返回副本以避免并发问题
	publishers := make([]common.Publisher, len(s.publishers))
	copy(publishers, s.publishers)
	return publishers
}

// GetProcessors 获取所有设置的处理器
func (s *SimpleNewsSystem) GetProcessors() []common.Processor {
	s.mutex.RLock()
	defer s.mutex.RUnlock()

	// 返回副本以避免并发问题
	processors := make([]common.Processor, len(s.processors))
	copy(processors, s.processors)
	return processors
}
