# kibana docker file based on yflog:nginx

FROM yflog:nginx

RUN apt-get install -y wget ca-certificates
#RUN wget -q https://download.elastic.co/kibana/kibana/kibana-4.3.1-linux-x64.tar.gz -O /tmp/kibana-r.3.1-linux-x64.tar.gz \
#    && cd /tmp && tar zxvf kibana-4.3.1-linux-x64.tar.gz     \
#    && mkdir -p /opt/kibana        \
#    && cp -R kibana*/* /opt/kibana/

ADD  kibana-4.3.1-linux-x64.tar.gz /tmp  

RUN  cd /tmp && mkdir -p /opt/kibana && cp -R kibana*/* /opt/kibana/

COPY default /etc/nginx/sites-available/default

COPY htpasswd.users /etc/nginx/htpasswd.users

COPY kibana.yml /opt/kibana/config/kibana.yml

CMD service nginx start && /opt/kibana/bin/kibana 
