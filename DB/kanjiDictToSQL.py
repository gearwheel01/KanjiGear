# -*- coding: utf-8 -*-
"""
transforms kanjidic xml into sql inserts
"""

import xml.etree.ElementTree as ET

tree = ET.parse('kanjidic2.xml')
root = tree.getroot()

sqlFileKanji = open("SQLDataKanji.sql","w",encoding='utf-8')
sqlFileKanjiMeaning = open("SQLDataKanjiMeaning.sql","w",encoding='utf-8')
sqlFileKanjiReading = open("SQLDataKanjiReading.sql","w",encoding='utf-8')

KMID = 1
KRID = 1

sqlFileKanji.write("DELETE FROM kanji; \n")
sqlFileKanjiMeaning.write("DELETE FROM kanjimeaning; \n")
sqlFileKanjiReading.write("DELETE FROM kanjireading; \n")

sqlFileKanji.write("INSERT INTO kanji VALUES \n")
sqlFileKanjiMeaning.write("INSERT INTO kanjimeaning VALUES \n")
sqlFileKanjiReading.write("INSERT INTO kanjireading VALUES \n")

firstKommaKanji = False
firstKommaKanjiMeaning = False
firstKommaKanjiReading = False

addedKanji = ""

for entry in root.findall('character'):
    kanji = entry.find('literal').text
    if kanji in addedKanji:
        print("kanji already added")
    else:
        addedKanji += kanji
        print(kanji)
        
        misc = entry.find("misc")
        
        grade = "NULL"
        gradeObj = misc.find('grade')
        if gradeObj != None:
            grade = gradeObj.text
        
        jlpt = "NULL"
        jlptObj = misc.find('jlpt')
        if jlptObj != None:
            jlpt = jlptObj.text
        
        
        freq = "NULL"
        freqObj = misc.find('freq')
        if freqObj != None:
            freq = freqObj.text
            
        if firstKommaKanji:
            sqlFileKanji.write(",")
        else:
            firstKommaKanji = True
        
        sqlKanji = "('" + kanji + "', " + str(grade) + ", " + str(jlpt) + ", " + str(freq) + ", 0)"
        print(sqlKanji)
        sqlFileKanji.write(sqlKanji + "\n")
        
        readingmeaning = entry.find('reading_meaning')
        if readingmeaning != None:
            rm = readingmeaning.find('rmgroup')
            
            for reading in rm.findall('reading'):
                r = reading.attrib['r_type']
                if r == 'ja_kun' or r == 'ja_on':
                    read = "'KUN'"
                    if r == "ja_on":
                        read = "'ON'"
                    
                    
                    if firstKommaKanjiReading:
                        sqlFileKanjiReading.write(",")
                    else:
                        firstKommaKanjiReading = True
                    
                    sqlReading = "(" + str(KRID) + ", " + read + ", '" + reading.text + "', '" + kanji + "')"
                    KRID += 1
                    sqlFileKanjiReading.write(sqlReading + "\n")
                    print(sqlReading)
                    
            for meaning in rm.findall('meaning'):
                if 'm_lang' not in meaning.attrib.keys():  
                    m = meaning.text
                    m = m.replace("\"", "\"\"")
                    sqlMeaning = "(" + str(KMID) + ", 'en', \"" + m + "\", '" + kanji + "')"
                    KMID += 1
                    
                    if firstKommaKanjiMeaning:
                        sqlFileKanjiMeaning.write(",")
                    else:
                        firstKommaKanjiMeaning = True
                    
                    sqlFileKanjiMeaning.write(sqlMeaning + "\n")
                    print(sqlMeaning)
            
            print("")
        
        
    
sqlFileKanji.write(";")
sqlFileKanjiMeaning.write(";")
sqlFileKanjiReading.write(";")

sqlFileKanji.close()
sqlFileKanjiMeaning.close()
sqlFileKanjiReading.close()