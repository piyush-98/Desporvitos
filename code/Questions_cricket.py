import pandas as pd
import numpy as np
import json
from nltk import word_tokenize, pos_tag, ne_chunk
from nltk.chunk import conlltags2tree, tree2conlltags
from nltk.tag import StanfordNERTagger
import nltk
from nltk.tokenize import word_tokenize
from nltk.tokenize import sent_tokenize
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
from nltk.stem import PorterStemmer
from collections import Counter
from nltk.corpus import stopwords
stop_words=set(stopwords.words('english'))
lemm = WordNetLemmatizer()
import re
import datetime
import en_core_web_sm
import spacy
from pycricbuzz import Cricbuzz
import json
import requests
import pyrebase
import time


def main():
    while(True):
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
        print(q_id)
        question=(dlink[q_id]['content'])
        match_id=(dlink[q_id]['matchid'])
        df=Q_analsys(question,q_id)
        result=decision(match_id,q_id)
        print(result)
        if result==-1:
            u="Questions/CRICKET/".format(q_id)
            print(u)
            db.child(u).child(q_id).update({"reviewed":"-1"})
        else:
            u="Questions/CRICKET/".format(q_id)
            print(u)
            db.child(u).child(q_id).update({"reviewed":"1"})
        url="https://enigmatic-hamlet-61462.herokuapp.com/submitSolution/{}/{}".format(q_id,str(result))
        res=requests.get(url)
        hash_i=(res.text)
        print(hash_i)
        time.sleep(5)

def Data_make():
    columns=['q_id', 'Team','Over', 'Bowler', 'Batsmen', 'Bow_wickets',
           'Maiden', 'Wide', 'Noball', 'Team_win', 'Team_run', 'Team_wickets',
           'Boundaries', 'Sixes', 'Valid Before']
    todays_date = datetime.datetime.now().date()
    index = pd.date_range(todays_date-datetime.timedelta(10), periods=1, freq='D')
    df=pd.DataFrame(columns=columns,index=index)
    df.to_pickle("data.pickle")
def Data_app():
    df=pd.read_pickle("data.pickle")
    return df
def Q_analsys(s,q_id):
    Data_make()
    name=[]
    ent=[]
    nlp = en_core_web_sm.load()
    df=Data_app()
    df.iloc[0]['q_id']=q_id
    doc=nlp(s)
    for X in doc.ents:
        name.append(X.text)
        ent.append(X.label_)
    s=pos_tag(word_tokenize(s))
    ls=[]
    ls2=[]
    for i in s:
        if i[1][0]=="W" or i[1][0]=="J" or i[1][0]=="D" or i[1][0]=="W" or i[1][0]=="M" or i[1][0]=="R":
            continue
        else:
            ls.append(i)
            ls2.append(i[0].lower())
    c=0## index for dataframe
    if len(ent):
        for i in range(len(ent)):
            print(ent[i])
            if ent[i]=='GPE' or ent[i]=="ORG":
                df.iloc[c]["Team"]=name[i]
                if "win" in ls2:
                    df.iloc[c]["Team_win"]=1
                    df.iloc[c]["Valid Before"]="first Innings"
                elif "wickets" in ls2 or "wicket" in ls2:
                    df.iloc[c]["Team_wickets"]=1
                    df.iloc[c]["Valid Before"]=20
                elif "runs" in ls2 or "run" in ls2:
                    df.iloc[c]["Team_run"]=1
                    df.iloc[c]["Valid Before"]=20
                elif "boundaries" in ls2:
                        df.iloc[c]["Boundaries"]=1
                        df.iloc[c]["Valid Before"]=20
                elif "sixes" in ls2:
                        df.iloc[c]["Sixes"]=1
                        df.iloc[c]["Valid Before"]=20
            elif ent[i]=='ORDINAL'or ent[i]=='CARDINAL' or ent[i]=='DATE':
                if "over" in ls2 or "overs" in ls2:
                    if "." in name[i]:
                        s=name[i].split(".")
                        df.iloc[c]["Over"]=int(s[0])
                        df.iloc[c]["Ball"]=int(s[1])
                    df.iloc[c]["Over"]=int(name[i][0:-2])
            else:
                if "wickets" in ls2 or "wicket" in ls2:
                    print(ent[i])
                    df.iloc[c]["Bowler"]=name[i]
                    df.iloc[c]["Valid Before"]=5
                    df.iloc[c]["Bow_wickets"]=1
                elif "maidens" in ls2 or "maiden" in ls2:
                    df.iloc[c]["Bowler"]=name[i]
                    df.iloc[c]["Maiden"]=1
                    df.iloc[c]["Valid Before"]=5
                elif "noball" in ls2 or "noballs" in ls2:
                    df.iloc[c]["Bowler"]=name[i]
                    df.iloc[c]["Noball"]=1
                    df.iloc[c]["Valid Before"]=5
                elif "wides" in ls2 or "wide" in ls2:
                    df.iloc[c]["Bowler"]=name[i]
                    df.iloc[c]["Wide"]=1
                    df.iloc[c]["Valid Before"]=5

                else:
                    df.iloc[c]["Batsmen"]=name[i]
                    df.iloc[c]["Valid Before"]=20
                    if "boundaries" in ls2:
                        df.iloc[c]["Boundaries"]=1
                        df.iloc[c]["Valid Before"]=20
                    elif "sixes" in ls2:
                        df.iloc[c]["Sixes"]=1
                        df.iloc[c]["Valid Before"]=20


    elif "wickets" in ls2 or "wicket" in ls2:
            df.iloc[c]["Team"]="Both"
            df.iloc[c]["Team_wickets"]=1
            df.iloc[c]["Valid Before"]=20
    elif "win" in ls2:
            df.iloc[c]["Team"]="Both"
            df.iloc[c]["Team_win"]=1
            df.iloc[c]["Valid Before"]=20
    elif "runs" in ls2 or "run" in ls2:
            df.iloc[c]["Team"]="Both"
            df.iloc[c]["Team_run"]=1
            df.iloc[c]["Valid Before"]=20
    elif "boundaries" in ls2:
            df.iloc[c]["Boundaries"]=1
            df.iloc[c]["Team"]="Both"
            df.iloc[c]["Valid Before"]=20
    elif "sixes" in ls2:
            df.iloc[c]["Sixes"]=1
            df.iloc[c]["Team"]="Both"
            df.iloc[c]["Valid Before"]=20
    else:
        print("INVALID QUESTION")
    df.to_pickle("data.pickle")
    print(df)
    return df


def scorecard(mid):
    url="http://mapps.cricbuzz.com/cbzios/match/{}/scorecard.json".format(mid)
    res=(requests.get(url))
    data=(json.loads(res.text))
    return(data)
def id_gen(name): ###
    return "1394"
def match_info(mid):
    url="http://mapps.cricbuzz.com/cbzios/match/{}".format(mid)
    res=(requests.get(url))
    data=(json.loads(res.text))
    return(data)
def max_over_gen(mid):
    data=match_info(mid)
    match_type=(data["header"]["type"])
    if match_type=="T20":
        return 20
def valid_bat(id,mid):
    data=scorecard(mid)
    s=(data["Innings"][0]['next_batsman'])## nextbatsman
    s=s.split(",")
    if id in s:
        valid=True
        ind=s.index(id)
        return ind
    else:
        return -1
def valid_Team_over(over,mid):
    data=scorecard(mid)
    if float(data["Innings"][0]['ovr'])>float(over):
        return -1

def valid_bat_over(id,over,mid):
    data=scorecard(mid)
    s=(data["Innings"][0]['next_batsman'])## nextbatsman
    s=s.split(",")
    if float(data["Innings"][0]['ovr'])<float(over):
        if id in s:
            valid=True
            ind=s.index(id)
            return ind
        else:
            return -1
    else:
        return -1

def Batsmen_runs(id,max_ovr,mid): ##batsman id
    data=scorecard(mid)
    s=(data["Innings"][0]['batsmen'])
    s=s.split(",")
    if id in s:
        for i in (data["Innings"][0]['batsmen']):
            if i['id']==id:
                #batsmen is playing or have been dismissed
                if i['out_desc']!="not out":## batsmen dismissed
                    return(i['r'])
                elif data["Innings"][0]['ovr']==max_ovr:
                    return(i['r'])

                else:
                    Batsmen_runs(id,max_ovr,mid)
            else:
                continue
    elif data["Innings"][0]['ovr']==max_ovr: #not playing but finished
        return -1 ## INVALID
    else:
        Batsmen_runs(id,max_ovr,mid)

def Batsmen_bound(id,max_ovr,mid): ##batsman id
    data=scorecard(mid)
    s=(data["Innings"][0]['batsmen'])
    s=s.split(",")
    if id in s:
        for i in (data["Innings"][0]['batsmen']):
            if i['id']==id:
                #batsmen is playing or have been dismissed
                if i['out_desc']!="not out":## batsmen dismissed
                    return(i['4s'])
                elif data["Innings"][0]['ovr']==max_ovr:
                    return(i['4s'])

                else:
                    Batsmen_bound(id,max_ovr,mid)
            else:
                continue
    elif data["Innings"][0]['ovr']==max_ovr: #not playing but finished
                return -1 ## INVALID
    else:
        Batsmen_bound(id,max_ovr)## not playing but not finished
def Batsmen_six(id,max_ovr,mid):
    data=scorecard(mid)
    s=(data["Innings"][0]['batsmen'])
    s=s.split(",")
    if id in s:
            for i in (data["Innings"][0]['batsmen']):
                if i['id']==id:
                    #batsmen is playing or have been dismissed
                    if i['out_desc']!="not out":## batsmen dismissed
                        return(i['6s'])
                    elif data["Innings"][0]['ovr']==max_ovr:
                        return(i['6s'])

                    else:
                        Batsmen_six(id,max_ovr,mid)
                else:
                    continue

    elif float(data["Innings"][0]['ovr'])==float(max_ovr): #not playing but finished
                return -1 ## INVALID
    else:
        Batsmen_six(id,max_ovr,mid)## not playing but not finished
def bowlers_wickets(id,max_ovr,mid):
    data=scorecard(mid)
    if data["Innings"][0]['ovr']==max_ovr:
        for i in (data["Innings"][0]['bowlers']):
            if i['id']==id:
                return(i['w'])
def bowlers_maidens(id,max_ovr,mid):
    data=scorecard(mid)
    if data["Innings"][0]['ovr']==max_ovr:
        for i in (data["Innings"][0]['bowlers']):
            if i['id']==id:
                return(i['m'])
def bowlers_nb(id,max_ovr,mid):
    data=scorecard(mid)
    if data["Innings"][0]['ovr']==max_ovr:
        for i in (data["Innings"][0]['bowlers']):
            if i['id']==id:
                return(i['n'])
def bowlers_wide(id,max_ovr,mid):
    data=scorecard(mid)
    if data["Innings"][0]['ovr']==max_ovr:
        for i in (data["Innings"][0]['bowlers']):
            if i['id']==id:
                return(i['n'])
def batsman_over(id,over,runs_prev,mid):
    data=scorecard(mid)
    if float(data["Innings"][0]['ovr'])==float(over)-1:
        for i in (data["Innings"][0]['batsmen']):
            if i['id']==id:
                if i['out_desc']!="not out":
                    return -1
                else:
                    runs_prev=i['r']
                    batsman_over(id,over,runs_prev,mid)

    elif (data["Innings"][0]['ovr'])==(over):
        for i in (data["Innings"][0]['batsmen']):
            if i['id']==id:
                runs_cur=i['r']
                return (runs_cur-runs_prev)
    else:
        batsman_over(id,over,runs_prev,mid)
def Team_run(over,runs_prev,mid):
    while(True):
        data=scorecard(mid)
        if float(data["Innings"][0]['ovr'])==float(over)-1:
            print("here")
            runs_prev=data["Innings"][0]['score']
            print("runs_prev",runs_prev)
        elif float(data["Innings"][0]['ovr'])==float(over):
            print("here2")
            runs_cur=data["Innings"][0]['score']
            return str(float(runs_cur)-float(runs_prev))
        else:
            time.sleep(20)
        time.sleep(20)


def decision(mid,q_id):
    ls=[]
    index=0
    df=Data_app()
    col=['q_id', 'Team','Over', 'Bowler', 'Batsmen', 'Bow_wickets',
           'Maiden', 'Wide', 'Noball', 'Team_win', 'Team_run', 'Team_wickets',
           'Boundaries', 'Sixes', 'Valid Before']
    response=df.iloc[index].isnull()
    for i in range(len(response)):
        if(response[i]==False):
            ls.append(i)
    print(ls)
    for i in ls:
        if i==1:
            if 2 in ls:
                over=df.iloc[index]["Over"]
                if valid_Team_over(over,mid)==-1:
                    return -1

                runs=Team_run(over,-1,mid)
                return runs
        elif i==2:
            print("here") ##over

            if 4 in ls:
                name=df.iloc[index]["Batsmen"]
                over=df.iloc[index]["Over"]
                id=id_gen(name)
                if valid_bat_over(id,over,mid)==-1:
                    return-1
                else:
                    if 11 in ls:
                        return Batsmen_bound_over(id,max_ovr,mid)
                    elif 12 in ls:
                        return Batsmen_six_over(id,max_ovr,mid)
                    else:
                        return Batsmen_over(id,max_ovr,mid)
        elif i==3:
            name=df.iloc[index]["Bowler"]
            id=id_gen(name)
            max_ovr=max_over_gen(mid)
            if 5 in ls:
                return bowlers_wickets(id,max_ovr,mid)
            elif 6 in ls:
                return bowlers_maidens(id,max_ovr,mid)
            elif 7 in ls:
                return bowlers_wide(id,max_ovr,mid)
            else:
                return bowlers_nb(id,max_ovr,mid)
        elif i==4:
            name=df.iloc[index]["Batsmen"]
            id=id_gen(name)
            max_ovr=max_over_gen(mid)
            if valid_bat(id,mid)==-1:
                return -1
                break
            else:
                if 11 in ls:
                    Batsmen_bound(id,max_ovr,mid)
                elif 12 in ls:
                    Batsmen_six(id,max_ovr,mid)
                else:
                    Batsmen_runs(id,max_ovr,mid)
        else:
            continue
main()
