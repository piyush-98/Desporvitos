import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import threading
import pyrebase
def main(event):
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
    q_id=key
    print(q_id)
    question=(dlink[q_id]['content'])
    match_id=(dlink[q_id]['matchid'])
    # df=a.Q_analsys(question,q_id)
    # result=a.decision(match_id,q_id,db,dlink)
    # print(result)
    # if result==-1:
    #     u="Questions/CRICKET/".format(q_id)
    #     print(u)
    #     db.child(u).child(q_id).update({"reviewed":"-1"})
    # else:
    #     u="Questions/CRICKET/".format(q_id)
    #     print(u)
    #     db.child(u).child(q_id).update({"reviewed":"2"})
    #     url="https://enigmatic-hamlet-61462.herokuapp.com/submitSolution/{}/{}".format(int(q_id),int(result))
    #     res=requests.get(url)
    #     hash_i=(res.text)
    #     print(hash_i)
    #     return 0


config = {
                  "apiKey": "apiKey",
                  "authDomain":"geographicindicationspl.firebaseapp.com" ,
                  "databaseURL": "https://geographicindicationspl.firebaseio.com/",
                  "storageBucket":"geographicindicationspl.appspot.com/",
                  #"serviceAccount": "path/to/serviceAccountCredentials.json"
                  }
cred = credentials.Certificate("C:/Users/PIYUSH/Desktop/Desportivos/code/geographicindicationspl-firebase-adminsdk-fs115-869a219ea1.json")
firebase_admin.initialize_app(cred,options=config)
firebase_admin.db.reference('Questions/CRICKET').listen(main)
