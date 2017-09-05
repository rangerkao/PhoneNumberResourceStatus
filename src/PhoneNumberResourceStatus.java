import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("/status")
public class PhoneNumberResourceStatus {

	 @GET
	 @Produces(MediaType.TEXT_PLAIN)
	 public String getData() {
	   String response = "enter Date to request.";
	   return response;
	 }
	   
	 @GET
	 @Path("/{getparameter}")
	 @Produces(MediaType.APPLICATION_JSON)
	 public String getDataByDate(
	   @PathParam(value="getparameter") String date,
	   @QueryParam(value="q") String q) throws FileNotFoundException, IOException  {
	   System.out.println(date);
	   System.out.println(q);
	   
	   Map<String,Object> result = new HashMap<String,Object>();
	   List<StatusBean> list = new ArrayList<StatusBean>();
	   Connection conn = null;
	   Statement st = null;
	   ResultSet rs = null;
	   DBConnect dbc =new DBConnect();
		try {
			conn = dbc.getConn1();
			
			 st = conn.createStatement();
			
			String sql = ""
					+ "select to_char(B.statusdate,'yyyy/MM/dd') d,A.RANGENAME NUM_LVL,B.LOCKED_No,"
					+ "			B.IDLE_No,B.Spared_No,B. Accum_USED_No,B.Ported_out_No,C.Accum_USED_No YesTDY_Accum "
					+ "from NUMBERRANGE A,"
					+ "		(select rangeID,statusdate,LOCKED_No,IDLE_No,Spared_No,Accum_USED_No,Ported_out_No "
					+ "		from NUMBERSTATUS "
					+ "		where STATUSDATE >=to_date('"+date+"','yyyyMMdd')  and STATUSDATE < (to_date('"+date+"','yyyyMMdd')+1)) B, "
					+ ""
					+ "		(select rangeID,Accum_USED_No "
					+ "		from NUMBERSTATUS "
					+ "		where STATUSDATE >=(to_date('"+date+"','yyyyMMdd')-1)  and STATUSDATE < to_date('"+date+"','yyyyMMdd')) C "
					+ "where a.rangeID = b.rangeid and a.rangeID = c.rangeid(+) "
					+ " order by a.rangeid ";
			
			System.out.println("Execute SQL:"+sql);
			rs = st.executeQuery(sql);
			int LOCKED_No = 0;
			int IDLE_No = 0;
			int Spared_No = 0;
			int Accum_USED_No = 0;
			int Ported_out_No = 0;
			
			int TDY_USED_No = 0;
			int YesTDY_Accum = 0;
			
			while(rs.next()){
				StatusBean s = new StatusBean();
				s.setDATE(rs.getString("d"));
				s.setNUM_LVL(rs.getString("NUM_LVL"));
				s.setLOCKED_No(rs.getInt("LOCKED_No"));
				s.setIDLE_No(rs.getInt("IDLE_No"));
				s.setSpared_No(rs.getInt("Spared_No"));
				s.setAccum_USED_No(rs.getInt("Accum_USED_No"));
				s.setPorted_out_No(rs.getInt("Ported_out_No"));
				s.setYesTDY_Accum(rs.getInt("YesTDY_Accum"));
				
				s.setTDY_USED_No(s.getAccum_USED_No()-s.getYesTDY_Accum());
				s.setUtilization(new BigDecimal(
						(Double.parseDouble(String.valueOf(s.getAccum_USED_No()))/(s.getLOCKED_No()+s.getIDLE_No()+s.getSpared_No()+s.getAccum_USED_No())*100)
						).setScale(2, BigDecimal.ROUND_HALF_UP).toString()//小數點2位四捨五入
						+"%");
				
				LOCKED_No += s.getLOCKED_No(); 
				IDLE_No += s.getIDLE_No(); 
				Spared_No += s.getSpared_No();
				Accum_USED_No += s.getAccum_USED_No();
				Ported_out_No += s.getPorted_out_No();
				TDY_USED_No += s.getTDY_USED_No();
				YesTDY_Accum += s.getYesTDY_Accum();
				
				list.add(s);
			}
			
			if(list.size()==0){
				result.put("error", "No data.");
			}
			else{
				StatusBean s = new StatusBean();
				s.setDATE(list.get(0).getDATE());
				s.setNUM_LVL("ALL");
				s.setLOCKED_No(LOCKED_No);
				s.setIDLE_No(IDLE_No);
				s.setSpared_No(Spared_No);
				s.setAccum_USED_No(Accum_USED_No);
				s.setPorted_out_No(Ported_out_No);
				s.setTDY_USED_No(TDY_USED_No);
				s.setYesTDY_Accum(YesTDY_Accum);
				s.setUtilization(new BigDecimal(
						(Double.parseDouble(String.valueOf(s.getAccum_USED_No()))/(s.getLOCKED_No()+s.getIDLE_No()+s.getSpared_No()+s.getAccum_USED_No())*100)
						).setScale(2, BigDecimal.ROUND_HALF_UP).toString()//小數點2位四捨五入
						+"%");
				
				list.add(s);
				result.put("data", list);
			}			
		} catch (SQLException e) {
			result.put("error", e.getMessage());
		} catch (ClassNotFoundException e) {
			result.put("error", e.getMessage());
		}finally{
			
				try { 	if(conn!=null)		conn.close(); 	} catch (SQLException e) {}
				try { 	if(st!=null)		st.close(); 	} catch (SQLException e) {}
				try { 	if(rs!=null)		rs.close(); 	} catch (SQLException e) {}
		}
		
		JSONObject json = (JSONObject) JSONObject.wrap(result);
	   return json.toString();
	   
	 }
}
