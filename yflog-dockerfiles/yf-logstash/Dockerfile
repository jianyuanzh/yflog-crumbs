FROM yflog:gosu

# install plugin dependencies
#RUN apt-get update && apt-get install -y --no-install-recommends \
#		libzmq3 \
#	&& rm -rf /var/lib/apt/lists/*

# the "ffi-rzmq-core" gem is very picky about where it looks for libzmq.so
#RUN mkdir -p /usr/local/lib \
#	&& ln -s /usr/lib/*/libzmq.so.3 /usr/local/lib/libzmq.so


# https://www.elastic.co/guide/en/logstash/2.0/package-repositories.html
# https://packages.elasticsearch.org/GPG-KEY-elasticsearch
#RUN apt-key adv --keyserver ha.pool.sks-keyservers.net --recv-keys 46095ACC8548582C1A2699A9D27D666CD88E42B4

#ENV LOGSTASH_MAJOR 2.1
#ENV LOGSTASH_VERSION 1:2.1.1-1

ADD 02-filebeat-input.conf /etc/logstash/conf.d/02-filebeat-input.conf
ADD 10-syslog.conf  /etc/logstash/conf.d/10-syslog.conf
ADD 30-elasticsearch-output.conf /etc/logstash/conf.d/30-elasticsearch-output.conf

#RUN echo "deb http://packages.elasticsearch.org/logstash/${LOGSTASH_MAJOR}/debian stable main" > /etc/apt/sources.list.d/logstash.list

#RUN set -x \
#	&& apt-get update \
#	&& apt-get install -y --no-install-recommends logstash=$LOGSTASH_VERSION \
#	&& rm -rf /var/lib/apt/lists/*

COPY  logstash_2.1.1-1_all.deb /tmp/
RUN cd /tmp && dpkg -i  logstash_2.1.1-1_all.deb 

ENV PATH /opt/logstash/bin:$PATH

COPY docker-entrypoint.sh /

EXPOSE 5044
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD service logstash start &&  /usr/sbin/sshd -D
