from flask import Flask, jsonify
from app import app
from user.models import User

@app.route("/register", methods=["GET"])
def sign():
    return User().sign()