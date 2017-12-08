
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;

public class User extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	    	throws ServletException, IOException {
		processRequest(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	    	throws ServletException, IOException {
		processRequest(req, resp);
	}

	protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
	    	throws ServletException, IOException {
		System.out.println("Processinig");
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {

			String acc = req.getParameter("account");
			String pwd = req.getParameter("password");
			String ePwd = md5Encode(pwd);
			System.out.println("Account:"+acc+";Passworld:"+pwd);
			System.out.println(ePwd);
			 DBConnect dbc =new DBConnect();
			conn =  dbc.getConn1();
			
			st = conn.createStatement();
			rs = st.executeQuery("select password from PNRS_USER where account = '"+acc+"' ");
			
			if(rs.next()){
				
				if(ePwd.equals(rs.getString("password"))) {
					Cookie cookie = new Cookie("account", acc);
					//短時間權限結束
					cookie.setMaxAge(3);
					resp.addCookie(cookie);
					
					resp.sendRedirect("S2T.html");
				}else{
					resp.sendRedirect("fail.html");
				};
				
			}else{
				resp.sendRedirect("fail.html");
			}
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			try { 	if(conn!=null)		conn.close(); 	} catch (SQLException e) {}
			try { 	if(st!=null)		st.close(); 	} catch (SQLException e) {}
			try { 	if(rs!=null)		rs.close(); 	} catch (SQLException e) {}
		}
		
	}
	
	public String md5Encode(String source) throws NoSuchAlgorithmException{
		String input=source;
		 MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        return byteToString1(messageDigest);
	}
	
	public static String byteToString1(byte[] source){
		BigInteger number = new BigInteger(1, source);
        String hashtext = number.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
	} 


}
