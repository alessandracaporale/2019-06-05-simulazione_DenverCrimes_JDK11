package it.polito.tdp.denvercrimes.db;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;

import it.polito.tdp.denvercrimes.model.Event;


public class EventsDAO {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			List<Event> list = new ArrayList<>() ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getAllYears() {
		String sql = "SELECT distinct Year(reported_date) as anno "
				+ "FROM events";
		List<Integer> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getInt("anno"));
			}
			conn.close();
			st.close();
			res.close();
			return result;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}
	
	public List<Integer> getDistrict() {
		String sql = "SELECT distinct district_id as id "
				+ "FROM events";
		
		List<Integer> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getInt("id"));
			}
			conn.close();
			st.close();
			res.close();
			return result;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public Double getLatMedia(int district, int anno) {
		String sql ="SELECT AVG(geo_lat) as lat "
				+ "FROM events "
				+ "WHERE district_id = ? and Year(reported_date) = ?";
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, district);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				conn.close();
				return res.getDouble("lat");
			}
			else {
				return null;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Double getLonMedia(int district, int anno) {
		String sql ="SELECT AVG(geo_lon) as lon "
				+ "FROM events "
				+ "WHERE district_id = ? and Year(reported_date) = ?";
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, district);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				conn.close();
				return res.getDouble("lon");
			}
			else {
				return null;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Integer getDistrettoMin(int anno) {
		String sql = "SELECT district_id "
				+ "FROM events "
				+ "WHERE Year(reported_date) = ? "
				+ "GROUP BY district_id "
				+ "ORDER BY COUNT(*) ASC "
				+ "LIMIT 1";
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				conn.close();
				return res.getInt("district_id");
			}
			else {
				conn.close();
				return null;
			}
				
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public List<Event> listAllEventsByDate(int anno, int mese, int giorno) {
		String sql = "SELECT * "
				+ "FROM events "
				+ "WHERE Year(reported_date) = ? AND Month(reported_date) = ? AND Day(reported_date) = ?";
		List<Event> lista = new ArrayList<Event>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					lista.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} 
				catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return lista ;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}

