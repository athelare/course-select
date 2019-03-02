#!/usr/bin/python3
#Program function: Get all the teaching plans from the jw.cn and storge it in a file.
import os
import time
import requests
import getpass
import re
import MySQLdb
from bs4 import BeautifulSoup

#Often used variable:
selectGradeMajorPage = 'http://jwdep.dhu.edu.cn/dhu/commonquery/selectgradeyearmajor.jsp'

#获取公共会话变量
def getSession():
    login_url = 'http://jwdep.dhu.edu.cn/dhu/login_wz.jsp'
    stuID = input('---课程信息导入---\n学号:')
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
    

session = getSession()

class Course:
	def __init__(self,csItem):

		self.type = csItem[0]
		self.index = csItem[1]
		self.name = csItem[2]
		if(csItem[9] != ''):
			self.semester = '1a'
		elif(csItem[10]!= ''):
			self.semester = '1b'
		elif(csItem[11]!= ''):
			self.semester = '2a'
		elif(csItem[12]!= ''):
			self.semester = '2b'
		elif(csItem[13]!= ''):
			self.semester = '3a'
		elif(csItem[14]!= ''):
			self.semester = '3b'
		elif(csItem[15]!= ''):
			self.semester = '4a'
		elif(csItem[16]!= ''):
			self.semester = '4b'
		else:
			self.semester = ''



class Major:
	majorPage = 'http://jwdep.dhu.edu.cn/dhu/commonquery/teachschemasquery.jsp'
	csPat = re.compile(r'<tr><td>(.{4})</td><td>(\d{6})</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td>')
	ridPat = re.compile(r'\s|&nbsp;')
	def __init__(self,gradeYear,majorIndex,majorName):
		self.name = majorName
		self.index = majorIndex
		self.courses = []
		planPage = session.post(Major.majorPage,{
			'gradeYear':gradeYear,
			'majorId':majorIndex
			})
		
		purePage = Major.ridPat.sub('',planPage.text)
		csinfo = re.findall(Major.csPat,purePage)
		for  item in csinfo:
			self.courses.append(Course(item))

#Storge teaching plan by grade.
class Grade:
	ridPat = re.compile(r'\s|\d')
	def __init__(self,year):
		self.majors = []
		self.year = year
		majorSelectPage = BeautifulSoup(session.post(selectGradeMajorPage,{
			'gradeYear':year,
			'majorId':'110110'
			}).text,'html.parser')
		majorList = majorSelectPage.form.table.contents[5].findAll('option')

#A STATEMENT TO TEST FUNCTION------------TO CHANGE LATER.
#		self.majors.append(Major(year,majorList[0]['value'],Grade.ridPat.sub('',majorList[0].text)))
		count = 1
		totalNum = str(len(majorList))
		for item in majorList:
			print('爬取第 '+str(count)+'/'+totalNum+'个')
			self.majors.append(Major(year,item['value'],Grade.ridPat.sub('',item.text)))
			time.sleep(1)


class WholePlan:
	def __init__(self,gradeList):
		self.grades = []
#A STATEMENT TO TEST FUNCTION------------TO CHANGE LATER.
#		self.grades.append(Grade(gradeList[0]))
		for item in gradeList:
			self.grades.append(Grade(item))
			print(item+ '级教学计划获取完毕。暂停十秒钟')
			time.sleep(10)
			print('暂停结束')

def main():
	selectPage = BeautifulSoup(session.get(selectGradeMajorPage).text,'html.parser')
	gradePat = re.compile(r'\d{4}a')
	gradeList = re.findall(gradePat,selectPage.form.table.contents[3].text)
	wholePlans = WholePlan(gradeList)
	print('所有教学计划信息获取完毕.')


	print('写入数据...')
	db = MySQLdb.connect('localhost','testu','123','cs',charset = 'utf8')
	cur = db.cursor()
	cur.execute('DROP TABLE IF EXISTS TeachPlan')
	cur.execute('DROP TABLE IF EXISTS GradeMajor')
	cur.execute('''
		CREATE TABLE GradeMajor(
			gradeId char(7),
			majorId char(20),
			majorName char(20) CHARACTER SET utf8,
			PRIMARY KEY(gradeId,majorId)
		)
		''')
	cur.execute('''
		CREATE TABLE TeachPlan(
			gradeId char(7),
			majorId char(20),
			majorName char(20) CHARACTER SET utf8,
			semesterId char(5),
			courseId char(10),
			courseType char(10) CHARACTER SET utf8,
			PRIMARY KEY(gradeId,majorId,semesterId,courseId),
			FOREIGN KEY(courseId)REFERENCES Course(courseId)
		)''')

	for igrade in wholePlans.grades:
		for imajor in igrade.majors:
			for icourse in imajor.courses:
				if(icourse.semester!= ''):
					cur.execute(
						'INSERT IGNORE TeachPlan VALUE(\''+
						igrade.year+'\',\''+
						imajor.index+'\',\''+
						imajor.name+'\',\''+
						icourse.semester+'\',\''+
						icourse.index+'\',\''+
						icourse.type+'\')')
					db.commit()
			cur.execute(
				'INSERT IGNORE GradeMajor VALUE(\''+
				igrade.year+'\',\''+
				imajor.index[:20]+'\',\''+
				imajor.name[:20]+'\')')
			db.commit()

	db.commit()
	print('数据写入完毕.')
	
main()
session.get('http://jwdep.dhu.edu.cn/dhu/logout.jsp')
print('Bye')
#__END_OF_FILE
