package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BadConfigFormatException extends Exception{
	/*
	 * default exception without parameters that writes to log (invalid dimensions)
	 */
	public BadConfigFormatException() {
		super("Board does not have the same number of columns per row.");
		System.err.println(this.getMessage());
		PrintWriter out;
		try {
			out = new PrintWriter("logfile.txt");
			out.println("Board does not have the same number of columns per row.");
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * exception with one error parameter that writes to log (invalid file format)
	 */
	public BadConfigFormatException(String error) {
		super(error);
		System.err.println(this.getMessage());
		PrintWriter out;
		try {
			out = new PrintWriter("logfile.txt");
			out.println(error);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
