import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class DBConnect {

	
	Properties props = new Properties();
	
	public DBConnect() throws FileNotFoundException, IOException{
		loadProperties();
	}
	
	//Load Properties
	void loadProperties() throws FileNotFoundException, IOException{
		String path = DBConnect.class.getClassLoader().getResource("/").toString().replace("file:","");
		System.out.println(path);
		props.load(new FileInputStream(path+"Log4j.properties"));
		PropertyConfigurator.configure(props);
		System.out.println("loadProperties success!"+props.getProperty("Oracle.Host"));
	}
	
	public Connection getConn1() throws ClassNotFoundException, SQLException{
		
		Class.forName(props.getProperty("Oracle.DriverClass"));
		return DriverManager.getConnection(props.getProperty("Oracle.URL")
				.replace("{{Host}}", props.getProperty("Oracle.Host"))
				.replace("{{Port}}", props.getProperty("Oracle.Port"))
				.replace("{{ServiceName}}", 	(props.getProperty("Oracle.ServiceName")!=null?props.getProperty("Oracle.ServiceName"):""))
				.replace("{{SID}}", (props.getProperty("Oracle.SID")!=null?props.getProperty("Oracle.SID"):"")), props.getProperty("Oracle.UserName"), props.getProperty("Oracle.PassWord"));
	}
	
}
