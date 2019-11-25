import requests
import json
def scorecard(mid):
        url="http://mapps.cricbuzz.com/cbzios/match/{}/scorecard.json".format(mid)
        res=(requests.get(url))
        data=(json.loads(res.text))
        return(data)
data=scorecard("24117")
print(data['state'])
