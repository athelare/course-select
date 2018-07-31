#Program function: Get all the teaching plans from the jw.cn and storge it in a file.


import requests
import getpass
import re
from bs4 import BeautifulSoup

#Often used variable:
selectGradeMajorPage = 'http://jwdep.dhu.edu.cn/dhu/commonquery/selectgradeyearmajor.jsp'

#A function to get loggin session and storge it into a public session variable.
def getSession():
    login_url = 'http://jwdep.dhu.edu.cn/dhu/login_wz.jsp'
    stuID = input('Load All_Teaching_Plan to local file.\nStudent ID:')
    passwd = getpass.getpass()
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
			self.semester = 'n'



class Major:
	majorPage = 'http://jwdep.dhu.edu.cn/dhu/commonquery/teachschemasquery.jsp'
	csPat = re.compile('<tr><td>(.{4})</td><td>(\d{6})</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td>')
	ridPat = re.compile('\s|&nbsp;')
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
	ridPat = re.compile('\s|\d')
	def __init__(self,year):
		self.majors = []
		self.year = year
		majorSelectPage = BeautifulSoup(session.post(selectGradeMajorPage,{
			'gradeYear':year,
			'majorId':'110110'
			}).text,'html.parser')
		majorList = majorSelectPage.form.table.contents[5].findAll('option')

#A STATEMENT TO TEST FUNCTION------------TO CHANGE LATER.
		self.majors.append(Major(year,majorList[0]['value'],Grade.ridPat.sub('',majorList[0].text)))
#		for item in majorList:
#			self.majors.append(Major(year,item['value'],Grade.ridPat.sub('',item.text)))

class WholePlan:
	def __init__(self,gradeList):
		self.grades = []
#A STATEMENT TO TEST FUNCTION------------TO CHANGE LATER.
#		self.grades.append(Grade(gradeList[0]))
		for item in gradeList:
			self.grades.append(Grade(item))

def main():
	selectPage = BeautifulSoup(session.get(selectGradeMajorPage).text,'html.parser')
	gradePat = re.compile('\d{4}a')
	gradeList = re.findall(gradePat,selectPage.form.table.contents[3].text)
	wholePlans = WholePlan(gradeList)
	print('Data access completed.')

#	to delete
	print('Writing to files...')
	outfile = open('/home/lijiyu/course-select/getInfo/All_Teaching_Plan','w')
	for grade in wholePlans.grades:
#		outfile.write(grade.year+'\n')
		for major in grade.majors:
#			outfile.write(major.index+'-'+major.name+'\n')
			for course in major.courses:
				outfile.write('(\''+grade.year+'\',\''+major.index+'\',\''+course.semester+'\',\''+course.type+'\',\''+course.index+'\',\''+course.name+'\',\''+course.semester+'\')\n')
	outfile.close()
	print('wirte completed.')
#	to delete

	
main()
session.get('http://jwdep.dhu.edu.cn/dhu/logout.jsp')
#__END_OF_FILE