package Main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {
	
	//The chrome driver
	private static ChromeDriver Chrome1;
	public static void main(String[] args) {
		
		//Setup of the Chrome window
		String path = System.getProperty("user.dir");
		String pathToChromeDriver = path + "/res/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(JOptionPane.showInputDialog("Path to the Chrome exe"));
        Chrome1 = new ChromeDriver(options);
        Chrome1.get(JOptionPane.showInputDialog("Path to the Match history"));
        
        //Get a list of every game played
        List<WebElement> list = Chrome1.findElements(By.className("game-summary"));
        
        //Get a list of every game duration of every game you have ever played
        ArrayList<String> timesPlayed = new ArrayList<String>();
        for(WebElement element: list)
        {
        	timesPlayed.add(element.findElement(By.className("date-duration-duration")).getText());
        }
       
        //Seconds played
        int seconds = 0;
        //This loops determines the time played
        for(String timePlayed: timesPlayed)
        {
        	String s = timePlayed.substring(0, timePlayed.length()-3)+"."+timePlayed.substring(timePlayed.length()-2, timePlayed.length());
        	if(s.contains(":"))
        	{
        		seconds += Integer.parseInt(s.charAt(0) + "") * 3600;
        		s = s.substring(2);
        	}
        	if(s.length() == 5)
        	{
        		seconds += Integer.parseInt(s.substring(0, 2)) * 60;
        		seconds += Integer.parseInt(s.substring(3, 5));
        	}else if(s.length() == 4)
        	{
        		seconds += Integer.parseInt(s.substring(0, 1)) * 60;
        		seconds += Integer.parseInt(s.substring(2, 4));
        	}
        }
        
        //The output of the time played
        try {
        	String seperator = System.getProperty("line.separator");
			IOOut("TimePlayed.txt", "Exact time played(only ingame): " + seconds/3600 + seperator + "Estimated time played(+10 Minutes pregame per game): " + (seconds/3600 + timesPlayed.size()*0.1) + seperator + "\nGames played: " + timesPlayed.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        Chrome1.close();
	}
	
	//Output method (Was to lazy to write a new one so it is a method wich printed objects into a file of another project)
	public static void IOOut(String path, Object content) throws UnsupportedEncodingException, FileNotFoundException, IOException
	{
		ObjectOutputStream OOS = new ObjectOutputStream(new FileOutputStream(path));
		OOS.writeObject(content);
		OOS.close();
	}
}
