/*     */ package com.maxmind.geoip2;
/*     */ 
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.InjectableValues;
/*     */ import com.fasterxml.jackson.databind.InjectableValues.Std;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectReader;
/*     */ import com.google.api.client.http.GenericUrl;
/*     */ import com.google.api.client.http.HttpHeaders;
/*     */ import com.google.api.client.http.HttpRequest;
/*     */ import com.google.api.client.http.HttpRequestFactory;
/*     */ import com.google.api.client.http.HttpResponse;
/*     */ import com.google.api.client.http.HttpResponseException;
/*     */ import com.google.api.client.http.HttpTransport;
/*     */ import com.google.api.client.http.javanet.NetHttpTransport;
/*     */ import com.maxmind.geoip2.exception.AddressNotFoundException;
/*     */ import com.maxmind.geoip2.exception.AuthenticationException;
/*     */ import com.maxmind.geoip2.exception.GeoIp2Exception;
/*     */ import com.maxmind.geoip2.exception.HttpException;
/*     */ import com.maxmind.geoip2.exception.InvalidRequestException;
/*     */ import com.maxmind.geoip2.exception.OutOfQueriesException;
/*     */ import com.maxmind.geoip2.model.CityResponse;
/*     */ import com.maxmind.geoip2.model.CountryResponse;
/*     */ import com.maxmind.geoip2.model.InsightsResponse;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebServiceClient
/*     */   implements GeoIp2Provider
/*     */ {
/*     */   private final String host;
/*     */   private final List<String> locales;
/*     */   private final String licenseKey;
/*     */   private final int connectTimeout;
/*     */   private final int readTimeout;
/*     */   private final HttpTransport testTransport;
/*     */   private final int userId;
/*     */   
/*     */   private WebServiceClient(Builder builder)
/*     */   {
/*  97 */     this.host = builder.host;
/*  98 */     this.locales = builder.locales;
/*  99 */     this.licenseKey = builder.licenseKey;
/* 100 */     this.connectTimeout = builder.connectTimeout;
/* 101 */     this.readTimeout = builder.readTimeout;
/* 102 */     this.testTransport = builder.testTransport;
/* 103 */     this.userId = builder.userId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Builder
/*     */   {
/*     */     final int userId;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     final String licenseKey;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */     String host = "geoip.maxmind.com";
/* 129 */     List<String> locales = Collections.singletonList("en");
/* 130 */     int connectTimeout = 3000;
/* 131 */     int readTimeout = 20000;
/*     */     
/*     */ 
/*     */     HttpTransport testTransport;
/*     */     
/*     */ 
/*     */     public Builder(int userId, String licenseKey)
/*     */     {
/* 139 */       this.userId = userId;
/* 140 */       this.licenseKey = licenseKey;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder connectTimeout(int val)
/*     */     {
/* 149 */       this.connectTimeout = val;
/* 150 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder host(String val)
/*     */     {
/* 158 */       this.host = val;
/* 159 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder locales(List<String> val)
/*     */     {
/* 168 */       this.locales = val;
/* 169 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder readTimeout(int val)
/*     */     {
/* 179 */       this.readTimeout = val;
/* 180 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     Builder testTransport(HttpTransport val)
/*     */     {
/* 188 */       this.testTransport = val;
/* 189 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public WebServiceClient build()
/*     */     {
/* 197 */       return new WebServiceClient(this, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CountryResponse country()
/*     */     throws IOException, GeoIp2Exception
/*     */   {
/* 207 */     return country(null);
/*     */   }
/*     */   
/*     */   public CountryResponse country(InetAddress ipAddress)
/*     */     throws IOException, GeoIp2Exception
/*     */   {
/* 213 */     return (CountryResponse)responseFor("country", ipAddress, CountryResponse.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CityResponse city()
/*     */     throws IOException, GeoIp2Exception
/*     */   {
/* 222 */     return city(null);
/*     */   }
/*     */   
/*     */   public CityResponse city(InetAddress ipAddress)
/*     */     throws IOException, GeoIp2Exception
/*     */   {
/* 228 */     return (CityResponse)responseFor("city", ipAddress, CityResponse.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public InsightsResponse insights()
/*     */     throws IOException, GeoIp2Exception
/*     */   {
/* 237 */     return insights(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InsightsResponse insights(InetAddress ipAddress)
/*     */     throws IOException, GeoIp2Exception
/*     */   {
/* 248 */     return (InsightsResponse)responseFor("insights", ipAddress, InsightsResponse.class);
/*     */   }
/*     */   
/*     */   private <T> T responseFor(String path, InetAddress ipAddress, Class<T> cls) throws GeoIp2Exception, IOException
/*     */   {
/* 253 */     GenericUrl uri = createUri(path, ipAddress);
/* 254 */     HttpResponse response = getResponse(uri);
/* 255 */     Long contentLength = response.getHeaders().getContentLength();
/*     */     
/* 257 */     if ((contentLength == null) || (contentLength.intValue() <= 0))
/*     */     {
/* 259 */       throw new HttpException("Received a 200 response for " + uri + " but there was no message body.", 200, uri.toURL());
/*     */     }
/*     */     
/* 262 */     String body = getSuccessBody(response, uri);
/*     */     
/* 264 */     InjectableValues inject = new InjectableValues.Std().addValue("locales", this.locales);
/*     */     
/* 266 */     ObjectMapper mapper = new ObjectMapper();
/* 267 */     mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
/*     */     
/*     */     try
/*     */     {
/* 271 */       return (T)mapper.reader(cls).with(inject).readValue(body);
/*     */     } catch (IOException e) {
/* 273 */       throw new GeoIp2Exception("Received a 200 response but not decode it as JSON: " + body);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HttpResponse getResponse(GenericUrl uri)
/*     */     throws GeoIp2Exception, IOException
/*     */   {
/* 284 */     HttpTransport transport = this.testTransport == null ? new NetHttpTransport() : this.testTransport;
/*     */     
/* 286 */     HttpRequestFactory requestFactory = transport.createRequestFactory();
/*     */     try
/*     */     {
/* 289 */       request = requestFactory.buildGetRequest(uri);
/*     */     } catch (IOException e) { HttpRequest request;
/* 291 */       throw new GeoIp2Exception("Error building request", e); }
/*     */     HttpRequest request;
/* 293 */     request.setConnectTimeout(this.connectTimeout);
/* 294 */     request.setReadTimeout(this.readTimeout);
/*     */     
/* 296 */     HttpHeaders headers = request.getHeaders();
/* 297 */     headers.setAccept("application/json");
/* 298 */     headers.setBasicAuthentication(String.valueOf(this.userId), this.licenseKey);
/*     */     
/* 300 */     headers.setUserAgent("GeoIP2 Java Client v" + 
/* 301 */       getClass().getPackage().getImplementationVersion() + ";");
/*     */     try
/*     */     {
/* 304 */       return request.execute();
/*     */     } catch (HttpResponseException e) {
/* 306 */       int status = e.getStatusCode();
/* 307 */       if ((status >= 400) && (status < 500)) {
/* 308 */         handle4xxStatus(e.getContent(), status, uri);
/* 309 */       } else if ((status >= 500) && (status < 600))
/*     */       {
/* 311 */         throw new HttpException("Received a server error (" + status + ") for " + uri, status, uri.toURL());
/*     */       }
/*     */       
/*     */ 
/* 315 */       throw new HttpException("Received a very surprising HTTP status (" + status + ") for " + uri, status, uri.toURL());
/*     */     }
/*     */   }
/*     */   
/*     */   private static String getSuccessBody(HttpResponse response, GenericUrl uri) throws GeoIp2Exception
/*     */   {
/*     */     try
/*     */     {
/* 323 */       body = response.parseAsString();
/*     */     }
/*     */     catch (IOException e) {
/*     */       String body;
/* 327 */       throw new GeoIp2Exception("Received a 200 response but not decode message body: " + e.getMessage());
/*     */     }
/*     */     String body;
/* 330 */     if ((response.getContentType() == null) || 
/* 331 */       (!response.getContentType().contains("json"))) {
/* 332 */       throw new GeoIp2Exception("Received a 200 response for " + uri + " but it does not appear to be JSON:\n" + body);
/*     */     }
/*     */     
/* 335 */     return body;
/*     */   }
/*     */   
/*     */   private static void handle4xxStatus(String body, int status, GenericUrl uri)
/*     */     throws GeoIp2Exception, HttpException
/*     */   {
/* 341 */     if (body == null)
/*     */     {
/* 343 */       throw new HttpException("Received a " + status + " error for " + uri + " with no body", status, uri.toURL());
/*     */     }
/*     */     try
/*     */     {
/* 347 */       ObjectMapper mapper = new ObjectMapper();
/* 348 */       Map<String, String> content = (Map)mapper.readValue(body, new TypeReference() {});
/* 351 */       handleErrorWithJsonBody(content, body, status, uri);
/*     */     } catch (HttpException e) {
/* 353 */       throw e;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 357 */       throw new HttpException("Received a " + status + " error for " + uri + " but it did not include the expected JSON body: " + body, status, uri.toURL());
/*     */     }
/*     */   }
/*     */   
/*     */   private static void handleErrorWithJsonBody(Map<String, String> content, String body, int status, GenericUrl uri)
/*     */     throws GeoIp2Exception, HttpException
/*     */   {
/* 364 */     String error = (String)content.get("error");
/* 365 */     String code = (String)content.get("code");
/*     */     
/* 367 */     if ((error == null) || (code == null))
/*     */     {
/*     */ 
/* 370 */       throw new HttpException("Response contains JSON but it does not specify code or error keys: " + body, status, uri.toURL());
/*     */     }
/*     */     
/* 373 */     if ((code.equals("IP_ADDRESS_NOT_FOUND")) || 
/* 374 */       (code.equals("IP_ADDRESS_RESERVED")))
/* 375 */       throw new AddressNotFoundException(error);
/* 376 */     if ((code.equals("AUTHORIZATION_INVALID")) || 
/* 377 */       (code.equals("LICENSE_KEY_REQUIRED")) || 
/* 378 */       (code.equals("USER_ID_REQUIRED")))
/* 379 */       throw new AuthenticationException(error);
/* 380 */     if (code.equals("OUT_OF_QUERIES")) {
/* 381 */       throw new OutOfQueriesException(error);
/*     */     }
/*     */     
/*     */ 
/* 385 */     throw new InvalidRequestException(error, code, uri.toURL());
/*     */   }
/*     */   
/*     */   private GenericUrl createUri(String path, InetAddress ipAddress)
/*     */   {
/* 390 */     return new GenericUrl("https://" + this.host + "/geoip/v2.1/" + path + "/" + (ipAddress == null ? "me" : ipAddress.getHostAddress()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\WebServiceClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */