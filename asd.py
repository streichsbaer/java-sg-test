from flask import request
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import text
from database.users import User


@app.route("hello")
def hello():
    id = request.args.get("id")
    stmt = text(
        "SELECT * FROM users where id=%s" % id
    )  # Query is constructed based on user inputs
    query = SQLAlchemy().session.query(User).from_statement(stmt)  # Noncompliant
    user = query.one()
    return "Hello %s" % user.username


from django.http import HttpResponse
from django.db import connection


def hello(request):
    id = request.GET.get("id", "")
    cursor = connection.cursor()
    cursor.execute(
        "SELECT username FROM auth_user WHERE id=%s" % id
    )  # Noncompliant; Query is constructed based on user inputs
    row = cursor.fetchone()
    return HttpResponse("Hello %s" % row[0])


import pickle
import yaml


@app.route("/pickle")
def pickle_loads():
    file = request.files["pickle"]
    pickle.load(
        file
    )  # Noncompliant; Never use pickle module to deserialize user inputs


@app.route("/yaml")
def yaml_load():
    data = request.GET.get("data")
    yaml.load(
        data, Loader=yaml.Loader
    )  # Noncompliant; Avoid using yaml.load with unsafe yaml.Loader
