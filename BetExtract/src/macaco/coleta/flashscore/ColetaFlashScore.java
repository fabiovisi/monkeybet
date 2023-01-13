package macaco.coleta.flashscore;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import macaco.coleta.bean.Partida;
import macaco.coleta.motor.MotorFlashScore;
import macaco.coleta.negocio.ManipulaArquivos;
import macaco.coleta.negocio.ManipulaOdds;
import macaco.coleta.tipo.DiaColetaEnum;

public class ColetaFlashScore {

	public static void main(String[] args) throws IOException, ParseException {

		List<Partida> partidasSemPalpite = MotorFlashScore.carregarTodosJogos(DiaColetaEnum.AMANHA);
		
		Float odd1;
		Float oddx;
		Float odd2;
		
		List<Partida> partidas = new ArrayList<Partida>();
		
		for(Partida partida : partidasSemPalpite) {
			
			odd1 = Float.parseFloat(partida.getOdd1());
			oddx = Float.parseFloat(partida.getOddx());
			odd2 = Float.parseFloat(partida.getOdd2());
			
			partida.setPalpite(ManipulaOdds.calculaPalpilte(odd1, oddx, odd2));
			partida.setDupla(ManipulaOdds.duplaHipotese(odd1, odd2));
			partidas.add(partida);
						
		}
		
		try {
			System.out.println("Inciando geração do arquivo!");
			ManipulaArquivos.salvarArquivo(partidas);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Carregamento concluído!");
	}
	
}
