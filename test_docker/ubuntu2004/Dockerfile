FROM    ubuntu:20.04
LABEL   Fisher "fisher@sudops.com"

RUN /bin/echo 'root:123456' |chpasswd \
    useradd runoob  \
    /bin/echo 'runoob:123456' |chpasswd \
    /bin/echo -e "LANG=\"en_US.UTF-8\"" >/etc/default/local

# enable ubuntu ssh server
# https://blog.csdn.net/weixin_54703767/article/details/134818090
RUN apt-get update \
    && apt-get install -y openssh-server \
    && mkdir /var/run/sshd \
    && echo 'root:123456' |chpasswd \
    && sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed -i 's/#PasswordAuthentication yes/PasswordAuthentication yes/' /etc/ssh/sshd_config \
    && sed -i 's/#AllowTcpForwarding yes/AllowTcpForwarding yes/' /etc/ssh/sshd_config \
    && service ssh restart

USER root
EXPOSE  22
EXPOSE  80
CMD     /usr/sbin/sshd -D



