import requests
import json
def scorecard(mid):
    url="http://mapps.cricbuzz.com/cbzios/match/{}/scorecard.json".format(mid)
    res=(requests.get(url))
    data=(json.loads(res.text))
    return(data)
def match_info(mid):
    url="http://mapps.cricbuzz.com/cbzios/match/{}".format(mid)
    res=(requests.get(url))
    data=(json.loads(res.text))
    return(data)
def id_gen(p_name,mid):
    data=match_info(mid)
    player_ids=(data["team1"]['squad'])
    player_ids+=(data["team2"]['squad'])
    waste=[]
    waste=data["team1"]['squad_bench']
    waste+=(data["team2"]['squad_bench'])
    names=[]
    c=-1
    p_id=0
    for i in data['players']:
        if int(i['id']) in waste:
            continue
        else:
            names.append((i['f_name'],i['name']))
    for i in names:
        c+=1
        for j in i:
            if p_name==j:
                p_id=player_ids[c]
                return str(p_id)
    if p_id==0:
        return -1

print((id_gen("Mushfiqur Rahim","22750")))
data=scorecard("22750")
if(data["Innings"][0]['batsmen'][7]['out_desc'])!="batting":
    print("asdfbgvfewsfghfdsa")
