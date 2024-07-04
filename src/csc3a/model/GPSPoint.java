package csc3a.model;

import java.io.Serializable;

public class GPSPoint implements Serializable {
  
     /**
	 * 
	 */private static final long serialVersionUID = 1L;
      private Double longitude;
      private Double latitude;
      static final double MARSRADIUS = 3390.0;
  
  public GPSPoint(double longitude, double latitude) {
    this.longitude = Double.valueOf(longitude);
    this.latitude = Double.valueOf(latitude);
  }
  
  public Double getLat() {
    return this.latitude;
  }
  
  public Double getLong() {
    return this.longitude;
  }
  
  public void setLat(double latitude) {
    this.latitude = Double.valueOf(latitude);
  }
  
  public void setLong(double longitude) {
    this.longitude = Double.valueOf(longitude);
  }
  
  public Double calculateDistance(GPSPoint other) {
    double lat1 = this.latitude.doubleValue();
    double lat2 = other.getLat().doubleValue();
    double lon1 = this.longitude.doubleValue();
    double lon2 = other.getLong().doubleValue();
    double dlat = Math.toRadians(lat2 - lat1);
    double dlon = Math.toRadians(lon2 - lon1);
    double a = Math.pow(Math.sin(dlat/2), 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(dlon/2), 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return Double.valueOf(MARSRADIUS * c);
  }
  @Override
  public String toString() {
      return "(" + latitude + ", " + longitude + ")";
  }
}

