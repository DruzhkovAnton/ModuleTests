package edu.innotech;

public class Starter {
    public static void main(String[] args){
        System.out.println("hellow world");
        Student st = new Student("pete");
        st.addGrade(5);
        System.out.println(st);
    }
}

