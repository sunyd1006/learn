package main

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"os/exec"
	"path/filepath"
	"time"

	"big_sun.com/auto_crawler/pkg/common"
)

type PythonPublisher struct {
	scriptPath string
}

func NewPythonPublisher() *PythonPublisher {
	scriptDir, _ := filepath.Abs("./scripts")
	scriptPath := filepath.Join(scriptDir, "toutiao_publisher.py")

	return &PythonPublisher{
		scriptPath: scriptPath,
	}
}

func (p *PythonPublisher) Name() string {
	return "python_publisher"
}

func (p *PythonPublisher) Description() string {
	return "使用 Python Playwright 发布到今日头条"
}

func (p *PythonPublisher) Configure(config map[string]interface{}) error {
	if scriptPath, ok := config["script_path"].(string); ok && scriptPath != "" {
		p.scriptPath = scriptPath
	}
	return nil
}

func (p *PythonPublisher) Close() error {
	return nil
}

func (p *PythonPublisher) SupportedPlatforms() []string {
	return []string{"toutiao", "python", "playwright"}
}

func (p *PythonPublisher) Publish(ctx context.Context, platform string, data *common.ProcessorResult) (*common.PublishResult, error) {
	if data == nil || len(data.UniqueNews) == 0 {
		return &common.PublishResult{
			Platform: platform,
			Success:  true,
			Count:    0,
			Message:  "没有新闻数据需要发布",
		}, nil
	}

	log.Printf("[%s] 开始通过 Python Playwright 发布 %d 条新闻", p.Name(), len(data.UniqueNews))

	tempFile, err := p.createTempNewsFile(data)
	if err != nil {
		return nil, fmt.Errorf("创建临时文件失败: %v", err)
	}
	defer os.Remove(tempFile)

	result, err := p.runPythonScript(ctx, tempFile, len(data.UniqueNews))
	if err != nil {
		return &common.PublishResult{
			Platform: platform,
			Success:  false,
			Count:    0,
			Message:  fmt.Sprintf("Python 脚本执行失败: %v", err),
		}, nil
	}

	return result, nil
}

func (p *PythonPublisher) createTempNewsFile(data *common.ProcessorResult) (string, error) {
	tempDir := os.TempDir()
	timestamp := time.Now().Format("20060102_150405")
	tempFile := filepath.Join(tempDir, fmt.Sprintf("toutiao_news_%s.json", timestamp))

	fileData, err := json.MarshalIndent(data, "", "  ")
	if err != nil {
		return "", err
	}

	err = os.WriteFile(tempFile, fileData, 0644)
	if err != nil {
		return "", err
	}

	return tempFile, nil
}

func (p *PythonPublisher) runPythonScript(ctx context.Context, newsFile string, newsCount int) (*common.PublishResult, error) {
	cmd := exec.CommandContext(ctx, "python3", p.scriptPath, newsFile)

	var stdout, stderr bytes.Buffer
	cmd.Stdout = &stdout
	cmd.Stderr = &stderr

	err := cmd.Run()

	output := stdout.String()
	errorOutput := stderr.String()

	log.Printf("[%s] Python 脚本输出:\n%s", p.Name(), output)

	if errorOutput != "" {
		log.Printf("[%s] Python 脚本错误:\n%s", p.Name(), errorOutput)
	}

	if err != nil {
		return &common.PublishResult{
			Platform: "toutiao",
			Success:  false,
			Count:    0,
			Message:  fmt.Sprintf("执行失败: %v - %s", err, errorOutput),
		}, nil
	}

	return &common.PublishResult{
		Platform: "toutiao",
		Success:  true,
		Count:    newsCount,
		Message:  fmt.Sprintf("Python 脚本执行成功:\n%s", output),
	}, nil
}

func main() {
	fmt.Println("测试 Go 调用 Python 脚本")
	fmt.Println("============================")

	publisher := NewPythonPublisher()

	testData := &common.ProcessorResult{
		UniqueNews: []common.News{
			{
				Title:    "测试新闻标题",
				Content:  "这是测试新闻内容",
				URL:      "https://example.com/test",
				Source:   "测试来源",
				Category: "科技",
			},
		},
		ClassifiedNews: map[string][]common.News{
			"科技": {
				{
					Title:    "测试新闻标题",
					Content:  "这是测试新闻内容",
					URL:      "https://example.com/test",
					Source:   "测试来源",
					Category: "科技",
				},
			},
		},
	}

	ctx := context.Background()
	result, err := publisher.Publish(ctx, "toutiao", testData)

	if err != nil {
		fmt.Printf("发布失败: %v\n", err)
		return
	}

	fmt.Printf("发布结果: %+v\n", result)
}
