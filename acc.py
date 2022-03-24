import pymongo
import dns
import os
from dotenv import load_dotenv


load_dotenv()

Mongo_URI = os.getenv('dbfc7')
client = pymongo.MongoClient(Mongo_URI)

# client = pymongo.MongoClient('mongodb+srv://<NAME>:<PASS>@cluster0.bxtpa.mongodb.net/myFirstDatabase?retryWrites=true&w=majority')

db = client.get_database("Capstone7")## Access our collection
collection = db['collection'] ## This is for authentication

db = client.test