package com.aims.model;

public class Conste {
    private static int constValue = 1; // Biến dùng để đối chiếu, giá trị mặc định
    //user  1
    //cart  2
    //order 3
    //admin 4
    //
    //
    public static void changeConst(int a) {
        constValue = a;
    }

    public static int getConst() {
        return constValue;
    }
}
