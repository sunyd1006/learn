

# GRPC 教程


* 官网：https://grpc.io/docs/what-is-grpc/core-concepts/#metadata
* grpc中文文档：https://doc.oschina.net/grpc?t=58009


核心概念

* channel：一个channel代表一个和服务端的host+post的连接，主要在create stub的时候使用。 client 能制定cahnnel 的参数来修改 grpc的行为，如 message是否要压缩 或者关闭，提供了对连接、超时、重试等的控制能力，间接地实现了对通信的控制。channel 的状态有：connected、idle。gRPC 如何处理closed 的chanel是语言相关的，有些语言可允许查询频道状态。
* stub：一个客户端利用stub，发送rpc 请求。
* grpc server context和 ServerContext。
* metadata：是一个键值对。User-defined metadata is not used by gRPC, which allows the client to provide information associated with the call to the server and vice versa.
* rpc 是可以取消的。
* rpc 终止。可能服务端认为rpc call 完成（例如成功响应了消息)，但是客户端却以rpc call超时为理由来拒绝。
* deadlines/timesouts。 客户端的可以制定一个rpc call的timeouts时间、或者durations time。
* grpc提供4类关于流式API：普通的非流式unary rpc、server streaming rpc、client streaming rpc、bidiractional straming rpc。server streaming rpc。 客户端发送一个请求，服务端返回流式数据，等服务端返回所有的stream data后，After sending all its messages, the server’s status details (status code and optional status message) and optional trailing metadata are sent to the client。

```
A server-streaming RPC is similar to a unary RPC, except that the server returns a stream of messages in response to a client’s request. After sending all its messages, the server’s status details (status code and optional status message) and optional trailing metadata are sent to the client. This completes processing on the server side. The client completes once it has all the server’s messages.
```
