Academia - Server
=================

Requirements
------------

### Cloned repository

    git clone https://github.com/zosiakropka/academia.git

### Dependencies

- python-django
- python-gevent
- python-cherrypy
- diff_match_patch port:

	https://github.com/JoshData/diff_match_patch-python

- python-m2crypto
- google-api-python-client
- python-gflags

#### Production

- libapache2-mod-wsgi

### Academia server settings

Create `academia/server/academia/config.py` file. The easies way is to copy
example config file (`academia/server/academia/config.py.example`) and adjust
approperiate settings.

You should be especially concerned about `DATABASES`, `SECRET_KEY` and
`LOGS_PATH` settings:

**Database** Configuration of the academia database you created / are willing
to create.

**Secret key** Unique key for a specific app installation for cryptographic
signing.

**Logs** By default Academia development server will try to store logs in
`academia/logs` directory. You may want to create logs directory under some
more adequate path. Remember to ensure that user on which Academia is running
has the right to write to that directory.

### Database

Django-supported database server engine needs to be installed. Then database
(with settings consistent with those listed in config.py file) needs to be
manually created, eg.:

```mysql
CREATE USER 'academia'@'localhost' IDENTIFIED BY 'password';
CREATE DATABASE academia DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
GRANT ALL ON academia.* TO 'academia'@'localhost';
```

To create required tables in database:

- navigate to `academia/server` directory and execute `syncdb` command:

```bash
cd academia/server
python manage.py syncdb
```

- if you need some example data (eg. for system demo), issue following:

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

- run:

```bash
python manage.py runserver [<host>:<port_number>] &
python manage.py padtcpserver &
python manage.py padwsproxy &
```

### Production server

Docs on production server shall be written down as soon as Academia is capable
of being used in production.

Only academia server providing API and WebApp shall be ran the other way. TCP
pad server and its WebSocket-based proxy for realtime collaboration are
supposed to be launched the way these where launched under development phase:

```bash
python manage.py padtcpserver &
python manage.py padwsproxy &
```
