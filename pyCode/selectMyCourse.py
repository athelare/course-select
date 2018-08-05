#!/usr/bin/python3
#Sample program of course selection
import MySQLdb as mysql


N = 16
candidateLesson =[]

resultList = []
Max_Conflict = int(2*float(input('请输入最大的冲突学分数： ')))
db = mysql.connect('localhost','testu','123','raw_cs_info',charset = 'utf8')
cur = db.cursor()

ofile = open('/home/lijiyu/course-select/getInfo/myCoursePlan.txt','w')
lessonCount = 0
#统计二进制里面1的个数，1的个数代表有多少课冲突了
def countBit1(a):
    num = 0
    while a != 0:
        a = a & (a-1)
        num+=1
    return num

selectedLessons = []
def searchLesson(curIndex,sumHalfA,sumHalfB,conflictNum):
    if(conflictNum > Max_Conflict):
        return
    if(curIndex == N):
        global lessonCount
        lessonCount += 1
        tmp=[]
        for item in selectedLessons:
            tmp.append(item)
        resultList.append(tmp)
#        ofile.write(str(tmp)+'\n')
        ofile.write(bin(sumHalfA)+','+bin(sumHalfB)+'\n')
#        ofile.write('冲突学分数: '+ str(conflictNum/2)+'\n')
#        if (lessonCount > 1000):
#            print('Too many solutions(1000+),quit anyway.')
#            ofile.close()
#            quit()
        return
    for ilesson in candidateLesson[curIndex]:
        selectedLessons.append(ilesson[0])
        if(((ilesson[1] & sumHalfA) == 0) and ((ilesson[2] & sumHalfB) == 0)):
            searchLesson(curIndex+1,sumHalfA|ilesson[1],sumHalfB|ilesson[2],conflictNum)
        else:
            newCon = conflictNum+countBit1(sumHalfA & ilesson[1])+countBit1(ilesson[2] & sumHalfB)
            searchLesson(
                curIndex+1,
                sumHalfA|ilesson[1],
                sumHalfB|ilesson[2],
                newCon)
        selectedLessons.pop()


def Sample_input():
    #课程序号
    courseList = ['010122','010132','012092','024133','130101','130132','130153','130163','130461','131211','131521','131551','131561','131591','139011','139931']
    #存储某个课程选课列表里的左右边界
    lessons = [[2 ,18],[3 ,16],[2, 2],[18 ,31],[1, 3],[1, 3],[1, 1],[1, 1],[1, 1],[1, 2],[1, 2],[1, 1],[1, 1],[1, 1],[1, 1],[1, 1]]
    for i in range(len(courseList)):
        cur.execute('select lessonId,halfA,halfB,teacher from Lesson where courseId = '+str(courseList[i]))
        candidateLesson.append([])
        lesson_Course = cur.fetchall()
        for ns in range(lessons[i][0],lessons[i][1]+1):
            if(ns>0 and ns<=len(lesson_Course)):
                candidateLesson[-1].append(lesson_Course[ns-1])
def main():

    Sample_input()
    searchLesson(0,0,0,0)
    ofile.close()
    print('总共方案数: '+str(len(resultList)))
main()

