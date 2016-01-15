FROM ubuntu:14.04

MAINTAINER Vincent Zhang Version 0.1

RUN apt-get install -y wget vim tar unzip openssh-server openssh-client

RUN mkdir /var/run/sshd

RUN echo "root:testuser" | chpasswd

CMD /usr/sbin/sshd -D
