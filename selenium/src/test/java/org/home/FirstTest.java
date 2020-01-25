package org.home;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstTest
{
    @Test
    public void test1() throws IOException {

        WebDriver driver = new ChromeDriver();
        String wikiUrl = "https://en.wikipedia.org";
        String currentLine;

        String fileName = System.getProperty("fileName");

        driver.get(wikiUrl);

        FileInputStream fstream = new FileInputStream(fileName);
        DataInputStream fileInput = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));

        while ((currentLine = br.readLine()) != null)   {
            String nameToSearch = currentLine.replace("_", " ");
            WebElement inputField = driver.findElement(By.id("searchInput"));
            WebElement searchButton = driver.findElement(By.id("searchButton"));

            ClearInputField(inputField);

            inputField.sendKeys(nameToSearch);
            searchButton.click();

            String allString = GetString(driver);

            String dates = GetDates(allString);
            System.out.println(currentLine + dates);
        }
        fileInput.close();
        driver.quit();
}
        private static void ClearInputField(WebElement inputField){
            inputField.sendKeys(Keys.CONTROL + "a");
            inputField.sendKeys(Keys.BACK_SPACE);
        }

        private static String GetString(WebDriver driver){
            WebElement dates = driver.findElement(By.xpath("//table[starts-with(@class, 'infobox')]/following-sibling::p[1]"));
            return dates.getText();
        }

        private static String GetDates(String inputString){
            String pattern = "\\d{1,2} (January|February|March|April|May|June|July|August|September|October|November|December) \\d{4} " +
                    "(\\-|\\–) \\d{1,2} (January|February|March|April|May|June|July|August|September|October|November|December) \\d{4}" +
                    "|(January|February|March|April|May|June|July|August|September|October|November|December) \\d{1,2}, \\d{4} " +
                    "(\\-|\\–) (January|February|March|April|May|June|July|August|September|October|November|December) \\d{1,2}, \\d{4}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(inputString);
            if(m.find()) return ": " + m.group(0).replace("\\–", "\\till");
            else return ": Could not found the entire date format.";
        }
}

