from flask import Flask, jsonify, request, session, redirect, url_for
import uuid
from acc import db



class User:


    def SignUp(self):
        print("Information sent to sign up:\t", request.form)
        user = {
            "_id": uuid.uuid4().hex,
            "username": request.form.get('username', type=str),
            "password": request.form.get('password',  type=str)  
        }

        if db.user_info.find_one({"username": user["username"]}):
            return jsonify({"error": "Sorry, this username is already in use"}), 400
        else:
            db.user_info.insert_one(
                {"_id": user["_id"],
                 "username": user["username"],
                 "password": user["password"]
                }
            #user    
        )
        
        return redirect(url_for('index'))

    def LogIn(self):
        print("Information sent to login:\t",request.form)
        user = {
            "username": request.form.get('user', type=str),
            "password": request.form.get('pass', type=str)
        }
        validate = db.user_info.find_one({"username": user["username"]})
        if(validate == None):
            return jsonify({"error": "Wrong User"}), 400
        else:
            IsLoggedIn = True
        if(validate["password"] == user["password"] and validate["username"] == user["username"]):
                self.IsLoggedIn = True
                return redirect(url_for('landing'))
                 
        else:
                return jsonify({"error": "Wrong User or Password"}), 400
