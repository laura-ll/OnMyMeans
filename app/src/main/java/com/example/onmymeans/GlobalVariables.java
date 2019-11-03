package com.example.onmymeans;


public class GlobalVariables {

    private static String Date = "";
    private static boolean mHasDot = false;
    private static String mInputMoney = "";
    private static String mDescription = "";
    private static int mBookId = 1;
    private static int mBookPos = 0;

    public static void setDate(String date)      { Date = date;     }
    public static void setHasDot(boolean hasDot)  { mHasDot = hasDot; }
    public static void setmInputMoney(String a)   { mInputMoney = a;  }
    public static void setmDescription(String a ) { mDescription = a; }
    public static void setmBookId(int id)         { mBookId = id;     }
    public static void setmBookPos(int pos)       { mBookPos = pos;   }

    public static String getDate()        { return Date;        }
    public static boolean getmHasDot()     { return mHasDot;      }
    public static String getmInputMoney()  { return mInputMoney;  }
    public static String getmDescription() { return mDescription; }
    public static int getmBookId()         { return mBookId;      }
    public static int getmBookPos()        { return mBookPos;     }
}
