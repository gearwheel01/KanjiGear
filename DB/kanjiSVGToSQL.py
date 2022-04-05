# -*- coding: utf-8 -*-
"""
reads all files in directory and writes their data into sql inserts
"""

from os import listdir
from os.path import isfile, join
from xml.dom import minidom

path = "kanji"
noreadfiles = ['Kaisho','Fst','Lst','Hyougai','Insatsu']
addedKanjis = ""

sqlFileStroke = open("SQLDataStroke.sql","w",encoding='utf-8')
sqlFileBezier = open("SQLDataBezier.sql","w",encoding='utf-8')

SID = 1
BID = 1

sqlFileStroke.write("DELETE FROM stroke; \n")
sqlFileBezier.write("DELETE FROM bezier; \n")

sqlFileStroke.write("INSERT INTO stroke VALUES ")
sqlFileBezier.write("INSERT INTO bezier VALUES ")

firstKommaStroke = False
firstKommaBezier = False

files = [f for f in listdir(path) if isfile(join(path, f))]

for file in files:
    read = True
    for term in noreadfiles:
        if term in file:
            read = False
            
    if read:
        xml = minidom.parse(path + "/" + file)
        if "kvg:element" in xml.getElementsByTagName('g')[1].attributes.keys():
            kanji = xml.getElementsByTagName('g')[1].attributes["kvg:element"].value
            if kanji not in addedKanjis:
                print(kanji)
                
                if kanji == '本':
                    print("yahoo")
                
                strokes = xml.getElementsByTagName('path')
                addedKanjis += kanji
                strokeNumber = 1
                
                for stroke in strokes:
                    strokeInformation = stroke.attributes['d'].value
                    
                    strokeInformation = strokeInformation.replace('-',',-')
                    strokeInformation = strokeInformation.replace('c',',c,')
                    strokeInformation = strokeInformation.replace('C',',C,')
                    strokeInformation = strokeInformation.replace('s',',s,')
                    strokeInformation = strokeInformation.replace('S',',S,')
                    strokeInformation = strokeInformation.replace(' ',',')
                    
                    strokeInformation = strokeInformation[1:len(strokeInformation)]
                    strokeSplits = strokeInformation.split(",")
                    
                    filtered = []
                    for split in strokeSplits:
                        if split != '' and split != " ":
                            filtered.append(split)
                            
                    if firstKommaStroke:
                        sqlFileStroke.write(",")
                    else:
                        firstKommaStroke = True
                    
                    strokeSQL = "(" + str(SID) + ", " + str(strokeNumber) + ", '" + kanji + "', NULL, " + filtered[0] + ", " + filtered[1] + ")";
                    strokeNumber += 1
                    sqlFileStroke.write(strokeSQL + "\n")
                    print(strokeSQL)
                    
                    lastX = float(filtered[0])
                    lastY = float(filtered[1])
                    lastRelative = 0
                    startXAbs = lastX
                    startYAbs = lastY
                    
                    i = 2
                    while i < len(filtered):
                        if firstKommaBezier:
                            sqlFileBezier.write(",")
                        else:
                            firstKommaBezier = True
                        
                        s = False
                        if 's' in filtered[i] or 'S' in filtered[i]:
                            s = True
                            
                        relative = 1
                        if 'c' in filtered[i] or 's' in filtered[i]:
                            i += 1
                        elif 'C' in filtered[i] or 'S' in filtered[i]:
                            i += 1
                            relative = 0
                            
                        if s:
                            sx1 = startXAbs
                            sy1 = startYAbs
                            sx2 = float(filtered[i])
                            sy2 = float(filtered[i + 1])
                            sx = float(filtered[i + 2])
                            sy = float(filtered[i + 3])
                            srelative = relative
                            
                            if relative == 1:
                                sx1 = 0
                                sy1 = 0
                            if lastRelative == 0 and relative == 1:
                                sx1 = float(lastX)
                                sy1 = float(lastY)
                                srelative = 0
                                sx2 = startXAbs + float(filtered[i])
                                sy2 = startYAbs + float(filtered[i + 1])
                                sx = startXAbs + float(filtered[i + 2])
                                sy = startYAbs + float(filtered[i + 3])
                            
                            bezierSQL = "(" + str(BID) + ", " + str(srelative) + ", " + str(SID) + ", " + str(sx1) + ", " + str(sy1) + ", " + str(sx2) + ", " + str(sy2) + ", " + str(sx) + ", " + str(sy) + ")"

                            BID += 1
                            i += 4
                            
                            lastX = str(sx)
                            lastY = str(sy)
                            lastRelative = srelative
                            
                            if srelative == 0:
                                startXAbs = float(lastX)
                                startYAbs = float(lastY)
                            else:
                                startXAbs += float(lastX)
                                startYAbs += float(lastY)
                            
                            sqlFileBezier.write(bezierSQL + "\n")
                            print(bezierSQL)
                        else:
                            bezierSQL = "(" + str(BID) + ", " + str(relative) + ", " + str(SID)
                            for a in range(6):
                                bezierSQL += ", " + filtered[i]
                                i += 1
                            bezierSQL += ")"
                            BID += 1
                            
                            lastX = filtered[i - 2]
                            lastY = filtered[i - 1]
                            lastRelative = relative
                            
                            if relative == 0:
                                startXAbs = float(lastX)
                                startYAbs = float(lastY)
                            else:
                                startXAbs += float(lastX)
                                startYAbs += float(lastY)
                            
                            
                            sqlFileBezier.write(bezierSQL + "\n")
                            print(bezierSQL)
                        
                        
                    SID += 1
                    
                print("")
            

sqlFileStroke.write(";")
sqlFileBezier.write(";")

sqlFileStroke.close()
sqlFileBezier.close()