# 选课规划系统
---
## 简介
教务系统选课对学生来说比较苦恼，因为时间要在点到最后的选课的界面的时候才会看到，而且还需要自己制作一张课表来规划，避免课程的冲突，选课期间十分浪费时间。联想到计算机的无所不能，我认为可以制作一个程序来自动的规划选课。这个程序应该获取到教务处的所有开课信息，教学计划信息，课程分类，以便学生选到自己应该选的课程，还要方便选择文化组织类和体育类的课程（这和学院无关，因此最容易出现时间的冲突而无法选课），这两类课程每学期都有一百门左右，程序可以筛选出哪些是可以选的，哪一些不能选。
  
## 功能
* 自动获取教务系统的开课信息，包括课程序号，开课序号，任课老师，学院，上课时间和地点等等。
* 根据课程序号将要选的课输入到程序中，然后再为每一门课删除掉不选的开课项（有的课程不适合学生的专业或者开课的校区不一样，不能进行选课）
 
## 实现概述
### 获取课程信息
#### 使用Python工具来解析教务网页
1. 使用requests工具通过管理员的学号密码登入教务系统
2. 使用BeautifulSoup和re来解析网页内容，提取网页信息
3. 使用MySQLdb来实现python3和MySQL之间的信息交互
### 维护课程信息
#### 初步:使用MySQL来进行维护操作
1. 开发阶段更新信息采用全部擦除，一次导入的方法，因为数据规模不大，因此并不会耗费比较长的时间。
2. 通过MySQL和特定编程语言之间的接口部件实现信息交互
### 课程计划生成
* 获取课程信息的时候对课程进行归类
* 将开课时间信息存储到二进制里面，一个二进制代表一周中某一天的某一节课，即可利用位运算来判断两个课程或多个课程时间是否冲突，位运算比简单循环的效率要高
#### 初步算法（暴力枚举）
* 设置最大的冲突学分数，然后根据课程输入的顺序进行深度优先搜索。
* 搜索过程中将冲突数作为传递的参数，统计原有的开课时间信息（二进制）和当前选择的班级的时间信息进行与操作，根据结果里面‘1’的个数就可以得出冲突的学分数。

- 如果冲突数小于最大冲突数，就将两个表示课程时间的数值进行或操作，传递给下一层的搜索。
- 最后排课成功，就将计划输出。如果方案数量过多，比如说有一千多项，不需要全部输出，如果方案数量过多也没有必要使用本系统。
### 网站搭建
上述步骤完成之后主要工作就是网站搭建。
#### 工具
+ Apache2
+ PHP 7.0
+ Mysql
#### 页面设计
分为三个页面，一个是主页面，就是选课页面，这个页面提供两种选课方式，即根据课程名称选课或者根据教学计划选课。根据课程名称选课的时候每选一个课就发送一个POST请求，而通过教学计划选课的时候使用勾选框，避免在一个页面上刷新太过频繁。

第二个页面是选择开课班级的页面，里面会有课程的各个开课班级，使用勾选框选择，设置最大冲突的学分数量，成功后就可以点击开始。之后如果当前冲突学分数下没有任何方案的话，就增加0.5个学分。

第三个界面就是选课算法的PHP实现，外加一个显示课程方案（显示为课程表）的代码。

-----------------------------------------------
## 预期功能
* 将本系统部署到动态网站上
* 能够有一个友好的课程选择界面，对课程进行搜索，选择开课班级，等等
* 将选课方案自动制作成一张课程表展示给用户
* 允许设置禁止冲突的课程，允许预留出时间禁止选课
