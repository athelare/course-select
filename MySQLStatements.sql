DROP TABLE IF EXISTS Course
DROP TABLE IF EXISTS Lesson
DROP TABLE IF EXISTS lessonTime


CREATE TABLE lessonTime(
	lessonId char(12) NOT NULL,
	timeId char(3)NOT NULL,
	week1 BIGINT,
	week2 BIGINT,
	PRIMARY KEY(lessonId,timeId)
)

CREATE TABLE Lesson(
	courseId char(12)NOT NULL,
	lessonId char(12)NOT NULL,
	teacher char(15) CHARACTER SET utf8 DEFAULT NULL,
	PRIMARY KEY(courseId,lessonId),
	FOREIGN KEY(lessonId)REFERENCES lessonTime(lessonId)
)

CREATE TABLE Course(
	courseId char(12)NOT NULL,
	name char(30) CHARACTER SET utf8 NOT NULL,
	PRIMARY KEY(courseId),
	FOREIGN KEY(courseId)REFERENCES Lesson(courseId)
)

CREATE TABLE lessonTime(lessonId char(12) NOT NULL,timeId char(3)NOT NULL,PRIMARY KEY(lessonId,timeId))

CREATE TABLE Lesson(courseId char(12)NOT NULL,lessonId char(12)NOT NULL,teacher char(15) CHARACTER SET utf8 DEFAULT NULL,PRIMARY KEY(courseId,lessonId),FOREIGN KEY(lessonId)REFERENCES lessonTime(lessonId))

CREATE TABLE Course(courseId char(12)NOT NULL,name char(30) CHARACTER SET utf8 NOT NULL,PRIMARY KEY(courseId),FOREIGN KEY(courseId)REFERENCES Lesson(courseId))

DROP TABLE IF EXISTS TeachPlan

CREATE TABLE TeachPlan(
	gradeId char(7),
	majorId char(20),
	semesterId char(5),
	courseId char(10),
	courseType char(10) CHARACTER SET utf8,
	PRIMARY KEY(gradeId,majorId,semesterId),
	FOREIGN KEY(courseId)REFERENCES Course(courseId)
)

CREATE TABLE TeachPlan(gradeId char(7),majorId char(10),semesterId char(5),courseId char(10),courseType char(10) CHARACTER SET utf8,PRIMARY KEY(gradeId,majorId,semesterId),FOREIGN KEY(courseId)REFERENCES Course(courseId))