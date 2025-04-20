#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <fcntl.h>

#define BUF_SIZE 1024

#ifdef __APPLE__  // macOS 使用 kqueue
#include <sys/event.h>
#include <sys/time.h>

void run_event_loop() {
    int kq = kqueue();
    if (kq == -1) {
        perror("kqueue");
        exit(EXIT_FAILURE);
    }

    struct kevent evSet;
    EV_SET(&evSet, STDIN_FILENO, EVFILT_READ, EV_ADD, 0, 0, NULL);

    if (kevent(kq, &evSet, 1, NULL, 0, NULL) == -1) {
        perror("kevent register");
        exit(EXIT_FAILURE);
    }

    printf("Using kqueue (macOS): Listening on stdin (type 'exit' to quit)...\n");

    while (1) {
        struct kevent evList[1];

        struct timespec timeout;
        timeout.tv_sec = 5;
        timeout.tv_nsec = 0;

        int nev = kevent(kq, NULL, 0, evList, 1, &timeout);
        if (nev == -1) {
            perror("kevent wait");
            exit(EXIT_FAILURE);
        } else if (nev == 0) {
            printf("Timeout: no input received.\n");
            break;
        }

        if (evList[0].filter == EVFILT_READ) {
            char buf[BUF_SIZE];
            ssize_t len = read(STDIN_FILENO, buf, sizeof(buf) - 1);
            if (len > 0) {
                buf[len] = '\0';
                printf("You typed: %s", buf);
                if (strncmp(buf, "exit", 4) == 0) break;
            }
        }
    }

    close(kq);
}

#else  // Linux 使用 epoll

#include <sys/epoll.h>

int set_nonblocking(int fd) {
    int flags = fcntl(fd, F_GETFL, 0);
    if (flags == -1) return -1;
    return fcntl(fd, F_SETFL, flags | O_NONBLOCK);
}

void run_event_loop() {
    int epoll_fd = epoll_create1(0);
    if (epoll_fd == -1) {
        perror("epoll_create1");
        exit(EXIT_FAILURE);
    }

    set_nonblocking(STDIN_FILENO);

    struct epoll_event ev;
    ev.events = EPOLLIN;
    ev.data.fd = STDIN_FILENO;

    if (epoll_ctl(epoll_fd, EPOLL_CTL_ADD, STDIN_FILENO, &ev) == -1) {
        perror("epoll_ctl");
        exit(EXIT_FAILURE);
    }

    printf("Using epoll (Linux): Listening on stdin (type 'exit' to quit)...\n");

    while (1) {
        struct epoll_event events[1];
        int n_ready = epoll_wait(epoll_fd, events, 1, 5000);  // timeout = 5000ms = 5s
        if (n_ready == -1) {
            perror("epoll_wait");
            exit(EXIT_FAILURE);
        } else if (n_ready == 0) {
            printf("Timeout: no input received.\n");
            break;
        }

        if (events[0].data.fd == STDIN_FILENO) {
            char buf[BUF_SIZE];
            ssize_t len = read(STDIN_FILENO, buf, sizeof(buf) - 1);
            if (len > 0) {
                buf[len] = '\0';
                printf("You typed: %s", buf);
                if (strncmp(buf, "exit", 4) == 0) break;
            }
        }
    }

    close(epoll_fd);
}

#endif

int main() {
    run_event_loop();
    printf("Exiting program.\n");
    return 0;
}