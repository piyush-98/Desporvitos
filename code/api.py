# from nltk.tag import StanfordNERTagger
# from nltk.tokenize import word_tokenize
# st = StanfordNERTagger('C:/Users/PIYUSH/Downloads/stanford-ner-2018-10-16/classifiers/english.all.3class.distsim.crf.ser.gz',
# 					   'C:/Users/PIYUSH/Downloads/stanford-ner-2018-10-16/stanford-ner.jar',
# 					   encoding='utf-8')
# text ='How many runs will Northern Warriors score in 20th overe'
# tokenized_text = word_tokenize(text)
# classified_text = st.tag(tokenized_text)
# print(classified_text)
# stan_ent=[]
# for i in classified_text:
# 	if i[1]!='O':
# 		stan_ent.append(i[1])
# print(stan_ent)
result='20.0'
print(int(result[0:len(result)-2]))
