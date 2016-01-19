## Build ngix from base
FROM yflog:base

MAINTAINER Vincent Zhang Version 0.1

RUN apt-get install -y nginx   

EXPOSE 80 443

CMD ["nginx", "-g", "daemon off;"]

