#!/usr/bin/python3
import MySQLdb
import getpass
import re
import requests

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

def main():
    csdb = MySQLdb.connect('localhost','testu','123','cs',charset = 'utf8')
    cur = csdb.cursor()
    cur.execute('DROP TABLE IF EXISTS SpecialCourses')
    cur.execute('CREATE TABLE SpecialCourses(courseType char(3),courseId CHAR(8) NOT NULL, PRIMARY KEY(courseType,courseId),FOREIGN KEY(courseId)REFERENCES Course(courseId))')

    #去除空格和回车
    blk = re.compile(r'\s|\n')
    #获取网页中的课程项
    pat = re.compile(r'<trheight="20"><td><ahref="teachclasslist.*?</a></td><td>(.*?)</td><td>\d\.\d</td><td><ahref=.*?课程日历</a></td><td><ahref=.*?教学大纲</a></td><td>.*?</td><td>.*?</td><td>.*?</td>')
    for i in range(1,5):
        page = session.get('http://jwdep.dhu.edu.cn/dhu/student/selectcourse/allcourseinfo2.jsp?commonSort='+str(i)).text
        page = blk.sub('',page)
        res = re.findall(pat,page)

        for csId in res:
            cur.execute('INSERT IGNORE SpecialCourses VALUE(\''+str(i)+'\',\''+csId+'\')')
        csdb.commit()
    print('信息获取完毕.')
main()
session.get('http://jwdep.dhu.edu.cn/dhu/logout.jsp')
print('Bye')



    

