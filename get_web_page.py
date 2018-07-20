#get page for futher research.
import getpass
import requests
login_url = 'http://jwdep.dhu.edu.cn/dhu/login_wz.jsp'
info_url = 'http://jwdep.dhu.edu.cn/dhu/commonquery/coursetimetableinfo.jsp?courseId=010751'
stuID = input('Load Course infomation to local file.\nStudent ID:')
passwd = getpass.getpass("Password:")
session = requests.Session()
#post data to get session
response = session.post(login_url,{
    'userName':stuID,
    'userPwd':passwd
    })
contents = session.get(info_url)

outfile = open('Single_Course_Page.txt','w');
outfile.write(contents.text);
outfile.close()
