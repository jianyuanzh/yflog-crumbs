FROM yflog:base

MAINTAINER Vincent Zhang Version 0.1

RUN apt-get install -y curl   && \
    cd /tmp && \
    curl -LO 'http://download.oracle.com/otn-pub/java/jdk/8u65-b17/jdk-8u65-linux-x64.tar.gz' -H 'Cookie: oraclelicense=accept-securebackup-cookie' && \
    cd /usr/local && \
    tar zxvf /tmp/jdk-8u65-linux-x64.tar.gz && \
    ln -s /usr/local/jdk1.8.0_65/ /usr/local/jdk   && \
    rm -fr /tmp/jdk-8u65-linux-x64.tar.gz

# add JAVA_HOME and /usr/local/jdk/bin into /etc/profile
ENV JAVA_HOME /usr/local/jdk
ENV PATH $PATH:/usr/local/jdk/bin

CMD /usr/sbin/sshd -D

