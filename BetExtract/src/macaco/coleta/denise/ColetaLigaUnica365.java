package macaco.coleta.denise;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import macaco.coleta.bean.Liga;
import macaco.coleta.motor.MotorDenise;
import macaco.coleta.negocio.ManipulaArquivos;
import macaco.coleta.negocio.ManipulaProperties;

public class ColetaLigaUnica365 {

	static ArrayList<String> ligas;
	private static Scanner scLiga;

	public static void main(String[] args) throws IOException{
		
		MotorDenise.configuraBet();
		
		scLiga = new Scanner(System.in);
		String ligaInformada;

		System.out.println("Digite a liga: ");
		ligaInformada = scLiga.nextLine();
		
		Properties prop = ManipulaProperties.getProp();
		String urlDireta = prop.getProperty("bet.url.direta");
		
		MotorDenise.setUrlDireta(urlDireta + ligaInformada + "/");

		MotorDenise.conectar();
		Liga liga = MotorDenise.carregarJogosUrlDireta(ligaInformada);
		MotorDenise.desconectar();

		try {
			if (liga.isProcessada()) {
				ManipulaArquivos.salvarArquivo(liga);
			} else {
				ManipulaArquivos.salvarErro(liga.getNomeLiga());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
