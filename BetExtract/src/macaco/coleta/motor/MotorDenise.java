package macaco.coleta.motor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import macaco.coleta.bean.Liga;
import macaco.coleta.bean.Partida;
import macaco.coleta.negocio.ManipulaOdds;
import macaco.coleta.negocio.ManipulaProperties;

public class MotorDenise {

	// data de referencia
	private static String dataExtracao;

	// URL Denise
	private static String urlLigas;
	private static String urlBusca;
	private static String urlDireta;

	public static String getUrlDireta() {
		return urlDireta;
	}

	public static void setUrlDireta(String urlDireta) {
		MotorDenise.urlDireta = urlDireta;
	}

	// carrega o node do nome das ligas
	static final String NOME_LIGA = "//div[@class='suf-CompetitionMarketGroupButton_Text ']";
	// ufm-MarketGroupButtonUpcomingCompetition_Text
	// suf-CompetitionMarketGroupButton_Text

	// navegacao no site
	static final String CAMPO_BUSCA = "//input[@class='sml-SearchTextInput ']";
	static final String FECHAR_BUSCA = "//div[@class='sml-SearchHeader_CloseButton ']";
	static final String GRUPO_LIGA_JOGOS = "//div[@class='ssm-SiteSearchTextHeaderMarketGroupButton_Description ']";

	// pre-montagem da lista
	static final String EQUIPES = "(//div[@class='ssm-SiteSearchNameMarket gl-Market_General gl-Market_General-topborder gl-Market_General-pwidth50 '])[1]/div[@class='ssm-SiteSearchLabelOnlyParticipant gl-Market_General-cn1 ' and ./span[contains(@class,'ssm-SiteSearchLabelOnlyParticipant_Name')]]";
	static final String HORARIOS = "(//div[@class='ssm-SiteSearchNameMarket gl-Market_General gl-Market_General-topborder gl-Market_General-pwidth50 '])[1]/div[@class='ssm-SiteSearchLabelOnlyParticipant gl-Market_General-cn1 ']/div[@class='ssm-SiteSearchLabelOnlyParticipant_StartTime ' or @class='ssm-SiteSearchLabelOnlyParticipant_InplayRow']";
	static final String ODD1 = "(//div[@class='ssm-SiteSearchOddsMarketWidth16 gl-Market_General gl-Market_General-topborder gl-Market_General-pwidth16-6 '])[1]/div[@class='ssm-SiteSearchOddsOnlyParticipant gl-Participant_General gl-Market_General-cn1 ']/div[@class='ssm-SiteSearchOddsOnlyParticipant_Wrapper ' and ./span[contains(@class,'ssm-SiteSearchOddsOnlyParticipant_OddsNoHandicap')]]";
	static final String ODDX = "(//div[@class='ssm-SiteSearchOddsMarketWidth16 gl-Market_General gl-Market_General-topborder gl-Market_General-pwidth16-6 '])[2]/div[@class='ssm-SiteSearchOddsOnlyParticipant gl-Participant_General gl-Market_General-cn1 ']/div[@class='ssm-SiteSearchOddsOnlyParticipant_Wrapper ' and ./span[contains(@class,'ssm-SiteSearchOddsOnlyParticipant_OddsNoHandicap')]]";
	static final String ODD2 = "(//div[@class='ssm-SiteSearchOddsMarketWidth16 gl-Market_General gl-Market_General-topborder gl-Market_General-pwidth16-6 '])[3]/div[@class='ssm-SiteSearchOddsOnlyParticipant gl-Participant_General gl-Market_General-cn1 ']/div[@class='ssm-SiteSearchOddsOnlyParticipant_Wrapper ' and ./span[contains(@class,'ssm-SiteSearchOddsOnlyParticipant_OddsNoHandicap')]]";

	private static WebDriver driver = null;
	private static WebDriverWait wait = null;

	// instancia o campo de busca
	private static WebElement weCampoBusca = null;

	public static void configuraBet() throws IOException {

		Properties prop = ManipulaProperties.getProp();

		setUrlLigas(prop.getProperty("bet.url.ligas"));
		setUrlBusca(prop.getProperty("bet.url.busca"));
		setDataExtracao(prop.getProperty("bet.data"));

		// System.setProperty("webdriver.chrome.driver", "D:\\DEV\\chromedriver.exe");
		System.setProperty("webdriver.gecko.driver", "D:\\DEV\\geckodriver.exe");
	}

	public static void conectar() {
		// driver = new ChromeDriver();
		driver = new FirefoxDriver();
		wait = new WebDriverWait(driver, 10);

	}

	public static void desconectar() {
		driver.quit();
	}

	public static ArrayList<String> carregarLigas() {

		driver.get(getUrlLigas());

		// aguarda a lista de jogos por liga
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(NOME_LIGA)));

		// carrega as ligas
		List<WebElement> weLigas = driver.findElements(By.xpath(NOME_LIGA));

		// popula array com as ligas
		ArrayList<String> listaLigas = new ArrayList<String>();
		for (WebElement weLiga : weLigas) {
			listaLigas.add(weLiga.getText());
		}

		return listaLigas;
	}

	public static Liga carregarJogosPorLiga(String LigaUnica) {

		driver.get(getUrlBusca());

		Liga liga = null;

		// aguarda a lista de jogos por liga
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CAMPO_BUSCA)));

		// instancia o campo de busca
		weCampoBusca = driver.findElement(By.xpath(CAMPO_BUSCA));

		List<WebElement> weEquipes = new ArrayList<WebElement>();
		List<WebElement> weHorarios = new ArrayList<WebElement>();
		List<WebElement> weOdds1 = new ArrayList<WebElement>();
		List<WebElement> weOddsx = new ArrayList<WebElement>();
		List<WebElement> weOdds2 = new ArrayList<WebElement>();

		Float odd1;
		Float oddx;
		Float odd2;

		List<Partida> partidas = null;
		Partida partida = null;

		if (LigaUnica != null) {

			liga = new Liga();
			liga.setNomeLiga(LigaUnica);
			liga.setProcessada(true);

			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			weCampoBusca.clear();
			weCampoBusca.sendKeys(LigaUnica);

			try {

				// aguarda carregar os componentes
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(GRUPO_LIGA_JOGOS)));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(EQUIPES)));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ODD1)));

				// seleciona o node da liga
				WebElement weLiga = driver.findElement(By.xpath(
						"//div[@class='ssm-SiteSearchTextHeaderMarketGroup ']//div[@class='ssm-SiteSearchTextHeaderMarketGroupButton_Description '] | //div[text()='"
								+ LigaUnica + "']"));

				weEquipes = weLiga.findElements(By.xpath(EQUIPES));
				weHorarios = weLiga.findElements(By.xpath(HORARIOS));
				weOdds1 = weLiga.findElements(By.xpath(ODD1));
				weOddsx = weLiga.findElements(By.xpath(ODDX));
				weOdds2 = weLiga.findElements(By.xpath(ODD2));

				// lista de partidas
				partidas = new ArrayList<Partida>();

				// total de jogos na liga
				int totalJogos = weEquipes.size();

				for (int i = 0; i < totalJogos; i++) {

					partida = new Partida();

					String horario = weHorarios.get(i).getText();

					if (horario != null && horario.contains(dataExtracao)) {

						odd1 = Float.parseFloat(weOdds1.get(i).getText());
						oddx = Float.parseFloat(weOddsx.get(i).getText());
						odd2 = Float.parseFloat(weOdds2.get(i).getText());

						if (ManipulaOdds.bomEmpate(odd1, oddx, odd2)) {

							partida.setData(horario.substring(0, 10).trim());
							partida.setHora(horario.substring(10).trim());
							partida.setDescricao(removeDataDoNome(weEquipes.get(i).getText()));
							partida.setOdd1(weOdds1.get(i).getText());
							partida.setOddx(weOddsx.get(i).getText());
							partida.setOdd2(weOdds2.get(i).getText());
							partida.setDupla(ManipulaOdds.duplaHipotese(odd1, odd2));

							partidas.add(partida);
							liga.setPartidas(partidas);
						}

					}

				}

			} catch (Exception e) {
				// e.printStackTrace();
				liga.setPartidas(null);
				liga.setProcessada(false);
				return liga;
			}
		}

		return liga;
	}

	public static Liga carregarJogosUrlDireta(String LigaUnica) {

		driver.get(getUrlDireta());

		Liga liga = null;

		List<WebElement> weEquipes = new ArrayList<WebElement>();
		List<WebElement> weHorarios = new ArrayList<WebElement>();
		List<WebElement> weOdds1 = new ArrayList<WebElement>();
		List<WebElement> weOddsx = new ArrayList<WebElement>();
		List<WebElement> weOdds2 = new ArrayList<WebElement>();

		Float odd1;
		Float oddx;
		Float odd2;

		List<Partida> partidas = null;
		Partida partida = null;

		if (LigaUnica != null) {

			liga = new Liga();
			liga.setNomeLiga(LigaUnica);
			liga.setProcessada(true);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			try {

				// aguarda carregar os componentes
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(GRUPO_LIGA_JOGOS)));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(EQUIPES)));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ODD1)));

				// seleciona o node da liga
				WebElement weLiga = driver.findElement(By.xpath(
						"//div[@class='ssm-SiteSearchTextHeaderMarketGroup ']//div[@class='ssm-SiteSearchTextHeaderMarketGroupButton_Description '] | //div[text()='"
								+ LigaUnica + "']"));

				weEquipes = weLiga.findElements(By.xpath(EQUIPES));
				weHorarios = weLiga.findElements(By.xpath(HORARIOS));
				weOdds1 = weLiga.findElements(By.xpath(ODD1));
				weOddsx = weLiga.findElements(By.xpath(ODDX));
				weOdds2 = weLiga.findElements(By.xpath(ODD2));

				// lista de partidas
				partidas = new ArrayList<Partida>();

				// total de jogos na liga
				int totalJogos = weEquipes.size();

				for (int i = 0; i < totalJogos; i++) {

					partida = new Partida();

					String horario = weHorarios.get(i).getText();

					if (horario != null && horario.contains(dataExtracao)) {

						odd1 = Float.parseFloat(weOdds1.get(i).getText());
						oddx = Float.parseFloat(weOddsx.get(i).getText());
						odd2 = Float.parseFloat(weOdds2.get(i).getText());

						if (ManipulaOdds.bomEmpate(odd1, oddx, odd2)) {

							partida.setData(horario.substring(0, 10).trim());
							partida.setHora(horario.substring(10).trim());
							partida.setDescricao(removeDataDoNome(weEquipes.get(i).getText()));
							partida.setOdd1(weOdds1.get(i).getText());
							partida.setOddx(weOddsx.get(i).getText());
							partida.setOdd2(weOdds2.get(i).getText());
							partida.setDupla(ManipulaOdds.duplaHipotese(odd1, odd2));

							partidas.add(partida);
							liga.setPartidas(partidas);
						}

					}

				}

			} catch (Exception e) {
				// e.printStackTrace();
				liga.setPartidas(null);
				liga.setProcessada(false);
				return liga;
			}
		}

		return liga;
	}

	private static String removeDataDoNome(String descricaoPartida) {

		int tamanhoData = 16;
		int tamanho = descricaoPartida.length();

		return descricaoPartida.substring(0, tamanho - tamanhoData).trim();

	}	

	public static String getDataExtracao() {
		return dataExtracao;
	}

	public static void setDataExtracao(String dataExtracao) {
		MotorDenise.dataExtracao = dataExtracao;
	}

	public static String getUrlLigas() {
		return urlLigas;
	}

	public static void setUrlLigas(String urlLigas) {
		MotorDenise.urlLigas = urlLigas;
	}

	public static String getUrlBusca() {
		return urlBusca;
	}

	public static void setUrlBusca(String urlBusca) {
		MotorDenise.urlBusca = urlBusca;
	}
}
