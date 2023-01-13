package macaco.coleta.motor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import macaco.coleta.bean.Partida;
import macaco.coleta.negocio.ManipulaOdds;
import macaco.coleta.tipo.DiaColetaEnum;
import macaco.coleta.tipo.DriverEnum;

public class MotorFlashScore {

	private static WebDriverWait wait;
	private static WebDriver driver;

	static final DriverEnum CHROME = DriverEnum.CHROME;
	static final DriverEnum GECKO = DriverEnum.GECKO;
	
	static final Logger LOG = Logger.getLogger(MotorFlashScore.class.getName());
	
	private static void configuraDriver(DriverEnum driverEnum) {
		
		if (driverEnum.equals(DriverEnum.CHROME)) {
			System.setProperty("webdriver.chrome.driver", "C:\\BET\\driver\\chromedriver.exe");

			driver = new ChromeDriver();
			wait = new WebDriverWait(driver, 20);
		}

		if (driverEnum.equals(DriverEnum.GECKO)) {
			System.setProperty("webdriver.gecko.driver", "D:\\DEV\\geckodriver.exe");

			driver = new FirefoxDriver();
			wait = new WebDriverWait(driver, 20);
		}

	}

	public static List<Partida> carregarJogosEmpate() {

		System.out.println("Iniciando carregamento dos jogos");

		configuraDriver(CHROME);

		driver.manage().window().maximize();
		driver.manage().window().setPosition(new org.openqa.selenium.Point(0, -2000));
		driver.get("https://www.flashscore.com/");

		// selecionar aba odds do site
		// ABA ODDS INÍCIO -----------------------------
		WebElement abaOdds = driver
				.findElement(By.xpath("//div[@class='tabs__text tabs__text--default'] | //div[text()='Odds']"));

		try {
			wait.until(ExpectedConditions.elementToBeClickable(abaOdds));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abaOdds.click();
		}

		// ABA ODDS FIM ------------------------------

		// selecionar eventos do proximo dia
		// NEXT DAY INÍCIO -----------------------------
		WebElement nextDay = driver
				.findElement(By.xpath("//div[@class='calendar__navigation calendar__navigation--tomorrow']"));

		try {
			// seleciona o node da liga
			wait.until(ExpectedConditions.elementToBeClickable(nextDay));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			nextDay.click();
		}

		// NEXT DAY FIM --------------------------------

		try {
			Thread.sleep(1000);
			// Aguarda o carregamento de todos os elementos do container
			wait.until(
					ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='sportName soccer']")));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Carrega o container com todos os jogos
		WebElement tabelaJogos = driver.findElement(By.xpath("//div[@class='sportName soccer']"));

		try {
			// Aguarda o carregamento de todos os jogos
			wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'event__match ')]")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Instancia os jogos
		List<WebElement> jogos = tabelaJogos.findElements(By.xpath("//div[contains(@class,'event__match ')]"));

		List<Partida> partidas = new ArrayList<Partida>();
		Partida partida = null;

		Calendar calendario = Calendar.getInstance();
		calendario.add(Calendar.DAY_OF_YEAR, 1);
		Date amanha = calendario.getTime();
		SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

		String dataJogo = formatDate.format(amanha).toString();
		String horario = null;
		String casa = null;
		String visitante = null;

		String strOdd1 = null;
		String strOddx = null;
		String strOdd2 = null;

		Float odd1;
		Float oddx;
		Float odd2;

		for (WebElement jogo : jogos) {

			partida = new Partida();
			partida.setData(dataJogo);

			try {
				wait.until(ExpectedConditions.visibilityOf(jogo.findElement(By.className("event__time"))));
				wait.until(ExpectedConditions.visibilityOf(jogo.findElement(By.className("event__participant--home"))));
				wait.until(ExpectedConditions.visibilityOf(jogo.findElement(By.className("event__participant--away"))));
				wait.until(ExpectedConditions.visibilityOf(jogo.findElement(By.className("event__odd--odd1"))));
				wait.until(ExpectedConditions.visibilityOf(jogo.findElement(By.className("event__odd--odd2"))));
				wait.until(ExpectedConditions.visibilityOf(jogo.findElement(By.className("event__odd--odd3"))));

				horario = jogo.findElement(By.className("event__time")).getText();
				horario = horario.substring(0, 5).trim();

				casa = jogo.findElement(By.className("event__participant--home")).getText();
				visitante = jogo.findElement(By.className("event__participant--away")).getText();
				strOdd1 = jogo.findElement(By.className("event__odd--odd1")).getText();
				strOddx = jogo.findElement(By.className("event__odd--odd2")).getText();
				strOdd2 = jogo.findElement(By.className("event__odd--odd3")).getText();

				if (strOdd1 != null && !strOdd1.equals("-")) {

					odd1 = Float.parseFloat(strOdd1);
					oddx = Float.parseFloat(strOddx);
					odd2 = Float.parseFloat(strOdd2);
					
					if (ManipulaOdds.bomEmpate(odd1, oddx, odd2)) {

						String codBanca = jogo.findElement(By.className("event__odd--odd1"))
								.getAttribute("data-bookmaker-id");
						String banca = "";

						if (codBanca != null) {
							switch (codBanca) {
							case "5":
								banca = "UNIBET";
								break;
							case "16":
								banca = "bet365";
								break;
							case "417":
								banca = "1XBET";
								break;
							}
						}

						partida.setBanca(banca);
						partida.setHora(horario);
						partida.setDescricao(casa + " x " + visitante);
						partida.setOdd1(strOdd1);
						partida.setOddx(strOddx);
						partida.setOdd2(strOdd2);
						partida.setDupla(ManipulaOdds.duplaHipotese(odd1, odd2));

						partidas.add(partida);
					}
				}

			} catch (Exception e) {
				System.out.println("Erro: " + casa + " x " + visitante);
			}
		}

		System.out.println("Retornando lista de jogos");

		driver.quit();
		return partidas;
	}

	public static List<Partida> carregarTodosJogos(DiaColetaEnum diaExtracao) {
		
		System.out.println("Iniciando carregamento dos jogos");

		configuraDriver(CHROME);

		driver.manage().window().maximize();
		driver.manage().window().setPosition(new org.openqa.selenium.Point(0, -2000));
		driver.get("https://www.flashscore.com/");

		// selecionar aba odds do site
		// ABA ODDS INÍCIO -----------------------------
		WebElement abaOdds = driver
				.findElement(By.xpath("//div[@class='tabs__text tabs__text--default'] | //div[text()='Odds']"));

		try {
			wait.until(ExpectedConditions.elementToBeClickable(abaOdds));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abaOdds.click();
		}

		// ABA ODDS FIM ------------------------------

		// selecionar eventos do proximo dia
		
		if (diaExtracao == DiaColetaEnum.AMANHA) {

			// NEXT DAY INÍCIO -----------------------------
			WebElement nextDay = driver
					.findElement(By.xpath("//div[@class='calendar__navigation calendar__navigation--tomorrow']"));

			try {
				// seleciona o node da liga
				wait.until(ExpectedConditions.elementToBeClickable(nextDay));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				nextDay.click();
			}

			// NEXT DAY FIM --------------------------------

		}

		try {
			Thread.sleep(1000);
			// Aguarda o carregamento de todos os elementos do container
			wait.until(
					ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='sportName soccer']")));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Carrega o container com todos os jogos
		WebElement tabelaJogos = driver.findElement(By.xpath("//div[@class='sportName soccer']"));

		try {
			// Aguarda o carregamento de todos os jogos
			wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'event__match ')]")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Instancia os jogos
		List<WebElement> jogos = tabelaJogos.findElements(By.xpath("//div[contains(@class,'event__match ')]"));

		List<Partida> partidas = new ArrayList<Partida>();
		Partida partida = null;

		Calendar calendario = Calendar.getInstance();
		calendario.add(Calendar.DAY_OF_YEAR, 1);
		Date amanha = calendario.getTime();
		SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

		String dataJogo = formatDate.format(amanha).toString();
		String horario = null;
		String casa = null;
		String visitante = null;

		String strOdd1 = null;
		String strOddx = null;
		String strOdd2 = null;

		for (WebElement jogo : jogos) {

			partida = new Partida();
			partida.setData(dataJogo);

			try {
				wait.until(ExpectedConditions.visibilityOf(jogo.findElement(By.className("event__time"))));
				wait.until(ExpectedConditions.visibilityOf(jogo.findElement(By.className("event__participant--home"))));
				wait.until(ExpectedConditions.visibilityOf(jogo.findElement(By.className("event__participant--away"))));
				wait.until(ExpectedConditions.visibilityOf(jogo.findElement(By.className("event__odd--odd1"))));
				wait.until(ExpectedConditions.visibilityOf(jogo.findElement(By.className("event__odd--odd2"))));
				wait.until(ExpectedConditions.visibilityOf(jogo.findElement(By.className("event__odd--odd3"))));

				horario = jogo.findElement(By.className("event__time")).getText();
				horario = horario.substring(0, 5).trim();

				casa = jogo.findElement(By.className("event__participant--home")).getText();
				visitante = jogo.findElement(By.className("event__participant--away")).getText();
				strOdd1 = jogo.findElement(By.className("event__odd--odd1")).getText();
				strOddx = jogo.findElement(By.className("event__odd--odd2")).getText();
				strOdd2 = jogo.findElement(By.className("event__odd--odd3")).getText();

				if (strOdd1 != null && !strOdd1.equals("-")) {

					String codBanca = jogo.findElement(By.className("event__odd--odd1"))
							.getAttribute("data-bookmaker-id");
					String banca = "";

					if (codBanca != null) {
						switch (codBanca) {
						case "5":
							banca = "UNIBET";
							break;
						case "16":
							banca = "bet365";
							break;
						case "417":
							banca = "1XBET";
							break;
						}
					}

					partida.setBanca(banca);
					partida.setHora(horario);
					partida.setDescricao(casa + " x " + visitante);
					partida.setOdd1(strOdd1);
					partida.setOddx(strOddx);
					partida.setOdd2(strOdd2);					

					partidas.add(partida);
				}
				
				LOG.log(Level.INFO, "coletado: " + casa + " x " + visitante);

			} catch (Exception e) {
				LOG.log(Level.WARNING, "falha: " + casa + " x " + visitante);
			}
		}

		System.out.println("Finalizando lista de jogos");

		driver.quit();
		return partidas;
	}
}
