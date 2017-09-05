
import java.io.IOException;
import java.io.InputStream;
 
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/*import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;*/

 

 
@Path("/helloWorld")
public class HelloWorld {
   /*
//GET:http://hostname:port/webname/services/helloWorld
 @GET
 @Produces(MediaType.TEXT_PLAIN)
 public String getHelloWorld() {
   String response = "Hello World :)";
   return response;
 }
  
 
 POST:http://hostname:port/webname/services/helloWorld

	 RequestBody:
	 helloword
	 output:
	 {"requestbody":"helloword"}
 
 @POST
 @Produces(MediaType.APPLICATION_JSON)
 public String postHelloWorld(InputStream requestBodyStream) {
   JSONObject json = new JSONObject();
   try {
    json.put("requestbody", this.convertRequestBODY2String(requestBodyStream));
   } catch (JSONException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
   return json.toString();
 }
   
 //GET:http://hostname:port/webname/services/helloWorld/mytest
 @GET
 @Path("/{getparameter}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getByPathParameterAndQueryPath(
   @PathParam(value="getparameter") String getparameter,
   @QueryParam(value="q") String q) throws JSONException {
   System.out.println(getparameter);
   System.out.println(q);
   JSONObject json = new JSONObject();
   json.put("pathParameter", getparameter);
   json.put("q", q);
   return json.toString();
 }
   
 //convert request inputstream to string
 private String convertRequestBODY2String(InputStream requestBodyStream){
    StringBuffer buffer = new StringBuffer();
    int bufferContent = 0;
    do
    {
     try {
      bufferContent = requestBodyStream.read();
      System.out.println(bufferContent);
      if(bufferContent > 0)
       buffer.append((char) bufferContent);
        
     } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
     }
    }while(bufferContent > 0 );
    return buffer.toString();
 }*/
 }