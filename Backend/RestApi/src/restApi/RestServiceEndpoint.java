package restApi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.DatabaseQueries;;

@ApplicationPath("app")
@Path("GeoJson")
public class RestServiceEndpoint extends Application {
	private DatabaseQueries data = new DatabaseQueries();
	
	private List<String> setDistricts(
				boolean c1, boolean c2, boolean c3, boolean c4, boolean c5,
				boolean c6, boolean c7, boolean c8, boolean c9, boolean c10,
				boolean c11, boolean c12, boolean c13){
		List<String> districts = new ArrayList<String>();
		if(c1)districts.add("3611");
		if(c2)districts.add("8833");
		if(c3)districts.add("21258");
		if(c4)districts.add("35849");
		if(c5)districts.add("48369");
		if(c6)districts.add("68426");
		if(c7)districts.add("71372");
		if(c8)districts.add("72407");
		if(c9)districts.add("74982");
		if(c10)districts.add("76348");
		if(c11)districts.add("95151");
		if(c12)districts.add("106751");
		if(c13)districts.add("109093");
		return districts;
	}

	// http://localhost:8080/RestApi/app/GeoJson/all
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAll(){
		JSONArray restMessage = new JSONArray();
		try {
			List<JSONObject> databaseData = data.getAll();
			for(JSONObject jsonData : databaseData){
				restMessage.put(jsonData);
			}
			
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		
		return restMessage.toString();
	}
	
	@GET
	@Path("/museums/{c1}/{c2}/{c3}/{c4}/{c5}/{c6}/{c7}/{c8}/{c9}/{c10}/{c11}/{c12}/{c13}/{h}/{v}/{radius}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMuseums(@PathParam("c1") boolean c1,@PathParam("c2") boolean c2,
			@PathParam("c3") boolean c3,@PathParam("c4") boolean c4,@PathParam("c5") boolean c5,
			@PathParam("c6") boolean c6,@PathParam("c7") boolean c7,@PathParam("c8") boolean c8,
			@PathParam("c9") boolean c9,@PathParam("c10") boolean c10,@PathParam("c11") boolean c11,
			@PathParam("c12") boolean c12,@PathParam("c13") boolean c13,@PathParam("h") double h,
			@PathParam("v") double v,@PathParam("radius") int radius){
		JSONArray restMessage = new JSONArray();
		List<String> districts = setDistricts(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13);
		
		try {
			List<JSONObject> databaseData = data.getMuseums(districts, radius, h, v);
			for(JSONObject jsonData : databaseData){
				restMessage.put(jsonData);
			}			
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		
		return restMessage.toString();
	}
	
	@GET
	@Path("/theatres/{c1}/{c2}/{c3}/{c4}/{c5}/{c6}/{c7}/{c8}/{c9}/{c10}/{c11}/{c12}/{c13}/{h}/{v}/{radius}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTheatres(@PathParam("c1") boolean c1,@PathParam("c2") boolean c2,
			@PathParam("c3") boolean c3,@PathParam("c4") boolean c4,@PathParam("c5") boolean c5,
			@PathParam("c6") boolean c6,@PathParam("c7") boolean c7,@PathParam("c8") boolean c8,
			@PathParam("c9") boolean c9,@PathParam("c10") boolean c10,@PathParam("c11") boolean c11,
			@PathParam("c12") boolean c12,@PathParam("c13") boolean c13,@PathParam("h") double h,
			@PathParam("v") double v,@PathParam("radius") int radius){
		JSONArray restMessage = new JSONArray();
		List<String> districts = setDistricts(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13);
		
		try {
			List<JSONObject> databaseData = data.getTheatres(districts, radius, h, v);
			for(JSONObject jsonData : databaseData){
				restMessage.put(jsonData);
			}			
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		
		return restMessage.toString();
	}
	
	@GET
	@Path("/libraries/{c1}/{c2}/{c3}/{c4}/{c5}/{c6}/{c7}/{c8}/{c9}/{c10}/{c11}/{c12}/{c13}/{h}/{v}/{radius}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getLibraries(@PathParam("c1") boolean c1,@PathParam("c2") boolean c2,
			@PathParam("c3") boolean c3,@PathParam("c4") boolean c4,@PathParam("c5") boolean c5,
			@PathParam("c6") boolean c6,@PathParam("c7") boolean c7,@PathParam("c8") boolean c8,
			@PathParam("c9") boolean c9,@PathParam("c10") boolean c10,@PathParam("c11") boolean c11,
			@PathParam("c12") boolean c12,@PathParam("c13") boolean c13,@PathParam("h") double h,
			@PathParam("v") double v,@PathParam("radius") int radius){
		JSONArray restMessage = new JSONArray();
		List<String> districts = setDistricts(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13);
		
		try {
			List<JSONObject> databaseData = data.getLibraries(districts, radius, h, v);
			for(JSONObject jsonData : databaseData){
				restMessage.put(jsonData);
			}			
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		
		return restMessage.toString();
	}

	@GET
	@Path("/allSorted/{c1}/{c2}/{c3}/{c4}/{c5}/{c6}/{c7}/{c8}/{c9}/{c10}/{c11}/{c12}/{c13}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllSorted(@PathParam("c1") boolean c1,@PathParam("c2") boolean c2,
			@PathParam("c3") boolean c3,@PathParam("c4") boolean c4,@PathParam("c5") boolean c5,
			@PathParam("c6") boolean c6,@PathParam("c7") boolean c7,@PathParam("c8") boolean c8,
			@PathParam("c9") boolean c9,@PathParam("c10") boolean c10,@PathParam("c11") boolean c11,
			@PathParam("c12") boolean c12,@PathParam("c13") boolean c13){
		JSONArray restMessage = new JSONArray();
		List<String> districts = setDistricts(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13);
		
		try {
			List<JSONObject> databaseData = data.getAllByDistrict(districts);
			for(JSONObject jsonData : databaseData){
				restMessage.put(jsonData);
			}			
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		
		return restMessage.toString();
	}
}
  