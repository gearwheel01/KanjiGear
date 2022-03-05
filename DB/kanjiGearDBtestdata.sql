-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `isradicalof`
--

DROP TABLE IF EXISTS `isradicalof`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `isradicalof` (
  `Radical` varchar(1) NOT NULL,
  `Kanji` varchar(1) NOT NULL,
  PRIMARY KEY (`Radical`,`Kanji`),
  KEY `fk_Kanji_has_Kanji_Kanji2_idx` (`Kanji`),
  KEY `fk_Kanji_has_Kanji_Kanji1_idx` (`Radical`),
  CONSTRAINT `fk_Kanji_has_Kanji_Kanji1` FOREIGN KEY (`Radical`) REFERENCES `kanji` (`symbol`),
  CONSTRAINT `fk_Kanji_has_Kanji_Kanji2` FOREIGN KEY (`Kanji`) REFERENCES `kanji` (`symbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `isradicalof`
--

LOCK TABLES `isradicalof` WRITE;
/*!40000 ALTER TABLE `isradicalof` DISABLE KEYS */;
/*!40000 ALTER TABLE `isradicalof` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kanji`
--

DROP TABLE IF EXISTS `kanji`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kanji` (
  `symbol` varchar(1) NOT NULL,
  `grade` varchar(10) DEFAULT NULL,
  `learningProgress` int DEFAULT NULL,
  PRIMARY KEY (`symbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kanji`
--

LOCK TABLES `kanji` WRITE;
/*!40000 ALTER TABLE `kanji` DISABLE KEYS */;
INSERT INTO `kanji` VALUES ('日','1',0),('明','1',0),('本','1',0);
/*!40000 ALTER TABLE `kanji` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kanjimeaning`
--

DROP TABLE IF EXISTS `kanjimeaning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kanjimeaning` (
  `KMID` int NOT NULL AUTO_INCREMENT,
  `language` varchar(2) DEFAULT NULL,
  `meaning` varchar(20) DEFAULT NULL,
  `Kanji_symbol` varchar(1) NOT NULL,
  PRIMARY KEY (`KMID`),
  KEY `fk_KanjiMeaning_Kanji1_idx` (`Kanji_symbol`),
  CONSTRAINT `fk_KanjiMeaning_Kanji1` FOREIGN KEY (`Kanji_symbol`) REFERENCES `kanji` (`symbol`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kanjimeaning`
--

LOCK TABLES `kanjimeaning` WRITE;
/*!40000 ALTER TABLE `kanjimeaning` DISABLE KEYS */;
INSERT INTO `kanjimeaning` VALUES (1,'DE','Sonne','日'),(2,'DE','Tag','日'),(3,'DE','hell','明'),(4,'DE','Buch','本');
/*!40000 ALTER TABLE `kanjimeaning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listcontainskanji`
--

DROP TABLE IF EXISTS `listcontainskanji`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `listcontainskanji` (
  `StudyList_SLID` int NOT NULL,
  `Kanji_symbol` varchar(1) NOT NULL,
  PRIMARY KEY (`StudyList_SLID`,`Kanji_symbol`),
  KEY `fk_StudyList_has_Kanji_Kanji1_idx` (`Kanji_symbol`),
  KEY `fk_StudyList_has_Kanji_StudyList1_idx` (`StudyList_SLID`),
  CONSTRAINT `fk_StudyList_has_Kanji_Kanji1` FOREIGN KEY (`Kanji_symbol`) REFERENCES `kanji` (`symbol`),
  CONSTRAINT `fk_StudyList_has_Kanji_StudyList1` FOREIGN KEY (`StudyList_SLID`) REFERENCES `studylist` (`SLID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listcontainskanji`
--

LOCK TABLES `listcontainskanji` WRITE;
/*!40000 ALTER TABLE `listcontainskanji` DISABLE KEYS */;
INSERT INTO `listcontainskanji` VALUES (1,'明');
/*!40000 ALTER TABLE `listcontainskanji` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listcontainssentence`
--

DROP TABLE IF EXISTS `listcontainssentence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `listcontainssentence` (
  `StudyList_SLID` int NOT NULL,
  `Sentence_SID` int NOT NULL,
  PRIMARY KEY (`StudyList_SLID`,`Sentence_SID`),
  KEY `fk_StudyList_has_Sentence_Sentence1_idx` (`Sentence_SID`),
  KEY `fk_StudyList_has_Sentence_StudyList1_idx` (`StudyList_SLID`),
  CONSTRAINT `fk_StudyList_has_Sentence_Sentence1` FOREIGN KEY (`Sentence_SID`) REFERENCES `sentence` (`SID`),
  CONSTRAINT `fk_StudyList_has_Sentence_StudyList1` FOREIGN KEY (`StudyList_SLID`) REFERENCES `studylist` (`SLID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listcontainssentence`
--

LOCK TABLES `listcontainssentence` WRITE;
/*!40000 ALTER TABLE `listcontainssentence` DISABLE KEYS */;
INSERT INTO `listcontainssentence` VALUES (1,1);
/*!40000 ALTER TABLE `listcontainssentence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listcontainsword`
--

DROP TABLE IF EXISTS `listcontainsword`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `listcontainsword` (
  `StudyList_SLID` int NOT NULL,
  `Word_WID` int NOT NULL,
  PRIMARY KEY (`StudyList_SLID`,`Word_WID`),
  KEY `fk_StudyList_has_Word_Word1_idx` (`Word_WID`),
  KEY `fk_StudyList_has_Word_StudyList1_idx` (`StudyList_SLID`),
  CONSTRAINT `fk_StudyList_has_Word_StudyList1` FOREIGN KEY (`StudyList_SLID`) REFERENCES `studylist` (`SLID`),
  CONSTRAINT `fk_StudyList_has_Word_Word1` FOREIGN KEY (`Word_WID`) REFERENCES `word` (`WID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listcontainsword`
--

LOCK TABLES `listcontainsword` WRITE;
/*!40000 ALTER TABLE `listcontainsword` DISABLE KEYS */;
INSERT INTO `listcontainsword` VALUES (1,2);
/*!40000 ALTER TABLE `listcontainsword` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reading`
--

DROP TABLE IF EXISTS `reading`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reading` (
  `RID` int NOT NULL AUTO_INCREMENT,
  `type` varchar(3) DEFAULT NULL,
  `reading` varchar(10) DEFAULT NULL,
  `Kanji_symbol` varchar(1) NOT NULL,
  PRIMARY KEY (`RID`),
  KEY `fk_Reading_Kanji_idx` (`Kanji_symbol`),
  CONSTRAINT `fk_Reading_Kanji` FOREIGN KEY (`Kanji_symbol`) REFERENCES `kanji` (`symbol`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reading`
--

LOCK TABLES `reading` WRITE;
/*!40000 ALTER TABLE `reading` DISABLE KEYS */;
INSERT INTO `reading` VALUES (1,'KUN','ひ','日'),(2,'ON','にち','日'),(3,'ON','じつ','日'),(4,'KUN','もと','本'),(5,'ON','ほん','本'),(6,'KUN','あか','明'),(7,'ON','めい','明'),(8,'ON','みょう','明');
/*!40000 ALTER TABLE `reading` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sentence`
--

DROP TABLE IF EXISTS `sentence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sentence` (
  `SID` int NOT NULL AUTO_INCREMENT,
  `text` varchar(300) DEFAULT NULL,
  `learningProgress` int DEFAULT NULL,
  PRIMARY KEY (`SID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sentence`
--

LOCK TABLES `sentence` WRITE;
/*!40000 ALTER TABLE `sentence` DISABLE KEYS */;
INSERT INTO `sentence` VALUES (1,'明日は本を読みます。',0);
/*!40000 ALTER TABLE `sentence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sentencecontainsword`
--

DROP TABLE IF EXISTS `sentencecontainsword`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sentencecontainsword` (
  `Sentence_SID` int NOT NULL,
  `Word_WID` int NOT NULL,
  PRIMARY KEY (`Sentence_SID`,`Word_WID`),
  KEY `fk_Sentence_has_Word_Word1_idx` (`Word_WID`),
  KEY `fk_Sentence_has_Word_Sentence1_idx` (`Sentence_SID`),
  CONSTRAINT `fk_Sentence_has_Word_Sentence1` FOREIGN KEY (`Sentence_SID`) REFERENCES `sentence` (`SID`),
  CONSTRAINT `fk_Sentence_has_Word_Word1` FOREIGN KEY (`Word_WID`) REFERENCES `word` (`WID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sentencecontainsword`
--

LOCK TABLES `sentencecontainsword` WRITE;
/*!40000 ALTER TABLE `sentencecontainsword` DISABLE KEYS */;
INSERT INTO `sentencecontainsword` VALUES (1,2),(1,3);
/*!40000 ALTER TABLE `sentencecontainsword` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sentencetranslation`
--

DROP TABLE IF EXISTS `sentencetranslation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sentencetranslation` (
  `STID` int NOT NULL AUTO_INCREMENT,
  `meaning` varchar(300) DEFAULT NULL,
  `language` varchar(2) DEFAULT NULL,
  `Sentence_SID` int NOT NULL,
  PRIMARY KEY (`STID`),
  KEY `fk_SentenceTranslation_Sentence1_idx` (`Sentence_SID`),
  CONSTRAINT `fk_SentenceTranslation_Sentence1` FOREIGN KEY (`Sentence_SID`) REFERENCES `sentence` (`SID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sentencetranslation`
--

LOCK TABLES `sentencetranslation` WRITE;
/*!40000 ALTER TABLE `sentencetranslation` DISABLE KEYS */;
INSERT INTO `sentencetranslation` VALUES (1,'Morgen lese ich ein Buch.','DE',1);
/*!40000 ALTER TABLE `sentencetranslation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stroke`
--

DROP TABLE IF EXISTS `stroke`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stroke` (
  `SID` int NOT NULL AUTO_INCREMENT,
  `strokeInformation` varchar(200) DEFAULT NULL,
  `number` int DEFAULT NULL,
  `Kanji_symbol` varchar(1) NOT NULL,
  `component` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SID`),
  KEY `fk_Stroke_Kanji1_idx` (`Kanji_symbol`),
  CONSTRAINT `fk_Stroke_Kanji1` FOREIGN KEY (`Kanji_symbol`) REFERENCES `kanji` (`symbol`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stroke`
--

LOCK TABLES `stroke` WRITE;
/*!40000 ALTER TABLE `stroke` DISABLE KEYS */;
INSERT INTO `stroke` VALUES (1,'<path id=\"kvg:0672c-s1\" kvg:type=\"㇐\" d=\"M20.5,33.5c1.93,0.62,4.91,1.07,8.1,0.75C42.43,32.88,66,30.75,79.64,30c3.2-0.18,7.22,0.25,9.23,0.5\"/>',1,'本','木'),(2,'<path id=\"kvg:0672c-s2\" kvg:type=\"㇑\" d=\"M52.1,11.12c1.25,1.25,2.05,3.23,2.05,4.99c0,0.84,0,57.16-0.02,76.76c-0.01,3.96-0.01,6.42-0.02,6.62\"/>',2,'本','木'),(3,'<path id=\"kvg:0672c-s3\" kvg:type=\"㇒\" d=\"M51.75,33.5c0,1-0.41,2.22-1.29,3.88C43.62,50.25,30.12,65.5,13.25,75.5\"/>',3,'本','木'),(4,'<path id=\"kvg:0672c-s4\" kvg:type=\"㇏\" d=\"M54.75,35.5c4.92,5.74,23.48,23.33,32.85,31.27c2.58,2.18,5.16,4.41,8.52,5.23\"/>',4,'本','木'),(5,'<path id=\"kvg:0672c-s5\" kvg:type=\"㇐\" d=\"M33.88,73.92c1.5,0.46,2.74,0.75,5.3,0.59c9.95-0.63,21.2-2.13,27.96-2.95c1.93-0.23,3.62-0.31,6-0.02\"/>',5,'本',NULL),(6,'<path id=\"kvg:065e5-s1\" kvg:type=\"㇑\" d=\"M31.5,24.5c1.12,1.12,1.74,2.75,1.74,4.75c0,1.6-0.16,38.11-0.09,53.5c0.02,3.82,0.05,6.35,0.09,6.75\"/>',1,'日','日'),(7,'<path id=\"kvg:065e5-s2\" kvg:type=\"㇕a\" d=\"M33.48,26c0.8-0.05,37.67-3.01,40.77-3.25c3.19-0.25,5,1.75,5,4.25c0,4-0.22,40.84-0.23,56c0,3.48,0,5.72,0,6\"/>',2,'日','日'),(8,'<path id=\"kvg:065e5-s3\" kvg:type=\"㇐a\" d=\"M34.22,55.25c7.78-0.5,35.9-2.5,44.06-2.75\"/>',3,'日','日'),(9,'<path id=\"kvg:065e5-s4\" kvg:type=\"㇐a\" d=\"M34.23,86.5c10.52-0.75,34.15-2.12,43.81-2.25\"/>',4,'日','日'),(10,'<path id=\"kvg:0660e-s1\" kvg:type=\"㇑\" d=\"M16.75,25.22c0.89,0.89,1.32,2.29,1.32,3.66c0,1.11,0.07,27.44,0,37.63c-0.02,2.32-0.02,3.78-0.02,3.91\"/>',1,'明','日'),(11,'<path id=\"kvg:0660e-s2\" kvg:type=\"㇕a\" d=\"M19.29,27.75c5.66-0.81,19.54-2.59,21.22-2.75c1.76-0.17,2.89,1.89,2.75,2.91c-0.23,1.7-0.45,27.34-0.52,36.6c-0.01,1.9-0.02,3.11-0.02,3.29\"/>',2,'明','日'),(12,'<path id=\"kvg:0660e-s3\" kvg:type=\"㇐a\" d=\"M19.16,46.97C23.5,46.5,37,45.12,41.77,44.76\"/>',3,'明','日'),(13,'<path id=\"kvg:0660e-s4\" kvg:type=\"㇐a\" d=\"M19,66.59c8.5-0.97,14.37-1.59,22.49-2.11\"/>',4,'明','日'),(14,'<path id=\"kvg:0660e-s5\" kvg:type=\"㇒\" d=\"M57.55,16c0.95,1.25,1.09,2.48,1.11,3.5c1.09,49.25-4.41,64.75-17.41,75.75\"/>',5,'明','月'),(15,'<path id=\"kvg:0660e-s6\" kvg:type=\"㇆a\" d=\"M59.5,17.98c6.56-1.04,21.76-3.51,23.07-3.73c2.97-0.5,5.21,0.62,5.21,4.5c0,1.49,0.19,50.25,0.19,70.5c0,11.88-7.22,3.5-8.3,2.5\"/>',6,'明','月'),(16,'<path id=\"kvg:0660e-s7\" kvg:type=\"㇐a\" d=\"M59.77,39c7.85-1,19.73-2.25,26.95-2.75\"/>',7,'明','月'),(17,'<path id=\"kvg:0660e-s8\" kvg:type=\"㇐a\" d=\"M59.25,57.75c6.62-0.62,20-1.5,27.36-2\"/>',8,'明','月');
/*!40000 ALTER TABLE `stroke` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `studylist`
--

DROP TABLE IF EXISTS `studylist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studylist` (
  `SLID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`SLID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studylist`
--

LOCK TABLES `studylist` WRITE;
/*!40000 ALTER TABLE `studylist` DISABLE KEYS */;
INSERT INTO `studylist` VALUES (1,'Test List 1');
/*!40000 ALTER TABLE `studylist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `word`
--

DROP TABLE IF EXISTS `word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `word` (
  `WID` int NOT NULL AUTO_INCREMENT,
  `word` varchar(20) DEFAULT NULL,
  `grade` varchar(10) DEFAULT NULL,
  `learningProgress` int DEFAULT NULL,
  `pronunciation` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`WID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `word`
--

LOCK TABLES `word` WRITE;
/*!40000 ALTER TABLE `word` DISABLE KEYS */;
INSERT INTO `word` VALUES (1,'日本','1',0,'にほん'),(2,'明日','1',0,'あした'),(3,'本','1',0,'ほん');
/*!40000 ALTER TABLE `word` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wordmeaning`
--

DROP TABLE IF EXISTS `wordmeaning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wordmeaning` (
  `WMID` int NOT NULL AUTO_INCREMENT,
  `language` varchar(2) DEFAULT NULL,
  `meaning` varchar(20) DEFAULT NULL,
  `Word_WID` int NOT NULL,
  PRIMARY KEY (`WMID`),
  KEY `fk_WordMeaning_Word1_idx` (`Word_WID`),
  CONSTRAINT `fk_WordMeaning_Word1` FOREIGN KEY (`Word_WID`) REFERENCES `word` (`WID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wordmeaning`
--

LOCK TABLES `wordmeaning` WRITE;
/*!40000 ALTER TABLE `wordmeaning` DISABLE KEYS */;
INSERT INTO `wordmeaning` VALUES (1,'DE','Japan',1),(2,'DE','morgen',2),(3,'DE','Buch',3);
/*!40000 ALTER TABLE `wordmeaning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wordwrittenwithkanji`
--

DROP TABLE IF EXISTS `wordwrittenwithkanji`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wordwrittenwithkanji` (
  `Word_WID` int NOT NULL,
  `Kanji_symbol` varchar(1) NOT NULL,
  PRIMARY KEY (`Word_WID`,`Kanji_symbol`),
  KEY `fk_Word_has_Kanji_Kanji1_idx` (`Kanji_symbol`),
  KEY `fk_Word_has_Kanji_Word1_idx` (`Word_WID`),
  CONSTRAINT `fk_Word_has_Kanji_Kanji1` FOREIGN KEY (`Kanji_symbol`) REFERENCES `kanji` (`symbol`),
  CONSTRAINT `fk_Word_has_Kanji_Word1` FOREIGN KEY (`Word_WID`) REFERENCES `word` (`WID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wordwrittenwithkanji`
--

LOCK TABLES `wordwrittenwithkanji` WRITE;
/*!40000 ALTER TABLE `wordwrittenwithkanji` DISABLE KEYS */;
INSERT INTO `wordwrittenwithkanji` VALUES (1,'日'),(1,'本'),(2,'日'),(2,'明'),(3,'本');
/*!40000 ALTER TABLE `wordwrittenwithkanji` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-03-04 15:15:41
