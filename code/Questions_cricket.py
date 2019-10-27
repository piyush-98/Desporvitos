
# coding: utf-8

# In[50]:


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

columns=["Team","Bowler","Batsmen","Over","Ball","Team_win","Team_run","Team_wickets","Boundaries","Sixes","Valid Before"]
todays_date = datetime.datetime.now().date()
index = pd.date_range(todays_date-datetime.timedelta(10), periods=100, freq='D')
df=pd.DataFrame(columns=columns,index=index)

from tkinter import *
root=Tk()
def retrieve_input():
    s=textBox.get("1.0","end-1c")
    import spacy
    name=[]
    ent=[]
    nlp = en_core_web_sm.load()
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
    c=1 ## index for dataframe
    if len(ent):
        for i in range(len(ent)):
            if ent[i]=='GPE':
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
                    df.iloc[c]["Bowler"]=name[i]
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
textBox=Text(root, height=20, width=80)
textBox.pack()
buttonCommit=Button(root, height=1, width=10, text="Post Question", 
                    command=lambda: retrieve_input())
#command=lambda: retrieve_input() >>> just means do this when i press the button
buttonCommit.pack()
mainloop()

from tkinter import *
from pandastable import Table, TableModel

class TestApp(Frame):
    """Basic test frame for the table"""
    def __init__(self, parent=None):
        self.parent = parent
        Frame.__init__(self)
        self.main = self.master
        self.main.geometry('600x400+200+100')
        self.main.title('Table app')
        f = Frame(self.main)
        f.pack(fill=BOTH,expand=1)
        #df = TableModel.getSampleData()
        self.table = pt = Table(f, dataframe=df,
                                showtoolbar=True, showstatusbar=True)
        pt.show()
        return

app = TestApp()
#launch the app
app.mainloop()

