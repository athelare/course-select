import os
import getpass
import requests

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