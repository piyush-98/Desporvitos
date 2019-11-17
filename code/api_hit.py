# import pandas as pd
# data={
#     "batting": {
#         "batsman": [
#             {
#                 "balls": "46",
#                 "fours": "4",
#                 "name": "Mehidy Hasan",
#                 "runs": "31",
#                 "six": "1"
#             },
#             {
#                 "balls": "109",
#                 "fours": "6",
#                 "name": "Rahim",
#                 "runs": "52",
#                 "six": "0"
#             }
#         ],
#         "score": [
#             {
#                 "declare": "null",
#                 "inning_num": "1",
#                 "overs": "58.3",
#                 "runs": "150",
#                 "wickets": "10"
#             },
#             {
#                 "declare": "null",
#                 "inning_num": "3",
#                 "overs": "51.5",
#                 "runs": "183",
#                 "wickets": "6"
#             }
#         ],
#         "team": "Bangladesh"
#     },
#     "bowling": {
#         "bowler": [
#             {
#                 "maidens": "4",
#                 "name": "Shami",
#                 "overs": "9.5",
#                 "runs": "19",
#                 "wickets": "3"
#             }
#         ],
#         "score": [
#             {
#                 "declare": "true",
#                 "inning_num": "2",
#                 "overs": "114",
#                 "runs": "493",
#                 "wickets": "6"
#             }
#         ],
#         "team": "India"
#     }
# }
df = pd.read_pickle("data.pickle")
ls=(df.iloc[1].isnull())
def valid(id):
    data=scorecard()
    s=(data["Innings"][0]['next_batsman'])## nextbatsman
    s=s.split(",")
    if id in s:
        valid=True
        ind=s.index(id)
        return ind
    else:
        return False
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
            return False ## INVALID
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
            return False ## INVALID
        else:
            Batsmen_bound(id,max_ovr)## not playing but not finished     
