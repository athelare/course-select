#!/usr/bin/python3
#Program function: Login to jwdep.cn and access all course infomation via the website.
#dependence : requests and BeautifulSoup and getpass.

import requests
import getpass
import re
import MySQLdb
from bs4 import BeautifulSoup


#获取公共会话变量
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
        self.time = time
        self.place = place
        #raw week info.
        self.rweek = week
        self.week1 = 0
        self.week2 = 0
        num_time = re.findall(lessonTime_Place.time_pat,time)
        num_week = re.findall(lessonTime_Place.time_pat,week)
        self.place = place.text.rstrip()
        t=0
        #将开课时间信息存储到二进制位里面
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
    def __init__(self, info_array):
        self.lessonIndex = info_array[0].text
        self.recommond = info_array[5].text
        self.teacher = info_array[6].text
        self.time_place = []
        self.halfA=0
        self.halfB=0
        timesp = info_array[7].find('table').find_all('tr')
        for tp in timesp:
            tpItems = tp.find_all('td')
            self.time_place.append(lessonTime_Place(tpItems[0].text,tpItems[1].text,tpItems[2]))
            self.halfA |= self.time_place[-1].week1
            self.halfB |= self.time_place[-1].week2


class Course:
    def __init__(self, csItem):
        self.name = csItem[0]
        self.index = csItem[1]
        self.credit = csItem[2]
        self.faculty = csItem[3]
        self.lessons = []
        
        #打开一个课程的界面
        coursePage = BeautifulSoup(session.get('http://jwdep.dhu.edu.cn/dhu/commonquery/coursetimetableinfo.jsp?courseId='+self.index).text, "html.parser")
        courseTable = coursePage.find_all('table')
        lesson_item = courseTable[1].find_all('tr',recursive=False)
        for i in range(1,len(lesson_item)):
            lesson_info_array = lesson_item[i].find_all('td')
            self.lessons.append(Lesson(lesson_info_array))

def main():

    #如果是为了测试，更改下面这条链接里面的'pageSize值即可讲数据规模变小
    pageContent = session.get('http://jwdep.dhu.edu.cn/dhu/commonquery/selectcoursetermcourses.jsp?pageSize=10000&curPage=1')
    bnpat = re.compile(r'\s|\n')
    pageContent = bnpat.sub('',pageContent.text)
    pat = re.compile(r'<tr><td><.*?"target="_blank">(.*?)</a></td><td>(.*?)</td><td>(.*?)</td><td>.*?</a></td><td>.*?</a></td><td>(.*?)</td>')
    courseInfo = re.findall(pat,pageContent)
    #下面的数组存储所有开课的信息
    courses = []


    course_count = 0
    print('正在收集开课信息...')
    for item in courseInfo:
        courses.append(Course(item))
        course_count+=1
        if (course_count%100 == 0):
            print('已经获取 '+str(course_count)+' 条记录。')
    #Storge Course Data
    print('总共获得 '+str(course_count)+' 个课程信息。\n\n写入数据库ing...')

    csdb = MySQLdb.connect('localhost','testu','123','cs',charset = 'utf8')
    cur = csdb.cursor()
    print('删库ing\n重新定义数据库')
    cur.execute('DROP TABLE IF EXISTS TeachPlan')
    cur.execute('DROP TABLE IF EXISTS Course')
    cur.execute('DROP TABLE IF EXISTS Lesson')
    cur.execute('DROP TABLE IF EXISTS lessonTime')
    cur.execute('''
        CREATE TABLE lessonTime(
            lessonId CHAR(8) NOT NULL,
            timeId TINYINT NOT NULL,
            time CHAR(30)CHARACTER SET utf8,
            place CHAR(12)CHARACTER SET utf8,
            week CHAR(12)CHARACTER SET utf8,
            week1 BIGINT UNSIGNED,
            week2 BIGINT UNSIGNED,
            PRIMARY KEY(lessonId,timeId)
        )''')
    cur.execute('''
        CREATE TABLE Lesson(
            courseId CHAR(8)NOT NULL,
            lessonId CHAR(8)NOT NULL,
            recommend char(20)CHARACTER SET utf8,
            halfA BIGINT UNSIGNED,
            halfB BIGINT UNSIGNED,
            teacher CHAR(15) CHARACTER SET utf8,
            PRIMARY KEY(courseId,lessonId),
            FOREIGN KEY(lessonId)REFERENCES lessonTime(lessonId)
        )''')
    cur.execute('''
        CREATE TABLE Course(
            courseId CHAR(8)NOT NULL,
            name CHAR(30) CHARACTER SET utf8 NOT NULL,
            credit FLOAT,
            faculty char(20)CHARACTER SET utf8,
            PRIMARY KEY(courseId),
            FOREIGN KEY(courseId)REFERENCES Lesson(courseId)
        )''')

    for item in courses:
        for ls in item.lessons:
            for it in range(len(ls.time_place)):
                cur.execute(
                    'INSERT lessonTime(lessonId,timeId,time,place,week,week1,week2) VALUE(\''+
                    ls.lessonIndex+'\',\''+
                    str(it+1)+'\',\''+
                    ls.time_place[it].time[:30]+'\',\''+
                    ls.time_place[it].place[:12]+'\',\''+
                    ls.time_place[it].rweek[:12]+'\',\''+
                    str(ls.time_place[it].week1)+'\',\''+
                    str(ls.time_place[it].week2)+'\')')
            if(len(ls.time_place) == 0):#如果没有开课时间的记录，就添加一条空记录，便于课程的插入（因为必须有一条主键）
                cur.execute('INSERT lessonTime(lessonId,timeId) VALUE(\''+ls.lessonIndex+'\',\'0\')')
            cur.execute(
                'INSERT Lesson(courseId,lessonId,recommend,halfA,halfB,teacher) VALUE(\''+
                item.index+'\',\''+
                ls.lessonIndex+'\',\''+
                ls.recommond[:20]+'\',\''+
                str(ls.halfA)+'\',\''+
                str(ls.halfB)+'\',\''+
                ls.teacher+'\')')
        cur.execute(
            'INSERT Course(courseId,name,credit,faculty) VALUE(\''+
            item.index+'\',\''+
            item.name[:30]+'\',\''+
            str(item.credit)+'\',\''+
            item.faculty[:20]+'\')')
        csdb.commit()
    csdb.close()
    print('数据库写入完成.')

main()

#从网站登出
session.get('http://jwdep.dhu.edu.cn/dhu/logout.jsp')
print('Bye')
#__END_OF_FILE 



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
