package br.com.framework.interfac.crud;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public interface InterfaceCrud<T> extends Serializable {
	void save(T obj) throws Exception;

	void persist(T obj) throws Exception;

	void saveOrUpdate(T obj) throws Exception;

	void update(T obj) throws Exception;

	void delete(T obj) throws Exception;

	T merge(T obj) throws Exception;

	List<T> findList(Class<T> objs) throws Exception;

	Object findById(Class<T> obj, Long id) throws Exception;

	T findPorId(Class<T> obj, Long id) throws Exception;

	List<T> findListByQueryDinamica(String s) throws Exception;

	void executeUpdateQueryDinamica(String s) throws Exception;

	void executeUpdateSQLDinamica(String s) throws Exception;

	// Fazer hibernate limpar a sessão nele
	void clearSession() throws Exception;

	// retira um objeto da sessão hibernate
	void evict(Object objs) throws Exception;

	Session getSession() throws Exception;

	List<?> getListSQLDinamica(String sql) throws Exception;

	List<Object[]> getListSQLDinamicaArray(String sql) throws Exception;

	// Jdbc do spring
	JdbcTemplate getJdbcTempalte();

	SimpleJdbcTemplate getSimpleJdbcTemplate();

	SimpleJdbcInsert getSimpleJdbcInsert();

	Long totalRegistros(String table) throws Exception;

	Query obterQuery(String query) throws Exception;

	List<T> findListByQueryDinamica(String query, int iniciaNoRegistro, int maximoResultado) throws Exception;

	SimpleJdbcCall getsimpleJdbcClass();
}
