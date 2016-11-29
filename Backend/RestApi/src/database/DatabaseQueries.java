package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseQueries {
	private ConnectPostGisDatabase connector;
	private Statement stmt;
	
	public DatabaseQueries(){
		try {
			connector = new ConnectPostGisDatabase();
			stmt = connector.getStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	public List<JSONObject> getAll() throws SQLException, JSONException{
		List<JSONObject> geojson = new ArrayList<>();
		String query = "select o.name, o.amenity,ST_AsGeoJSON(ST_Transform(o.geom, 4326)) AS result from bratislava_slovakia_osm_point o " +
					"where o.amenity='library' and ST_WITHIN(o.geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid=26411 limit 1)) "+
					"group by o.amenity, o.name, result "+
					"order by o.amenity, o.name;";
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(rs.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("name", rs.getString("name"));
			properties.put("type", rs.getString("amenity"));
			properties.put("marker-color", "#e8aa1b");
			properties.put("marker-size", "large");
			properties.put("marker-symbol", "library");
			json.put("properties", properties);
			geojson.add(json);
		}
		query = "select o.name, o.amenity,ST_AsGeoJSON(ST_Transform(o.geom, 4326)) AS result from bratislava_slovakia_osm_point o " +
				"where o.amenity='theatre' and ST_WITHIN(o.geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid=26411 limit 1)) "+
				"group by o.amenity, o.name, result "+
				"order by o.amenity, o.name;";
		rs = stmt.executeQuery(query);
		while(rs.next()){
			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(rs.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("name", rs.getString("name"));
			properties.put("type", rs.getString("amenity"));
			properties.put("marker-color", "#34d834");
			properties.put("marker-size", "large");
			properties.put("marker-symbol", "theatre");
			json.put("properties", properties);
			geojson.add(json);
		}
		query = "select o.name, o.tourism, ST_AsGeoJSON(ST_Transform(o.geom, 4326)) AS result from bratislava_slovakia_osm_point o " +
				"where o.tourism='museum' and ST_WITHIN(o.geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid=26411 limit 1)) "+
				"group by   o.tourism, o.name, result "+
				"order by o.tourism, o.name;";
		rs = stmt.executeQuery(query);
		while(rs.next()){
			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(rs.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("name", rs.getString("name"));
			properties.put("type", rs.getString("tourism"));
			properties.put("marker-color", "#24c0d8");
			properties.put("marker-size", "large");
			properties.put("marker-symbol", "museum");
			json.put("properties", properties);
			geojson.add(json);
		}
		return geojson;
	}
	
	public List<JSONObject> getMuseums(List<String> districts, int radius, double h, double v ) throws SQLException, JSONException{
		List<JSONObject> geojson = new ArrayList<>();
		String district = "";
		int i=0;
		if (districts.size()==0){
			district = district.concat(" ST_within(geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid=26411 limit 1)) ");
		}
		for(String number : districts){
			i++;
			if(i==1){
				district = district.concat(" ST_within(geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid="+number+" limit 1)) ");
			}else{
				district = district.concat(" OR ST_within(geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid="+number+" limit 1)) ");
			}
		}
		String query = "With distance_all AS ( "+
							"select name, tourism, geom, ST_distance(geom::geography,ST_GeomFromText('POINT("+h+" "+v+")', 4326)) AS distance "+
							"from bratislava_slovakia_osm_point) "+
						"select name,distance,tourism, ST_AsGeoJSON(ST_Transform(geom, 4326)) AS result "+
						"from distance_all "+
						"where tourism='museum' and distance < "+radius+" and ("+ district +") "+
						"group by tourism, name, distance, result "+
						"order by tourism, distance;";
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(rs.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("name", rs.getString("name"));
			properties.put("type", rs.getString("tourism"));
			properties.put("distance", rs.getString("distance"));
			properties.put("marker-color", "#24c0d8");
			properties.put("marker-size", "large");
			properties.put("marker-symbol", "museum");
			json.put("properties", properties);
			geojson.add(json);
		}
		return geojson;
	}
	
	public List<JSONObject> getTheatres(List<String> districts, int radius, double h, double v ) throws SQLException, JSONException{
		List<JSONObject> geojson = new ArrayList<>();
		String district = "";
		int i=0;
		if (districts.size()==0){
			district = district.concat(" ST_within(geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid=26411 limit 1)) ");
		}
		for(String number : districts){
			i++;
			if(i==1){
				district = district.concat(" ST_within(geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid="+number+" limit 1)) ");
			}else{
				district = district.concat(" OR ST_within(geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid="+number+" limit 1)) ");
			}
		}
		String query = "With distance_all AS ( "+
							"select amenity, name, geom, ST_distance(geom::geography,ST_GeomFromText('POINT("+h+" "+v+")', 4326)) AS distance "+
							"from bratislava_slovakia_osm_point) "+
						"select name,distance,amenity, ST_AsGeoJSON(ST_Transform(geom, 4326)) AS result "+
						"from distance_all "+
						"where amenity='theatre' and distance < "+radius+" and ("+ district +") "+
						"group by name, distance, amenity,result "+
						"order by amenity, distance;";
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(rs.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("name", rs.getString("name"));
			properties.put("type", rs.getString("amenity"));
			properties.put("distance", rs.getString("distance"));
			properties.put("marker-color", "#34d834");
			properties.put("marker-size", "large");
			properties.put("marker-symbol", "theatre");
			json.put("properties", properties);
			geojson.add(json);
		}
		return geojson;
	}
	
	public List<JSONObject> getLibraries(List<String> districts, int radius, double h, double v ) throws SQLException, JSONException{
		List<JSONObject> geojson = new ArrayList<>();
		String district = "";
		int i=0;
		if (districts.size()==0){
			district = district.concat(" ST_within(geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid=26411 limit 1)) ");
		}
		for(String number : districts){
			i++;
			if(i==1){
				district = district.concat(" ST_within(geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid="+number+" limit 1)) ");
			}else{
				district = district.concat(" OR ST_within(geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid="+number+" limit 1)) ");
			}
		}
		String query = "With distance_all AS ( "+
							"select amenity, name, geom, ST_distance(geom::geography,ST_GeomFromText('POINT("+h+" "+v+")', 4326)) AS distance "+
							"from bratislava_slovakia_osm_point) "+
						"select name,distance,amenity, ST_AsGeoJSON(ST_Transform(geom, 4326)) AS result "+
						"from distance_all "+
						"where amenity='library' and distance < "+radius+" and ("+ district +") "+
						"group by name, distance, amenity,result "+
						"order by amenity, distance;";
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(rs.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("name", rs.getString("name"));
			properties.put("type", rs.getString("amenity"));
			properties.put("distance", rs.getString("distance"));
			properties.put("marker-color", "#e8aa1b");
			properties.put("marker-size", "large");
			properties.put("marker-symbol", "library");
			json.put("properties", properties);
			geojson.add(json);
		}
		return geojson;
	}
	
	public List<JSONObject> getAllByDistrict(List<String> districts) throws SQLException, JSONException{
		List<JSONObject> geojson = new ArrayList<>();
		String district = "";
		int i=0;
		if (districts.size()==0){
			district = district.concat(" ST_within(geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid=26411 limit 1)) ");
		}
		for(String number : districts){
			i++;
			if(i==1){
				district = district.concat(" ST_within(geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid="+number+" limit 1)) ");
			}else{
				district = district.concat(" OR ST_within(geom,(SELECT geom from bratislava_slovakia_osm_polygon WHERE gid="+number+" limit 1)) ");
			}
		}
		String query = "select o.name, o.amenity, ST_AsGeoJSON(ST_Transform(o.geom, 4326)) AS result from bratislava_slovakia_osm_point o " +
				"where o.amenity='library' and ("+district+") "+
				"group by   o.amenity, o.name, result "+
				"order by o.amenity, o.name;";
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(rs.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("title", rs.getString("name"));
			properties.put("type", rs.getString("amenity"));
			properties.put("marker-color", "#e8aa1b");
			properties.put("marker-size", "large");
			properties.put("marker-symbol", "library");
			json.put("properties", properties);
			geojson.add(json);
		}
		query = "select o.name, o.amenity, ST_AsGeoJSON(ST_Transform(o.geom, 4326)) AS result from bratislava_slovakia_osm_point o " +
				"where o.amenity='theatre' and ("+district+") "+
				"group by  o.amenity, o.name, result "+
				"order by o.amenity, o.name;";
		rs = stmt.executeQuery(query);
		while(rs.next()){
			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(rs.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("title", rs.getString("name"));
			properties.put("type", rs.getString("amenity"));
			properties.put("marker-color", "#34d834");
			properties.put("marker-size", "large");
			properties.put("marker-symbol", "theatre");
			json.put("properties", properties);
			geojson.add(json);
		}
		query = "select o.name, o.tourism, ST_AsGeoJSON(ST_Transform(o.geom, 4326)) AS result from bratislava_slovakia_osm_point o " +
				"where o.tourism='museum' and ("+district+") "+
				"group by   o.tourism, o.name, result "+
				"order by o.tourism, o.name;";
		rs = stmt.executeQuery(query);
		while(rs.next()){
			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(rs.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("title", rs.getString("name"));
			properties.put("type", rs.getString("tourism"));
			properties.put("marker-color", "#24c0d8");
			properties.put("marker-size", "large");
			properties.put("marker-symbol", "museum");
			json.put("properties", properties);
			geojson.add(json);
		}
		return geojson;
	}
	
		
}