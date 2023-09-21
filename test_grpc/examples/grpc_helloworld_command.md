
# cpp最简单的grpc demo
cd examples/cpp/helloworld/

1. 生成 gRPC 代码
make helloworld.grpc.pb.cc helloworld.pb.cc

相当于调用如下两个命令，第一个命令用于生成 grpc 的服务代码，第二个命令用于生成 protobuf 的消息代码。
protoc -I ../../protos/ --grpc_out=. --plugin=protoc-gen-grpc=grpc_cpp_plugin ../../protos/helloworld.proto
protoc -I ../../protos/ --cpp_out=. ../../protos/helloworld.proto


// 第1个命令主要用来生成 grpc 的服务代码，这个服务代码用于创建 grpc 服务以及客户端的存根。 -I ../../protos/：设置 protoc 查找 .proto 文件的目录。--grpc_out=.：设置生成的 grpc 服务代码的输出目录，这里是当前目录。--plugin=protoc-gen-grpc=grpc_cpp_plugin：指定用于生成 grpc 服务代码的插件，这里使用的是 grpc_cpp_plugin。../../protos/helloworld.proto：这是待编译的 .proto 文件。

// 第2个命令要用来生成 protobuf 的消息代码，这个消息代码用于序列化和反序列化 protobuf 消息。--cpp_out=.：设置生成的 protobuf 消息代码的输出目录，这里是当前目录。

2. 写一个服务器并启动服务端

  greeter_server.cc 实现了 Greeter 服务所需要的行为。

3. 写一个客户端并启动

  greeter_client.cc 里查看完整的客户端代码。Status status = stub_->SayHello(&context, request, &reply); stub存根是grpc发送rpc请求的对象，stub都构造依赖channel，channel(localhost:50051)是一个ip+port的绑定。


4. 测试执行
    执行make或者g++ -std=c++17 `pkg-config --cflags protobuf grpc absl_flags absl_flags_parse`  -c -o helloworld.pb.o helloworld.pb.cc。注意make依赖的g++14不支持某些类型，所以大话孙直接改成了g++17。


