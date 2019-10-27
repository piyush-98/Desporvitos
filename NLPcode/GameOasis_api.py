import requests
import json
print("Enter the option number",end="\n")
print("1 Verification",end="\n")
print("2 Submit Solution")
inp=int(input())
if inp==1:
    print("Enter the Acc_name",end="\n")
    Acc_name=input()
    url="https://enigmatic-hamlet-61462.herokuapp.com/whitelist/{}".format(Acc_name)
    print(url)
    res=requests.get(url)
    hash_i=(res.text)
elif inp==2:
    print("Enter the question id",end="\n")
    q_id=int(input())
    print("Enter the answer id",end="\n")
    a_id=int(input())
    url="https://enigmatic-hamlet-61462.herokuapp.com/submitSolution/{}/{}".format(q_id,a_id)
    res=requests.get(url)
    hash_i=(res.text)
