from pycricbuzz import Cricbuzz
import json
import requests
url="http://mapps.cricbuzz.com/cbzios/match/22977/scorecard.json"
res=(requests.get(url))
data=(json.loads(res.text))
print(data["Innings"][0]['batsmen'])
