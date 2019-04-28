package com.ljy.courseselect.pojo;

import com.ljy.courseselect.domain.LessonEntity;
import com.ljy.courseselect.domain.LessonTimeEntity;

import java.util.ArrayList;
import java.util.List;

public class SelectPlan {
    private List<LessonEntity>lessons;
    private Long firstHalf,secondHalf;
    private List<List<List<LessonTimeEntity>>> courseTable;
    private int rowSpan[][];

    public SelectPlan(){
        lessons=new ArrayList<>();
        firstHalf=0L;
        secondHalf=0L;
    }

    public List<LessonEntity> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonEntity> lessons) {
        this.lessons = lessons;
        //Initiate rowSpanRecord.
        rowSpan = new int[5][13];
        for(int i=0;i<5;++i)for(int j=0;j<13;++j)rowSpan[i][j]=0;
        //Initiate course table.
        courseTable = new ArrayList<>();
        for(int i=0;i<5;++i){
            List<List<LessonTimeEntity>> tmp = new ArrayList<>();
            for(int j=0;j<13;++j){
                tmp.add(new ArrayList<>());
            }
            courseTable.add(tmp);
        }
        //Fill LessonTimeEntities into courseTable.
        for(LessonEntity lesson:lessons){
            for(LessonTimeEntity item:lesson.getLessonTimesByLessonId()){
                for(int i=0;i<64;++i){
                    if(((1L<<i)&item.getFirstHalf()) != 0 || ((1L<<i)&item.getSecondHalf()) != 0){
                        courseTable.get(i/13).get(i%13).add(item);
                    }
                }
            }
        }
        //Finish time periods with lessons.
        for(int i=0;i<5;++i){
            for(int j=0;j<13;++j){
                if(courseTable.get(i).get(j).size()==0)continue;
                while(j+rowSpan[i][j]<13 &&
                        courseTable.get(i).get(j).size() == courseTable.get(i).get(j+rowSpan[i][j]).size() &&
                        courseTable.get(i).get(j).containsAll(courseTable.get(i).get(j+rowSpan[i][j]))
                ){
                    rowSpan[i][j]++;
                }
                j+=rowSpan[i][j]-1;
            }
        }
        //Finish time periods without Lessons.
        for(int i=0;i<5;++i){
            for(int j=0;j<13;++j){
                if(0 == courseTable.get(i).get(j).size()){
                    rowSpan[i][j]=1;
                    int k=j;
                    while(j != 3 && j != 8 && j!= 12 && courseTable.get(i).get(j+1).size()==0){
                        j++;
                        rowSpan[i][k]++;
                    }
                }else{
                    j+=rowSpan[i][j]-1;
                }
            }
        }
        //courseTable construction finished.

    }

    public Long getSecondHalf() {
        return secondHalf;
    }

    public void setSecondHalf(Long secondHalf) {
        this.secondHalf = secondHalf;
    }

    public Long getFirstHalf() {
        return firstHalf;
    }

    public void setFirstHalf(Long firstHalf) {
        this.firstHalf = firstHalf;
    }

    public List<List<List<LessonTimeEntity>>> getCourseTable() {
        return courseTable;
    }

    public void setCourseTable(List<List<List<LessonTimeEntity>>> courseTable) {
        this.courseTable = courseTable;
    }

    public int[][] getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int[][] rowSpan) {
        this.rowSpan = rowSpan;
    }
}
