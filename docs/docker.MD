# Docker postgres connection
1. sudo iptables -A INPUT -i docker0 -j ACCEPT
2. /etc/postgresql/12/main/pg_hba.conf: host   all             all             0.0.0.0/0               md5
3. /etc/postgresql/12/main/postgresql.conf: listen_addresses = '*'
4. sudo service postgresql restart
5. dokcer run: --spring.profiles.active=docker