import re
file = open('/home/lijiyu/CourseSelectProject/PlanPage.txt','r')
blank = re.compile('\s|&nbsp')
purePage = blank.sub('',file.read())

csPat = re.compile('<tr><td>(.{4})</td><td>(\d{6})</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td>')
result = re.findall(pat,purePage)
for item in result:
	print(item)
