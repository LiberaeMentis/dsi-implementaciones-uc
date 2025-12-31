#!/bin/bash
cd "$(dirname "$0")"
python3 manage.py migrate
python3 manage.py poblar_datos
python3 manage.py runserver 8080 --noreload --nothreading

