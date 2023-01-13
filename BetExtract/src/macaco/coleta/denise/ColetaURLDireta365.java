package macaco.coleta.denise;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import macaco.coleta.bean.Liga;
import macaco.coleta.motor.MotorDenise;
import macaco.coleta.negocio.ManipulaArquivos;
import macaco.coleta.negocio.ManipulaProperties;

public class ColetaURLDireta365 {

	static ArrayList<String> ligas;

	public static void main(String[] args) throws IOException {

		MotorDenise.configuraBet();
		
		ArrayList<String> ligas = ManipulaArquivos.lerArquivoLigas();
		
		Properties prop = ManipulaProperties.getProp();
		String urlDireta = prop.getProperty("bet.url.direta");
		
		MotorDenise.conectar();
		
		for (String ligaunica : ligas) {
			
			MotorDenise.setUrlDireta(urlDireta + ligaunica + "/");
			
			Liga liga  = MotorDenise.carregarJogosUrlDireta(ligaunica);
			
			try {
				if(liga.isProcessada()) {
					ManipulaArquivos.salvarArquivo(liga);
				} else {
					ManipulaArquivos.salvarErro(liga.getNomeLiga());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		MotorDenise.desconectar();

	}
	
}
