From ubuntu:14.04


##update server

RUN apt-get update \

##install supervisor and setup supervisord.conf file

&& apt-get install -y supervisor \
 
&& mkdir -p /var/log/supervisor \

##install varnish

&& apt-get -y install varnish \

&& rm /etc/varnish/default.vcl \

&& rm /etc/default/varnish 


##add varnish configuration file


COPY ./default.vcl /etc/varnish/default.vcl
COPY ./varnish /etc/default/varnish
COPY ./supervisord.conf /etc/supervisor/conf.d/supervisord.conf

EXPOSE 6082 80

CMD ["/usr/bin/supervisord"]

