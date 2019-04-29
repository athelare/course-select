#!/usr/bin/python3
import MySQLdb
import getpass
import re
import requests
import os

#获取公共会话变量
def getSession():
    login_url = 'http://jwdep.dhu.edu.cn/dhu/login_wz.jsp'
    stuID = input('---Step3导入文化素质＆外语＆体育＆计算机等课程---\n学号:')
    passwd = getpass.getpass("密码:")
    session = requests.Session()
    
    header = {
        'User-Agent':'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36',
        'Cookie':'userid=Cs4Tl57o5Ny66j7Zy6Vb7Ru82g8Ia8Vb9Gi9QxAGxB4g; username=1OiKJDkGQSxI8Hq; loginname=19o5Pz6Af6Kb6Zo7Km7Rz8Kv8Za9Mb; baseperm=1Rp; taskperm=13x; usertype=Ca4Tk; workurl=2Ry91a9Iy9Yj9VbAIjAZyBWz; departid=Mb54e; JSESSIONID=aEYMk1S0FhV9',
    }
    image = session.get('http://jwdep.dhu.edu.cn/dhu/servlet/com.collegesoft.eduadmin.tables.captcha.CaptchaController',headers=header)
    with open('/home/lijiyu/Desktop/captcha.jpg','wb') as img:
        img.write(image.content)
    
    captcha = input('验证码:')
    response = session.post(login_url,{
        'userName':stuID,
        'userPwd':passwd,
	    'code':captcha
        },headers = header)
    #Check Login status:
    if '补考成绩记录查询' in response.text:
        print('\n登陆成功!\n开始获取数据...\n')
        return session
    else:
        print('\n登录失败!\n请检查用户名和密码，以及网络连接...\n')
        print(response.text)
        quit()
#公共会话变量，供所有之后的函数访问，最后使用它退出登录
    
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



    

