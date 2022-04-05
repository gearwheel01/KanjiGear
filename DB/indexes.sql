create index indexWordMeaning on wordmeaning(meaning);
create index indexWordWriting on wordwriting(writing);
create index indexWordReading on wordreading(reading);

create index indexReadingWord on wordreading(Word_WID);
create index indexWritingWord on wordwriting(Word_WID);
create index indexMeaningWord on wordmeaning(Word_WID);

create index indexStrokeKanji on stroke(Kanji_symbol);
create index indexBezierStroke on bezier(SID);