import re
times = ['周一.1.2节','周一.3.4节','周一.5.6节','周一.7.8节','周一.10.11节','周一.12.13节','周二.1.2节','周二.3.4节','周二.5.6节','周二.7.8节','周二.10.11节','周二.12.13节','周三.1.2节','周三.3.4节','周三.5.6节','周三.7.8节','周三.10.11节','周三.12.13节','周四.1.2节','周四.3.4节','周四.5.6节','周四.7.8节','周四.10.11节','周四.12.13节','周五.1.2节','周五.3.4节','周五.5.6节','周五.7.8节','周五.10.11节','周五.12.13']
week = '1-18'
pl = '松1121'
class lessonTime_Place:
    time_pat = re.compile(r'\d+')
    def __init__(self, week, time, place):
        self.week1=0
        num_time = re.findall(lessonTime_Place.time_pat,time)
#        num_week = re.findall(lessonTime_Place.time_pat,week)
        self.place = place.rstrip()
        t=0
        if('一' in time):
            for item in num_time:
                t |= (1<<(int(item)-1))
        elif('二' in time):
            for item in num_time:
                t |= (1<<(int(item)+12))
        elif('三' in time):
            for item in num_time:
                t |= (1<<(int(item)+25))
        elif('四' in time):
            for item in num_time:
                t |= (1<<(int(item)+38))
        elif('五' in time):
            for item in num_time:
                if(item != '13'):
                    t |= (1<<(int(item)+51))
        else:
            t=0
        self.week1 = t

for item in times:
    print(bin((lessonTime_Place(week,item,pl).week1)+(1<<65)))
