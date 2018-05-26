package br.edu.etec.lojainformatica.persistence;

import java.util.List;

public interface IDAO<T> {
	void persistir(T o) throws Exception;
	void alterar(T o) throws Exception;
	void excluir(Integer id) throws Exception;
	T encontraPeloID(Integer id) throws Exception;
	List<T> Listar() throws Exception;
}
