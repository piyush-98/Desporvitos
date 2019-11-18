import pyrebase
config = {
  "apiKey": "apiKey",
  "authDomain":"geographicindicationspl.firebaseapp.com" ,
  "databaseURL": "https://geographicindicationspl.firebaseio.com/",
  "storageBucket":"geographicindicationspl.appspot.com/",
  #"serviceAccount": "path/to/serviceAccountCredentials.json"
}

firebase = pyrebase.initialize_app(config)
#storage.child("example.jpeg").put("thumbDiv.jpeg")
db = firebase.database()
#db.child("users").set({1:"example.jpeg"})
users = db.child("Questions/CRICKET").get()
dlink=users.val()
key=[]
for i in dlink:
    key.append(i)
q_id=key[-1]
question=(dlink[q_id]['content'])
print(question)
