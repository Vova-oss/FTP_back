:set fileformat=unix
#!/bin/bash

psql -d onlineShop -U postgres -c "\copy os_type FROM os_type.csv delimiter ';' csv header ENCODING 'utf8'"
psql -d onlineShop -U postgres -c "\copy os_brand FROM os_brand.csv delimiter ';' csv header ENCODING 'utf8'"
psql -d onlineShop -U postgres -c "\copy os_device FROM os_device.csv delimiter ';' csv header ENCODING 'utf8'"
psql -d onlineShop -U postgres -c "\copy os_device_info FROM os_device_info.csv delimiter ';' csv header ENCODING 'utf8'"

