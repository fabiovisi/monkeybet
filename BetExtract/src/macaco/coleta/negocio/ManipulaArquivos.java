package macaco.coleta.negocio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimeZone;

import macaco.coleta.bean.Equilibrio;
import macaco.coleta.bean.Liga;
import macaco.coleta.bean.Partida;

public class ManipulaArquivos {
	
	public static ArrayList<String> lerArquivoLigas() {

		ArrayList<String> ligas = new ArrayList<String>();

		String fileName = "C:\\BET\\Ligas.txt";

		File file = new File(fileName);
		Scanner scanner;

		try {

			if (file.exists()) {

				scanner = new Scanner(file, "UTF8");
				scanner.useDelimiter("line.separator");

				while (scanner.hasNextLine()) {
					String liga = (String) scanner.nextLine();
					ligas.add(liga);
				}

				ligas.trimToSize();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return ligas;
	}

	public static void salvarArquivo(Liga ligaBet) throws IOException {

		String nomeArquivo = "lista_" + pegarNomeArquivo() + ".txt";
		
		File dir = new File("C:\\BET");
		File arq = new File(dir, nomeArquivo);

		arq.createNewFile();

		FileWriter fileWriter = new FileWriter(arq, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);

		String risco;

		if (ligaBet != null) {

			if (ligaBet.getPartidas() != null) {

				for (Partida partida : ligaBet.getPartidas()) {

					risco = ManipulaOdds.calculaEquilibrioInverso(partida.getOdd1(), partida.getOddx(), partida.getOdd2());
					
					printWriter.println(risco + ";" + partida.getDupla() + ";" + partida.getOdd1() + ";" + partida.getOddx() + ";"
									+ partida.getOdd2() + ";" + partida.getHora() + ";" + partida.getDescricao());

				}
			}
		}

		printWriter.flush();
		printWriter.close();

	}


	public static void salvarLigas(ArrayList<String> todasLigas) throws IOException {

		File dir = new File("C:\\BET");
		File arq = new File(dir, "ligas_" + pegarNomeArquivo() + ".txt");

		arq.createNewFile();

		FileWriter fileWriter = new FileWriter(arq, false);
		PrintWriter printWriter = new PrintWriter(fileWriter);

		printWriter.println("LIGAS");
		printWriter.println("----------------------------------------");
		for (int i = 0; i < todasLigas.size(); i++) {
			printWriter.println(todasLigas.get(i).toString());
		}

		printWriter.flush();
		printWriter.close();

	}

	public static void salvarErro(String ligaErro) throws IOException {
		
		File dir = new File("C:\\BET");
		File arq = new File(dir, "erros_" + pegarNomeArquivo() + ".txt");

		arq.createNewFile();

		FileWriter fileWriter = new FileWriter(arq, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);

		printWriter.println(ligaErro);

		printWriter.flush();
		printWriter.close();

	}

	public static void salvarArquivo(List<Partida> partidas) throws IOException, ParseException {
		
		String nomeArquivo = "lista_" + pegarNomeArquivo() + ".txt";
		File dir = new File("C:\\BET");
		File arq = new File(dir, nomeArquivo);

		arq.createNewFile();

		FileWriter fileWriter = new FileWriter(arq, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		
		Equilibrio eq = null; 
		
		for (Partida partida : partidas) {

			eq = new Equilibrio();
			eq = ManipulaOdds.calculaEquilibrio(partida.getOdd1(), partida.getOddx(), partida.getOdd2());

			DateFormat sdf = new SimpleDateFormat("HH:mm");

			Date date = sdf.parse(partida.getHora());
			sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));

			printWriter.println(sdf.format(date) + ";" + partida.getDescricao() + ";" + partida.getOdd1() + ";"
					+ partida.getOddx() + ";" + partida.getOdd2() + ";" + partida.getDupla() + ";" + eq.getDiferença()
					+ ";" + eq.getEquilibrio() + ";" + partida.getBanca() + ";" + partida.getPalpite());
		}
		
		printWriter.flush();
		printWriter.close();
		
	}
	
	private static String pegarNomeArquivo() throws IOException {
		
		Properties prop = ManipulaProperties.getProp();
		String nomeArquivo = prop.getProperty("bet.data").toLowerCase().replaceAll(" ", "_");
		
		return nomeArquivo;
		
	}

	
}
