package br.com.framework.hibernate.session;
//Responsavel por estabelecer conexão com hibernate

import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.bean.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

import br.com.framework.implementacao.crud.VariavelConexaoUtil;

@ApplicationScoped
public class HibernateUtil {

	private static SessionFactory sessionFactory = buildSessionFactory();
//le o arquivo de configuração hibernate.cfg.xml
	private static SessionFactory buildSessionFactory() {
		try {
			if (sessionFactory == null) {
				sessionFactory = new Configuration().configure().buildSessionFactory();
			}
			return sessionFactory;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Erro ao criar conexão Session Factory");
		}
	}

	// retorna o session factory corrente
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// retorna sessão do session factory
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	// abre uma nova sessão no sessionfactory
	public static Session openSession() {
		if (sessionFactory == null) {
			buildSessionFactory();
		}
		return sessionFactory.openSession();
	}

	// retorna Connection do SQL do provedor de conexão do hibernate
	public static Connection getConnectionProvider() throws SQLException {
		return ((SessionFactoryImplementor) sessionFactory).getConnectionProvider().getConnection();
	}

	// retorna connection no InitialConext
	public static Connection getConnection() throws Exception {
		InitialContext context = new InitialContext();
		DataSource ds = (DataSource) context.lookup(VariavelConexaoUtil.JAVA_COMP_ENV_DATA_SOURCE);
		return ds.getConnection();
	}

	// Retorna Datasouce JNDI do TOMCAT
	public DataSource getDataSouceJNDI() throws NamingException {
		InitialContext context = new InitialContext();
		return (DataSource) context.lookup(VariavelConexaoUtil.JAVA_COMP_ENV_DATA_SOURCE);
	}
}
