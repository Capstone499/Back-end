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
<<<<<<< HEAD
        password = user["password"]                     # set password to the input password
        hashed_password = self.hash_password(password)  # hash password before inserted into database
        print(hashed_password)
=======
>>>>>>> 8b71a7dcf97a818c8764416886c38351e16e4fc3

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
        
<<<<<<< HEAD
        return jsonify({"Success": "You have been added into our system"}), 200
=======
        return redirect(url_for('index'))
>>>>>>> 8b71a7dcf97a818c8764416886c38351e16e4fc3

    def LogIn(self):
        print("Information sent to login:\t",request.form)
        user = {
            "username": request.form.get('user', type=str),
            "password": request.form.get('pass', type=str)
        }
        validate = db.user_info.find_one({"username": user["username"]})
        if(validate == None):
<<<<<<< HEAD
            return jsonify({"error": "Wrong Username or Password"}), 400 
        else:
            IsLoggedIn = True
        
        password = user["password"]                 # set variable to the password input on login
        found_password = db.user_info["password"]   # set variable to hashed password stored in db
        # check hashed input password with hashed password stored in databse
        if((bcrypt.checkpw(password.encode(), found_password)) and validate["username"] == user["username"]):
=======
            return jsonify({"error": "Wrong User"}), 400
        else:
            IsLoggedIn = True
        if(validate["password"] == user["password"] and validate["username"] == user["username"]):
>>>>>>> 8b71a7dcf97a818c8764416886c38351e16e4fc3
                self.IsLoggedIn = True
                return redirect(url_for('success'))
                 
        else:
                return jsonify({"error": "Wrong User or Password"}), 400
<<<<<<< HEAD

            
=======
>>>>>>> 8b71a7dcf97a818c8764416886c38351e16e4fc3
