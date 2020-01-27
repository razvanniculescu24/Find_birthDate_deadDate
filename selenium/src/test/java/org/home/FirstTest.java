package org.home;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstTest
{
    @Test
    public void test1() throws IOException, InterruptedException {

        WebDriver driver = new ChromeDriver();
        String wikiUrl = "https://en.wikipedia.org";
        String currentLine;

        String fileName = System.getProperty("fileName");

        driver.get(wikiUrl);

        FileInputStream fstream = new FileInputStream(fileName);
        DataInputStream fileInput = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
        System.out.println("\n***************************************************");

        while ((currentLine = br.readLine()) != null)   {
            String nameToSearch = currentLine.replace("_", " ");
            WebElement inputField = driver.findElement(By.id("searchInput"));
            WebElement searchButton = driver.findElement(By.id("searchButton"));

            ClearInputField(inputField);

            InputPersonalityString(inputField, nameToSearch);

            searchButton.click();

            String dates = GetDates(GetAllString(driver, nameToSearch));

            System.out.println(currentLine + dates);
        }
        System.out.println("***************************************************\n");
        fileInput.close();
        driver.quit();
}
        private static void ClearInputField(WebElement inputField){
            inputField.click();
            inputField.sendKeys(Keys.CONTROL + "a");
            inputField.sendKeys(Keys.BACK_SPACE);
        }

        private static String GetAllString(WebDriver driver, String nameToSearch) {
         try {
             String locator = "//div[@id= 'content' and contains(., '"+nameToSearch+"')]//div[@class='mw-parser-output']/p[descendant::b][1]";
             WebElement allString = (new WebDriverWait(driver, 4))
                     .until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
             return allString.getText();
         }
         catch (NoSuchElementException e) {
             return "Element not found. ";
         }
        }

        private static String GetDates(String inputString){
            String pattern1 = "\\d{1,2} (January|February|March|April|May|June|July|August|September|October|November|December) \\d{4} " +
                    "(\\-|\\–) \\d{1,2} (January|February|March|April|May|June|July|August|September|October|November|December) \\d{4}" +
                    "|(January|February|March|April|May|June|July|August|September|October|November|December) \\d{1,2}, \\d{4} " +
                    "(\\-|\\–) (January|February|March|April|May|June|July|August|September|October|November|December) \\d{1,2}, \\d{4}";
            String pattern2 = "\\d{1,2} (January|February|March|April|May|June|July|August|September|October|November|December) \\d{4}" +
                    "|(January|February|March|April|May|June|July|August|September|October|November|December) \\d{1,2}, \\d{4}";
            Pattern r1 = Pattern.compile(pattern1);
            Matcher m1 = r1.matcher(inputString);
            Pattern r2 = Pattern.compile(pattern2);
            Matcher m2 = r2.matcher(inputString);
            if(m1.find()) return ": " + m1.group().replace("–", "-");
            if(m2.find()) return ": " + m2.group();
            else return ": Could not find the proper date. ";
        }

        private static void InputPersonalityString(WebElement inputField, String nameToSearch) throws InterruptedException {
            inputField.sendKeys(nameToSearch);
            if(inputField.getCssValue("value") != nameToSearch){
                Thread.sleep(300);  //manual sleep because it seems that after some searches the web page auto refreshes
                ClearInputField(inputField);
                inputField.sendKeys(nameToSearch);
            }
        }
}

