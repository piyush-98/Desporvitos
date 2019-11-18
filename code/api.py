from pycricbuzz import Cricbuzz
import json
import requests
url="http://mapps.cricbuzz.com/cbzios/match/22977/scorecard.json"
res=(requests.get(url))

def scorecard():
    url="http://mapps.cricbuzz.com/cbzios/match/22977/scorecard.json"
    res=(requests.get(url))
    data=(json.loads(res.text))
    return data
def batsman_over(id,over,runs_prev):
    data=scorecard()
    if float(data["Innings"][0]['ovr'])==float(over)-1:
        print("Ss")
        for i in (data["Innings"][0]['batsmen']):
            if i['id']==id:
                # if i['out_desc']!="not out":
                #     print("ss")
                #     return -1
                #else:
                runs_prev=i['r']
                print(runs_prev)
                batsman_over(id,over,runs_prev)


    elif data["Innings"][0]['ovr']==over:
        for i in (data["Innings"][0]['batsmen']):
            if i['id']==id:
                runs_cur=i['r']
                return (runs_cur-runs_prev)

batsman_over('599','21',-1)
