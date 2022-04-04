from flask import Flask, jsonify, request
import uuid
import acc as db

class User:
    def signing(self):
        user = {
            "_id": uuid.uuid4().hex,
            "username": request.form.get('username'),
            "password": request.form.get('password')
        }
        db.userpass.insert_one(
                {"_id": user["_id"],
                 "username": user["username"],
                 "password": user["password"]
                 }
            )
        db.userinfo.insert_one(
                 {"_id": user["_id"],
                 "username": user["username"],
                 "password": user["password"]
                 }
            )
        return jsonify(user), 200