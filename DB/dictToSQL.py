# -*- coding: utf-8 -*-
"""
reads dict xml and creates sql inserts
"""

import xml.etree.ElementTree as ET

tree = ET.parse('dict.xml')
root = tree.getroot()

sqlFileWord = open("SQLDataWord.sql","w",encoding='utf-8')
sqlFileWordMeaning = open("SQLDataWordMeaning.sql","w",encoding='utf-8')
sqlFileWordWriting = open("SQLDataWordWriting.sql","w",encoding='utf-8')
sqlFileWordReading = open("SQLDataWordReading.sql","w",encoding='utf-8')
sqlFileKanjiInWord = open("SQLDataKanjiInWord.sql","w",encoding='utf-8')

WID = 1
WMID = 1
WWID = 1
WRID = 1

sqlFileWord.write("DELETE FROM word; \n")
sqlFileWordMeaning.write("DELETE FROM wordmeaning; \n")
sqlFileWordWriting.write("DELETE FROM wordwriting; \n")
sqlFileWordReading.write("DELETE FROM wordreading; \n")
sqlFileKanjiInWord.write("DELETE FROM wordwrittenwithkanji; \n")

sqlFileWord.write("INSERT INTO word VALUES \n")
sqlFileWordMeaning.write("INSERT INTO wordmeaning VALUES \n")
sqlFileWordWriting.write("INSERT INTO wordwriting VALUES \n")
sqlFileWordReading.write("INSERT INTO wordreading VALUES \n")
sqlFileKanjiInWord.write("INSERT INTO wordwrittenwithkanji VALUES \n")

firstKommaWord = False
firstKommaWordMeaning = False
firstKommaWordWriting = False
firstKommaWordReading = False
firstKommaKanjiInWord = False

notkanjichars="あいうえおかきくけこがぎぐげごさしすせそざじずぜぞたちつてとだぢづでどなにぬねのはひふへほばびぶべぼぱぴぷぺぽまみむめもやゆよらりるれろわをんっゃょゅぁぃぅぇぉゖゕ"
notkanjichars+="アイウエオカキクケコガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨラリルレロワヲンーャョュァィゥェォヵヶッ"
notkanjichars+="abcdefghaijklmnopqrstuvwxvzöäüABCDEFGHIJKLMNOPQRSTUVWXYZÖÄÜ1234567890<>|-_"

for entry in root.findall('entry'):
    
    frequency = 0
    
    for k_ele in entry.findall('k_ele'):
        for keb in k_ele.findall('keb'):
            if firstKommaWordWriting:
                sqlFileWordWriting.write(",")
            else:
                firstKommaWordWriting = True
            
            sqlWriting = "(" + str(WWID) + ", \"" + keb.text + "\", " + str(WID) + ")"
            sqlFileWordWriting.write(sqlWriting + "\n")
            print(sqlWriting)
            WWID += 1
            
        for ke_pri in k_ele.findall('ke_pri'):
            if 'nf' in ke_pri.text:
                nr = 101 - int(ke_pri.text[2:4])
                frequency += nr
            if ke_pri.text == 'news1':
                frequency += 50
            if ke_pri.text == 'news2':
                frequency += 25
            if ke_pri.text == 'ichi1':
                frequency += 40
            if ke_pri.text == 'ichi2':
                frequency += 20
            if ke_pri.text == 'spec1':
                frequency += 30
            if ke_pri.text == 'spec2':
                frequency += 15
            if ke_pri.text == 'gai1':
                frequency += 20
            if ke_pri.text == 'gai12':
                frequency += 10
                
            
            
    for r_ele in entry.findall('r_ele'):
        for reb in r_ele.findall('reb'):
            if firstKommaWordReading:
                sqlFileWordReading.write(",")
            else:
                firstKommaWordReading = True
                
            sqlReading = "(" + str(WRID) + ", \"" + reb.text + "\", " + str(WID) + ")"
            sqlFileWordReading.write(sqlReading + "\n")
            print(sqlReading)
            WRID += 1
            

    for sense in entry.findall('sense'):
        for gloss in sense.findall('gloss'):
            lang = gloss.attrib["{http://www.w3.org/XML/1998/namespace}lang"]
            if lang == "eng":
                if firstKommaWordMeaning:
                    sqlFileWordMeaning.write(",")
                else:
                    firstKommaWordMeaning = True
                    
                translation = gloss.text
                translation = translation.replace("\"", "\"\"")
                    
                sqlMeaning = "(" + str(WMID) + ", \"en\", \"" + translation + "\", " + str(WID) + ")"
                sqlFileWordMeaning.write(sqlMeaning + "\n")
                print(sqlMeaning)
                WMID += 1
           
    
    if firstKommaWord:
        sqlFileWord.write(",")
    else:
        firstKommaWord = True
        
    sqlWord = "(" + str(WID) + ", 0, " + str(frequency) + ")"
    sqlFileWord.write(sqlWord + "\n")
    print(sqlWord)
    
    WID += 1
    print("")
            
            
sqlFileWord.write(";")
sqlFileWordMeaning.write(";")
sqlFileWordWriting.write(";")
sqlFileWordReading.write(";")

sqlFileWord.close()
sqlFileWordWriting.close()
sqlFileWordReading.close()
sqlFileWordMeaning.close()