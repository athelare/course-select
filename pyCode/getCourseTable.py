#Program function: Login to jwdep.cn and access all course infomation via the website.
#dependence : requests and BeautifulSoup and getpass.

import requests
import getpass
import re
import MySQLdb
from bs4 import BeautifulSoup


#A function to get loggin session and storge it into a public session variable.
def getSession():
    login_url = 'http://jwdep.dhu.edu.cn/dhu/login_wz.jsp'
    stuID = input('Load Course infomation to local file.\nStudent ID:')
    passwd = getpass.getpass("Password:")
    session = requests.Session()
    #post data to get session
    response = session.post(login_url,{
        'userName':stuID,
        'userPwd':passwd
        })
    #Check Login status:
    if '补考成绩记录查询' in response.text:
        print('\nLogin successfully!\nAccessing data...\n')
        return session
    else:
        print('\nLogin failed!\nPlease try again later...\n')
        quit()
    
#public session for all functions.
session = getSession()


class lessonTime_Place:
    def __init__(self, week, time, place):
        self.week = week.text
        self.time = time.text
        self.place = place.text.rstrip()

class Lesson:
    def __init__(self, lessonIndex, teacher, time_place_table):
        #lessonIndex:Integer
        self.lessonIndex = lessonIndex
        self.teacher = teacher
        self.time_place = []

        timesp = time_place_table.find_all('tr')
        for tp in timesp:
            tpItems = tp.find_all('td')
            self.time_place.append(lessonTime_Place(tpItems[0],tpItems[1],tpItems[2]))

class Course:
    def __init__(self, index, name, addr):
        self.name = name
        self.index=index
        self.lessons = []
        
        #Open page of a course.
        coursePage = BeautifulSoup(session.get(addr).text, "html.parser")
        courseTable = coursePage.find_all('table')
        lesson_item = courseTable[1].find_all('tr',recursive=False)
        for i in range(1,len(lesson_item)):
            lesson_info_array = lesson_item[i].find_all('td')
            self.lessons.append(Lesson(lesson_info_array[0].text,lesson_info_array[6].text,lesson_info_array[7].find('table')))

#TODO:Change Statements to storge in the database directely.
def main():

    #for test purpose, the size can be small.
    pageContent = session.get('http://jwdep.dhu.edu.cn/dhu/commonquery/selectcoursetermcourses.jsp?pageSize=100000&curPage=1')
    pat = re.compile('courseId=(\d{6})&courseName=(.*?)"')
    contentsPairs = re.findall(pat,pageContent.text)
    #storge each individual course info.
    courses = []
    #Write infomation into a file.

    course_count = 0
    print('Collecting infomations...')
    for item in contentsPairs:
        courses.append(Course(item[0], item[1], 'http://jwdep.dhu.edu.cn/dhu/commonquery/coursetimetableinfo.jsp?courseId=' + item[0]))
        course_count+=1
        if (course_count%100 == 0):
            print(str(course_count)+' courses get.')
    #Storge Course Data
    print(str(course_count)+'coursed in total.\n\nWriting to Database...')

    csdb = MySQLdb.connect('localhost','testu','123','cs',charset = 'utf8')
    cur = csdb.cursor();
    cur.execute('DROP TABLE IF EXISTS Course')
    cur.execute('DROP TABLE IF EXISTS Lesson')
    cur.execute('DROP TABLE IF EXISTS lessonTime')
    cur.execute('CREATE TABLE lessonTime(lessonId char(12) NOT NULL,timeId int NOT NULL,PRIMARY KEY(lessonId,timeId))')
    cur.execute('CREATE TABLE Lesson(courseId char(12)NOT NULL,lessonId char(12)NOT NULL,teacher char(15) CHARACTER SET utf8 DEFAULT NULL,PRIMARY KEY(courseId,lessonId),FOREIGN KEY(lessonId)REFERENCES lessonTime(lessonId))')
    cur.execute('CREATE TABLE Course(courseId char(12)NOT NULL,name char(40) CHARACTER SET utf8 DEFAULT NULL,PRIMARY KEY(courseId),FOREIGN KEY(courseId)REFERENCES Lesson(courseId))')
    
    for item in courses:
        for ls in item.lessons:
            for it in range(len(ls.time_place)):
                cur.execute('INSERT lessonTime VALUE(\''+ls.lessonIndex+'\',\''+str(it)+'\')')
            #csdb.commit()
            if(len(ls.time_place)>0):
                cur.execute('INSERT Lesson VALUE(\''+item.index+'\',\''+ls.lessonIndex+'\',\''+ls.teacher+'\')')
            else:
                cur.execute('INSERT lessonTime VALUE(\''+ls.lessonIndex+'\',\'0\')')
                cur.execute('INSERT Lesson VALUE(\''+item.index+'\',\''+ls.lessonIndex+'\',\'\')')
            #csdb.commit()
        cur.execute('INSERT Course VALUE(\''+item.index+'\',\''+item.name+'\')')
        csdb.commit()
    csdb.close()
    print('Database writing finished.')

'''
    out_file = open('Course_infomation.txt','w')
    db = 
    for item in courses:
        out_file.write(item.index+','+item.name+':\n')
        for ls in item.lessons:
            out_file.write(str(ls.lessonIndex)+','+ ls.teacher+',')
            for tp in ls.time_place:
                out_file.write(tp.week+','+tp.time+','+tp.place+';')
            out_file.write('\n')
        out_file.write('#\n')
    out_file.close()
    print('File writing finished.')
'''
main()

#Logout from the website.
session.get('http://jwdep.dhu.edu.cn/dhu/logout.jsp')
#__END_OF_FILE
