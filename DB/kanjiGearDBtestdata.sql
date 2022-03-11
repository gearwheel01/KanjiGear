
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
  `number` integer DEFAULT NULL,
  `Kanji_symbol` text NOT NULL,
  `component` text DEFAULT NULL,
  `mx` real DEFAULT NULL,
  `my` real DEFAULT NULL
);

CREATE TABLE `bezier` (
  `BID` integer PRIMARY KEY AUTOINCREMENT,
  `relative` integer DEFAULT NULL,
  `SID` integer NOT NULL,
  `x1` real DEFAULT NULL,
  `y1` real DEFAULT NULL,
  `x2` real DEFAULT NULL,
  `y2` real DEFAULT NULL,
  `x` real DEFAULT NULL,
  `y` real DEFAULT NULL
);



INSERT INTO `stroke` VALUES (1,1,'本','木',20.5,33.5),
  (2,2,'本','木',52.1,11.12),
  (3,3,'本','木',51.75,33.5),
  (4,4,'本','木',54.75,35.5),
  (5,5,'本',NULL,33.88,73.92),
  (6,1,'日','日',31.5,24.5),
  (7,2,'日','日',33.48,26),
  (8,3,'日','日',34.22,55.25),
  (9,4,'日','日',34.23,86.5),

  INSERT INTO `bezier` VALUES 
  (1,1,1,1.93,0.62,4.91,1.07,8.1,0.75),
  (2,0,1,42.43,32.88,66,30.75,79.64,30),
  (3,1,1,3.2-0.18,7.22,0.25,9.23,0.5),
  (4,1,2,1.25,1.25,2.05,3.23,2.05,4.99),
  (5,1,2,0,0.84,0,57.16-0.02,76.76),
  (6,1,2,-0.01,3.96,-0.01,6.42,-0.02,6.62),
  (7,1,3,0,1-0.41,2.22-1.29,3.88),
  (8,0,3,43.62,50.25,30.12,65.5,13.25,75.5),
  (9,1,4,4.92,5.74,23.48,23.33,32.85,31.27),
  (10,1,4,2.58,2.18,5.16,4.41,8.52,5.23),
  (11,1,5,1.5,0.46,2.74,0.75,5.3,0.59),
  (12,1,5,9.95,-0.63,21.2,-2.13,27.96,-2.95),
  (13,1,5,1.93-0.23,3.62-0.31,6-0.02),

  (14,1,6,1.12,1.12,1.74,2.75,1.74,4.75),
  (15,1,6,0,1.6-0.16,38.11-0.09,53.5),
  (16,1,6,0.02,3.82,0.05,6.35,0.09,6.75),
  (17,1,7,0.8-0.05,37.67-3.01,40.77-3.25),
  (18,1,7,3.19-0.25,5,1.75,5,4.25),
  (19,1,7,0,4-0.22,40.84-0.23,56),
  (20,1,7,0,3.48,0,5.72,0,6),
  (21,1,8,7.78-0.5,35.9-2.5,44.06-2.75),
  (22,1,9,10.52-0.75,34.15-2.12,43.81-2.25);



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
  `pronunciation` text DEFAULT NULL,
  `romaji` text DEFAULT NULL
);



INSERT INTO `word` VALUES (1,'日本','1',0,'にほん','nihon'),(2,'明日','1',0,'あした','ashita'),(3,'本','1',0,'ほん','hon');

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