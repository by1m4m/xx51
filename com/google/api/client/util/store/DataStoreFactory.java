package com.google.api.client.util.store;

import java.io.IOException;
import java.io.Serializable;

public abstract interface DataStoreFactory
{
  public abstract <V extends Serializable> DataStore<V> getDataStore(String paramString)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\store\DataStoreFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */