from flask import Flask, render_template, request, redirect, url_for, session, jsonify
from flask_pymongo import PyMongo 
from flask_cors import CORS
from acc import Mongo_URI
from models import User
#import bcrypt


app=Flask(__name__,template_folder='templates')
CORS(app)
app.config['MONGO_URI'] = Mongo_URI
mongo = PyMongo(app)


@app.route("/", methods=["GET", "POST"])
def index():
    return render_template("index.html")

@app.route("/success", methods = ["GET","POST"])
def success():
    return redirect(url_for("index"))

@app.route("/signup", methods=["GET", "POST"])
def signup():
    if request.method == "POST":
        self = mongo.db.self
        return User.SignUp(self)
    return render_template("signup.html")


@app.route("/login", methods=["GET", "POST"])
def login():
    if request.method == "POST":
        self = mongo.db.self
        return User.LogIn(self)
    return render_template("login.html")

@app.route("/logsuccess", methods=["GET", "POST"])
def logsuccess():
    return 'Log In Success'




