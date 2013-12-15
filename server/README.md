Academia - Server
=================

Requirements
------------

### Clone repository

    git clone https://github.com/zosiakropka/academia.git

### Python packages

- python-django
- python-gevent
- python-cherrypy

### Academia server settings

Adjust settings in academia/server/settings.py

Especially `DATABASES`, `SECRET_KEY` and `LOGS_PATH` vars should concern you.

**Database** You need to provide configuration of the academia database you
created / are willing to create.

**Secret key** Unique key for a specific app installation for cryptographic
signing.

**Logs** By default Academia development server will try to store logs in
`academia/logs` directory, which doesn't exist. You should either create that
directory, or provide more adequate (existing) path.

### Database

Django-supported database server needs to be installed.
Next proper database (with settings consistent with those listed in settings.py
file) needs to be created, eg.:

```mysql
CREATE USER 'academia'@'localhost' IDENTIFIED BY 'password';
CREATE DATABASE academia DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
GRANT ALL ON academia.* TO 'academia'@'localhost';
```

To create required tables in database:

- navigate to academia/server directory:

```bash
cd academia/server
```

- run:

```bash
python manage.py syncdb
```

- if you need example data, run:


```bash
python manage.py exampledb
```

Launch app
----------

Currently only launching Django-build-in development server is described. In
the future (as project evolves enough for production use) docs on running
proper production Apache WSGI based server ought to appear.

### Development server

#### Web browser interface (main Django app) and WebSocket subserver

- navigate to academia/server directory:

```bash
cd academia/server
```

- add current dir to `$PYTHONPATH`:

```bash
export PYTHONPATH=$PYTHONPATH:"./"
```

- run:

```bash
python manage.py runserver [<host>:<port_number>] &
python manage.py padwsserver [<host>:<port_number>]
```

#### Subserver for Android client TCP connection

```bash
python manage.py padtcpserver
```

### Production server

Docs on production server shall be written down as soon as Academia is capable of being used in production.

Only web browser interface shall be ran the other way. TCP and WebSocket servers for realtime collaboration are supposed to be launched the way these where launched under development phase:

```bash
python manage.py padwsserver [<host>:<port_number>]
python manage.py padtcpserver [<host>:<port_number>]
```
