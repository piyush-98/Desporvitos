import requests
import json
def match_info(mid):
    url="http://mapps.cricbuzz.com/cbzios/match/{}".format(mid)
    res=(requests.get(url))
    data=(json.loads(res.text))
    player_ids=(data["team1"]['squad'])
    player_ids+=(data["team2"]['squad'])
    waste=[]
    waste=data["team1"]['squad_bench']
    waste+=(data["team2"]['squad_bench'])
    names=[]
    c=-1
    for i in data['players']:
        if int(i['id']) in waste:
            continue
        else:
            names.append((i['f_name'],i['name']))
    name='Iftikhar Ahmed'
    for i in names:
        c+=1
        for j in i:
            if name==j:
                print(player_ids[c])
                break
match_info("22579")
