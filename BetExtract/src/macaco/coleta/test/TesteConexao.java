package macaco.coleta.test;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TesteConexao {

	private static WebDriverWait wait;
	private static WebDriver driver;

	public static void main(String[] args) throws IOException {
		
		driver = null;
		wait = null;
		
		//System.setProperty("webdriver.gecko.driver", "C:\\BET\\driver\\geckodriver.exe");
		System.setProperty("webdriver.chrome.driver", "C:\\BET\\driver\\chromedriver.exe");
		
		driver = new ChromeDriver();
		//driver = new FirefoxDriver();
		wait = new WebDriverWait(driver, 10);

		//driver.get("https://www.betexplorer.com/odds-filter/soccer/");
		//driver.get("https://www.bet365.com/#/HO/");
		driver.get("https://www.flashscore.com/");
		
		//tabs__text tabs__text--default
	}
}
