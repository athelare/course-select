#Program function: Login to jwdep.cn and access all course infomation via the website.
#dependence : requests and BeautifulSoup and getpass.

import requests
import getpass
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
    def __init__(self, name, index, addr):
        self.name = name
        self.int_index = int(index)
        self.index=index
        self.lessons = []
        
        #Open page of a course.
        coursePage = BeautifulSoup(session.get(addr).text, "html.parser")
        courseTable = coursePage.find_all('table')
        lesson_item = courseTable[1].find_all('tr',recursive=False)
        for i in range(1,len(lesson_item)):
            lesson_info_array = lesson_item[i].find_all('td')
            time_place_info = lesson_info_array[7].find('table')
            self.lessons.append(Lesson(int(lesson_info_array[0].text),lesson_info_array[6].text,time_place_info))

def getContents():
    pageContent = session.get('http://jwdep.dhu.edu.cn/dhu/commonquery/selectcoursetermcourses.jsp?pageSize=10000&curPage=1')
    #get bs Object:
    bsContent = BeautifulSoup(pageContent.text, "html.parser")
    #find table of coursed
    tables_in_page = bsContent.find_all("table")
    #get main table
    courses = tables_in_page[1].find_all('tr')
    courses = courses[1].find_all('td')
    return courses

def main():
    #raw_contents_info : unorgnized infomation of courses content.
    #each course item takes up six rows.
    raw_contents_info = getContents()
    #storge each individual course info.
    courses = []
    #Write infomation into a file.

    course_count = 0
    print('Collecting infomations...')
    for i in range(len(raw_contents_info)):
        if (i % 6 == 1):
            courses.append(Course(raw_contents_info[i - 1].text, raw_contents_info[i].text, 'http://jwdep.dhu.edu.cn/dhu/commonquery/coursetimetableinfo.jsp?courseId=' + raw_contents_info[i].text))
            course_count+=1
            if (course_count%100 == 0):
                print(str(course_count)+' courses get.')

    print(str(course_count)+'coursed in total.\n\nWriting to file...')
    out_file = open('new_Course_infomation.txt','w')
    for item in courses:
#       print(item.index+' '+item.name)
        out_file.write(item.index+','+item.name+':\n')
        for ls in item.lessons:
#           print('    '+str(ls.lessonIndex)+' '+ ls.teacher)
            out_file.write(str(ls.lessonIndex)+','+ ls.teacher+',')
            for tp in ls.time_place:
#               print('        '+tp.week+' '+tp.time+' '+tp.place)
                out_file.write(tp.week+','+tp.time+','+tp.place+';')
            out_file.write('\n')
        out_file.write('#\n')
    out_file.close()
    print('finished.')
main()

