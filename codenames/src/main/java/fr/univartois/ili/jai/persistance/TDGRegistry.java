package fr.univartois.ili.jai.persistance;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class TDGRegistry {

	private static final Logger LOGGER = Logger.getLogger(TDGRegistry.class.getName());
	private static final Map<Class<?>, GenericTDG<?>> REGISTRY = new HashMap<>();
	private static Connection conn;
	private static String jdbcurl;
	private static String jdbcusername;
	private static String jdbcpassword;

	static {
		Properties properties = new Properties();
		try {
			properties.load(TDGRegistry.class.getResourceAsStream("/registry.properties"));
			jdbcurl = properties.getProperty("jdbcurl");
			jdbcusername = properties.getProperty("jdbcusername");
			jdbcpassword = properties.getProperty("jdbcpassword");
			if (jdbcurl == null) {
				throw new IllegalStateException("Cannot find JDBC URL");
			}
			if (jdbcusername != null && jdbcpassword != null) {
				conn = DriverManager.getConnection(jdbcurl, jdbcusername, jdbcpassword);
			} else {
				conn = DriverManager.getConnection(jdbcurl);
			}
			Enumeration<?> keys = properties.propertyNames();
			String key;
			while (keys.hasMoreElements()) {
				key = keys.nextElement().toString();
				if (!"jdbcurl".equals(key) && (jdbcusername == null || !"jdbcusername".equals(key))
						&& (jdbcpassword == null || !"jdbcpassword".equals(key))) {
					Class<? extends Persistable> persistable = (Class<? extends Persistable>) Class.forName(key);
					Class<? extends GenericTDG> tdg = (Class<? extends GenericTDG>) Class
							.forName(properties.getProperty(key));
					register(persistable, tdg);
				}
			}
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect to database ", e);
		} catch (IOException e1) {
			throw new IllegalStateException("Cannot access file registry.properties", e1);
		} catch (IllegalArgumentException | SecurityException | ClassNotFoundException e) {
			LOGGER.warning(e.getMessage());
		}
	}

	private TDGRegistry() {
		throw new IllegalStateException("Utility class");
	}

	public static Connection getConnection() {
		return conn;
	}

	public static void init() {
		REGISTRY.clear();
		try {
			if (jdbcusername != null && jdbcpassword != null) {
				conn = DriverManager.getConnection(jdbcurl, jdbcusername, jdbcpassword);
			} else {
				conn = DriverManager.getConnection(jdbcurl);
			}
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect to database ", e);
		}
	}

	public static <U extends Persistable, T extends GenericTDG<U>> void register(Class<U> persistable, Class<T> tdg) {
		try {
			REGISTRY.put(persistable, tdg.getConstructor().newInstance());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException
				| InvocationTargetException | NoSuchMethodException e) {
			// fail silently
			LOGGER.warning(e.getMessage());
		}
	}

	public static <U extends Persistable, T extends GenericTDG<U>> T findTDG(Class<U> clazz) {
		return (T) REGISTRY.get(clazz);
	}
}
