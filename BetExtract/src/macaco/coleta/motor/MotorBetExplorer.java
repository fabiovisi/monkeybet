package macaco.coleta.motor;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import macaco.coleta.bean.Partida;
import macaco.coleta.negocio.ManipulaOdds;
import macaco.coleta.tipo.DriverEnum;

public class MotorBetExplorer {
	
	private static WebDriverWait wait;
	private static WebDriver driver;
	
	static final DriverEnum CHROME = DriverEnum.CHROME;
	static final DriverEnum GECKO = DriverEnum.GECKO;
	
	private static void configuraDriver(DriverEnum driverEnum) {
		
		if(driverEnum.equals(DriverEnum.CHROME)) {
			System.setProperty("webdriver.chrome.driver", "C:\\BET\\driver\\chromedriver.exe");
			
			driver = new ChromeDriver();
			wait = new WebDriverWait(driver, 10);
		}
		
		if(driverEnum.equals(DriverEnum.GECKO)) {
			System.setProperty("webdriver.gecko.driver", "D:\\DEV\\geckodriver.exe");
			
			driver = new FirefoxDriver();
			wait = new WebDriverWait(driver, 10);
		}

	}
	
	public static List<Partida> carregarJogos() {
		
		configuraDriver(CHROME);
		
		driver.get("https://www.betexplorer.com/odds-filter/soccer/");
		
		try {
			// seleciona o node da liga
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//table[@class='table-main js-tablebanner-t' or @class='table-main js-nrbanner-t']")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		WebElement weLiga = driver.findElement(By.xpath("//table[@class='table-main js-tablebanner-t' or @class='table-main js-nrbanner-t']"));
		
		List<WebElement> weEquipes = new ArrayList<WebElement>();
		List<WebElement> weHorarios = new ArrayList<WebElement>();
		List<WebElement> weOdds1 = new ArrayList<WebElement>();
		List<WebElement> weOddsx = new ArrayList<WebElement>();
		List<WebElement> weOdds2 = new ArrayList<WebElement>();
		
		weEquipes = weLiga.findElements(By.xpath("//td[@class='table-main__tt']//a"));
		weHorarios = weLiga.findElements(By.xpath("//td[@class='table-main__tt']//span[@class='table-main__time']"));
		weOdds1 = weLiga.findElements(By.xpath("//td[@class='table-main__odds' or @class='table-main__odds fav-odd'][1]//a"));
		weOddsx = weLiga.findElements(By.xpath("//td[@class='table-main__odds' or @class='table-main__odds fav-odd'][2]//a"));
		weOdds2 = weLiga.findElements(By.xpath("//td[@class='table-main__odds' or @class='table-main__odds fav-odd'][3]//a"));
		
		// lista de partidas
		List<Partida> partidas = new ArrayList<Partida>();
		Partida partida = null;
		
		Float odd1;
		Float odd2;

		// total de jogos na liga
		int totalJogos = weEquipes.size();
		
		for (int i = 0; i < totalJogos; i++) {
			
			partida = new Partida();
			odd1 = Float.parseFloat(weOdds1.get(i).getText());
			odd2 = Float.parseFloat(weOdds2.get(i).getText());
			
			partida.setHora(weHorarios.get(i).getText());
			partida.setDescricao(weEquipes.get(i).getText());
			partida.setOdd1(weOdds1.get(i).getText());
			partida.setOddx(weOddsx.get(i).getText());
			partida.setOdd2(weOdds2.get(i).getText());
			partida.setDupla(ManipulaOdds.duplaHipotese(odd1, odd2));
			
			partidas.add(partida);
			
		}
		
		driver.quit();
		
		return partidas;
		
	}
}
