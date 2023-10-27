package com.google.api.client.http.javanet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract interface ConnectionFactory
{
  public abstract HttpURLConnection openConnection(URL paramURL)
    throws IOException, ClassCastException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\javanet\ConnectionFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */