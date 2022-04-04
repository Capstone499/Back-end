from flask import Flask, render_template
from flask_pymongo import PyMongo 
from flask_cors import CORS
from pymongo import MongoClient
import acc
from models import User


app=Flask(__name__,template_folder='templates')
CORS(app)


@app.route("/", methods=["GET", "POST"])
def signup():
    return render_template("signup.html")

#@app.route('/login', methods=["GET"])
#def login():
#    return render_template('login.html')


