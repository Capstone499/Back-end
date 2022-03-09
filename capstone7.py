#######stuff 
#https://www.geeksforgeeks.org/how-to-fetch-data-from-mongodb-using-python/
#https://pymongo.readthedocs.io/en/stable/tutorial.html
#https://www.geeksforgeeks.org/mongodb-python-insert-update-data/ 
#https://pymongo.readthedocs.io/en/stable/tutorial.html 
#https://pymongo.readthedocs.io/en/stable/api/pymongo/database.html
#https://pymongo.readthedocs.io/en/stable/api/pymongo/encryption.html

from pymongo import MongoClient
import pymongo

# establish connection with port and host
#conn = MongoClient('localhost', 27017)
#create database
#db = conn.database
collection = db.test_collection

cluster = "mongodb+srv://Capstone7:team7@cluster0.n2jcc.mongodb.net/myFirstDatabase?retryWrites=true&w=majority"
client = MongoClient(cluster)
print(client.list_database_names())
db = client.test
print(db.list_collection_names())


#for users
users = {"name": "a", "password": "b", "other": "stuff"}
people = db.people
users2 = people.insert_one(users)

#for location
location = {"longitude": "a", "latitude": "b", "ip": "address", "time": "zone"}
locations = db.locations
location2 = locations.insert_one(location)

