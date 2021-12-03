package com.example.model;

//подборка пастельных цветов: https://awdee.ru/podborka-pastelnyh-tsvetov-ot-color-thisislosko/

import java.io.Serializable;

public enum MyColor implements Serializable {
    COLOR1(0xFFF8F4D1),
    COLOR2(0xFFFED6BC),
    COLOR3(0xFFDEF7FE),
    COLOR4(0xFFE7ECFF),
    COLOR5(0xFFC3FBD8),
    COLOR6(0xFFFDEED9),
    COLOR7(0xFFE5AFEE),
    COLOR8(0xFFdbdad6),
    COLOR9(0xFFB5F2EA),
    COLOR10(0xFF909CAC),
    COLOR11(0xFFecdab9),
    COLOR12(0xFFffa577),
    COLOR13(0xFFa47053),
//    COLOR14(0xFFd55448),
//    COLOR14(0xFFff420e),
    COLOR14(0xFFe74c3c),
    COLOR15(0xFFf9dc24),
    COLOR16(0xFF8eba43),
    ;


    private final int code;

    MyColor(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MyColor get(int num) {
        MyColor[] colors = values();
        return colors[num];
    }
}

