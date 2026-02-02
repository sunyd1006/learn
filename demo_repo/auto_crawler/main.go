// main.go
// 新闻采集系统主程序
package main

import (
	"context"
	"fmt"
	"log"
	"os"
	"os/signal"
	"syscall"

	"big_sun.com/auto_crawler/pkg/common"
	"big_sun.com/auto_crawler/pkg/crawler"
	"big_sun.com/auto_crawler/pkg/processor"
	"big_sun.com/auto_crawler/pkg/publisher"
	"big_sun.com/auto_crawler/pkg/service"
)

// main 程序入口函数
func main() {
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	// 设置信号处理，支持优雅退出
	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)
	go func() {
		<-sigChan
		fmt.Println("\n接收到终止信号，正在优雅退出...")
		cancel()
	}()

	// 1. 创建新闻系统
	system := service.NewSimpleNewsSystem()
	// 注意：SimpleNewsSystem目前没有Close方法，后续可以添加

	// 2. 创建并注册采集器
	// 2.1 创建RSS采集器
	rssCrawler := crawler.NewRssCrawler()
	system.RegisterCrawler(rssCrawler)

	// 2.2 创建HTTP采集器
	httpCrawler := crawler.NewHttpCrawler()
	system.RegisterCrawler(httpCrawler)

	// 2.3 创建API采集器
	apiCrawler := crawler.NewAPICrawler()
	system.RegisterCrawler(apiCrawler)

	// 注意：不再需要单独注册钛媒体采集器，通过配置的crawler_type动态使用RSS采集器
	// tmtPostCrawler := crawler.NewTmtPostCrawler()
	// system.RegisterCrawler(tmtPostCrawler)

	// 3. 创建并设置处理器
	defaultProcessor := processor.NewDefaultProcessor()
	system.SetProcessor(defaultProcessor)

	// 4. 创建并注册发布器
	// 4.1 创建JSON发布器
	jsonPublisher := publisher.NewJSONPublisher()
	system.RegisterPublisher(jsonPublisher)

	// 4.2 创建今日头条发布器
	toutiaoPublisher := publisher.NewToutiaoPublisher()
	system.RegisterPublisher(toutiaoPublisher)

	// 5. 配置整个系统
	config := createExampleConfig()

	// 6. 运行系统
	results, err := system.Run(ctx, config)
	if err != nil {
		log.Fatalf("系统运行失败: %v", err)
	}

	// 7. 输出运行结果
	printResults(results)

	fmt.Println("\n✅ 新闻采集系统运行完成!")
}

// createExampleConfig 创建示例配置
func createExampleConfig() map[string]interface{} {
	return map[string]interface{}{
		"crawlers": map[string]interface{}{
			// WSJ RSS采集配置
			// "wsj_rss": map[string]interface{}{
			// 	"crawler_type": "rss_crawler", // 明确指定采集器类型
			// 	"feed_urls": map[string]interface{}{
			// 		"business":   "https://feeds.a.dj.com/rss/WSJcomUSBusiness.xml",
			// 		"technology": "https://feeds.a.dj.com/rss/RSSWSJD.xml",
			// 	},
			// 	"fetch_params": map[string]interface{}{
			// 		"category": "business",
			// 		"source":   "华尔街日报",
			// 	},
			// },
			// 钛媒体RSS采集配置
			"tmtpost_rss": map[string]interface{}{
				"crawler_type": "rss_crawler", // 同样使用RSS采集器类型
				"feed_urls": map[string]interface{}{
					"default":  "https://www.tmtpost.com/feed",
					"tech":     "https://www.tmtpost.com/feed",
					"business": "https://www.tmtpost.com/feed",
				},
				"fetch_params": map[string]interface{}{
					"category": "default",
					"source":   "钛媒体",
				},
			},
			// // HTTP采集器配置
			// "thepaper_http": map[string]interface{}{
			// 	"crawler_type":    "http_crawler",
			// 	"allowed_domains": []string{"www.thepaper.cn", "thepaper.cn"},
			// 	"user_agent":      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36",
			// 	"selectors": map[string]interface{}{
			// 		"list":  ".news_li",
			// 		"title": "h2 a",
			// 		"link":  "h2 a",
			// 		"time":  ".time",
			// 	},
			// 	"fetch_params": map[string]interface{}{
			// 		"url":      "https://www.thepaper.cn/channel_tech",
			// 		"category": "tech",
			// 		"source":   "澎湃新闻",
			// 	},
			// },
			// // API采集器配置
			// "example_api": map[string]interface{}{
			// 	"crawler_type": "api_crawler",
			// 	"headers": map[string]string{
			// 		"Accept":     "application/json",
			// 		"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36",
			// 	},
			// 	"fetch_params": map[string]interface{}{
			// 		"api_url":  "https://api.example.com/news",
			// 		"category": "general",
			// 		"source":   "API Source",
			// 	},
			// },
		},
		"publishers": map[string]interface{}{
			// "json_publisher": map[string]interface{}{
			// 	"publisher_type": "json_publisher",
			// 	"save_path":      "./output",
			// 	"platforms":      []string{"json"},
			// },
			"toutiao_publisher": map[string]interface{}{
				"publisher_type": "toutiao_publisher",
				"app_id":         "your_app_id",
				"app_secret":     "your_app_secret",
				"platforms":      []string{"toutiao"},
			},
			// "markdown_publisher": map[string]interface{}{
			// 	"publisher_type": "markdown_publisher",
			// 	"save_path":      "./output",
			// 	"platforms":      []string{"markdown"},
			// },
		},
	}
}

// printResults 打印运行结果
func printResults(results []*common.PublishResult) {
	fmt.Println("\n========================================")
	fmt.Println("        系统运行结果")
	fmt.Println("========================================")

	if len(results) == 0 {
		fmt.Println("没有发布结果")
		return
	}

	totalSuccess := 0
	totalCount := 0

	for _, result := range results {
		status := "成功"
		if !result.Success {
			status = "失败"
		}

		fmt.Printf("平台: %s\n", result.Platform)
		fmt.Printf("状态: %s\n", status)
		fmt.Printf("数量: %d\n", result.Count)
		if result.Message != "" {
			fmt.Printf("消息: %s\n", result.Message)
		}
		fmt.Println("----------------------------------------")

		if result.Success {
			totalSuccess++
			totalCount += result.Count
		}
	}

	fmt.Printf("\n总计: %d/%d 个发布任务成功, 共发布 %d 条新闻\n", totalSuccess, len(results), totalCount)
}
