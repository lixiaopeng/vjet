package vjo.java.sun.misc;

//Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://www.kpdus.com/jad.html
//Decompiler options: packimports(3) radix(10) lradix(10) 
//Source File Name:   DoubleConsts.java


public class DoubleConsts {

	private DoubleConsts() {
	}

	public static final double POSITIVE_INFINITY = (1.0D / 0.0D);

	public static final double NEGATIVE_INFINITY = (-1.0D / 0.0D);

	public static final double NaN = (0.0D / 0.0D);

	public static final double MAX_VALUE = 1.7976931348623157E+308D;

	public static final double MIN_VALUE = 4.9406564584124654E-324D;

	public static final double MIN_NORMAL = 2.2250738585072014E-308D;

	public static final int SIGNIFICAND_WIDTH = 53;

	public static final int MAX_EXPONENT = 1023;

	public static final int MIN_EXPONENT = -1022;

	public static final int MIN_SUB_EXPONENT = -1074;

	public static final int EXP_BIAS = 1023;

	public static final long SIGN_BIT_MASK = -9223372036854775808L;

	public static final long EXP_BIT_MASK = 9218868437227405312L;

	public static final long SIGNIF_BIT_MASK = 4503599627370495L;

}


/*
	DECOMPILATION REPORT

	Decompiled from: C:\opt\java5-ibm-2007-12-13\jre\lib\core.jar
	Total time: 0 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/
