import pymongo
from pymongo import MongoClient
import dns
import os
from dotenv import load_dotenv


load_dotenv()

Mongo_URI = "mongodb+srv://Capstone7:team7@cluster0.n2jcc.mongodb.net/cluster0?retryWrites=true&w=majority"
#os.getenv('dbfc7')
client = MongoClient(Mongo_URI)

# client = pymongo.MongoClient('mongodb+srv://<NAME>:<PASS>@cluster0.bxtpa.mongodb.net/myFirstDatabase?retryWrites=true&w=majority')

db = client.get_database("Capstone")## Access our collection
#collection = db['collection'] ## This is for authentication

print(db.list_collection_names())


