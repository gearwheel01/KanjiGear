
DROP TABLE IF EXISTS `isradicalof`;

CREATE TABLE `isradicalof` (
  `Radical` text NOT NULL,
  `Kanji` text NOT NULL
);

INSERT INTO `isradicalof` VALUES ('日','明');


DROP TABLE IF EXISTS `kanji`;

CREATE TABLE `kanji` (
  `symbol` text NOT NULL,
  `grade` text DEFAULT NULL,
  `learningProgress` integer DEFAULT NULL
);

INSERT INTO `kanji` VALUES ('日','1',0),('明','1',0),('本','1',0);

DROP TABLE IF EXISTS `kanjimeaning`;

CREATE TABLE `kanjimeaning` (
  `KMID` integer PRIMARY KEY AUTOINCREMENT,
  `language` text DEFAULT NULL,
  `meaning` text DEFAULT NULL,
  `Kanji_symbol` text NOT NULL
);


INSERT INTO `kanjimeaning` VALUES (1,'DE','Sonne','日'),(2,'DE','Tag','日'),(3,'DE','hell','明'),(4,'DE','Buch','本');

DROP TABLE IF EXISTS `listcontainskanji`;

CREATE TABLE `listcontainskanji` (
  `StudyList_SLID` integer NOT NULL,
  `Kanji_symbol` text NOT NULL
);



INSERT INTO `listcontainskanji` VALUES (1,'明');


DROP TABLE IF EXISTS `listcontainssentence`;

CREATE TABLE `listcontainssentence` (
  `StudyList_SLID` integer NOT NULL,
  `Sentence_SID` integer NOT NULL
);

INSERT INTO `listcontainssentence` VALUES (1,1);

DROP TABLE IF EXISTS `listcontainsword`;

CREATE TABLE `listcontainsword` (
  `StudyList_SLID` integer NOT NULL,
  `Word_WID` integer NOT NULL
);


INSERT INTO `listcontainsword` VALUES (1,2);


DROP TABLE IF EXISTS `reading`;

CREATE TABLE `reading` (
  `RID` integer PRIMARY KEY AUTOINCREMENT,
  `type` text DEFAULT NULL,
  `reading` text DEFAULT NULL,
  `Kanji_symbol` text NOT NULL
);


INSERT INTO `reading` VALUES (1,'KUN','ひ','日'),(2,'ON','にち','日'),(3,'ON','じつ','日'),(4,'KUN','もと','本'),(5,'ON','ほん','本'),(6,'KUN','あか','明'),(7,'ON','めい','明'),(8,'ON','みょう','明');


DROP TABLE IF EXISTS `sentence`;

CREATE TABLE `sentence` (
  `SID` integer PRIMARY KEY AUTOINCREMENT,
  `text` text DEFAULT NULL,
  `learningProgress` integer DEFAULT NULL
);


INSERT INTO `sentence` VALUES (1,'明日は本を読みます。',0);


DROP TABLE IF EXISTS `sentencecontainsword`;

CREATE TABLE `sentencecontainsword` (
  `Sentence_SID` integer NOT NULL,
  `Word_WID` integer NOT NULL
);


INSERT INTO `sentencecontainsword` VALUES (1,2),(1,3);


DROP TABLE IF EXISTS `sentencetranslation`;

CREATE TABLE `sentencetranslation` (
  `STID` integer PRIMARY KEY AUTOINCREMENT,
  `meaning` text DEFAULT NULL,
  `language` text DEFAULT NULL,
  `Sentence_SID` integer NOT NULL
);

INSERT INTO `sentencetranslation` VALUES (1,'Morgen lese ich ein Buch.','DE',1);



DROP TABLE IF EXISTS `stroke`;

CREATE TABLE `stroke` (
  `SID` integer PRIMARY KEY AUTOINCREMENT,
  `strokeInformation` text DEFAULT NULL,
  `number` integer DEFAULT NULL,
  `Kanji_symbol` text NOT NULL,
  `component` text DEFAULT NULL
);



INSERT INTO `stroke` VALUES (1,'<path id=\"kvg:0672c-s1\" kvg:type=\"㇐\" d=\"M20.5,33.5c1.93,0.62,4.91,1.07,8.1,0.75C42.43,32.88,66,30.75,79.64,30c3.2-0.18,7.22,0.25,9.23,0.5\"/>',1,'本','木'),(2,'<path id=\"kvg:0672c-s2\" kvg:type=\"㇑\" d=\"M52.1,11.12c1.25,1.25,2.05,3.23,2.05,4.99c0,0.84,0,57.16-0.02,76.76c-0.01,3.96-0.01,6.42-0.02,6.62\"/>',2,'本','木'),(3,'<path id=\"kvg:0672c-s3\" kvg:type=\"㇒\" d=\"M51.75,33.5c0,1-0.41,2.22-1.29,3.88C43.62,50.25,30.12,65.5,13.25,75.5\"/>',3,'本','木'),(4,'<path id=\"kvg:0672c-s4\" kvg:type=\"㇏\" d=\"M54.75,35.5c4.92,5.74,23.48,23.33,32.85,31.27c2.58,2.18,5.16,4.41,8.52,5.23\"/>',4,'本','木'),(5,'<path id=\"kvg:0672c-s5\" kvg:type=\"㇐\" d=\"M33.88,73.92c1.5,0.46,2.74,0.75,5.3,0.59c9.95-0.63,21.2-2.13,27.96-2.95c1.93-0.23,3.62-0.31,6-0.02\"/>',5,'本',NULL),(6,'<path id=\"kvg:065e5-s1\" kvg:type=\"㇑\" d=\"M31.5,24.5c1.12,1.12,1.74,2.75,1.74,4.75c0,1.6-0.16,38.11-0.09,53.5c0.02,3.82,0.05,6.35,0.09,6.75\"/>',1,'日','日'),(7,'<path id=\"kvg:065e5-s2\" kvg:type=\"㇕a\" d=\"M33.48,26c0.8-0.05,37.67-3.01,40.77-3.25c3.19-0.25,5,1.75,5,4.25c0,4-0.22,40.84-0.23,56c0,3.48,0,5.72,0,6\"/>',2,'日','日'),(8,'<path id=\"kvg:065e5-s3\" kvg:type=\"㇐a\" d=\"M34.22,55.25c7.78-0.5,35.9-2.5,44.06-2.75\"/>',3,'日','日'),(9,'<path id=\"kvg:065e5-s4\" kvg:type=\"㇐a\" d=\"M34.23,86.5c10.52-0.75,34.15-2.12,43.81-2.25\"/>',4,'日','日'),(10,'<path id=\"kvg:0660e-s1\" kvg:type=\"㇑\" d=\"M16.75,25.22c0.89,0.89,1.32,2.29,1.32,3.66c0,1.11,0.07,27.44,0,37.63c-0.02,2.32-0.02,3.78-0.02,3.91\"/>',1,'明','日'),(11,'<path id=\"kvg:0660e-s2\" kvg:type=\"㇕a\" d=\"M19.29,27.75c5.66-0.81,19.54-2.59,21.22-2.75c1.76-0.17,2.89,1.89,2.75,2.91c-0.23,1.7-0.45,27.34-0.52,36.6c-0.01,1.9-0.02,3.11-0.02,3.29\"/>',2,'明','日'),(12,'<path id=\"kvg:0660e-s3\" kvg:type=\"㇐a\" d=\"M19.16,46.97C23.5,46.5,37,45.12,41.77,44.76\"/>',3,'明','日'),(13,'<path id=\"kvg:0660e-s4\" kvg:type=\"㇐a\" d=\"M19,66.59c8.5-0.97,14.37-1.59,22.49-2.11\"/>',4,'明','日'),(14,'<path id=\"kvg:0660e-s5\" kvg:type=\"㇒\" d=\"M57.55,16c0.95,1.25,1.09,2.48,1.11,3.5c1.09,49.25-4.41,64.75-17.41,75.75\"/>',5,'明','月'),(15,'<path id=\"kvg:0660e-s6\" kvg:type=\"㇆a\" d=\"M59.5,17.98c6.56-1.04,21.76-3.51,23.07-3.73c2.97-0.5,5.21,0.62,5.21,4.5c0,1.49,0.19,50.25,0.19,70.5c0,11.88-7.22,3.5-8.3,2.5\"/>',6,'明','月'),(16,'<path id=\"kvg:0660e-s7\" kvg:type=\"㇐a\" d=\"M59.77,39c7.85-1,19.73-2.25,26.95-2.75\"/>',7,'明','月'),(17,'<path id=\"kvg:0660e-s8\" kvg:type=\"㇐a\" d=\"M59.25,57.75c6.62-0.62,20-1.5,27.36-2\"/>',8,'明','月');



DROP TABLE IF EXISTS `studylist`;

CREATE TABLE `studylist` (
  `SLID` integer PRIMARY KEY AUTOINCREMENT,
  `name` text
);


INSERT INTO `studylist` VALUES (1,'Test List 1');



DROP TABLE IF EXISTS `word`;

CREATE TABLE `word` (
  `WID` integer PRIMARY KEY AUTOINCREMENT,
  `word` text DEFAULT NULL,
  `grade` text DEFAULT NULL,
  `learningProgress` integer DEFAULT NULL,
  `pronunciation` text DEFAULT NULL
);



INSERT INTO `word` VALUES (1,'日本','1',0,'にほん'),(2,'明日','1',0,'あした'),(3,'本','1',0,'ほん');

DROP TABLE IF EXISTS `wordmeaning`;

CREATE TABLE `wordmeaning` (
  `WMID` integer PRIMARY KEY AUTOINCREMENT,
  `language` text DEFAULT NULL,
  `meaning` text DEFAULT NULL,
  `Word_WID` integer NOT NULL
);


INSERT INTO `wordmeaning` VALUES (1,'DE','Japan',1),(2,'DE','morgen',2),(3,'DE','Buch',3);


DROP TABLE IF EXISTS `wordwrittenwithkanji`;

CREATE TABLE `wordwrittenwithkanji` (
  `Word_WID` integer NOT NULL,
  `Kanji_symbol` text NOT NULL
);


INSERT INTO `wordwrittenwithkanji` VALUES (1,'日'),(1,'本'),(2,'日'),(2,'明'),(3,'本');