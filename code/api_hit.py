from pycricbuzz import Cricbuzz
import json
import requests
import pyrebase

def scorecard(mid):
    url="http://mapps.cricbuzz.com/cbzios/match/{}/scorecard.json".format(mid)
    res=(requests.get(url))
    data=(json.loads(res.text))
    return(data)
def id_gen(name):###
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
def valid_bat(id):
    data=scorecard()
    s=(data["Innings"][0]['next_batsman'])## nextbatsman
    s=s.split(",")
    if id in s:
        valid=True
        ind=s.index(id)
        return ind
    else:
        return -1
def valid_Team_over(over):
    data=scorecard()
    if float(data["Innings"][0]['ovr'])>float(over):
        return -1

def valid_bat_over(id,over):
    data=scorecard()
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

def Batsmen_runs(id,max_ovr): ##batsman id
    data=scorecard()
    for i in (data["Innings"][0]['batsmen']):
        if i['id']==id:
            #batsmen is playing or have been dismissed
            if i['out_desc']!="not out":## batsmen dismissed
                return(i['r'])
            elif data["Innings"][0]['ovr']==max_ovr:
                return(i['r'])

            else:
                Batsmen_runs(id)
        elif data["Innings"][0]['ovr']==max_ovr: #not playing but finished
            return -1 ## INVALID
        else:
            Batsmen_runs(id,max_ovr)## not playing but not finished

def Batsmen_bound(id,max_ovr): ##batsman id
    data=scorecard()
    for i in (data["Innings"][0]['batsmen']):
        if i['id']==id:
            #batsmen is playing or have been dismissed
            if i['out_desc']!="not out":## batsmen dismissed
                return(i['4s'])
            elif data["Innings"][0]['ovr']==max_ovr:
                return(i['4s'])

            else:
                Batsmen_bound(id)
        elif data["Innings"][0]['ovr']==max_ovr: #not playing but finished
            return -1 ## INVALID
        else:
            Batsmen_bound(id,max_ovr)## not playing but not finished
def Batsmen_six(id,max_ovr): ##batsman id
    data=scorecard()
    for i in (data["Innings"][0]['batsmen']):
        if i['id']==id:
            #batsmen is playing or have been dismissed
            if i['out_desc']!="not out":## batsmen dismissed
                return(i['6s'])
            elif data["Innings"][0]['ovr']==max_ovr:
                return(i['6s'])

            else:
                Batsmen_six(id)
        elif data["Innings"][0]['ovr']==max_ovr: #not playing but finished
            return -1 ## INVALID
        else:
            Batsmen_six(id,max_ovr)## not playing but not finished
def bowlers_wickets(id,max_ovr):
    data=scorecard()
    if data["Innings"][0]['ovr']==max_ovr:
        for i in (data["Innings"][0]['bowlers']):
            if i['id']==id:
                return(i['w'])
def bowlers_maidens(id,max_ovr):
    data=scorecard()
    if data["Innings"][0]['ovr']==max_ovr:
        for i in (data["Innings"][0]['bowlers']):
            if i['id']==id:
                return(i['m'])
def bowlers_nb(id,max_ovr):
    data=scorecard()
    if data["Innings"][0]['ovr']==max_ovr:
        for i in (data["Innings"][0]['bowlers']):
            if i['id']==id:
                return(i['n'])
def bowlers_wide(id,max_ovr):
    data=scorecard()
    if data["Innings"][0]['ovr']==max_ovr:
        for i in (data["Innings"][0]['bowlers']):
            if i['id']==id:
                return(i['n'])
def batsman_over(id,over,runs_prev):
    data=scorecard()
    if float(data["Innings"][0]['ovr'])==float(over)-1:
        for i in (data["Innings"][0]['batsmen']):
            if i['id']==id:
                if i['out_desc']!="not out":
                    return -1
                else:
                    runs_prev=i['r']
                    batsman_over(id,over,runs_prev)

    elif (data["Innings"][0]['ovr'])==(over):
        for i in (data["Innings"][0]['batsmen']):
            if i['id']==id:
                runs_cur=i['r']
                return (runs_cur-runs_prev)
    else:
        batsman_over(id,over,runs_prev)
def Team_run(over,runs_prev):
        data=scorecard()
        if float(data["Innings"][0]['ovr'])==float(over)-1:
            runs_prev=data["Innings"][0]['batsmen']
            Team_run(over,runs_prev)
        elif (data["Innings"][0]['ovr'])==(over):
            runs_cur=data["Innings"][0]['batsmen']
            return (runs_cur-runs_prev)
        else:
            Team_run(over,runs_prev)
