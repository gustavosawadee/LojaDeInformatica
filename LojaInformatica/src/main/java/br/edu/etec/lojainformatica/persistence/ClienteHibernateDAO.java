package br.edu.etec.lojainformatica.persistence;

import java.util.List;
//import java.util.logging.Logger;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;
import br.edu.etec.lojainformatica.model.Cliente;

public class ClienteHibernateDAO<T> implements IDAO<T>{

	Logger logger = Logger.getLogger(ClienteHibernateDAO.class.getName());
	private Transaction currentTransaction;
	Session session;
	
	public ClienteHibernateDAO(Session session) {
		this.session = session;
	}
	
	public int encontrarPeloNome(String nome) {
		logger.info("public int encontrarPeloNome(String nome) {...");
		CriteriaBuilder cB = this.session.getCriteriaBuilder();
		CriteriaQuery<Cliente> qry = cB.createQuery(Cliente.class);
		Root<Cliente> from = qry.from(Cliente.class);
		qry.select(from);
		qry.where(cB.and(
				cB.equal(from.get("nome"), nome)//,cB.equal(from.get("OutroCampo"), vlrParaProcurar)
		));
		Query<Cliente> createdQuery = session.createQuery(qry);
		List<Cliente> resultList = createdQuery.getResultList();
		return resultList.size();
	}

	public void persistir(T o){
		// TODO Auto-generated method stub
		session.save(o);
	}
	
	public void excluiTodos() {
		CriteriaBuilder cB = this.session.getCriteriaBuilder();
		CriteriaDelete<Cliente> qryDelete = cB.createCriteriaDelete(Cliente.class);
		Root<Cliente> deleteFrom = qryDelete.from(Cliente.class);
		this.session.createQuery(qryDelete).executeUpdate();
	}
	
	public List<T> Listar() {
		CriteriaBuilder cB = this.session.getCriteriaBuilder();
		CriteriaQuery<Cliente> qry = cB.createQuery(Cliente.class);
		qry.select(qry.from(Cliente.class));
		Query<T> createdQuery = (Query<T>) this.session.createQuery(qry);
		return createdQuery.getResultList();
	}

	public void alterar(T e){
		// TODO Auto-generated method stub
		this.session.merge(e);
	}
	
	public void excluir(Integer id){
		// TODO Auto-generated method stub
		this.session.remove(id);
	}

	public T encontraPeloID(Integer id){
		// TODO Auto-generated method stub
		return (T) this.session.byId(Cliente.class).load(id);
	}
	
	public void closeSession() {
		this.session.close();
	}
	public void beginTransaction() {
		this.currentTransaction = this.session.beginTransaction();
	}
	public void commit() {
		this.currentTransaction.commit();
	}

	public void alterar(Integer id) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
