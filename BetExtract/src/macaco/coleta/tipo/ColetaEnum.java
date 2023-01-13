package macaco.coleta.tipo;

public enum ColetaEnum {
	LIGA("liga"),
	BUSCA("busca"),
	DIRETA("direta");
	
	private String descricao;

	ColetaEnum(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
        return descricao;
    }
	
}
