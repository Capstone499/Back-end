from flask import Flask, render_template
from flask_pymongo import PyMongo
from pymongo import MongoClient

app = Flask(__name__)
app.config["MONGO_URI"] = "mongodb://localhost: ..."
app.config['MONGO_DBNAME'] = 'SomeCollection'
app.config['SECRET_KEY'] = 'secret_key'

mongo = PyMongo(app)
db = mongo.db
col = mongo.db["Some Collection"]
print("MongoDB Database:", mongo.db)

@app.route("/")
def landing():
    return render_template("landing.html")

@app.route("/login")
def login():
    return render_template("login.html")


if __name__ == '__main__':
    app.run(debug=True)



