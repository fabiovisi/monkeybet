package macaco.coleta.denise;

import java.io.IOException;
import java.util.ArrayList;

import macaco.coleta.bean.Liga;
import macaco.coleta.motor.MotorDenise;
import macaco.coleta.negocio.ManipulaArquivos;

public class ColetaPorLiga365 {

	static ArrayList<String> ligas;

	public static void main(String[] args) throws IOException {

		MotorDenise.configuraBet();
		
		ArrayList<String> ligas = ManipulaArquivos.lerArquivoLigas();
		
		MotorDenise.conectar();
	
		for (String ligaunica : ligas) {

			Liga liga = MotorDenise.carregarJogosPorLiga(ligaunica);

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

		MotorDenise.desconectar();

	}
	
}
