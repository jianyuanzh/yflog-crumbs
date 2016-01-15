FROM yflog:base

MAINTAINER Vincent Zhang 0.1

# install jdk from current dir
ADD jdk-8u65-linux-x64.tar.gz /usr/local/


RUN \
    ln -s /usr/local/jdk1.8.0_65/ /usr/local/jdk 

#Setup JAVA_HOME and JAVA in path
ENV JAVA_HOME /usr/local/jdk
ENV PATH $PATH:/usr/local/jdk/bin

CMD /usr/sbin/sshd -D
