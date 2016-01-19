# How to use this image

Start Logstash with commandline configuration
If you need to run logstash with configuration provided on the commandline, you can use the logstash image as follows:

```
$ docker run -it --rm logstash logstash -e 'input { stdin { } } output { stdout { } }'
```

Start Logstash with configuration file
If you need to run logstash with a configuration file, logstash.conf, that's located in your current directory, you can use the logstash image as follows:

```
$ docker run -it --rm -v "$PWD":/config-dir logstash logstash -f /config-dir/logstash.conf
```
