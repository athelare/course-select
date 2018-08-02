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
    stuID = input('---课程信息导入---\n学号:')
    passwd = getpass.getpass("密码:")
    session = requests.Session()
    #post data to get session
    response = session.post(login_url,{
        'userName':stuID,
        'userPwd':passwd
        })
    #Check Login status:
    if '补考成绩记录查询' in response.text:
        print('\n登陆成功!\n开始获取数据...\n')
        return session
    else:
        print('\n登录失败!\n请检查用户名和密码，以及网络连接...\n')
        quit()
    
#public session for all functions.
session = getSession()


class lessonTime_Place:
    time_pat = re.compile(r'\d+')
    def __init__(self, week, time, place):
        self.week1=0
        self.week2=0
        num_time = re.findall(lessonTime_Place.time_pat,time)
        num_week = re.findall(lessonTime_Place.time_pat,week)
        self.place = place.text.rstrip()
        t=0
        if('一' in time):
            for item in num_time:
                t |= (1<<(int(item)-1))
        elif('二' in time):
            for item in num_time:
                t |= (1<<(int(item)+12))
        elif('三' in time):
            for item in num_time:
                t |= (1<<(int(item)+25))
        elif('四' in time):
            for item in num_time:
                t |= (1<<(int(item)+38))
        elif('五' in time):
            for item in num_time:
                if(item != '13'):
                    t |= (1<<(int(item)+51))
        else:
            t=0
        if('1' in num_week or '2' in num_week or '3' in num_week or '4' in num_week or '6' in num_week or '7' in num_week or '8' in num_week):
            self.week1=t
        if('12' in num_week or '13' in num_week or '14' in num_week or '15' in num_week or '16' in num_week or '17' in num_week or '18' in num_week):
            self.week2=t
        
class Lesson:
    def __init__(self, lessonIndex, teacher, time_place_table):
        #lessonIndex:Integer
        self.lessonIndex = lessonIndex
        self.teacher = teacher
        self.time_place = []

        timesp = time_place_table.find_all('tr')
        for tp in timesp:
            tpItems = tp.find_all('td')
            self.time_place.append(lessonTime_Place(tpItems[0].text,tpItems[1].text,tpItems[2]))

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
    pageContent = session.get('http://jwdep.dhu.edu.cn/dhu/commonquery/selectcoursetermcourses.jsp?pageSize=10000&curPage=1')
    pat = re.compile(r'courseId=(\d{6})&courseName=(.*?)"')
    contentsPairs = re.findall(pat,pageContent.text)
    #storge each individual course info.
    courses = []
    #Write infomation into a file.

    course_count = 0
    print('正在收集开课信息...')
    for item in contentsPairs:
        courses.append(Course(item[0], item[1], 'http://jwdep.dhu.edu.cn/dhu/commonquery/coursetimetableinfo.jsp?courseId=' + item[0]))
        course_count+=1
        if (course_count%100 == 0):
            print('已经获取 '+str(course_count)+' 条记录。')
    #Storge Course Data
    print('总共获得 '+str(course_count)+' 个课程信息。\n\n写入数据库ing...')

    csdb = MySQLdb.connect('localhost','testu','123','cs',charset = 'utf8')
    cur = csdb.cursor()
    cur.execute('DROP TABLE IF EXISTS TeachPlan')
    cur.execute('DROP TABLE IF EXISTS Course')
    cur.execute('DROP TABLE IF EXISTS Lesson')
    cur.execute('DROP TABLE IF EXISTS lessonTime')
    cur.execute('''
        CREATE TABLE lessonTime(
            lessonId char(12) NOT NULL,
            timeId char(3)NOT NULL,
            place char(12)CHARACTER SET utf8,
            week1 BIGINT UNSIGNED,
            week2 BIGINT UNSIGNED,
            PRIMARY KEY(lessonId,timeId)
        )''')
    cur.execute('''
        CREATE TABLE Lesson(
            courseId char(12)NOT NULL,
            lessonId char(12)NOT NULL,
            teacher char(15) CHARACTER SET utf8 DEFAULT NULL,
            PRIMARY KEY(courseId,lessonId),
            FOREIGN KEY(lessonId)REFERENCES lessonTime(lessonId)
        )''')
    cur.execute('''
        CREATE TABLE Course(
            courseId char(12)NOT NULL,
            name char(30) CHARACTER SET utf8 NOT NULL,
            PRIMARY KEY(courseId),
            FOREIGN KEY(courseId)REFERENCES Lesson(courseId)
        )''')
    
    for item in courses:
        for ls in item.lessons:
            for it in range(len(ls.time_place)):
                cur.execute('INSERT lessonTime VALUE(\''+ls.lessonIndex+'\',\''+str(it+1)+'\',\''+ls.time_place[it].place+'\',\''+str(ls.time_place[it].week1)+'\',\''+str(ls.time_place[it].week2)+'\')')
            #csdb.commit()
            if(len(ls.time_place)>0):
                cur.execute('INSERT Lesson VALUE(\''+item.index+'\',\''+ls.lessonIndex+'\',\''+ls.teacher+'\')')
            else:
                cur.execute('INSERT lessonTime(lessonId,timeId) VALUE(\''+ls.lessonIndex+'\',\'0\')')
                cur.execute('INSERT Lesson VALUE(\''+item.index+'\',\''+ls.lessonIndex+'\',\'\')')
            #csdb.commit()
        cur.execute('INSERT Course VALUE(\''+item.index+'\',\''+item.name[:30]+'\')')
        csdb.commit()
    csdb.close()
    print('数据库写入完成.')

'''
    out_file = open('Course_infomation.txt','w')
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
print('Bye')
#__END_OF_FILE
