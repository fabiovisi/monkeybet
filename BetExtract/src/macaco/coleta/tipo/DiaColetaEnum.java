package macaco.coleta.tipo;

public enum DiaColetaEnum {

	HOJE("hoje"),
	AMANHA("amanhã");
	
	private String descricao;

	DiaColetaEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}
