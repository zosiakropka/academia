Academia - Server
=================

Requirements
------------

python-gevent
python-cherrypy

Database
--------

Django-supported database server needs to be installed.
Next proper database (with settings consistent with those listed in settings.py file)
needs to be created, eg.:

CREATE USER 'academia'@'localhost' IDENTIFIED BY 'password';
CREATE DATABASE academia DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
GRANT ALL ON academia.* TO 'academia'@'localhost';
