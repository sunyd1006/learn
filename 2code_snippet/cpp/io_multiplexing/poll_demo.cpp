#include <stdio.h>
#include <sys/poll.h>
#include <unistd.h>

#define TIMEOUT 5 /* poll timeout, in seconds */

int main(void) {
    // 1. pollfd集合是无限长度大小的，不想select只能监听1024个
    struct pollfd fds[2];
    int ret;

    /* watch stdin for input */
    fds[0].fd = STDIN_FILENO;
    fds[0].events = POLLIN;

    /* watch stdout for ability to write (almost
    always true) */
    fds[1].fd = STDOUT_FILENO;
    fds[1].events = POLLOUT;

    /* All set, block! */
    /*
        成功时，poll返回具有非零revents字段的文件描述符个数，revents里面存储了那些事件是ready的
        超前前，没有任何事件则返回0
        失败时返回-1， errno被设置为下列值之一：EBADF/
    */
    ret = poll(fds, 2, TIMEOUT * 1000);
    if (ret == -1) {
        perror("poll");
        return 1;
    }

    if (!ret) {
        printf("%d seconds elapsed.\n", TIMEOUT);
        return 0;
    }

    // 2. pollfd集合是
    if (fds[0].revents & POLLIN) printf("stdin is readable\n");
    if (fds[1].revents & POLLOUT) printf("stdout is writable\n");

    return 0;
}
