// crawlers.go
// 实现各种类型的采集器
package crawler

import (
	"context"
	"fmt"
	"io/ioutil"
	"log"
	"math/rand"
	"net/http"
	"strings"
	"time"

	"big_sun.com/auto_crawler/pkg/common"
	"github.com/gocolly/colly/v2"
	"github.com/mmcdole/gofeed"
)

// RSSCrawler RSS采集器实现
type RSSCrawler struct {
	name        string
	description string
	feedURLs    map[string]string
	parser      *gofeed.Parser
	config      map[string]interface{}
}

// NewRssCrawler 创建新的RSS采集器
func NewRssCrawler() *RSSCrawler {
	return &RSSCrawler{
		name:        "rss_crawler",
		description: "使用RSS协议采集新闻的采集器",
		feedURLs:    make(map[string]string),
		parser:      gofeed.NewParser(),
		config:      make(map[string]interface{}),
	}
}

func (c *RSSCrawler) Name() string {
	return c.name
}

func (c *RSSCrawler) Description() string {
	return c.description
}

func (c *RSSCrawler) SupportedProtocols() []string {
	return []string{"rss", "atom", "xml"}
}

func (c *RSSCrawler) Configure(config map[string]interface{}) error {
	for k, v := range config {
		c.config[k] = v
	}

	// 配置RSS源URLs
	if feeds, ok := config["feed_urls"].(map[string]interface{}); ok {
		for category, url := range feeds {
			if urlStr, ok := url.(string); ok {
				c.feedURLs[category] = urlStr
			}
		}
	}

	return nil
}

func (c *RSSCrawler) Fetch(ctx context.Context, params map[string]interface{}) (*common.CrawlerResult, error) {
	// 获取要采集的分类
	category, _ := params["category"].(string)
	if category == "" {
		category = "default"
	}

	// 获取对应的RSS URL
	feedURL, exists := c.feedURLs[category]
	if !exists {
		return nil, fmt.Errorf("未找到分类 %s 的RSS源", category)
	}

	log.Printf("[%s] 正在采集RSS源: %s (分类: %s)", c.name, feedURL, category)

	// 解析RSS订阅源
	feed, err := c.parser.ParseURL(feedURL)
	if err != nil {
		return nil, fmt.Errorf("解析RSS失败: %v", err)
	}

	var newsItems []common.News
	for _, item := range feed.Items {
		// 解析发布时间
		publishedTime := time.Now()
		if item.PublishedParsed != nil {
			publishedTime = *item.PublishedParsed
		} else if item.UpdatedParsed != nil {
			publishedTime = *item.UpdatedParsed
		}

		// 构建新闻项
		newsItem := common.News{
			Title:     item.Title,
			URL:       item.Link,
			Source:    feed.Title,
			Summary:   item.Description,
			Published: publishedTime,
			Category:  category,
		}

		// 如果配置中指定了source字段，则使用配置的source
		if configSource, ok := c.config["source"].(string); ok && configSource != "" {
			newsItem.Source = configSource
		}

		// 尝试获取完整内容
		if len(item.Content) > 0 {
			newsItem.Content = item.Content
		}

		newsItems = append(newsItems, newsItem)
	}

	return &common.CrawlerResult{
		News:   newsItems,
		Source: feed.Title,
	}, nil
}

func (c *RSSCrawler) Close() error {
	// RSS采集器没有需要关闭的资源
	return nil
}

// HTTPCrawler HTTP/Web爬虫采集器实现
type HTTPCrawler struct {
	name        string
	description string
	collector   *colly.Collector
	config      map[string]interface{}
	selectors   map[string]string
}

// NewHttpCrawler 创建新的HTTP采集器
func NewHttpCrawler() *HTTPCrawler {
	c := &HTTPCrawler{
		name:        "http_crawler",
		description: "使用HTTP协议和网页爬虫采集新闻的采集器",
		config:      make(map[string]interface{}),
		selectors:   make(map[string]string),
	}

	// 初始化默认的colly collector
	c.collector = colly.NewCollector(
		colly.AllowURLRevisit(),
	)

	// 添加随机延时以避免被反爬虫检测
	c.collector.SetRequestTimeout(120 * time.Second)

	return c
}

func (c *HTTPCrawler) Name() string {
	return c.name
}

func (c *HTTPCrawler) Description() string {
	return c.description
}

func (c *HTTPCrawler) SupportedProtocols() []string {
	return []string{"http", "https"}
}

func (c *HTTPCrawler) Configure(config map[string]interface{}) error {
	for k, v := range config {
		c.config[k] = v
	}

	// 配置选择器
	if selectors, ok := config["selectors"].(map[string]interface{}); ok {
		for key, selector := range selectors {
			if selectorStr, ok := selector.(string); ok {
				c.selectors[key] = selectorStr
			}
		}
	}

	// 配置User-Agent
	if userAgent, ok := config["user_agent"].(string); ok {
		c.collector.UserAgent = userAgent
	}

	// 配置域名限制
	if domains, ok := config["allowed_domains"].([]string); ok {
		c.collector = colly.NewCollector(
			colly.AllowedDomains(domains...),
			colly.AllowURLRevisit(),
		)
	}

	return nil
}

func (c *HTTPCrawler) Fetch(ctx context.Context, params map[string]interface{}) (*common.CrawlerResult, error) {
	// 获取要采集的URL
	url, ok := params["url"].(string)
	if !ok || url == "" {
		return nil, fmt.Errorf("URL参数不能为空")
	}

	category, _ := params["category"].(string)
	source, _ := params["source"].(string)

	log.Printf("[%s] 正在爬取URL: %s (分类: %s)", c.name, url, category)

	var newsItems []common.News

	// 设置随机数生成器
	randomGen := rand.New(rand.NewSource(time.Now().UnixNano()))

	// 添加随机延时
	c.collector.OnScraped(func(r *colly.Response) {
		randomDelay := time.Duration(randomGen.Intn(2000)+1000) * time.Millisecond
		time.Sleep(randomDelay)
	})

	// 设置请求头
	c.collector.OnRequest(func(r *colly.Request) {
		r.Headers.Set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
		r.Headers.Set("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
		r.Headers.Set("Connection", "keep-alive")
	})

	// 获取列表选择器
	listSelector, ok := c.selectors["list"]
	if !ok {
		listSelector = ".news-item, article, .article"
	}

	// 获取标题选择器
	titleSelector, ok := c.selectors["title"]
	if !ok {
		titleSelector = "h2, h3, .title, .headline"
	}

	// 获取链接选择器
	linkSelector, ok := c.selectors["link"]
	if !ok {
		linkSelector = "a"
	}

	// 获取时间选择器
	timeSelector, ok := c.selectors["time"]
	if !ok {
		timeSelector = ".time, .date, time"
	}

	// 解析新闻列表
	c.collector.OnHTML(listSelector, func(e *colly.HTMLElement) {
		// 提取标题
		title := e.ChildText(titleSelector)

		// 提取链接
		link := e.ChildAttr(linkSelector, "href")
		// 补全相对链接
		if !strings.HasPrefix(link, "http") {
			link = e.Request.AbsoluteURL(link)
		}

		// 提取时间
		timeText := e.ChildText(timeSelector)
		// 尝试解析时间
		publishedTime := time.Now()
		if timeText != "" {
			// 简单的时间解析，实际应用中可能需要更复杂的解析
			// 这里仅作为示例
			if strings.Contains(timeText, "-") {
				// 尝试解析YYYY-MM-DD格式
				parsedTime, err := time.Parse("2006-01-02", timeText)
				if err == nil {
					publishedTime = parsedTime
				}
			}
		}

		// 创建新闻项
		if title != "" && link != "" {
			newsItem := common.News{
				Title:     title,
				URL:       link,
				Source:    source,
				Category:  category,
				Published: publishedTime,
			}
			newsItems = append(newsItems, newsItem)
		}
	})

	// 访问URL
	err := c.collector.Visit(url)
	if err != nil {
		return nil, fmt.Errorf("访问URL失败: %v", err)
	}

	// 等待爬虫完成
	c.collector.Wait()

	return &common.CrawlerResult{
		News:   newsItems,
		Source: source,
	}, nil
}

func (c *HTTPCrawler) Close() error {
	// HTTP采集器没有需要关闭的资源
	return nil
}

// APICrawler API采集器实现
type APICrawler struct {
	name        string
	description string
	client      *http.Client
	headers     map[string]string
	config      map[string]interface{}
}

// NewAPICrawler 创建新的API采集器
func NewAPICrawler() *APICrawler {
	return &APICrawler{
		name:        "api_crawler",
		description: "使用API接口采集新闻的采集器",
		client: &http.Client{
			Timeout: 30 * time.Second,
		},
		config:  make(map[string]interface{}),
		headers: make(map[string]string),
	}
}

func (c *APICrawler) Name() string {
	return c.name
}

func (c *APICrawler) Description() string {
	return c.description
}

func (c *APICrawler) SupportedProtocols() []string {
	return []string{"api", "json", "rest"}
}

func (c *APICrawler) Configure(config map[string]interface{}) error {
	for k, v := range config {
		c.config[k] = v
	}

	// 配置请求头
	if headers, ok := config["headers"].(map[string]string); ok {
		for k, v := range headers {
			c.headers[k] = v
		}
	}

	return nil
}

func (c *APICrawler) Fetch(ctx context.Context, params map[string]interface{}) (*common.CrawlerResult, error) {
	// 获取API URL
	apiURL, ok := params["api_url"].(string)
	if !ok || apiURL == "" {
		return nil, fmt.Errorf("API URL参数不能为空")
	}

	category, _ := params["category"].(string)
	source, _ := params["source"].(string)

	log.Printf("[%s] 正在调用API: %s (分类: %s)", c.name, apiURL, category)

	// 创建请求
	req, err := http.NewRequestWithContext(ctx, "GET", apiURL, nil)
	if err != nil {
		return nil, fmt.Errorf("创建请求失败: %v", err)
	}

	// 设置请求头
	for k, v := range c.headers {
		req.Header.Set(k, v)
	}

	// 发送请求
	resp, err := c.client.Do(req)
	if err != nil {
		return nil, fmt.Errorf("发送请求失败: %v", err)
	}
	defer resp.Body.Close()

	// 检查响应状态
	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("API返回错误状态码: %d", resp.StatusCode)
	}

	// 读取响应体
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return nil, fmt.Errorf("读取响应体失败: %v", err)
	}

	// 这里仅作为示例，实际应用中需要根据API的响应格式进行解析
	// 这里假设API返回的是一个包含新闻列表的JSON
	var newsItems []common.News

	// 示例解析逻辑，实际应用中需要根据具体的API响应格式进行调整
	// 由于这是一个模拟实现，我们直接返回一个空的结果
	// 在实际应用中，应该解析JSON响应并提取新闻信息
	_ = body // 避免未使用变量的警告

	log.Printf("[%s] API调用完成，获取到 %d 条新闻", c.name, len(newsItems))

	return &common.CrawlerResult{
		News:   newsItems,
		Source: source,
	}, nil
}

func (c *APICrawler) Close() error {
	// API采集器没有需要关闭的资源
	return nil
}

// SimpleCrawlerFactory 简单的采集器工厂实现
type SimpleCrawlerFactory struct{}

// NewSimpleCrawlerFactory 创建新的采集器工厂
func NewSimpleCrawlerFactory() *SimpleCrawlerFactory {
	return &SimpleCrawlerFactory{}
}

func (f *SimpleCrawlerFactory) Create(crawlerType string) (common.Crawler, error) {
	switch strings.ToLower(crawlerType) {
	case "rss":
		return NewRssCrawler(), nil
	case "http", "web":
		return NewHttpCrawler(), nil
	case "api":
		return NewAPICrawler(), nil
	default:
		return nil, fmt.Errorf("不支持的采集器类型: %s", crawlerType)
	}
}

func (f *SimpleCrawlerFactory) SupportedTypes() []string {
	return []string{"rss", "http", "web", "api"}
}
