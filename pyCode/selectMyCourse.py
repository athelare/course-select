import MySQLdb as mysql
courseList = ['010122','010132','012092','024133','130101','130132','130153','130163','130461','131211','131521','131551','131561','131591','139011','139931']
lessonTable =[]
lessonList = []
resultList = []
mconflict = 2
db = mysql.connect('localhost','testu','123','cs',charset = 'utf8')
cur = db.cursor()

def searchLesson(curIndex,sumHalfA,sumHalfB,conflictNum):
    if( conflictNum > mconflict):
        return
    elif(curIndex == len(courseList)):
        #print(bin(sumHalfA)+'  ,  '+bin(sumHalfB))
        res=[]
        for item in lessonList:
            res.append(item)
        resultList.append(res)
        print(res)
        quit()
        return
    for ilesson in lessonTable[curIndex]:
        lessonList.append(ilesson[0])
        if(((ilesson[1] & sumHalfA) == 0) and ((ilesson[2] & sumHalfB) == 0)):
            searchLesson(curIndex+1,sumHalfA|ilesson[1],sumHalfB|ilesson[2],conflictNum)
        else:
            searchLesson(curIndex+1,sumHalfA|ilesson[1],sumHalfB|ilesson[2],conflictNum+1)
        lessonList.pop()


        
def main():

    for item in courseList:
        print('Course NUm: '+ str(item))
        cur.execute('select lessonId,halfA,halfB from Lesson where courseId = '+item)
        lessonTable.append(cur.fetchall())
        for it in lessonTable[-1]:
            print(bin((1<<65)+it[1]) + '---'+ bin((1<<65)+it[2]))
    print(len(courseList))
    print(len(lessonTable))
    searchLesson(0,0,0,0)
    print(len(resultList))
    print(len(resultList[0]))
main()
