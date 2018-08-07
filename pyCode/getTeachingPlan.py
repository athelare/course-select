#!/usr/bin/python3
#Program function: Get all the teaching plans from the jw.cn and storge it in a file.

import requests
import getpass
import re
import MySQLdb
from bs4 import BeautifulSoup

#Often used variable:
selectGradeMajorPage = 'http://jwdep.dhu.edu.cn/dhu/commonquery/selectgradeyearmajor.jsp'

#A function to get loggin session and storge it into a public session variable.
def getSession():
    login_url = 'http://jwdep.dhu.edu.cn/dhu/login_wz.jsp'
    stuID = input('---导入教学计划信息---.\n学号:')
    passwd = getpass.getpass('密码：')
    session = requests.Session()
    #post data to get session
    response = session.post(login_url,{
        'userName':stuID,
        'userPwd':passwd
        })
    #Check Login status:
    if '补考成绩记录查询' in response.text:
        print('\n登陆成功!\n开始读取信息...\n')
        return session
    else:
        print('\n登录失败!\n请稍后再次尝试...\n')
        quit()
    
#public session for all functions.
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
		for item in majorList:
			self.majors.append(Major(year,item['value'],Grade.ridPat.sub('',item.text)))

class WholePlan:
	def __init__(self,gradeList):
		self.grades = []
#A STATEMENT TO TEST FUNCTION------------TO CHANGE LATER.
#		self.grades.append(Grade(gradeList[0]))
		for item in gradeList:
			self.grades.append(Grade(item))

def main():
	selectPage = BeautifulSoup(session.get(selectGradeMajorPage).text,'html.parser')
	gradePat = re.compile(r'\d{4}a')
	gradeList = re.findall(gradePat,selectPage.form.table.contents[3].text)
	wholePlans = WholePlan(gradeList)
	print('数据获取完毕.')


	print('写入数据...')
	db = MySQLdb.connect('localhost','testu','123','cs',charset = 'utf8')
	cur = db.cursor()
	cur.execute('DROP TABLE IF EXISTS TeachPlan')
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
	db.commit()
	print('数据写入完毕.')

#	to delete
#	print('Writing to files...')
#	outfile = open('/home/lijiyu/course-select/getInfo/All_Teaching_Plan','w')
#	for igrade in wholePlans.grades:
#		outfile.write(grade.year+'\n')
#		for imajor in igrade.majors:
#			outfile.write(major.index+'-'+major.name+'\n')
#			for icourse in imajor.courses:
#				outfile.write('(\''+igrade.year+'\',\''+imajor.index+'\',\''+icourse.semester+'\',\''+icourse.type+'\',\''+icourse.index+'\',\''+icourse.name+'\')\n')
#	outfile.close()
#	print('wirte completed.')
#	to delete
	
main()
session.get('http://jwdep.dhu.edu.cn/dhu/logout.jsp')
print('Bye')
#__END_OF_FILE
