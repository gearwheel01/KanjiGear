DROP TABLE IF EXISTS `kanji`;

CREATE TABLE `kanji` (
  `symbol` text NOT NULL,
  `grade` integer DEFAULT NULL,
  `jlpt` integer DEFAULT NULL,
  `frequency` integer DEFAULT NULL,
  `learningProgress` integer DEFAULT NULL
);

DROP TABLE IF EXISTS `kanjimeaning`;

CREATE TABLE `kanjimeaning` (
  `KMID` integer PRIMARY KEY AUTOINCREMENT,
  `language` text DEFAULT NULL,
  `meaning` text DEFAULT NULL,
  `Kanji_symbol` text NOT NULL
);

DROP TABLE IF EXISTS `listcontainskanji`;

CREATE TABLE `listcontainskanji` (
  `StudyList_SLID` integer NOT NULL,
  `Kanji_symbol` text NOT NULL,
  `nextTestDate` integer
);

DROP TABLE IF EXISTS `listcontainssentence`;

CREATE TABLE `listcontainssentence` (
  `StudyList_SLID` integer NOT NULL,
  `Sentence_SID` integer NOT NULL,
  `nextTestDate` integer
);

DROP TABLE IF EXISTS `listcontainsword`;

CREATE TABLE `listcontainsword` (
  `StudyList_SLID` integer NOT NULL,
  `Word_WID` integer NOT NULL,
  `nextTestDate` integer
);


DROP TABLE IF EXISTS `kanjireading`;

CREATE TABLE `kanjireading` (
  `KRID` integer PRIMARY KEY AUTOINCREMENT,
  `type` text DEFAULT NULL,
  `reading` text DEFAULT NULL,
  `Kanji_symbol` text NOT NULL
);


DROP TABLE IF EXISTS `sentence`;

CREATE TABLE `sentence` (
  `SID` integer PRIMARY KEY AUTOINCREMENT,
  `text` text DEFAULT NULL,
  `learningProgress` integer DEFAULT NULL
);


DROP TABLE IF EXISTS `sentencecontainsword`;

CREATE TABLE `sentencecontainsword` (
  `Sentence_SID` integer NOT NULL,
  `Word_WID` integer NOT NULL,
  `writingindex` integer
);



DROP TABLE IF EXISTS `sentencemeaning`;

CREATE TABLE `sentencemeaning` (
  `STID` integer PRIMARY KEY AUTOINCREMENT,
  `meaning` text DEFAULT NULL,
  `language` text DEFAULT NULL,
  `Sentence_SID` integer NOT NULL
);



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


DROP TABLE IF EXISTS `studylist`;

CREATE TABLE `studylist` (
  `SLID` integer PRIMARY KEY AUTOINCREMENT,
  `name` text,
  `isActive` integer
);



DROP TABLE IF EXISTS `word`;

CREATE TABLE `word` (
  `WID` integer PRIMARY KEY AUTOINCREMENT,
  `learningProgress` integer DEFAULT NULL,
  `frequency` integer DEFAULT NULL
);



DROP TABLE IF EXISTS `wordmeaning`;

CREATE TABLE `wordmeaning` (
  `WMID` integer PRIMARY KEY AUTOINCREMENT,
  `language` text DEFAULT NULL,
  `meaning` text DEFAULT NULL,
  `Word_WID` integer NOT NULL
);

DROP TABLE IF EXISTS `wordwriting`;

CREATE TABLE `wordwriting` (
  `WWID` integer PRIMARY KEY AUTOINCREMENT,
  `writing` text DEFAULT NULL,
  `Word_WID` integer NOT NULL
);

DROP TABLE IF EXISTS `wordreading`;

CREATE TABLE `wordreading` (
  `WRID` integer PRIMARY KEY AUTOINCREMENT,
  `reading` text DEFAULT NULL,
  `Word_WID` integer NOT NULL
);

