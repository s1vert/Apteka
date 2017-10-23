docker exec db /usr/bin/mysqldump -u magento --extended-insert=FALSE --password=magento magento > database/magento.sql
