package br.com.framework.implementacao.crud;

import java.io.Serializable;

public class VariavelConexaoUtil implements Serializable{
	private static final long serialVersionUID = 1L;
	// nome do caminho do JNDI datasource do tomcat
	public static String JAVA_COMP_ENV_DATA_SOURCE = "java:/comp/env/jdbc/datasource";
}
