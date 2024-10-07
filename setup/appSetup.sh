#!/usr/bin/env bash

sudo nano /etc/systemd/system/printerapp.service
sudo printf %"
[Unit]
Description=Printer sharing Spring Boot application
After=network.target

[Service]
User=whoami
ExecStart=/usr/bin/java -jar /home/whoami/web_printer/application.jar
SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
"
sudo systemctl enable printerapp.service
sudo systemctl start printerapp.service

# install and configure prometheus
wget https://github.com/prometheus/prometheus/releases/download/v2.37.0/prometheus-2.37.0.linux-amd64.tar.gz
tar xvfz prometheus-*.tar.gz
cd prometheus-*

# Prometheus configuration file
sudo nano /etc/prometheus/prometheus.yml
sudo printf %"
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'spring_boot_app'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8080']
"
sudo nano /etc/systemd/system/prometheus.service
[Unit]
Description=Prometheus
Wants=network-online.target
After=network-online.target

[Service]
User=prometheus
ExcecStart=/path/to/prometheus --config.file /etc/prometheus/prometheus.yml

[Install]
WantedBy=default.target

# enable the service
sudo systemctl enable prometheus
sudo systemctl start prometheus

# setup grafana
sudo apt-get install -y software-properties-common
sudo add-apt-repository "deb https://packages.grafana.com/oss/deb stable main"
wget -q -O - https://packages.grafana.com/gpg.key | sudo apt-key add -
sudo apt-get update
sudo apt-get install grafana

# enable grafana
sudo systemctl enable grafana-server
sudo systemctl start grafana-server

# loki for logging
wget https://github.com/grafana/loki/releases/download/v2.8.0/loki-linux-amd64.zip
unzip loki-linux-amd64.zip
sudo mv loki-linux-amd64 /usr/local/bin/loki

# Loki config
sudo mkdir -p /etc/loki
sudo nano /etc/loki/loki-config.yaml
Sudo printf %"
auth_enabled: false

server:
  http_listen_port: 3100

ingester:
  lifecycler:
    address: 127.0.0.1
    ring:
      kvstore:
        store: inmemory
    final_sleep: 0s
  chunk_idle_period: 5m
  chunk_retain_period: 30s

schema_config:
  configs:
  - from: 2020-05-15
    store: boltdb
    object_store: filesystem
    schema: v11
    index:
      prefix: index_
      period: 168h

storage_config:
  boltdb:
    directory: /tmp/loki/index
  filesystem:
    directory: /tmp/loki/chunks

limits_config:
  enforce_metric_name: false
  reject_old_samples: true
  reject_old_samples_max_age: 168h

chunk_store_config:
  max_look_back_period: 0s

table_manager:
  retention_deletes_enabled: false
  retention_period: 0s"

sudo nano /etc/systemd/system/loki.service

sudo printf %s"
[Unit]
Description=Loki service
After=network.target

[Service]
Type=simple
ExecStart=/usr/local/bin/loki -config.file /etc/loki/loki-config.yaml

[Install]
WantedBy=multi-user.target"

sudo systemctl enable loki.service
sudo systemctl start loki.service

# promtail config
wget https://github.com/grafana/loki/releases/download/v2.8.0/promtail-linux-amd64.zip
unzip promtail-linux-amd64.zip
sudo mv promtail-linux-amd64 /usr/local/bin/promtail

sudo mkdir -p /etc/promtail
sudo nano /etc/promtail/promtail-config.yaml
sudo printf %s"
server:
  http_listen_port: 9080
  grpc_listen_port: 0

positions:
  filename: /tmp/positions.yaml

clients:
  - url: http://localhost:3100/loki/api/v1/push

scrape_configs:
- job_name: system
  static_configs:
  - targets:
      - localhost
    labels:
      job: varlogs
      __path__: /var/log/*log
- job_name: spring_boot
  static_configs:
  - targets:
      - localhost
    labels:
      job: spring_boot
      __path__: /path/to/your/spring-boot/logs/*.log"
sudo nano /etc/systemd/system/promtail.service
[Unit]
Description=Promtail service
After=network.target

[Service]
Type=simple
ExecStart=/usr/local/bin/promtail -config.file /etc/promtail/promtail-config.yaml

[Install]
WantededBy=multi-user.target

sudo systemctl enable promtail.service
sudo systemctl start promtail.service
