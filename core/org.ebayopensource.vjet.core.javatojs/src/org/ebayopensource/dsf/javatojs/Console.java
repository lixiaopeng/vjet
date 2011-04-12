/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.javatojs;

/**
 * Provides basic Console capabilities like reading in a String or number.
 */
public class Console { 
	/**
	* print a prompt on the console but don't print a newline
	* @param prompt the prompt string to display
	*/
	public static void printPrompt(String prompt) {
		System.out.flush();
	}
	/**
	* read a string from the console. The string is 
	* terminated by a newline
	* @return the input string (without the newline)
	*/
	public static String readString() {
		int ch;
		String r = "";
		boolean done = false;
		while (!done) {
			try {
				ch = System.in.read();
				if (ch < 0 || (char) ch == '\n')
					done = true;
				else
					r = r + (char) ch;
			}
			catch (java.io.IOException e) {
				done = true;
			}
		}
		return r;
	}
	
	/**
	* read a string from the console. The string is 
	* terminated by a newline
	* @param prompt the prompt string to display
	* @return the input string (without the newline)
	*/
	public static String readString(String prompt) {
		printPrompt(prompt);
		return readString();
	}
	/**
	* read a word from the console. The word is 
	* any set of characters terminated by whitespace
	* @return the 'word' entered
	*/
	public static String readWord() {
		int ch;
		String r = "";
		boolean done = false;
		while (!done) {
			try {
				ch = System.in.read();
				if (ch < 0 || Character.isWhitespace((char) ch))
					done = true;
				else
					r = r + (char) ch;
			}
			catch (java.io.IOException e) {
				done = true;
			}
		}
		return r;
	}
	/**
	* read an integer from the console. The input is 
	* terminated by a newline
	* @param prompt the prompt string to display
	* @return the input value as an int
	* @exception NumberFormatException if bad input
	*/
	public static int readInt(String prompt) {
		while (true) {
			printPrompt(prompt);
			try {
				return Integer.valueOf(readString().trim()).intValue();
			}
			catch (NumberFormatException e) {
			}
		}
	}
	
	/**
	* read a floating point number from the console. 
	* The input is terminated by a newline
	* @param prompt the prompt string to display
	* @return the input value as a double
	* @exception NumberFormatException if bad input
	*/
	public static double readDouble(String prompt) {
		while (true) {
			printPrompt(prompt);
			try {
				return Double.valueOf(readString().trim()).doubleValue();
			}
			catch (NumberFormatException e) {
			}
		}
	}
	

}
