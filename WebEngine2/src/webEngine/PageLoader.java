package webEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class PageLoader {
	
	private static final String basePath = "base/";
	private static final String baseCSSPath = basePath + "style/";
	private static final String baseJSPath = basePath + "script/";
	
	public static String getPage(String path) {
		String page = "";
		if(!path.contains(".html")) {
			path = path + ".html";
		}
		try {
			page = loadFile(basePath + path);
		} catch(IOException e) {
			System.out.println("Encounted error loading page \"" + path + "\" on path \"" + basePath + path + "\"");
			e.printStackTrace();
			if(!path.equals("default.html")) {
				page = getDefaultPage("Failed to load \""+path+"\"");
			} else {
				page = "<!DOCTYPE html><html><h1>Somthing went wrong loading the default page</h1><h3>Please contact the server owner</h3></html>";
			}
		}
		
		return page;
	}
	public static String getCSS(String path) {
		String page = "";
		if(!path.contains(".css")) {
			path = path + ".css";
		}
		try {
			page = loadFile(baseCSSPath + path);
		} catch(IOException e) {
			System.out.println("Encounted error loading stylesheet \"" + path + "\" on path \"" + baseCSSPath + path + "\"");
			e.printStackTrace();
		}
		
		return page;
	}
	public static String getJS(String path) {
		String page = "";
		if(!path.contains(".js")) {
			path = path + ".js";
		}
		try {
			page = loadFile(baseJSPath + path);
		} catch(IOException e) {
			System.out.println("Encounted error loading script \"" + path + "\" on path \"" + baseJSPath + path + "\"");
			e.printStackTrace();
		}
		
		return page;
	}
	
	public static String loadFile(String path) throws FileNotFoundException {
		String file = "";
		Scanner scan = new Scanner(new File(path));
		while(scan.hasNextLine()) {
			file += scan.nextLine();
			if(scan.hasNextLine()) {
				file += "\n";
			}
		}
		return file;
	}
	public static String getDefaultPage(String msg) {
		String page = getPage("default.html");
		return page.replaceAll("%Message%", msg);
	}
	
}
