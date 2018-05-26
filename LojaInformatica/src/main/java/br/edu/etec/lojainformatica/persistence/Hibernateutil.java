package br.edu.etec.lojainformatica.persistence;

import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class Hibernateutil {
	static Logger logger = Logger.getLogger(Hibernateutil.class.getName());
	
	private static SessionFactory sessionFactory;
	
	private static SessionFactory buildSessionFactory() {
		logger.info("private static SessionFactory buildSessionFactory() {...");
		try {
			// cria o serviço de registro do xml do hibernate
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
			
			// cria os códigos de metadata usando o serbviço de registro especifico
			Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
			return metadata.getSessionFactoryBuilder().build();
		} catch(Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	public static SessionFactory getSessionFactory() {
		logger.info("private static SessionFactory getSessionFactory() {...");
		if(Hibernateutil.sessionFactory == null) {
			logger.info("Hibernateutil.sessionFactory was null.. Calling Hibernateutil.buildSessionFactory()...");
			Hibernateutil.sessionFactory = Hibernateutil.buildSessionFactory();
		}
		return Hibernateutil.sessionFactory;
	}
	public static void shutdown() {
		// fecha as conexões e exclui o cache
		getSessionFactory().close();
	}
}
