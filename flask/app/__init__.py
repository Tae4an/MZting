from flask import Flask
import firebase_admin
from firebase_admin import credentials, storage
import os

def file_exists(filepath):
    return os.path.isfile(filepath)

app = Flask(__name__)

file_path = "mzting-firebase-adminsdk-l1rjm-57eb0e0c97.json"

# # Firebase 초기화
cred = credentials.Certificate(file_path)
firebase_admin.initialize_app(cred, {
    'storageBucket': 'mzting.appspot.com'
})
bucket = storage.bucket()

from . import routes