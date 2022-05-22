from flask import Flask, jsonify, request, session, redirect, url_for
import uuid
from acc import db
import bcrypt



class User:


    def SignUp(self):
        print("Information sent to sign up:\t", request.form)
        user = {
            "_id": uuid.uuid4().hex,
            "username": request.form.get('username', type=str),
            "password": request.form.get('password',  type=str)  
        }
        password = user["password"]                    # set password to the input password
        # hash password before input to database   
        try:
            hashed_password = self.hash_password(password)
            print(hashed_password) 

        if db.user_info.find_one({"username": user["username"]}):
            return jsonify({"error": "Sorry, this username is already in use"}), 400
        else:
            db.user_info.insert_one(
                {"_id": user["_id"],
                 "username": user["username"],
                 "password": hashed_password
                }
            #user    
        )
        
        return jsonify({"Success": "You have been added into our system"}), 200
    
    # function to hash password 
    def hash_password(self, password):
        salt = bcrypt.gensalt()
        hashed = bcrypt.hashpw(password.encode(), salt)
        return hashed 

    def LogIn(self):
        print("Information sent to login:\t",request.form)
        user = {
            "username": request.form.get('user', type=str),
            "password": request.form.get('pass', type=str)
        }
        validate = db.user_info.find_one({"username": user["username"]})
        if(validate == None):
            return jsonify({"error": "Wrong Username"}), 400 
        else:
            IsLoggedIn = True  
        if(validate["password"] == user["password"] and validate["username"] == user["username"]): 
                self.IsLoggedIn = True
                return jsonify({"Success": "You have logged into our system"}), 200
        else:
                return jsonify({"error": "Wrong User or Password"}), 400


        password = user["password"]                               # set password variable to the input pw on login 
        found_password = db.user_info["password"]                 # set variable to hashed pw stored in database
        if bcrypt.checkpw(password.encode(), found_password):     # check hashed input pw with hashed pw stored in database
            self.IsLoggedIn = True
            return jsonify({"Success": "You have logged into our system"}), 200
        else:
            return jsonify({"error": "Wrong User or Password"}), 400