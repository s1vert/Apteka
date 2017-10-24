# Apteka

## Adresy

* localhost 		-> apteka
* localhost/admin 	-> panel admina (**login**: *admin123* | **hasło**: *admin123*)
* localhost:8888 	-> phpadmin




## Komendy

### Instalacja / start serwera 

```bash
cd <REPO_FOLDER>
docker-compose up -d
```

### Zatrzymanie serwera

```bash
docker-compose stop
```

### Zatrzymanie serwera i usunięcie kontenerów 
```bash
docker-compose down
```

### Lista aktywnych kontenerów
```bash
docker-compose ps
```

### Dump bazy
```bash
bash dump_database.sh
```

### Import bazy
```bash
bash import_database.sh
```





