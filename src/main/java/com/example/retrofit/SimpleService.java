package com.example.retrofit;

import java.io.IOException;
import java.util.*;
import retrofit2.converter.gson.*;
import retrofit2.http.*;
import retrofit2.*;
import okhttp3.logging.*;
import okhttp3.*;

public class SimpleService {
  public static final String API_URL = "https://api.swingbyswing.com";

  public static <T> String listToString(List<T> list) {
    String s = "";
    for(T t: list) {
      s += String.format("[%s]", t);
    }
    return s;
  }

  public static class Course {
    public int id;
    public String status;
    public int hole_count;
    public String measurement_type;
    public String name;
    public String addr_1;
    public String city;
    public String state_or_province;
    public String country;
    public String zip_code;
    public String phone;
    public String website;
    public String type;
    public String href;
    public List<Hole> holes;
    public List<TeeType> tee_types;
    public String toString() {
      return String.format("[id=%d, status=%s, hole_count=%d, measurement_type=%s,name=%s, website=%s,type=%s, holes=%s,tee_types=%s]", id, status, hole_count, measurement_type, name, website, type, SimpleService.listToString(holes), SimpleService.listToString(tee_types));
    }
  }

  public static class Hole {
    public int hole_num;
    public Location green_location;
    public Location pin_location;
    public List<TeeBox> tee_boxes;
    public String toString() {
      return String.format("[hole_num=%d,green=%s, pin=%s]", hole_num, green_location, pin_location, SimpleService.listToString(tee_boxes));
    }
  }

  public static class Location {
    public Double lat, lng;
    public String toString() {
      return String.format("[lat=%e,lng=%e]", lat, lng);
    }
  }

  public static class TeeBox {
    public int id;
    public String tee_type;
    public String tee_color_type;
    public int par;
    public int yards;
    public int meters;
    public int hcp;
    public String tee_hex_color;
    public Location location;

    public String toString() {
      return String.format("[id=%d, tee_type=%s, par=%d, yards=%d, location=%s]", id, tee_type, par, yards, location);
    }
  }

  public static class TeeType {
    public int id;
    public String tee_type;
    public String tee_color_type;
    public int par;
    public int yards;
    public int meters;
    public double rating;
    public int slope;
    public int front_nine_par;
    public int front_nine_yards;
    public int front_nine_meters;
    public double front_nine_rating;
    public int back_nine_par;
    public int back_nine_yards;
    public int back_nine_meters;
    public double back_nine_rating;
    public String tee_hex_color;

    public String toString() {
      return String.format("[id=%d,tee_type=%s,par=%d,yards=%d,rating=%e]", id, tee_type, par, yards, rating);
    }
  }

  public static class Courses {
    public List<Course> courses;
  }

  public static class CourseHolder {
    public Course course;
  }

  public interface SwingBySwing {
    @GET("/v2/courses/search_by_location")
    retrofit2.Call<Courses> courses(@QueryMap Map<String, String> options);

    @GET("/v2/courses/{id}")
    retrofit2.Call<CourseHolder> getCourse(@Path("id") int id, @QueryMap Map<String, String> options);
  }

  public static void main(String... args) throws IOException {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(API_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    SwingBySwing api = retrofit.create(SwingBySwing.class);
    HashMap<String,String> params = new HashMap<>();
    params.put("lat", "34.104458");
    params.put("lng", "-118.01");
    params.put("radius", "5");
    params.put("active_only", "yes");
    params.put("order_by","distance_from_me_miles");
    params.put("access_token", "FILL YOUR API KEY HERE");
    retrofit2.Call<Courses> call = api.courses(params);
    Courses apiResult = call.execute().body();
    List<Course> allCourses = apiResult.courses;
    for (Course course : allCourses) {
      System.out.println(course);
      if (course.id == 13927) {
        HashMap<String,String> params1 = new HashMap<>();
        params1.put("access_token", "FILL YOUR API KEY HERE");
        Course myCourse = api.getCourse(course.id,params1).execute().body().course;
        System.out.println("Special Print..............");
        System.out.println(myCourse);
      }
    }
  }
}
