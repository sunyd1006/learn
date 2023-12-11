 #!/bin/bash

# 通过加入一个 &, 启动一个进程，来并发的执行ut
for i in {1..254}; do
        ip="192.168.80.$i"
        ping -c 2 $ip &> /dev/null && echo $ip is up &
done
wait