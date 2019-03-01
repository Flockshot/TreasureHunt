package me.flockshot.treasurehunt.utils;

import java.text.NumberFormat;
import java.text.ParsePosition;


public class NumberUtility
{
	public static boolean isNum(String str)
	{
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(str, pos);
		return str.length() == pos.getIndex();
	}
}
