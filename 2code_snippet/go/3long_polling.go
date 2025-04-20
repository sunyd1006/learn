// server.go
package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"sync"
	"time"
)

var messages = make([]string, 0)
var mu sync.Mutex
var waiters = make([]chan string, 0)

func pollHandler(w http.ResponseWriter, r *http.Request) {
	mu.Lock()
	if len(messages) > 0 {
		msg := messages[0]
		messages = messages[1:]
		mu.Unlock()
		json.NewEncoder(w).Encode(map[string]string{"message": msg})
		return
	}

	ch := make(chan string, 1)
	waiters = append(waiters, ch)
	mu.Unlock()


	timeout := time.Duration(5) * time.Second
	select {
	case msg := <-ch:
		json.NewEncoder(w).Encode(map[string]string{"message": msg})
	case <-time.After(5 * time.Second):
		json.NewEncoder(w).Encode(map[string]string{"message": "timeout message from server after " + timeout.String() })
	}
}

func sendHandler(w http.ResponseWriter, r *http.Request) {
	var data map[string]string
	json.NewDecoder(r.Body).Decode(&data)
	msg := data["message"]

	mu.Lock()
	messages = append(messages, msg)
	for _, ch := range waiters {
		ch <- msg
	}
	fmt.Println("current long polling number: ", len(waiters))
	waiters = nil
	mu.Unlock()

	fmt.Fprintln(w, "ok")
}

func main() {
	http.Handle("/", http.FileServer(http.Dir(".")))
	http.HandleFunc("/poll", pollHandler)
	http.HandleFunc("/send", sendHandler)
	fmt.Println("Server at :8080")
	http.ListenAndServe(":8080", nil)
}