import MySQLdb as mysql
db = mysql.connect('localhost','testu','123','cs',charset = 'utf8')
cursor = db.cursor()
cursor.execute('select lessonId,teacher from Lesson where courseId = 010751')
lessons = cursor.fetchall()
for lesson in lessons:
    print(lesson[0]+','+lesson[1])
    cursor.execute('select week1,week2,place from lessonTime where lessonId = '+lesson[0])
    times = cursor.fetchall()
    for time in times:
        print(time[2] +' :1-8 week: '+ bin(time[0]) +', 9-16 week: '+ bin(time[1]))
