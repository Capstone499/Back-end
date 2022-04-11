from flask import Flask, jsonify, request, session, redirect, url_for
import uuid
from acc import db



class User:


    def SignUp(self):
        print("Information sent to sign up:\t", request.form)
        user = {
            "_id": uuid.uuid4().hex,
            "username": request.form.get('username'),
            "password": request.form.get('password')  
        }

        #if db.user_info.find_one({"username": user["username"]}):
        #    return jsonify({"error": "Sorry, this username is already in use"}), 400
        #else:
        db.user_info.insert_one(
                {"_id": user["_id"],
                 "username": user["username"],
                 "password": user["password"]
                }
            #user    
        )
        
        return jsonify({"Success": "You have been added into our system"}), 200

    def LogIn(self):
        print("Information sent to login:\t",request.form)
        user = {
            "username": request.form.get('user'),
            "password": request.form.get('pass')
        }
        validate = db.user_info.find_one({"username": user["username"]})
        if(validate == None):
            print("Username not found!\n")
            return jsonify({"error": "Wrong Username or Password"}), 400 
        else:
            print("FOUND IT\n")
            IsLoggedIn = True
        if(validate["password"] == user["password"] and validate["username"] == user["username"]):
                self.IsLoggedIn = True
                return jsonify({"Success": "You have logged into our system"}), 200
        else:
                return jsonify({"error": "Wrong User or Password"}), 400