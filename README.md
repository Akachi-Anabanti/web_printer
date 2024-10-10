# Application Setup Guide

## Application Start Up

### 1. Ubuntu Server

a. Download the setup script:
```bash
curl -O https://raw.githubusercontent.com/Akachi-Anabanti/web_printer/refs/heads/main/setup/ServerSetup.sh
```

b. Change the file permissions:
```bash
chmod u+x ServerSetup.sh
```

c. Execute the Setup Script:
This installs the full application with the monitoring systems.

**Note:** If you do not want to use the Monitoring systems, comment out the prometheus, grafana, and loki_promtail config imports in the `master.yml` file.

### 2. Vagrant

a. Ensure Vagrant and VirtualBox are installed and running on your computer.
   You will find detailed installation instructions on the VirtualBox and Vagrant pages.

b. Download the VagrantFile:
```bash
curl -O https://raw.githubusercontent.com/Akachi-Anabanti/web_printer/refs/heads/main/setup/VagrantFile
```

c. Start the Vagrant machine in the same folder where the file is downloaded:
```bash
vagrant up
```

The setup is automated and should set up the application.

Then set up CUPS.

You can then visit the application on localhost or server_ip.

## General Setup

### CUPS Setup

1. Edit the `/etc/cups/cupsd.conf` file and modify with the following values:

```conf
Listen *:631

# Restrict access to the server...
<Location />
  Order allow,deny
  Allow all
</Location>

# Restrict access to the admin pages...
<Location /admin>
  Order allow,deny
  Allow all
</Location>

# Restrict access to configuration files...
<Location /admin/conf>
  AuthType Default
  Require user @SYSTEM
  Order allow,deny
  Allow all
</Location>
```

2. Create a user, set password, and add user to cups group.
   Note the details as this is the admin of CUPS.

3. Install gutenprint:
```bash
sudo snap install gutenprint-printer-app
```

4. Plug in the printer via USB.
   Execute `lsusb` and verify that the printer is among the devices.

5. Open the CUPS web page at `localhost:631`.
   Go to the administrative page, add a new printer. You will be prompted for the username and password you created earlier.
   Select the printer and choose the driver from the list.
   Add printer and finish the process.

## Additional Configuration

### Nginx HTTPS Setup

1. Install Python3 Certbot:
```bash
sudo apt install certbot python3-certbot-nginx
```

2. Create a certificate for your site:
```bash
sudo certbot --nginx -d your_domain.com
```
Follow the prompts. It's recommended to choose the option to redirect HTTP traffic to HTTPS.

3. Update Nginx config `/etc/nginx/sites-available/printerapp` with the following configuration:

```nginx
server {
    listen 80;
    server_name your_domain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name your_domain.com;

    ssl_certificate /etc/letsencrypt/live/your_domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your_domain.com/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_prefer_server_ciphers on;
    ssl_ciphers EECDH+AESGCM:EDH+AESGCM;

    # HSTS (ngx_http_headers_module is required) (63072000 seconds = 2 years)
    add_header Strict-Transport-Security "max-age=63072000; includeSubDomains; preload";

    location /admin/monitoring/ {
        proxy_pass http://localhost:3000/;
        proxy_set_header Host $http_host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /admin/printer/ {
        proxy_pass http://localhost:631/;
        proxy_set_header Host $http_host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

**Note:** If you desire to access Grafana via the Nginx config above (that is `server_ip/admin/monitoring` rather than `server_ip:3000`), you would need to edit the `grafana.ini` file to allow for proxy pass:

```bash
sudo nano /etc/grafana/grafana.ini
```

Find and update with this content:

```ini
[server]
domain = your_domain.com
root_url = https://%(domain)s/admin/monitoring/
serve_from_sub_path = true
```

Restart the Grafana server:
```bash
sudo systemctl restart grafana-server
```

## Further Security Measures

### Firewall Configuration with UFW

1. Ensure UFW is installed:
```bash
sudo apt-get install ufw
```

2. Set new firewall rules:
```bash
sudo ufw allow 'Nginx Full'
sudo ufw delete allow 'Nginx HTTP'
sudo ufw enable
```

### Automatic SSL Certificate Renewal

Set up automatic regeneration of SSL certificates:
```bash
sudo certbot renew --dry-run
```
