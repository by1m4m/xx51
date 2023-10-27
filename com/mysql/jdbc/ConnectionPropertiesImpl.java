/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.log.Log;
/*      */ import com.mysql.jdbc.log.StandardLogger;
/*      */ import java.io.Serializable;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Field;
/*      */ import java.sql.DriverPropertyInfo;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.TreeMap;
/*      */ import javax.naming.RefAddr;
/*      */ import javax.naming.Reference;
/*      */ import javax.naming.StringRefAddr;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ConnectionPropertiesImpl
/*      */   implements Serializable, ConnectionProperties
/*      */ {
/*      */   private static final long serialVersionUID = 4257801713007640580L;
/*      */   
/*      */   static class BooleanConnectionProperty
/*      */     extends ConnectionPropertiesImpl.ConnectionProperty
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 2540132501709159404L;
/*      */     
/*      */     BooleanConnectionProperty(String propertyNameToSet, boolean defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*   71 */       super(Boolean.valueOf(defaultValueToSet), null, 0, 0, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     String[] getAllowableValues()
/*      */     {
/*   80 */       return new String[] { "true", "false", "yes", "no" };
/*      */     }
/*      */     
/*      */     boolean getValueAsBoolean() {
/*   84 */       return ((Boolean)this.valueAsObject).booleanValue();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean hasValueConstraints()
/*      */     {
/*   91 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */     void initializeFrom(String extractedValue, ExceptionInterceptor exceptionInterceptor)
/*      */       throws SQLException
/*      */     {
/*   98 */       if (extractedValue != null) {
/*   99 */         validateStringValues(extractedValue, exceptionInterceptor);
/*      */         
/*  101 */         this.valueAsObject = Boolean.valueOf((extractedValue.equalsIgnoreCase("TRUE")) || (extractedValue.equalsIgnoreCase("YES")));
/*      */       }
/*      */       else
/*      */       {
/*  105 */         this.valueAsObject = this.defaultValue;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean isRangeBased()
/*      */     {
/*  113 */       return false;
/*      */     }
/*      */     
/*      */     void setValue(boolean valueFlag) {
/*  117 */       this.valueAsObject = Boolean.valueOf(valueFlag);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static abstract class ConnectionProperty
/*      */     implements Serializable
/*      */   {
/*      */     static final long serialVersionUID = -6644853639584478367L;
/*      */     
/*      */     String[] allowableValues;
/*      */     
/*      */     String categoryName;
/*      */     
/*      */     Object defaultValue;
/*      */     
/*      */     int lowerBound;
/*      */     
/*      */     int order;
/*      */     
/*      */     String propertyName;
/*      */     
/*      */     String sinceVersion;
/*      */     
/*      */     int upperBound;
/*      */     
/*      */     Object valueAsObject;
/*      */     
/*      */     boolean required;
/*      */     
/*      */     String description;
/*      */     
/*      */ 
/*      */     public ConnectionProperty() {}
/*      */     
/*      */     ConnectionProperty(String propertyNameToSet, Object defaultValueToSet, String[] allowableValuesToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  154 */       this.description = descriptionToSet;
/*  155 */       this.propertyName = propertyNameToSet;
/*  156 */       this.defaultValue = defaultValueToSet;
/*  157 */       this.valueAsObject = defaultValueToSet;
/*  158 */       this.allowableValues = allowableValuesToSet;
/*  159 */       this.lowerBound = lowerBoundToSet;
/*  160 */       this.upperBound = upperBoundToSet;
/*  161 */       this.required = false;
/*  162 */       this.sinceVersion = sinceVersionToSet;
/*  163 */       this.categoryName = category;
/*  164 */       this.order = orderInCategory;
/*      */     }
/*      */     
/*      */     String[] getAllowableValues() {
/*  168 */       return this.allowableValues;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     String getCategoryName()
/*      */     {
/*  175 */       return this.categoryName;
/*      */     }
/*      */     
/*      */     Object getDefaultValue() {
/*  179 */       return this.defaultValue;
/*      */     }
/*      */     
/*      */     int getLowerBound() {
/*  183 */       return this.lowerBound;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     int getOrder()
/*      */     {
/*  190 */       return this.order;
/*      */     }
/*      */     
/*      */     String getPropertyName() {
/*  194 */       return this.propertyName;
/*      */     }
/*      */     
/*      */     int getUpperBound() {
/*  198 */       return this.upperBound;
/*      */     }
/*      */     
/*      */     Object getValueAsObject() {
/*  202 */       return this.valueAsObject;
/*      */     }
/*      */     
/*      */     abstract boolean hasValueConstraints();
/*      */     
/*      */     void initializeFrom(Properties extractFrom, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*  208 */       String extractedValue = extractFrom.getProperty(getPropertyName());
/*  209 */       extractFrom.remove(getPropertyName());
/*  210 */       initializeFrom(extractedValue, exceptionInterceptor);
/*      */     }
/*      */     
/*      */     void initializeFrom(Reference ref, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*  214 */       RefAddr refAddr = ref.get(getPropertyName());
/*      */       
/*  216 */       if (refAddr != null) {
/*  217 */         String refContentAsString = (String)refAddr.getContent();
/*      */         
/*  219 */         initializeFrom(refContentAsString, exceptionInterceptor);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     abstract void initializeFrom(String paramString, ExceptionInterceptor paramExceptionInterceptor)
/*      */       throws SQLException;
/*      */     
/*      */ 
/*      */     abstract boolean isRangeBased();
/*      */     
/*      */     void setCategoryName(String categoryName)
/*      */     {
/*  232 */       this.categoryName = categoryName;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     void setOrder(int order)
/*      */     {
/*  240 */       this.order = order;
/*      */     }
/*      */     
/*      */     void setValueAsObject(Object obj) {
/*  244 */       this.valueAsObject = obj;
/*      */     }
/*      */     
/*      */     void storeTo(Reference ref) {
/*  248 */       if (getValueAsObject() != null) {
/*  249 */         ref.add(new StringRefAddr(getPropertyName(), getValueAsObject().toString()));
/*      */       }
/*      */     }
/*      */     
/*      */     DriverPropertyInfo getAsDriverPropertyInfo()
/*      */     {
/*  255 */       DriverPropertyInfo dpi = new DriverPropertyInfo(this.propertyName, null);
/*  256 */       dpi.choices = getAllowableValues();
/*  257 */       dpi.value = (this.valueAsObject != null ? this.valueAsObject.toString() : null);
/*  258 */       dpi.required = this.required;
/*  259 */       dpi.description = this.description;
/*      */       
/*  261 */       return dpi;
/*      */     }
/*      */     
/*      */     void validateStringValues(String valueToValidate, ExceptionInterceptor exceptionInterceptor) throws SQLException
/*      */     {
/*  266 */       String[] validateAgainst = getAllowableValues();
/*      */       
/*  268 */       if (valueToValidate == null) {
/*  269 */         return;
/*      */       }
/*      */       
/*  272 */       if ((validateAgainst == null) || (validateAgainst.length == 0)) {
/*  273 */         return;
/*      */       }
/*      */       
/*  276 */       for (int i = 0; i < validateAgainst.length; i++) {
/*  277 */         if ((validateAgainst[i] != null) && (validateAgainst[i].equalsIgnoreCase(valueToValidate)))
/*      */         {
/*  279 */           return;
/*      */         }
/*      */       }
/*      */       
/*  283 */       StringBuffer errorMessageBuf = new StringBuffer();
/*      */       
/*  285 */       errorMessageBuf.append("The connection property '");
/*  286 */       errorMessageBuf.append(getPropertyName());
/*  287 */       errorMessageBuf.append("' only accepts values of the form: ");
/*      */       
/*  289 */       if (validateAgainst.length != 0) {
/*  290 */         errorMessageBuf.append("'");
/*  291 */         errorMessageBuf.append(validateAgainst[0]);
/*  292 */         errorMessageBuf.append("'");
/*      */         
/*  294 */         for (int i = 1; i < validateAgainst.length - 1; i++) {
/*  295 */           errorMessageBuf.append(", ");
/*  296 */           errorMessageBuf.append("'");
/*  297 */           errorMessageBuf.append(validateAgainst[i]);
/*  298 */           errorMessageBuf.append("'");
/*      */         }
/*      */         
/*  301 */         errorMessageBuf.append(" or '");
/*  302 */         errorMessageBuf.append(validateAgainst[(validateAgainst.length - 1)]);
/*      */         
/*  304 */         errorMessageBuf.append("'");
/*      */       }
/*      */       
/*  307 */       errorMessageBuf.append(". The value '");
/*  308 */       errorMessageBuf.append(valueToValidate);
/*  309 */       errorMessageBuf.append("' is not in this set.");
/*      */       
/*  311 */       throw SQLError.createSQLException(errorMessageBuf.toString(), "S1009", exceptionInterceptor);
/*      */     }
/*      */   }
/*      */   
/*      */   static class IntegerConnectionProperty
/*      */     extends ConnectionPropertiesImpl.ConnectionProperty
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -3004305481796850832L;
/*  320 */     int multiplier = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public IntegerConnectionProperty(String propertyNameToSet, Object defaultValueToSet, String[] allowableValuesToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  327 */       super(defaultValueToSet, allowableValuesToSet, lowerBoundToSet, upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     IntegerConnectionProperty(String propertyNameToSet, int defaultValueToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  336 */       super(Integer.valueOf(defaultValueToSet), null, lowerBoundToSet, upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     IntegerConnectionProperty(String propertyNameToSet, int defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  354 */       this(propertyNameToSet, defaultValueToSet, 0, 0, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     String[] getAllowableValues()
/*      */     {
/*  362 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     int getLowerBound()
/*      */     {
/*  369 */       return this.lowerBound;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     int getUpperBound()
/*      */     {
/*  376 */       return this.upperBound;
/*      */     }
/*      */     
/*      */     int getValueAsInt() {
/*  380 */       return ((Integer)this.valueAsObject).intValue();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean hasValueConstraints()
/*      */     {
/*  387 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     void initializeFrom(String extractedValue, ExceptionInterceptor exceptionInterceptor)
/*      */       throws SQLException
/*      */     {
/*  394 */       if (extractedValue != null) {
/*      */         try
/*      */         {
/*  397 */           int intValue = (int)(Double.valueOf(extractedValue).doubleValue() * this.multiplier);
/*      */           
/*  399 */           setValue(intValue, extractedValue, exceptionInterceptor);
/*      */         } catch (NumberFormatException nfe) {
/*  401 */           throw SQLError.createSQLException("The connection property '" + getPropertyName() + "' only accepts integer values. The value '" + extractedValue + "' can not be converted to an integer.", "S1009", exceptionInterceptor);
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  409 */         this.valueAsObject = this.defaultValue;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean isRangeBased()
/*      */     {
/*  417 */       return getUpperBound() != getLowerBound();
/*      */     }
/*      */     
/*      */     void setValue(int intValue, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*  421 */       setValue(intValue, null, exceptionInterceptor);
/*      */     }
/*      */     
/*      */     void setValue(int intValue, String valueAsString, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*  425 */       if ((isRangeBased()) && (
/*  426 */         (intValue < getLowerBound()) || (intValue > getUpperBound()))) {
/*  427 */         throw SQLError.createSQLException("The connection property '" + getPropertyName() + "' only accepts integer values in the range of " + getLowerBound() + " - " + getUpperBound() + ", the value '" + (valueAsString == null ? Integer.valueOf(intValue) : valueAsString) + "' exceeds this range.", "S1009", exceptionInterceptor);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  434 */       this.valueAsObject = Integer.valueOf(intValue);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class LongConnectionProperty
/*      */     extends ConnectionPropertiesImpl.IntegerConnectionProperty
/*      */   {
/*      */     private static final long serialVersionUID = 6068572984340480895L;
/*      */     
/*      */     LongConnectionProperty(String propertyNameToSet, long defaultValueToSet, long lowerBoundToSet, long upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  446 */       super(Long.valueOf(defaultValueToSet), null, (int)lowerBoundToSet, (int)upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     LongConnectionProperty(String propertyNameToSet, long defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  455 */       this(propertyNameToSet, defaultValueToSet, 0L, 0L, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */     void setValue(long longValue, ExceptionInterceptor exceptionInterceptor)
/*      */       throws SQLException
/*      */     {
/*  462 */       setValue(longValue, null, exceptionInterceptor);
/*      */     }
/*      */     
/*      */     void setValue(long longValue, String valueAsString, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*  466 */       if ((isRangeBased()) && (
/*  467 */         (longValue < getLowerBound()) || (longValue > getUpperBound()))) {
/*  468 */         throw SQLError.createSQLException("The connection property '" + getPropertyName() + "' only accepts long integer values in the range of " + getLowerBound() + " - " + getUpperBound() + ", the value '" + (valueAsString == null ? Long.valueOf(longValue) : valueAsString) + "' exceeds this range.", "S1009", exceptionInterceptor);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  474 */       this.valueAsObject = Long.valueOf(longValue);
/*      */     }
/*      */     
/*      */     long getValueAsLong() {
/*  478 */       return ((Long)this.valueAsObject).longValue();
/*      */     }
/*      */     
/*      */     void initializeFrom(String extractedValue, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*  482 */       if (extractedValue != null) {
/*      */         try
/*      */         {
/*  485 */           long longValue = Double.valueOf(extractedValue).longValue();
/*      */           
/*  487 */           setValue(longValue, extractedValue, exceptionInterceptor);
/*      */         } catch (NumberFormatException nfe) {
/*  489 */           throw SQLError.createSQLException("The connection property '" + getPropertyName() + "' only accepts long integer values. The value '" + extractedValue + "' can not be converted to a long integer.", "S1009", exceptionInterceptor);
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  497 */         this.valueAsObject = this.defaultValue;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static class MemorySizeConnectionProperty
/*      */     extends ConnectionPropertiesImpl.IntegerConnectionProperty
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 7351065128998572656L;
/*      */     private String valueAsString;
/*      */     
/*      */     MemorySizeConnectionProperty(String propertyNameToSet, int defaultValueToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  512 */       super(defaultValueToSet, lowerBoundToSet, upperBoundToSet, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */     void initializeFrom(String extractedValue, ExceptionInterceptor exceptionInterceptor)
/*      */       throws SQLException
/*      */     {
/*  518 */       this.valueAsString = extractedValue;
/*  519 */       this.multiplier = 1;
/*      */       
/*  521 */       if (extractedValue != null) {
/*  522 */         if ((extractedValue.endsWith("k")) || (extractedValue.endsWith("K")) || (extractedValue.endsWith("kb")) || (extractedValue.endsWith("Kb")) || (extractedValue.endsWith("kB")) || (extractedValue.endsWith("KB")))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  528 */           this.multiplier = 1024;
/*  529 */           int indexOfK = StringUtils.indexOfIgnoreCase(extractedValue, "k");
/*      */           
/*  531 */           extractedValue = extractedValue.substring(0, indexOfK);
/*  532 */         } else if ((extractedValue.endsWith("m")) || (extractedValue.endsWith("M")) || (extractedValue.endsWith("mb")) || (extractedValue.endsWith("Mb")) || (extractedValue.endsWith("mB")) || (extractedValue.endsWith("MB")))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  538 */           this.multiplier = 1048576;
/*  539 */           int indexOfM = StringUtils.indexOfIgnoreCase(extractedValue, "m");
/*      */           
/*  541 */           extractedValue = extractedValue.substring(0, indexOfM);
/*  542 */         } else if ((extractedValue.endsWith("g")) || (extractedValue.endsWith("G")) || (extractedValue.endsWith("gb")) || (extractedValue.endsWith("Gb")) || (extractedValue.endsWith("gB")) || (extractedValue.endsWith("GB")))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  548 */           this.multiplier = 1073741824;
/*  549 */           int indexOfG = StringUtils.indexOfIgnoreCase(extractedValue, "g");
/*      */           
/*  551 */           extractedValue = extractedValue.substring(0, indexOfG);
/*      */         }
/*      */       }
/*      */       
/*  555 */       super.initializeFrom(extractedValue, exceptionInterceptor);
/*      */     }
/*      */     
/*      */     void setValue(String value, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*  559 */       initializeFrom(value, exceptionInterceptor);
/*      */     }
/*      */     
/*      */     String getValueAsString() {
/*  563 */       return this.valueAsString;
/*      */     }
/*      */   }
/*      */   
/*      */   static class StringConnectionProperty
/*      */     extends ConnectionPropertiesImpl.ConnectionProperty implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 5432127962785948272L;
/*      */     
/*      */     StringConnectionProperty(String propertyNameToSet, String defaultValueToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  574 */       this(propertyNameToSet, defaultValueToSet, null, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     StringConnectionProperty(String propertyNameToSet, String defaultValueToSet, String[] allowableValuesToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory)
/*      */     {
/*  592 */       super(defaultValueToSet, allowableValuesToSet, 0, 0, descriptionToSet, sinceVersionToSet, category, orderInCategory);
/*      */     }
/*      */     
/*      */ 
/*      */     String getValueAsString()
/*      */     {
/*  598 */       return (String)this.valueAsObject;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean hasValueConstraints()
/*      */     {
/*  605 */       return (this.allowableValues != null) && (this.allowableValues.length > 0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     void initializeFrom(String extractedValue, ExceptionInterceptor exceptionInterceptor)
/*      */       throws SQLException
/*      */     {
/*  613 */       if (extractedValue != null) {
/*  614 */         validateStringValues(extractedValue, exceptionInterceptor);
/*      */         
/*  616 */         this.valueAsObject = extractedValue;
/*      */       } else {
/*  618 */         this.valueAsObject = this.defaultValue;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean isRangeBased()
/*      */     {
/*  626 */       return false;
/*      */     }
/*      */     
/*      */     void setValue(String valueFlag) {
/*  630 */       this.valueAsObject = valueFlag;
/*      */     }
/*      */   }
/*      */   
/*  634 */   private static final String CONNECTION_AND_AUTH_CATEGORY = Messages.getString("ConnectionProperties.categoryConnectionAuthentication");
/*      */   
/*  636 */   private static final String NETWORK_CATEGORY = Messages.getString("ConnectionProperties.categoryNetworking");
/*      */   
/*  638 */   private static final String DEBUGING_PROFILING_CATEGORY = Messages.getString("ConnectionProperties.categoryDebuggingProfiling");
/*      */   
/*  640 */   private static final String HA_CATEGORY = Messages.getString("ConnectionProperties.categorryHA");
/*      */   
/*  642 */   private static final String MISC_CATEGORY = Messages.getString("ConnectionProperties.categoryMisc");
/*      */   
/*  644 */   private static final String PERFORMANCE_CATEGORY = Messages.getString("ConnectionProperties.categoryPerformance");
/*      */   
/*  646 */   private static final String SECURITY_CATEGORY = Messages.getString("ConnectionProperties.categorySecurity");
/*      */   
/*  648 */   private static final String[] PROPERTY_CATEGORIES = { CONNECTION_AND_AUTH_CATEGORY, NETWORK_CATEGORY, HA_CATEGORY, SECURITY_CATEGORY, PERFORMANCE_CATEGORY, DEBUGING_PROFILING_CATEGORY, MISC_CATEGORY };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  653 */   private static final ArrayList<Field> PROPERTY_LIST = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  658 */   private static final String STANDARD_LOGGER_NAME = StandardLogger.class.getName();
/*      */   protected static final String ZERO_DATETIME_BEHAVIOR_CONVERT_TO_NULL = "convertToNull";
/*      */   protected static final String ZERO_DATETIME_BEHAVIOR_EXCEPTION = "exception";
/*      */   protected static final String ZERO_DATETIME_BEHAVIOR_ROUND = "round";
/*      */   private BooleanConnectionProperty allowLoadLocalInfile;
/*      */   private BooleanConnectionProperty allowMultiQueries;
/*      */   private BooleanConnectionProperty allowNanAndInf;
/*      */   private BooleanConnectionProperty allowUrlInLocalInfile;
/*      */   private BooleanConnectionProperty alwaysSendSetIsolation;
/*      */   private BooleanConnectionProperty autoClosePStmtStreams;
/*      */   private BooleanConnectionProperty allowMasterDownConnections;
/*      */   private BooleanConnectionProperty autoDeserialize;
/*      */   private BooleanConnectionProperty autoGenerateTestcaseScript;
/*      */   private boolean autoGenerateTestcaseScriptAsBoolean;
/*      */   private BooleanConnectionProperty autoReconnect;
/*      */   private BooleanConnectionProperty autoReconnectForPools;
/*      */   private boolean autoReconnectForPoolsAsBoolean;
/*      */   private MemorySizeConnectionProperty blobSendChunkSize;
/*      */   private BooleanConnectionProperty autoSlowLog;
/*      */   private BooleanConnectionProperty blobsAreStrings;
/*      */   
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*  668 */       Field[] declaredFields = ConnectionPropertiesImpl.class.getDeclaredFields();
/*      */       
/*      */ 
/*  671 */       for (int i = 0; i < declaredFields.length; i++) {
/*  672 */         if (ConnectionProperty.class.isAssignableFrom(declaredFields[i].getType())) {
/*  674 */           PROPERTY_LIST.add(declaredFields[i]);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  678 */       RuntimeException rtEx = new RuntimeException();
/*  679 */       rtEx.initCause(ex);
/*      */       
/*  681 */       throw rtEx;
/*      */     }
/*      */   }
/*      */   
/*      */   private BooleanConnectionProperty functionsNeverReturnBlobs;
/*      */   private BooleanConnectionProperty cacheCallableStatements;
/*      */   private BooleanConnectionProperty cachePreparedStatements;
/*      */   private BooleanConnectionProperty cacheResultSetMetadata;
/*      */   private boolean cacheResultSetMetaDataAsBoolean;
/*      */   private StringConnectionProperty serverConfigCacheFactory;
/*      */   
/*      */   public ExceptionInterceptor getExceptionInterceptor()
/*      */   {
/*  686 */     return null;
/*      */   }
/*      */   
/*      */   private BooleanConnectionProperty cacheServerConfiguration;
/*      */   private IntegerConnectionProperty callableStatementCacheSize;
/*      */   private BooleanConnectionProperty capitalizeTypeNames;
/*      */   private StringConnectionProperty characterEncoding;
/*      */   private String characterEncodingAsString;
/*      */   protected boolean characterEncodingIsAliasForSjis;
/*      */   private StringConnectionProperty characterSetResults;
/*      */   private StringConnectionProperty connectionAttributes;
/*      */   private StringConnectionProperty clientInfoProvider;
/*      */   private BooleanConnectionProperty clobberStreamingResults;
/*      */   private StringConnectionProperty clobCharacterEncoding;
/*      */   private BooleanConnectionProperty compensateOnDuplicateKeyUpdateCounts;
/*      */   private StringConnectionProperty connectionCollation;
/*      */   private StringConnectionProperty connectionLifecycleInterceptors;
/*      */   private IntegerConnectionProperty connectTimeout;
/*      */   private BooleanConnectionProperty continueBatchOnError;
/*      */   private BooleanConnectionProperty createDatabaseIfNotExist;
/*      */   
/*      */   protected static DriverPropertyInfo[] exposeAsDriverPropertyInfo(Properties info, int slotsToReserve)
/*      */     throws SQLException
/*      */   {
/*  705 */     new ConnectionPropertiesImpl() { private static final long serialVersionUID = 4257801713007640581L; }.exposeAsDriverPropertyInfoInternal(info, slotsToReserve);
/*      */   }
/*      */   
/*      */   public ConnectionPropertiesImpl()
/*      */   {
/*  710 */     this.allowLoadLocalInfile = new BooleanConnectionProperty("allowLoadLocalInfile", true, Messages.getString("ConnectionProperties.loadDataLocal"), "3.0.3", SECURITY_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  716 */     this.allowMultiQueries = new BooleanConnectionProperty("allowMultiQueries", false, Messages.getString("ConnectionProperties.allowMultiQueries"), "3.1.1", SECURITY_CATEGORY, 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  722 */     this.allowNanAndInf = new BooleanConnectionProperty("allowNanAndInf", false, Messages.getString("ConnectionProperties.allowNANandINF"), "3.1.5", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  728 */     this.allowUrlInLocalInfile = new BooleanConnectionProperty("allowUrlInLocalInfile", false, Messages.getString("ConnectionProperties.allowUrlInLoadLocal"), "3.1.4", SECURITY_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  734 */     this.alwaysSendSetIsolation = new BooleanConnectionProperty("alwaysSendSetIsolation", true, Messages.getString("ConnectionProperties.alwaysSendSetIsolation"), "3.1.7", PERFORMANCE_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  740 */     this.autoClosePStmtStreams = new BooleanConnectionProperty("autoClosePStmtStreams", false, Messages.getString("ConnectionProperties.autoClosePstmtStreams"), "3.1.12", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  748 */     this.allowMasterDownConnections = new BooleanConnectionProperty("allowMasterDownConnections", false, Messages.getString("ConnectionProperties.allowMasterDownConnections"), "5.1.27", HA_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  756 */     this.autoDeserialize = new BooleanConnectionProperty("autoDeserialize", false, Messages.getString("ConnectionProperties.autoDeserialize"), "3.1.5", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  762 */     this.autoGenerateTestcaseScript = new BooleanConnectionProperty("autoGenerateTestcaseScript", false, Messages.getString("ConnectionProperties.autoGenerateTestcaseScript"), "3.1.9", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  767 */     this.autoGenerateTestcaseScriptAsBoolean = false;
/*      */     
/*  769 */     this.autoReconnect = new BooleanConnectionProperty("autoReconnect", false, Messages.getString("ConnectionProperties.autoReconnect"), "1.1", HA_CATEGORY, 0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  775 */     this.autoReconnectForPools = new BooleanConnectionProperty("autoReconnectForPools", false, Messages.getString("ConnectionProperties.autoReconnectForPools"), "3.1.3", HA_CATEGORY, 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  781 */     this.autoReconnectForPoolsAsBoolean = false;
/*      */     
/*  783 */     this.blobSendChunkSize = new MemorySizeConnectionProperty("blobSendChunkSize", 1048576, 0, 0, Messages.getString("ConnectionProperties.blobSendChunkSize"), "3.1.9", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  791 */     this.autoSlowLog = new BooleanConnectionProperty("autoSlowLog", true, Messages.getString("ConnectionProperties.autoSlowLog"), "5.1.4", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  796 */     this.blobsAreStrings = new BooleanConnectionProperty("blobsAreStrings", false, "Should the driver always treat BLOBs as Strings - specifically to work around dubious metadata returned by the server for GROUP BY clauses?", "5.0.8", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  802 */     this.functionsNeverReturnBlobs = new BooleanConnectionProperty("functionsNeverReturnBlobs", false, "Should the driver always treat data from functions returning BLOBs as Strings - specifically to work around dubious metadata returned by the server for GROUP BY clauses?", "5.0.8", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  808 */     this.cacheCallableStatements = new BooleanConnectionProperty("cacheCallableStmts", false, Messages.getString("ConnectionProperties.cacheCallableStatements"), "3.1.2", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  813 */     this.cachePreparedStatements = new BooleanConnectionProperty("cachePrepStmts", false, Messages.getString("ConnectionProperties.cachePrepStmts"), "3.0.10", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  819 */     this.cacheResultSetMetadata = new BooleanConnectionProperty("cacheResultSetMetadata", false, Messages.getString("ConnectionProperties.cacheRSMetadata"), "3.1.1", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  827 */     this.serverConfigCacheFactory = new StringConnectionProperty("serverConfigCacheFactory", PerVmServerConfigCacheFactory.class.getName(), Messages.getString("ConnectionProperties.serverConfigCacheFactory"), "5.1.1", PERFORMANCE_CATEGORY, 12);
/*      */     
/*      */ 
/*  830 */     this.cacheServerConfiguration = new BooleanConnectionProperty("cacheServerConfiguration", false, Messages.getString("ConnectionProperties.cacheServerConfiguration"), "3.1.5", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  836 */     this.callableStatementCacheSize = new IntegerConnectionProperty("callableStmtCacheSize", 100, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.callableStmtCacheSize"), "3.1.2", PERFORMANCE_CATEGORY, 5);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  844 */     this.capitalizeTypeNames = new BooleanConnectionProperty("capitalizeTypeNames", true, Messages.getString("ConnectionProperties.capitalizeTypeNames"), "2.0.7", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  850 */     this.characterEncoding = new StringConnectionProperty("characterEncoding", null, Messages.getString("ConnectionProperties.characterEncoding"), "1.1g", MISC_CATEGORY, 5);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  856 */     this.characterEncodingAsString = null;
/*      */     
/*  858 */     this.characterEncodingIsAliasForSjis = false;
/*      */     
/*  860 */     this.characterSetResults = new StringConnectionProperty("characterSetResults", null, Messages.getString("ConnectionProperties.characterSetResults"), "3.0.13", MISC_CATEGORY, 6);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  865 */     this.connectionAttributes = new StringConnectionProperty("connectionAttributes", null, Messages.getString("ConnectionProperties.connectionAttributes"), "5.1.25", MISC_CATEGORY, 7);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  871 */     this.clientInfoProvider = new StringConnectionProperty("clientInfoProvider", "com.mysql.jdbc.JDBC4CommentClientInfoProvider", Messages.getString("ConnectionProperties.clientInfoProvider"), "5.1.0", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  877 */     this.clobberStreamingResults = new BooleanConnectionProperty("clobberStreamingResults", false, Messages.getString("ConnectionProperties.clobberStreamingResults"), "3.0.9", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  883 */     this.clobCharacterEncoding = new StringConnectionProperty("clobCharacterEncoding", null, Messages.getString("ConnectionProperties.clobCharacterEncoding"), "5.0.0", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  889 */     this.compensateOnDuplicateKeyUpdateCounts = new BooleanConnectionProperty("compensateOnDuplicateKeyUpdateCounts", false, Messages.getString("ConnectionProperties.compensateOnDuplicateKeyUpdateCounts"), "5.1.7", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  894 */     this.connectionCollation = new StringConnectionProperty("connectionCollation", null, Messages.getString("ConnectionProperties.connectionCollation"), "3.0.13", MISC_CATEGORY, 7);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  900 */     this.connectionLifecycleInterceptors = new StringConnectionProperty("connectionLifecycleInterceptors", null, Messages.getString("ConnectionProperties.connectionLifecycleInterceptors"), "5.1.4", CONNECTION_AND_AUTH_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  906 */     this.connectTimeout = new IntegerConnectionProperty("connectTimeout", 0, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.connectTimeout"), "3.0.1", CONNECTION_AND_AUTH_CATEGORY, 9);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  911 */     this.continueBatchOnError = new BooleanConnectionProperty("continueBatchOnError", true, Messages.getString("ConnectionProperties.continueBatchOnError"), "3.0.3", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  917 */     this.createDatabaseIfNotExist = new BooleanConnectionProperty("createDatabaseIfNotExist", false, Messages.getString("ConnectionProperties.createDatabaseIfNotExist"), "3.1.9", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  923 */     this.defaultFetchSize = new IntegerConnectionProperty("defaultFetchSize", 0, Messages.getString("ConnectionProperties.defaultFetchSize"), "3.1.9", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  929 */     this.detectServerPreparedStmts = new BooleanConnectionProperty("useServerPrepStmts", false, Messages.getString("ConnectionProperties.useServerPrepStmts"), "3.1.0", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  935 */     this.dontTrackOpenResources = new BooleanConnectionProperty("dontTrackOpenResources", false, Messages.getString("ConnectionProperties.dontTrackOpenResources"), "3.1.7", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  941 */     this.dumpQueriesOnException = new BooleanConnectionProperty("dumpQueriesOnException", false, Messages.getString("ConnectionProperties.dumpQueriesOnException"), "3.1.3", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  947 */     this.dynamicCalendars = new BooleanConnectionProperty("dynamicCalendars", false, Messages.getString("ConnectionProperties.dynamicCalendars"), "3.1.5", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  953 */     this.elideSetAutoCommits = new BooleanConnectionProperty("elideSetAutoCommits", false, Messages.getString("ConnectionProperties.eliseSetAutoCommit"), "3.1.3", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  959 */     this.emptyStringsConvertToZero = new BooleanConnectionProperty("emptyStringsConvertToZero", true, Messages.getString("ConnectionProperties.emptyStringsConvertToZero"), "3.1.8", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  964 */     this.emulateLocators = new BooleanConnectionProperty("emulateLocators", false, Messages.getString("ConnectionProperties.emulateLocators"), "3.1.0", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*  968 */     this.emulateUnsupportedPstmts = new BooleanConnectionProperty("emulateUnsupportedPstmts", true, Messages.getString("ConnectionProperties.emulateUnsupportedPstmts"), "3.1.7", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  974 */     this.enablePacketDebug = new BooleanConnectionProperty("enablePacketDebug", false, Messages.getString("ConnectionProperties.enablePacketDebug"), "3.1.3", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  980 */     this.enableQueryTimeouts = new BooleanConnectionProperty("enableQueryTimeouts", true, Messages.getString("ConnectionProperties.enableQueryTimeouts"), "5.0.6", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  987 */     this.explainSlowQueries = new BooleanConnectionProperty("explainSlowQueries", false, Messages.getString("ConnectionProperties.explainSlowQueries"), "3.1.2", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  993 */     this.exceptionInterceptors = new StringConnectionProperty("exceptionInterceptors", null, Messages.getString("ConnectionProperties.exceptionInterceptors"), "5.1.8", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1000 */     this.failOverReadOnly = new BooleanConnectionProperty("failOverReadOnly", true, Messages.getString("ConnectionProperties.failoverReadOnly"), "3.0.12", HA_CATEGORY, 2);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1006 */     this.gatherPerformanceMetrics = new BooleanConnectionProperty("gatherPerfMetrics", false, Messages.getString("ConnectionProperties.gatherPerfMetrics"), "3.1.2", DEBUGING_PROFILING_CATEGORY, 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1012 */     this.generateSimpleParameterMetadata = new BooleanConnectionProperty("generateSimpleParameterMetadata", false, Messages.getString("ConnectionProperties.generateSimpleParameterMetadata"), "5.0.5", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/* 1015 */     this.highAvailabilityAsBoolean = false;
/*      */     
/* 1017 */     this.holdResultsOpenOverStatementClose = new BooleanConnectionProperty("holdResultsOpenOverStatementClose", false, Messages.getString("ConnectionProperties.holdRSOpenOverStmtClose"), "3.1.7", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1023 */     this.includeInnodbStatusInDeadlockExceptions = new BooleanConnectionProperty("includeInnodbStatusInDeadlockExceptions", false, Messages.getString("ConnectionProperties.includeInnodbStatusInDeadlockExceptions"), "5.0.7", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1029 */     this.includeThreadDumpInDeadlockExceptions = new BooleanConnectionProperty("includeThreadDumpInDeadlockExceptions", false, Messages.getString("ConnectionProperties.includeThreadDumpInDeadlockExceptions"), "5.1.15", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1035 */     this.includeThreadNamesAsStatementComment = new BooleanConnectionProperty("includeThreadNamesAsStatementComment", false, Messages.getString("ConnectionProperties.includeThreadNamesAsStatementComment"), "5.1.15", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1041 */     this.ignoreNonTxTables = new BooleanConnectionProperty("ignoreNonTxTables", false, Messages.getString("ConnectionProperties.ignoreNonTxTables"), "3.0.9", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1047 */     this.initialTimeout = new IntegerConnectionProperty("initialTimeout", 2, 1, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.initialTimeout"), "1.1", HA_CATEGORY, 5);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1052 */     this.isInteractiveClient = new BooleanConnectionProperty("interactiveClient", false, Messages.getString("ConnectionProperties.interactiveClient"), "3.1.0", CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1058 */     this.jdbcCompliantTruncation = new BooleanConnectionProperty("jdbcCompliantTruncation", true, Messages.getString("ConnectionProperties.jdbcCompliantTruncation"), "3.1.2", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1064 */     this.jdbcCompliantTruncationForReads = this.jdbcCompliantTruncation.getValueAsBoolean();
/*      */     
/*      */ 
/* 1067 */     this.largeRowSizeThreshold = new MemorySizeConnectionProperty("largeRowSizeThreshold", 2048, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.largeRowSizeThreshold"), "5.1.1", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1072 */     this.loadBalanceStrategy = new StringConnectionProperty("loadBalanceStrategy", "random", null, Messages.getString("ConnectionProperties.loadBalanceStrategy"), "5.0.6", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1079 */     this.loadBalanceBlacklistTimeout = new IntegerConnectionProperty("loadBalanceBlacklistTimeout", 0, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.loadBalanceBlacklistTimeout"), "5.1.0", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1085 */     this.loadBalancePingTimeout = new IntegerConnectionProperty("loadBalancePingTimeout", 0, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.loadBalancePingTimeout"), "5.1.13", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1091 */     this.loadBalanceValidateConnectionOnSwapServer = new BooleanConnectionProperty("loadBalanceValidateConnectionOnSwapServer", false, Messages.getString("ConnectionProperties.loadBalanceValidateConnectionOnSwapServer"), "5.1.13", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1097 */     this.loadBalanceConnectionGroup = new StringConnectionProperty("loadBalanceConnectionGroup", null, Messages.getString("ConnectionProperties.loadBalanceConnectionGroup"), "5.1.13", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1103 */     this.loadBalanceExceptionChecker = new StringConnectionProperty("loadBalanceExceptionChecker", "com.mysql.jdbc.StandardLoadBalanceExceptionChecker", null, Messages.getString("ConnectionProperties.loadBalanceExceptionChecker"), "5.1.13", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1110 */     this.loadBalanceSQLStateFailover = new StringConnectionProperty("loadBalanceSQLStateFailover", null, Messages.getString("ConnectionProperties.loadBalanceSQLStateFailover"), "5.1.13", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1116 */     this.loadBalanceSQLExceptionSubclassFailover = new StringConnectionProperty("loadBalanceSQLExceptionSubclassFailover", null, Messages.getString("ConnectionProperties.loadBalanceSQLExceptionSubclassFailover"), "5.1.13", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1122 */     this.loadBalanceEnableJMX = new BooleanConnectionProperty("loadBalanceEnableJMX", false, Messages.getString("ConnectionProperties.loadBalanceEnableJMX"), "5.1.13", MISC_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1128 */     this.loadBalanceAutoCommitStatementRegex = new StringConnectionProperty("loadBalanceAutoCommitStatementRegex", null, Messages.getString("ConnectionProperties.loadBalanceAutoCommitStatementRegex"), "5.1.15", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1134 */     this.loadBalanceAutoCommitStatementThreshold = new IntegerConnectionProperty("loadBalanceAutoCommitStatementThreshold", 0, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.loadBalanceAutoCommitStatementThreshold"), "5.1.15", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1141 */     this.localSocketAddress = new StringConnectionProperty("localSocketAddress", null, Messages.getString("ConnectionProperties.localSocketAddress"), "5.0.5", CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/* 1145 */     this.locatorFetchBufferSize = new MemorySizeConnectionProperty("locatorFetchBufferSize", 1048576, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.locatorFetchBufferSize"), "3.2.1", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1153 */     this.loggerClassName = new StringConnectionProperty("logger", STANDARD_LOGGER_NAME, Messages.getString("ConnectionProperties.logger", new Object[] { Log.class.getName(), STANDARD_LOGGER_NAME }), "3.1.1", DEBUGING_PROFILING_CATEGORY, 0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1159 */     this.logSlowQueries = new BooleanConnectionProperty("logSlowQueries", false, Messages.getString("ConnectionProperties.logSlowQueries"), "3.1.2", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1165 */     this.logXaCommands = new BooleanConnectionProperty("logXaCommands", false, Messages.getString("ConnectionProperties.logXaCommands"), "5.0.5", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1171 */     this.maintainTimeStats = new BooleanConnectionProperty("maintainTimeStats", true, Messages.getString("ConnectionProperties.maintainTimeStats"), "3.1.9", PERFORMANCE_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1177 */     this.maintainTimeStatsAsBoolean = true;
/*      */     
/* 1179 */     this.maxQuerySizeToLog = new IntegerConnectionProperty("maxQuerySizeToLog", 2048, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.maxQuerySizeToLog"), "3.1.3", DEBUGING_PROFILING_CATEGORY, 4);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1187 */     this.maxReconnects = new IntegerConnectionProperty("maxReconnects", 3, 1, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.maxReconnects"), "1.1", HA_CATEGORY, 4);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1195 */     this.retriesAllDown = new IntegerConnectionProperty("retriesAllDown", 120, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.retriesAllDown"), "5.1.6", HA_CATEGORY, 4);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1203 */     this.maxRows = new IntegerConnectionProperty("maxRows", -1, -1, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.maxRows"), Messages.getString("ConnectionProperties.allVersions"), MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1208 */     this.maxRowsAsInt = -1;
/*      */     
/* 1210 */     this.metadataCacheSize = new IntegerConnectionProperty("metadataCacheSize", 50, 1, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.metadataCacheSize"), "3.1.1", PERFORMANCE_CATEGORY, 5);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1218 */     this.netTimeoutForStreamingResults = new IntegerConnectionProperty("netTimeoutForStreamingResults", 600, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.netTimeoutForStreamingResults"), "5.1.0", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1224 */     this.noAccessToProcedureBodies = new BooleanConnectionProperty("noAccessToProcedureBodies", false, "When determining procedure parameter types for CallableStatements, and the connected user  can't access procedure bodies through \"SHOW CREATE PROCEDURE\" or select on mysql.proc  should the driver instead create basic metadata (all parameters reported as IN VARCHARs, but allowing registerOutParameter() to be called on them anyway) instead  of throwing an exception?", "5.0.3", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1234 */     this.noDatetimeStringSync = new BooleanConnectionProperty("noDatetimeStringSync", false, Messages.getString("ConnectionProperties.noDatetimeStringSync"), "3.1.7", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1240 */     this.noTimezoneConversionForTimeType = new BooleanConnectionProperty("noTimezoneConversionForTimeType", false, Messages.getString("ConnectionProperties.noTzConversionForTimeType"), "5.0.0", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1246 */     this.nullCatalogMeansCurrent = new BooleanConnectionProperty("nullCatalogMeansCurrent", true, Messages.getString("ConnectionProperties.nullCatalogMeansCurrent"), "3.1.8", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1252 */     this.nullNamePatternMatchesAll = new BooleanConnectionProperty("nullNamePatternMatchesAll", true, Messages.getString("ConnectionProperties.nullNamePatternMatchesAll"), "3.1.8", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1258 */     this.packetDebugBufferSize = new IntegerConnectionProperty("packetDebugBufferSize", 20, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.packetDebugBufferSize"), "3.1.3", DEBUGING_PROFILING_CATEGORY, 7);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1266 */     this.padCharsWithSpace = new BooleanConnectionProperty("padCharsWithSpace", false, Messages.getString("ConnectionProperties.padCharsWithSpace"), "5.0.6", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1274 */     this.paranoid = new BooleanConnectionProperty("paranoid", false, Messages.getString("ConnectionProperties.paranoid"), "3.0.1", SECURITY_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1280 */     this.pedantic = new BooleanConnectionProperty("pedantic", false, Messages.getString("ConnectionProperties.pedantic"), "3.0.0", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/* 1284 */     this.pinGlobalTxToPhysicalConnection = new BooleanConnectionProperty("pinGlobalTxToPhysicalConnection", false, Messages.getString("ConnectionProperties.pinGlobalTxToPhysicalConnection"), "5.0.1", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/* 1288 */     this.populateInsertRowWithDefaultValues = new BooleanConnectionProperty("populateInsertRowWithDefaultValues", false, Messages.getString("ConnectionProperties.populateInsertRowWithDefaultValues"), "5.0.5", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1293 */     this.preparedStatementCacheSize = new IntegerConnectionProperty("prepStmtCacheSize", 25, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.prepStmtCacheSize"), "3.0.10", PERFORMANCE_CATEGORY, 10);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1298 */     this.preparedStatementCacheSqlLimit = new IntegerConnectionProperty("prepStmtCacheSqlLimit", 256, 1, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.prepStmtCacheSqlLimit"), "3.0.10", PERFORMANCE_CATEGORY, 11);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1306 */     this.parseInfoCacheFactory = new StringConnectionProperty("parseInfoCacheFactory", PerConnectionLRUFactory.class.getName(), Messages.getString("ConnectionProperties.parseInfoCacheFactory"), "5.1.1", PERFORMANCE_CATEGORY, 12);
/*      */     
/*      */ 
/* 1309 */     this.processEscapeCodesForPrepStmts = new BooleanConnectionProperty("processEscapeCodesForPrepStmts", true, Messages.getString("ConnectionProperties.processEscapeCodesForPrepStmts"), "3.1.12", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1316 */     this.profilerEventHandler = new StringConnectionProperty("profilerEventHandler", "com.mysql.jdbc.profiler.LoggingProfilerEventHandler", Messages.getString("ConnectionProperties.profilerEventHandler"), "5.1.6", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1322 */     this.profileSql = new StringConnectionProperty("profileSql", null, Messages.getString("ConnectionProperties.profileSqlDeprecated"), "2.0.14", DEBUGING_PROFILING_CATEGORY, 3);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1328 */     this.profileSQL = new BooleanConnectionProperty("profileSQL", false, Messages.getString("ConnectionProperties.profileSQL"), "3.1.0", DEBUGING_PROFILING_CATEGORY, 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1334 */     this.profileSQLAsBoolean = false;
/*      */     
/* 1336 */     this.propertiesTransform = new StringConnectionProperty("propertiesTransform", null, Messages.getString("ConnectionProperties.connectionPropertiesTransform"), "3.1.4", CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1342 */     this.queriesBeforeRetryMaster = new IntegerConnectionProperty("queriesBeforeRetryMaster", 50, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.queriesBeforeRetryMaster"), "3.0.2", HA_CATEGORY, 7);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1350 */     this.queryTimeoutKillsConnection = new BooleanConnectionProperty("queryTimeoutKillsConnection", false, Messages.getString("ConnectionProperties.queryTimeoutKillsConnection"), "5.1.9", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/* 1354 */     this.reconnectAtTxEnd = new BooleanConnectionProperty("reconnectAtTxEnd", false, Messages.getString("ConnectionProperties.reconnectAtTxEnd"), "3.0.10", HA_CATEGORY, 4);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1359 */     this.reconnectTxAtEndAsBoolean = false;
/*      */     
/* 1361 */     this.relaxAutoCommit = new BooleanConnectionProperty("relaxAutoCommit", false, Messages.getString("ConnectionProperties.relaxAutoCommit"), "2.0.13", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1367 */     this.reportMetricsIntervalMillis = new IntegerConnectionProperty("reportMetricsIntervalMillis", 30000, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.reportMetricsIntervalMillis"), "3.1.2", DEBUGING_PROFILING_CATEGORY, 3);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1375 */     this.requireSSL = new BooleanConnectionProperty("requireSSL", false, Messages.getString("ConnectionProperties.requireSSL"), "3.1.0", SECURITY_CATEGORY, 3);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1380 */     this.resourceId = new StringConnectionProperty("resourceId", null, Messages.getString("ConnectionProperties.resourceId"), "5.0.1", HA_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1387 */     this.resultSetSizeThreshold = new IntegerConnectionProperty("resultSetSizeThreshold", 100, Messages.getString("ConnectionProperties.resultSetSizeThreshold"), "5.0.5", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/* 1390 */     this.retainStatementAfterResultSetClose = new BooleanConnectionProperty("retainStatementAfterResultSetClose", false, Messages.getString("ConnectionProperties.retainStatementAfterResultSetClose"), "3.1.11", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1396 */     this.rewriteBatchedStatements = new BooleanConnectionProperty("rewriteBatchedStatements", false, Messages.getString("ConnectionProperties.rewriteBatchedStatements"), "3.1.13", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1402 */     this.rollbackOnPooledClose = new BooleanConnectionProperty("rollbackOnPooledClose", true, Messages.getString("ConnectionProperties.rollbackOnPooledClose"), "3.0.15", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1408 */     this.roundRobinLoadBalance = new BooleanConnectionProperty("roundRobinLoadBalance", false, Messages.getString("ConnectionProperties.roundRobinLoadBalance"), "3.1.2", HA_CATEGORY, 5);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1414 */     this.runningCTS13 = new BooleanConnectionProperty("runningCTS13", false, Messages.getString("ConnectionProperties.runningCTS13"), "3.1.7", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1420 */     this.secondsBeforeRetryMaster = new IntegerConnectionProperty("secondsBeforeRetryMaster", 30, 1, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.secondsBeforeRetryMaster"), "3.0.2", HA_CATEGORY, 8);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1428 */     this.selfDestructOnPingSecondsLifetime = new IntegerConnectionProperty("selfDestructOnPingSecondsLifetime", 0, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.selfDestructOnPingSecondsLifetime"), "5.1.6", HA_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1436 */     this.selfDestructOnPingMaxOperations = new IntegerConnectionProperty("selfDestructOnPingMaxOperations", 0, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.selfDestructOnPingMaxOperations"), "5.1.6", HA_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1444 */     this.replicationEnableJMX = new BooleanConnectionProperty("replicationEnableJMX", false, Messages.getString("ConnectionProperties.loadBalanceEnableJMX"), "5.1.27", HA_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1450 */     this.serverTimezone = new StringConnectionProperty("serverTimezone", null, Messages.getString("ConnectionProperties.serverTimezone"), "3.0.2", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1456 */     this.sessionVariables = new StringConnectionProperty("sessionVariables", null, Messages.getString("ConnectionProperties.sessionVariables"), "3.1.8", MISC_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1461 */     this.slowQueryThresholdMillis = new IntegerConnectionProperty("slowQueryThresholdMillis", 2000, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.slowQueryThresholdMillis"), "3.1.2", DEBUGING_PROFILING_CATEGORY, 9);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1469 */     this.slowQueryThresholdNanos = new LongConnectionProperty("slowQueryThresholdNanos", 0L, Messages.getString("ConnectionProperties.slowQueryThresholdNanos"), "5.0.7", DEBUGING_PROFILING_CATEGORY, 10);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1477 */     this.socketFactoryClassName = new StringConnectionProperty("socketFactory", StandardSocketFactory.class.getName(), Messages.getString("ConnectionProperties.socketFactory"), "3.0.3", CONNECTION_AND_AUTH_CATEGORY, 4);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1483 */     this.socketTimeout = new IntegerConnectionProperty("socketTimeout", 0, 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.socketTimeout"), "3.0.1", CONNECTION_AND_AUTH_CATEGORY, 10);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1491 */     this.statementInterceptors = new StringConnectionProperty("statementInterceptors", null, Messages.getString("ConnectionProperties.statementInterceptors"), "5.1.1", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/* 1494 */     this.strictFloatingPoint = new BooleanConnectionProperty("strictFloatingPoint", false, Messages.getString("ConnectionProperties.strictFloatingPoint"), "3.0.0", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1499 */     this.strictUpdates = new BooleanConnectionProperty("strictUpdates", true, Messages.getString("ConnectionProperties.strictUpdates"), "3.0.4", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1505 */     this.overrideSupportsIntegrityEnhancementFacility = new BooleanConnectionProperty("overrideSupportsIntegrityEnhancementFacility", false, Messages.getString("ConnectionProperties.overrideSupportsIEF"), "3.1.12", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1511 */     this.tcpNoDelay = new BooleanConnectionProperty("tcpNoDelay", Boolean.valueOf("true").booleanValue(), Messages.getString("ConnectionProperties.tcpNoDelay"), "5.0.7", NETWORK_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1517 */     this.tcpKeepAlive = new BooleanConnectionProperty("tcpKeepAlive", Boolean.valueOf("true").booleanValue(), Messages.getString("ConnectionProperties.tcpKeepAlive"), "5.0.7", NETWORK_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1523 */     this.tcpRcvBuf = new IntegerConnectionProperty("tcpRcvBuf", Integer.parseInt("0"), 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.tcpSoRcvBuf"), "5.0.7", NETWORK_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1530 */     this.tcpSndBuf = new IntegerConnectionProperty("tcpSndBuf", Integer.parseInt("0"), 0, Integer.MAX_VALUE, Messages.getString("ConnectionProperties.tcpSoSndBuf"), "5.0.7", NETWORK_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1537 */     this.tcpTrafficClass = new IntegerConnectionProperty("tcpTrafficClass", Integer.parseInt("0"), 0, 255, Messages.getString("ConnectionProperties.tcpTrafficClass"), "5.0.7", NETWORK_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1544 */     this.tinyInt1isBit = new BooleanConnectionProperty("tinyInt1isBit", true, Messages.getString("ConnectionProperties.tinyInt1isBit"), "3.0.16", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1550 */     this.traceProtocol = new BooleanConnectionProperty("traceProtocol", false, Messages.getString("ConnectionProperties.traceProtocol"), "3.1.2", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1555 */     this.treatUtilDateAsTimestamp = new BooleanConnectionProperty("treatUtilDateAsTimestamp", true, Messages.getString("ConnectionProperties.treatUtilDateAsTimestamp"), "5.0.5", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1560 */     this.transformedBitIsBoolean = new BooleanConnectionProperty("transformedBitIsBoolean", false, Messages.getString("ConnectionProperties.transformedBitIsBoolean"), "3.1.9", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1566 */     this.useBlobToStoreUTF8OutsideBMP = new BooleanConnectionProperty("useBlobToStoreUTF8OutsideBMP", false, Messages.getString("ConnectionProperties.useBlobToStoreUTF8OutsideBMP"), "5.1.3", MISC_CATEGORY, 128);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1572 */     this.utf8OutsideBmpExcludedColumnNamePattern = new StringConnectionProperty("utf8OutsideBmpExcludedColumnNamePattern", null, Messages.getString("ConnectionProperties.utf8OutsideBmpExcludedColumnNamePattern"), "5.1.3", MISC_CATEGORY, 129);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1578 */     this.utf8OutsideBmpIncludedColumnNamePattern = new StringConnectionProperty("utf8OutsideBmpIncludedColumnNamePattern", null, Messages.getString("ConnectionProperties.utf8OutsideBmpIncludedColumnNamePattern"), "5.1.3", MISC_CATEGORY, 129);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1584 */     this.useCompression = new BooleanConnectionProperty("useCompression", false, Messages.getString("ConnectionProperties.useCompression"), "3.0.17", CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1590 */     this.useColumnNamesInFindColumn = new BooleanConnectionProperty("useColumnNamesInFindColumn", false, Messages.getString("ConnectionProperties.useColumnNamesInFindColumn"), "5.1.7", MISC_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1596 */     this.useConfigs = new StringConnectionProperty("useConfigs", null, Messages.getString("ConnectionProperties.useConfigs"), "3.1.5", CONNECTION_AND_AUTH_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1602 */     this.useCursorFetch = new BooleanConnectionProperty("useCursorFetch", false, Messages.getString("ConnectionProperties.useCursorFetch"), "5.0.0", PERFORMANCE_CATEGORY, Integer.MAX_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1608 */     this.useDynamicCharsetInfo = new BooleanConnectionProperty("useDynamicCharsetInfo", true, Messages.getString("ConnectionProperties.useDynamicCharsetInfo"), "5.0.6", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1614 */     this.useDirectRowUnpack = new BooleanConnectionProperty("useDirectRowUnpack", true, "Use newer result set row unpacking code that skips a copy from network buffers  to a MySQL packet instance and instead reads directly into the result set row data buffers.", "5.1.1", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1620 */     this.useFastIntParsing = new BooleanConnectionProperty("useFastIntParsing", true, Messages.getString("ConnectionProperties.useFastIntParsing"), "3.1.4", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1626 */     this.useFastDateParsing = new BooleanConnectionProperty("useFastDateParsing", true, Messages.getString("ConnectionProperties.useFastDateParsing"), "5.0.5", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1632 */     this.useHostsInPrivileges = new BooleanConnectionProperty("useHostsInPrivileges", true, Messages.getString("ConnectionProperties.useHostsInPrivileges"), "3.0.2", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1637 */     this.useInformationSchema = new BooleanConnectionProperty("useInformationSchema", false, Messages.getString("ConnectionProperties.useInformationSchema"), "5.0.0", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1642 */     this.useJDBCCompliantTimezoneShift = new BooleanConnectionProperty("useJDBCCompliantTimezoneShift", false, Messages.getString("ConnectionProperties.useJDBCCompliantTimezoneShift"), "5.0.0", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1649 */     this.useLocalSessionState = new BooleanConnectionProperty("useLocalSessionState", false, Messages.getString("ConnectionProperties.useLocalSessionState"), "3.1.7", PERFORMANCE_CATEGORY, 5);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1655 */     this.useLocalTransactionState = new BooleanConnectionProperty("useLocalTransactionState", false, Messages.getString("ConnectionProperties.useLocalTransactionState"), "5.1.7", PERFORMANCE_CATEGORY, 6);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1661 */     this.useLegacyDatetimeCode = new BooleanConnectionProperty("useLegacyDatetimeCode", true, Messages.getString("ConnectionProperties.useLegacyDatetimeCode"), "5.1.6", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1667 */     this.useNanosForElapsedTime = new BooleanConnectionProperty("useNanosForElapsedTime", false, Messages.getString("ConnectionProperties.useNanosForElapsedTime"), "5.0.7", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1674 */     this.useOldAliasMetadataBehavior = new BooleanConnectionProperty("useOldAliasMetadataBehavior", false, Messages.getString("ConnectionProperties.useOldAliasMetadataBehavior"), "5.0.4", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1682 */     this.useOldUTF8Behavior = new BooleanConnectionProperty("useOldUTF8Behavior", false, Messages.getString("ConnectionProperties.useOldUtf8Behavior"), "3.1.6", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1688 */     this.useOldUTF8BehaviorAsBoolean = false;
/*      */     
/* 1690 */     this.useOnlyServerErrorMessages = new BooleanConnectionProperty("useOnlyServerErrorMessages", true, Messages.getString("ConnectionProperties.useOnlyServerErrorMessages"), "3.0.15", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1696 */     this.useReadAheadInput = new BooleanConnectionProperty("useReadAheadInput", true, Messages.getString("ConnectionProperties.useReadAheadInput"), "3.1.5", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1702 */     this.useSqlStateCodes = new BooleanConnectionProperty("useSqlStateCodes", true, Messages.getString("ConnectionProperties.useSqlStateCodes"), "3.1.3", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1708 */     this.useSSL = new BooleanConnectionProperty("useSSL", false, Messages.getString("ConnectionProperties.useSSL"), "3.0.2", SECURITY_CATEGORY, 2);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1714 */     this.useSSPSCompatibleTimezoneShift = new BooleanConnectionProperty("useSSPSCompatibleTimezoneShift", false, Messages.getString("ConnectionProperties.useSSPSCompatibleTimezoneShift"), "5.0.5", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1720 */     this.useStreamLengthsInPrepStmts = new BooleanConnectionProperty("useStreamLengthsInPrepStmts", true, Messages.getString("ConnectionProperties.useStreamLengthsInPrepStmts"), "3.0.2", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1726 */     this.useTimezone = new BooleanConnectionProperty("useTimezone", false, Messages.getString("ConnectionProperties.useTimezone"), "3.0.2", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1732 */     this.useUltraDevWorkAround = new BooleanConnectionProperty("ultraDevHack", false, Messages.getString("ConnectionProperties.ultraDevHack"), "2.0.3", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1738 */     this.useUnbufferedInput = new BooleanConnectionProperty("useUnbufferedInput", true, Messages.getString("ConnectionProperties.useUnbufferedInput"), "3.0.11", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1743 */     this.useUnicode = new BooleanConnectionProperty("useUnicode", true, Messages.getString("ConnectionProperties.useUnicode"), "1.1g", MISC_CATEGORY, 0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1750 */     this.useUnicodeAsBoolean = true;
/*      */     
/* 1752 */     this.useUsageAdvisor = new BooleanConnectionProperty("useUsageAdvisor", false, Messages.getString("ConnectionProperties.useUsageAdvisor"), "3.1.1", DEBUGING_PROFILING_CATEGORY, 10);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1758 */     this.useUsageAdvisorAsBoolean = false;
/*      */     
/* 1760 */     this.yearIsDateType = new BooleanConnectionProperty("yearIsDateType", true, Messages.getString("ConnectionProperties.yearIsDateType"), "3.1.9", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1766 */     this.zeroDateTimeBehavior = new StringConnectionProperty("zeroDateTimeBehavior", "exception", new String[] { "exception", "round", "convertToNull" }, Messages.getString("ConnectionProperties.zeroDateTimeBehavior", new Object[] { "exception", "round", "convertToNull" }), "3.1.4", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1776 */     this.useJvmCharsetConverters = new BooleanConnectionProperty("useJvmCharsetConverters", false, Messages.getString("ConnectionProperties.useJvmCharsetConverters"), "5.0.1", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/* 1779 */     this.useGmtMillisForDatetimes = new BooleanConnectionProperty("useGmtMillisForDatetimes", false, Messages.getString("ConnectionProperties.useGmtMillisForDatetimes"), "3.1.12", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/* 1781 */     this.dumpMetadataOnColumnNotFound = new BooleanConnectionProperty("dumpMetadataOnColumnNotFound", false, Messages.getString("ConnectionProperties.dumpMetadataOnColumnNotFound"), "3.1.13", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/* 1785 */     this.clientCertificateKeyStoreUrl = new StringConnectionProperty("clientCertificateKeyStoreUrl", null, Messages.getString("ConnectionProperties.clientCertificateKeyStoreUrl"), "5.1.0", SECURITY_CATEGORY, 5);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1790 */     this.trustCertificateKeyStoreUrl = new StringConnectionProperty("trustCertificateKeyStoreUrl", null, Messages.getString("ConnectionProperties.trustCertificateKeyStoreUrl"), "5.1.0", SECURITY_CATEGORY, 8);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1795 */     this.clientCertificateKeyStoreType = new StringConnectionProperty("clientCertificateKeyStoreType", "JKS", Messages.getString("ConnectionProperties.clientCertificateKeyStoreType"), "5.1.0", SECURITY_CATEGORY, 6);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1800 */     this.clientCertificateKeyStorePassword = new StringConnectionProperty("clientCertificateKeyStorePassword", null, Messages.getString("ConnectionProperties.clientCertificateKeyStorePassword"), "5.1.0", SECURITY_CATEGORY, 7);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1805 */     this.trustCertificateKeyStoreType = new StringConnectionProperty("trustCertificateKeyStoreType", "JKS", Messages.getString("ConnectionProperties.trustCertificateKeyStoreType"), "5.1.0", SECURITY_CATEGORY, 9);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1810 */     this.trustCertificateKeyStorePassword = new StringConnectionProperty("trustCertificateKeyStorePassword", null, Messages.getString("ConnectionProperties.trustCertificateKeyStorePassword"), "5.1.0", SECURITY_CATEGORY, 10);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1815 */     this.verifyServerCertificate = new BooleanConnectionProperty("verifyServerCertificate", true, Messages.getString("ConnectionProperties.verifyServerCertificate"), "5.1.6", SECURITY_CATEGORY, 4);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1821 */     this.useAffectedRows = new BooleanConnectionProperty("useAffectedRows", false, Messages.getString("ConnectionProperties.useAffectedRows"), "5.1.7", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1826 */     this.passwordCharacterEncoding = new StringConnectionProperty("passwordCharacterEncoding", null, Messages.getString("ConnectionProperties.passwordCharacterEncoding"), "5.1.7", SECURITY_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1831 */     this.maxAllowedPacket = new IntegerConnectionProperty("maxAllowedPacket", -1, Messages.getString("ConnectionProperties.maxAllowedPacket"), "5.1.8", NETWORK_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/* 1835 */     this.authenticationPlugins = new StringConnectionProperty("authenticationPlugins", null, Messages.getString("ConnectionProperties.authenticationPlugins"), "5.1.19", CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1841 */     this.disabledAuthenticationPlugins = new StringConnectionProperty("disabledAuthenticationPlugins", null, Messages.getString("ConnectionProperties.disabledAuthenticationPlugins"), "5.1.19", CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1847 */     this.defaultAuthenticationPlugin = new StringConnectionProperty("defaultAuthenticationPlugin", "com.mysql.jdbc.authentication.MysqlNativePasswordPlugin", Messages.getString("ConnectionProperties.defaultAuthenticationPlugin"), "5.1.19", CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1853 */     this.disconnectOnExpiredPasswords = new BooleanConnectionProperty("disconnectOnExpiredPasswords", true, Messages.getString("ConnectionProperties.disconnectOnExpiredPasswords"), "5.1.23", CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1858 */     this.getProceduresReturnsFunctions = new BooleanConnectionProperty("getProceduresReturnsFunctions", true, Messages.getString("ConnectionProperties.getProceduresReturnsFunctions"), "5.1.26", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1863 */     this.detectCustomCollations = new BooleanConnectionProperty("detectCustomCollations", false, Messages.getString("ConnectionProperties.detectCustomCollations"), "5.1.29", MISC_CATEGORY, Integer.MIN_VALUE);
/*      */   }
/*      */   
/*      */   protected DriverPropertyInfo[] exposeAsDriverPropertyInfoInternal(Properties info, int slotsToReserve)
/*      */     throws SQLException
/*      */   {
/* 1870 */     initializeProperties(info);
/*      */     
/* 1872 */     int numProperties = PROPERTY_LIST.size();
/*      */     
/* 1874 */     int listSize = numProperties + slotsToReserve;
/*      */     
/* 1876 */     DriverPropertyInfo[] driverProperties = new DriverPropertyInfo[listSize];
/*      */     
/* 1878 */     for (int i = slotsToReserve; i < listSize; i++) {
/* 1879 */       Field propertyField = (Field)PROPERTY_LIST.get(i - slotsToReserve);
/*      */       
/*      */       try
/*      */       {
/* 1883 */         ConnectionProperty propToExpose = (ConnectionProperty)propertyField.get(this);
/*      */         
/*      */ 
/* 1886 */         if (info != null) {
/* 1887 */           propToExpose.initializeFrom(info, getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/* 1891 */         driverProperties[i] = propToExpose.getAsDriverPropertyInfo();
/*      */       } catch (IllegalAccessException iae) {
/* 1893 */         throw SQLError.createSQLException(Messages.getString("ConnectionProperties.InternalPropertiesFailure"), "S1000", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1898 */     return driverProperties;
/*      */   }
/*      */   
/*      */   protected Properties exposeAsProperties(Properties info)
/*      */     throws SQLException
/*      */   {
/* 1903 */     if (info == null) {
/* 1904 */       info = new Properties();
/*      */     }
/*      */     
/* 1907 */     int numPropertiesToSet = PROPERTY_LIST.size();
/*      */     
/* 1909 */     for (int i = 0; i < numPropertiesToSet; i++) {
/* 1910 */       Field propertyField = (Field)PROPERTY_LIST.get(i);
/*      */       
/*      */       try
/*      */       {
/* 1914 */         ConnectionProperty propToGet = (ConnectionProperty)propertyField.get(this);
/*      */         
/*      */ 
/* 1917 */         Object propValue = propToGet.getValueAsObject();
/*      */         
/* 1919 */         if (propValue != null) {
/* 1920 */           info.setProperty(propToGet.getPropertyName(), propValue.toString());
/*      */         }
/*      */       }
/*      */       catch (IllegalAccessException iae) {
/* 1924 */         throw SQLError.createSQLException("Internal properties failure", "S1000", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1929 */     return info;
/*      */   }
/*      */   
/*      */   class XmlMap
/*      */   {
/* 1933 */     protected Map<Integer, Map<String, ConnectionPropertiesImpl.ConnectionProperty>> ordered = new TreeMap();
/* 1934 */     protected Map<String, ConnectionPropertiesImpl.ConnectionProperty> alpha = new TreeMap();
/*      */     
/*      */     XmlMap() {}
/*      */   }
/*      */   
/*      */   public String exposeAsXml()
/*      */     throws SQLException
/*      */   {
/* 1941 */     StringBuffer xmlBuf = new StringBuffer();
/* 1942 */     xmlBuf.append("<ConnectionProperties>");
/*      */     
/* 1944 */     int numPropertiesToSet = PROPERTY_LIST.size();
/*      */     
/* 1946 */     int numCategories = PROPERTY_CATEGORIES.length;
/*      */     
/* 1948 */     Map<String, XmlMap> propertyListByCategory = new HashMap();
/*      */     
/* 1950 */     for (int i = 0; i < numCategories; i++) {
/* 1951 */       propertyListByCategory.put(PROPERTY_CATEGORIES[i], new XmlMap());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1960 */     StringConnectionProperty userProp = new StringConnectionProperty("user", null, Messages.getString("ConnectionProperties.Username"), Messages.getString("ConnectionProperties.allVersions"), CONNECTION_AND_AUTH_CATEGORY, -2147483647);
/*      */     
/*      */ 
/*      */ 
/* 1964 */     StringConnectionProperty passwordProp = new StringConnectionProperty("password", null, Messages.getString("ConnectionProperties.Password"), Messages.getString("ConnectionProperties.allVersions"), CONNECTION_AND_AUTH_CATEGORY, -2147483646);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1969 */     XmlMap connectionSortMaps = (XmlMap)propertyListByCategory.get(CONNECTION_AND_AUTH_CATEGORY);
/* 1970 */     TreeMap<String, ConnectionProperty> userMap = new TreeMap();
/* 1971 */     userMap.put(userProp.getPropertyName(), userProp);
/*      */     
/* 1973 */     connectionSortMaps.ordered.put(Integer.valueOf(userProp.getOrder()), userMap);
/*      */     
/* 1975 */     TreeMap<String, ConnectionProperty> passwordMap = new TreeMap();
/* 1976 */     passwordMap.put(passwordProp.getPropertyName(), passwordProp);
/*      */     
/* 1978 */     connectionSortMaps.ordered.put(new Integer(passwordProp.getOrder()), passwordMap);
/*      */     try
/*      */     {
/* 1981 */       for (int i = 0; i < numPropertiesToSet; i++) {
/* 1982 */         Field propertyField = (Field)PROPERTY_LIST.get(i);
/*      */         
/* 1984 */         ConnectionProperty propToGet = (ConnectionProperty)propertyField.get(this);
/*      */         
/* 1986 */         XmlMap sortMaps = (XmlMap)propertyListByCategory.get(propToGet.getCategoryName());
/*      */         
/* 1988 */         int orderInCategory = propToGet.getOrder();
/*      */         
/* 1990 */         if (orderInCategory == Integer.MIN_VALUE) {
/* 1991 */           sortMaps.alpha.put(propToGet.getPropertyName(), propToGet);
/*      */         } else {
/* 1993 */           Integer order = Integer.valueOf(orderInCategory);
/* 1994 */           Map<String, ConnectionProperty> orderMap = (Map)sortMaps.ordered.get(order);
/*      */           
/* 1996 */           if (orderMap == null) {
/* 1997 */             orderMap = new TreeMap();
/* 1998 */             sortMaps.ordered.put(order, orderMap);
/*      */           }
/*      */           
/* 2001 */           orderMap.put(propToGet.getPropertyName(), propToGet);
/*      */         }
/*      */       }
/*      */       
/* 2005 */       for (int j = 0; j < numCategories; j++) {
/* 2006 */         XmlMap sortMaps = (XmlMap)propertyListByCategory.get(PROPERTY_CATEGORIES[j]);
/*      */         
/* 2008 */         xmlBuf.append("\n <PropertyCategory name=\"");
/* 2009 */         xmlBuf.append(PROPERTY_CATEGORIES[j]);
/* 2010 */         xmlBuf.append("\">");
/*      */         
/* 2012 */         for (Map<String, ConnectionProperty> orderedEl : sortMaps.ordered.values()) {
/* 2013 */           for (ConnectionProperty propToGet : orderedEl.values()) {
/* 2014 */             xmlBuf.append("\n  <Property name=\"");
/* 2015 */             xmlBuf.append(propToGet.getPropertyName());
/* 2016 */             xmlBuf.append("\" required=\"");
/* 2017 */             xmlBuf.append(propToGet.required ? "Yes" : "No");
/*      */             
/* 2019 */             xmlBuf.append("\" default=\"");
/*      */             
/* 2021 */             if (propToGet.getDefaultValue() != null) {
/* 2022 */               xmlBuf.append(propToGet.getDefaultValue());
/*      */             }
/*      */             
/* 2025 */             xmlBuf.append("\" sortOrder=\"");
/* 2026 */             xmlBuf.append(propToGet.getOrder());
/* 2027 */             xmlBuf.append("\" since=\"");
/* 2028 */             xmlBuf.append(propToGet.sinceVersion);
/* 2029 */             xmlBuf.append("\">\n");
/* 2030 */             xmlBuf.append("    ");
/* 2031 */             String escapedDescription = propToGet.description;
/* 2032 */             escapedDescription = escapedDescription.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
/*      */             
/* 2034 */             xmlBuf.append(escapedDescription);
/* 2035 */             xmlBuf.append("\n  </Property>");
/*      */           }
/*      */         }
/*      */         
/* 2039 */         for (ConnectionProperty propToGet : sortMaps.alpha.values()) {
/* 2040 */           xmlBuf.append("\n  <Property name=\"");
/* 2041 */           xmlBuf.append(propToGet.getPropertyName());
/* 2042 */           xmlBuf.append("\" required=\"");
/* 2043 */           xmlBuf.append(propToGet.required ? "Yes" : "No");
/*      */           
/* 2045 */           xmlBuf.append("\" default=\"");
/*      */           
/* 2047 */           if (propToGet.getDefaultValue() != null) {
/* 2048 */             xmlBuf.append(propToGet.getDefaultValue());
/*      */           }
/*      */           
/* 2051 */           xmlBuf.append("\" sortOrder=\"alpha\" since=\"");
/* 2052 */           xmlBuf.append(propToGet.sinceVersion);
/* 2053 */           xmlBuf.append("\">\n");
/* 2054 */           xmlBuf.append("    ");
/* 2055 */           xmlBuf.append(propToGet.description);
/* 2056 */           xmlBuf.append("\n  </Property>");
/*      */         }
/*      */         
/* 2059 */         xmlBuf.append("\n </PropertyCategory>");
/*      */       }
/*      */     } catch (IllegalAccessException iae) {
/* 2062 */       throw SQLError.createSQLException("Internal properties failure", "S1000", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 2066 */     xmlBuf.append("\n</ConnectionProperties>");
/*      */     
/* 2068 */     return xmlBuf.toString();
/*      */   }
/*      */   
/*      */   public boolean getAllowLoadLocalInfile()
/*      */   {
/* 2075 */     return this.allowLoadLocalInfile.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getAllowMultiQueries()
/*      */   {
/* 2082 */     return this.allowMultiQueries.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getAllowNanAndInf()
/*      */   {
/* 2089 */     return this.allowNanAndInf.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getAllowUrlInLocalInfile()
/*      */   {
/* 2096 */     return this.allowUrlInLocalInfile.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getAlwaysSendSetIsolation()
/*      */   {
/* 2103 */     return this.alwaysSendSetIsolation.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getAutoDeserialize()
/*      */   {
/* 2110 */     return this.autoDeserialize.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getAutoGenerateTestcaseScript()
/*      */   {
/* 2117 */     return this.autoGenerateTestcaseScriptAsBoolean;
/*      */   }
/*      */   
/*      */   public boolean getAutoReconnectForPools()
/*      */   {
/* 2124 */     return this.autoReconnectForPoolsAsBoolean;
/*      */   }
/*      */   
/*      */   public int getBlobSendChunkSize()
/*      */   {
/* 2131 */     return this.blobSendChunkSize.getValueAsInt();
/*      */   }
/*      */   
/*      */   public boolean getCacheCallableStatements()
/*      */   {
/* 2138 */     return this.cacheCallableStatements.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getCachePreparedStatements()
/*      */   {
/* 2145 */     return ((Boolean)this.cachePreparedStatements.getValueAsObject()).booleanValue();
/*      */   }
/*      */   
/*      */   public boolean getCacheResultSetMetadata()
/*      */   {
/* 2153 */     return this.cacheResultSetMetaDataAsBoolean;
/*      */   }
/*      */   
/*      */   public boolean getCacheServerConfiguration()
/*      */   {
/* 2160 */     return this.cacheServerConfiguration.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public int getCallableStatementCacheSize()
/*      */   {
/* 2167 */     return this.callableStatementCacheSize.getValueAsInt();
/*      */   }
/*      */   
/*      */   public boolean getCapitalizeTypeNames()
/*      */   {
/* 2174 */     return this.capitalizeTypeNames.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public String getCharacterSetResults()
/*      */   {
/* 2181 */     return this.characterSetResults.getValueAsString();
/*      */   }
/*      */   
/*      */   public String getConnectionAttributes()
/*      */   {
/* 2185 */     return this.connectionAttributes.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setConnectionAttributes(String val)
/*      */   {
/* 2189 */     this.connectionAttributes.setValue(val);
/*      */   }
/*      */   
/*      */   public boolean getClobberStreamingResults()
/*      */   {
/* 2196 */     return this.clobberStreamingResults.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public String getClobCharacterEncoding()
/*      */   {
/* 2203 */     return this.clobCharacterEncoding.getValueAsString();
/*      */   }
/*      */   
/*      */   public String getConnectionCollation()
/*      */   {
/* 2210 */     return this.connectionCollation.getValueAsString();
/*      */   }
/*      */   
/*      */   public int getConnectTimeout()
/*      */   {
/* 2217 */     return this.connectTimeout.getValueAsInt();
/*      */   }
/*      */   
/*      */   public boolean getContinueBatchOnError()
/*      */   {
/* 2224 */     return this.continueBatchOnError.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getCreateDatabaseIfNotExist()
/*      */   {
/* 2231 */     return this.createDatabaseIfNotExist.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public int getDefaultFetchSize()
/*      */   {
/* 2238 */     return this.defaultFetchSize.getValueAsInt();
/*      */   }
/*      */   
/*      */   public boolean getDontTrackOpenResources()
/*      */   {
/* 2245 */     return this.dontTrackOpenResources.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getDumpQueriesOnException()
/*      */   {
/* 2252 */     return this.dumpQueriesOnException.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getDynamicCalendars()
/*      */   {
/* 2259 */     return this.dynamicCalendars.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getElideSetAutoCommits()
/*      */   {
/* 2266 */     return this.elideSetAutoCommits.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getEmptyStringsConvertToZero()
/*      */   {
/* 2273 */     return this.emptyStringsConvertToZero.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getEmulateLocators()
/*      */   {
/* 2280 */     return this.emulateLocators.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getEmulateUnsupportedPstmts()
/*      */   {
/* 2287 */     return this.emulateUnsupportedPstmts.getValueAsBoolean();
/*      */   }
/*      */   private IntegerConnectionProperty defaultFetchSize;
/*      */   private BooleanConnectionProperty detectServerPreparedStmts;
/*      */   private BooleanConnectionProperty dontTrackOpenResources;
/*      */   private BooleanConnectionProperty dumpQueriesOnException;
/*      */   private BooleanConnectionProperty dynamicCalendars;
/*      */   private BooleanConnectionProperty elideSetAutoCommits;
/*      */   private BooleanConnectionProperty emptyStringsConvertToZero;
/*      */   private BooleanConnectionProperty emulateLocators;
/*      */   private BooleanConnectionProperty emulateUnsupportedPstmts;
/*      */   
/*      */   public boolean getEnablePacketDebug()
/*      */   {
/* 2294 */     return this.enablePacketDebug.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   private BooleanConnectionProperty enablePacketDebug;
/*      */   private BooleanConnectionProperty enableQueryTimeouts;
/*      */   private BooleanConnectionProperty explainSlowQueries;
/*      */   private StringConnectionProperty exceptionInterceptors;
/*      */   private BooleanConnectionProperty failOverReadOnly;
/*      */   private BooleanConnectionProperty gatherPerformanceMetrics;
/*      */   private BooleanConnectionProperty generateSimpleParameterMetadata;
/*      */   private boolean highAvailabilityAsBoolean;
/*      */   private BooleanConnectionProperty holdResultsOpenOverStatementClose;
/*      */   
/*      */   public String getEncoding()
/*      */   {
/* 2301 */     return this.characterEncodingAsString;
/*      */   }
/*      */   
/*      */   private BooleanConnectionProperty includeInnodbStatusInDeadlockExceptions;
/*      */   private BooleanConnectionProperty includeThreadDumpInDeadlockExceptions;
/*      */   private BooleanConnectionProperty includeThreadNamesAsStatementComment;
/*      */   private BooleanConnectionProperty ignoreNonTxTables;
/*      */   private IntegerConnectionProperty initialTimeout;
/*      */   private BooleanConnectionProperty isInteractiveClient;
/*      */   private BooleanConnectionProperty jdbcCompliantTruncation;
/*      */   private boolean jdbcCompliantTruncationForReads;
/*      */   protected MemorySizeConnectionProperty largeRowSizeThreshold;
/*      */   
/*      */   public boolean getExplainSlowQueries()
/*      */   {
/* 2308 */     return this.explainSlowQueries.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   private StringConnectionProperty loadBalanceStrategy;
/*      */   private IntegerConnectionProperty loadBalanceBlacklistTimeout;
/*      */   private IntegerConnectionProperty loadBalancePingTimeout;
/*      */   private BooleanConnectionProperty loadBalanceValidateConnectionOnSwapServer;
/*      */   private StringConnectionProperty loadBalanceConnectionGroup;
/*      */   private StringConnectionProperty loadBalanceExceptionChecker;
/*      */   private StringConnectionProperty loadBalanceSQLStateFailover;
/*      */   private StringConnectionProperty loadBalanceSQLExceptionSubclassFailover;
/*      */   private BooleanConnectionProperty loadBalanceEnableJMX;
/*      */   
/*      */   public boolean getFailOverReadOnly()
/*      */   {
/* 2315 */     return this.failOverReadOnly.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   private StringConnectionProperty loadBalanceAutoCommitStatementRegex;
/*      */   private IntegerConnectionProperty loadBalanceAutoCommitStatementThreshold;
/*      */   private StringConnectionProperty localSocketAddress;
/*      */   private MemorySizeConnectionProperty locatorFetchBufferSize;
/*      */   private StringConnectionProperty loggerClassName;
/*      */   private BooleanConnectionProperty logSlowQueries;
/*      */   private BooleanConnectionProperty logXaCommands;
/*      */   
/*      */   public boolean getGatherPerformanceMetrics()
/*      */   {
/* 2322 */     return this.gatherPerformanceMetrics.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   private BooleanConnectionProperty maintainTimeStats;
/*      */   private boolean maintainTimeStatsAsBoolean;
/*      */   private IntegerConnectionProperty maxQuerySizeToLog;
/*      */   private IntegerConnectionProperty maxReconnects;
/*      */   private IntegerConnectionProperty retriesAllDown;
/*      */   private IntegerConnectionProperty maxRows;
/*      */   private int maxRowsAsInt;
/*      */   private IntegerConnectionProperty metadataCacheSize;
/*      */   private IntegerConnectionProperty netTimeoutForStreamingResults;
/*      */   private BooleanConnectionProperty noAccessToProcedureBodies;
/*      */   private BooleanConnectionProperty noDatetimeStringSync;
/*      */   private BooleanConnectionProperty noTimezoneConversionForTimeType;
/*      */   private BooleanConnectionProperty nullCatalogMeansCurrent;
/*      */   private BooleanConnectionProperty nullNamePatternMatchesAll;
/*      */   private IntegerConnectionProperty packetDebugBufferSize;
/*      */   
/*      */   protected boolean getHighAvailability()
/*      */   {
/* 2331 */     return this.highAvailabilityAsBoolean;
/*      */   }
/*      */   
/*      */   public boolean getHoldResultsOpenOverStatementClose()
/*      */   {
/* 2338 */     return this.holdResultsOpenOverStatementClose.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getIgnoreNonTxTables()
/*      */   {
/* 2345 */     return this.ignoreNonTxTables.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public int getInitialTimeout()
/*      */   {
/* 2352 */     return this.initialTimeout.getValueAsInt();
/*      */   }
/*      */   
/*      */   public boolean getInteractiveClient()
/*      */   {
/* 2359 */     return this.isInteractiveClient.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getIsInteractiveClient()
/*      */   {
/* 2366 */     return this.isInteractiveClient.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getJdbcCompliantTruncation()
/*      */   {
/* 2373 */     return this.jdbcCompliantTruncation.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public int getLocatorFetchBufferSize()
/*      */   {
/* 2380 */     return this.locatorFetchBufferSize.getValueAsInt();
/*      */   }
/*      */   
/*      */   public String getLogger()
/*      */   {
/* 2387 */     return this.loggerClassName.getValueAsString();
/*      */   }
/*      */   
/*      */   public String getLoggerClassName()
/*      */   {
/* 2394 */     return this.loggerClassName.getValueAsString();
/*      */   }
/*      */   
/*      */   public boolean getLogSlowQueries()
/*      */   {
/* 2401 */     return this.logSlowQueries.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getMaintainTimeStats()
/*      */   {
/* 2408 */     return this.maintainTimeStatsAsBoolean;
/*      */   }
/*      */   
/*      */   public int getMaxQuerySizeToLog()
/*      */   {
/* 2415 */     return this.maxQuerySizeToLog.getValueAsInt();
/*      */   }
/*      */   
/*      */   public int getMaxReconnects()
/*      */   {
/* 2422 */     return this.maxReconnects.getValueAsInt();
/*      */   }
/*      */   
/*      */   public int getMaxRows()
/*      */   {
/* 2429 */     return this.maxRowsAsInt;
/*      */   }
/*      */   
/*      */   public int getMetadataCacheSize()
/*      */   {
/* 2436 */     return this.metadataCacheSize.getValueAsInt();
/*      */   }
/*      */   
/*      */   public boolean getNoDatetimeStringSync()
/*      */   {
/* 2443 */     return this.noDatetimeStringSync.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getNullCatalogMeansCurrent()
/*      */   {
/* 2450 */     return this.nullCatalogMeansCurrent.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getNullNamePatternMatchesAll()
/*      */   {
/* 2457 */     return this.nullNamePatternMatchesAll.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public int getPacketDebugBufferSize()
/*      */   {
/* 2464 */     return this.packetDebugBufferSize.getValueAsInt();
/*      */   }
/*      */   
/*      */   public boolean getParanoid()
/*      */   {
/* 2471 */     return this.paranoid.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getPedantic()
/*      */   {
/* 2478 */     return this.pedantic.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public int getPreparedStatementCacheSize()
/*      */   {
/* 2485 */     return ((Integer)this.preparedStatementCacheSize.getValueAsObject()).intValue();
/*      */   }
/*      */   
/*      */   public int getPreparedStatementCacheSqlLimit()
/*      */   {
/* 2493 */     return ((Integer)this.preparedStatementCacheSqlLimit.getValueAsObject()).intValue();
/*      */   }
/*      */   
/*      */   public boolean getProfileSql()
/*      */   {
/* 2501 */     return this.profileSQLAsBoolean;
/*      */   }
/*      */   
/*      */   public boolean getProfileSQL()
/*      */   {
/* 2508 */     return this.profileSQL.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public String getPropertiesTransform()
/*      */   {
/* 2515 */     return this.propertiesTransform.getValueAsString();
/*      */   }
/*      */   
/*      */   public int getQueriesBeforeRetryMaster()
/*      */   {
/* 2522 */     return this.queriesBeforeRetryMaster.getValueAsInt();
/*      */   }
/*      */   
/*      */   public boolean getReconnectAtTxEnd()
/*      */   {
/* 2529 */     return this.reconnectTxAtEndAsBoolean;
/*      */   }
/*      */   
/*      */   public boolean getRelaxAutoCommit()
/*      */   {
/* 2536 */     return this.relaxAutoCommit.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public int getReportMetricsIntervalMillis()
/*      */   {
/* 2543 */     return this.reportMetricsIntervalMillis.getValueAsInt();
/*      */   }
/*      */   
/*      */   public boolean getRequireSSL()
/*      */   {
/* 2550 */     return this.requireSSL.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getRetainStatementAfterResultSetClose()
/*      */   {
/* 2554 */     return this.retainStatementAfterResultSetClose.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getRollbackOnPooledClose()
/*      */   {
/* 2561 */     return this.rollbackOnPooledClose.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getRoundRobinLoadBalance()
/*      */   {
/* 2568 */     return this.roundRobinLoadBalance.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getRunningCTS13()
/*      */   {
/* 2575 */     return this.runningCTS13.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public int getSecondsBeforeRetryMaster()
/*      */   {
/* 2582 */     return this.secondsBeforeRetryMaster.getValueAsInt();
/*      */   }
/*      */   
/*      */   public String getServerTimezone()
/*      */   {
/* 2589 */     return this.serverTimezone.getValueAsString();
/*      */   }
/*      */   
/*      */   public String getSessionVariables()
/*      */   {
/* 2596 */     return this.sessionVariables.getValueAsString();
/*      */   }
/*      */   
/*      */   public int getSlowQueryThresholdMillis()
/*      */   {
/* 2603 */     return this.slowQueryThresholdMillis.getValueAsInt();
/*      */   }
/*      */   
/*      */   public String getSocketFactoryClassName()
/*      */   {
/* 2610 */     return this.socketFactoryClassName.getValueAsString();
/*      */   }
/*      */   
/*      */   public int getSocketTimeout()
/*      */   {
/* 2617 */     return this.socketTimeout.getValueAsInt();
/*      */   }
/*      */   
/*      */   public boolean getStrictFloatingPoint()
/*      */   {
/* 2624 */     return this.strictFloatingPoint.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getStrictUpdates()
/*      */   {
/* 2631 */     return this.strictUpdates.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getTinyInt1isBit()
/*      */   {
/* 2638 */     return this.tinyInt1isBit.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getTraceProtocol()
/*      */   {
/* 2645 */     return this.traceProtocol.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getTransformedBitIsBoolean()
/*      */   {
/* 2652 */     return this.transformedBitIsBoolean.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseCompression()
/*      */   {
/* 2659 */     return this.useCompression.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseFastIntParsing()
/*      */   {
/* 2666 */     return this.useFastIntParsing.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseHostsInPrivileges()
/*      */   {
/* 2673 */     return this.useHostsInPrivileges.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseInformationSchema()
/*      */   {
/* 2680 */     return this.useInformationSchema.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseLocalSessionState()
/*      */   {
/* 2687 */     return this.useLocalSessionState.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseOldUTF8Behavior()
/*      */   {
/* 2694 */     return this.useOldUTF8BehaviorAsBoolean;
/*      */   }
/*      */   
/*      */   public boolean getUseOnlyServerErrorMessages()
/*      */   {
/* 2701 */     return this.useOnlyServerErrorMessages.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseReadAheadInput()
/*      */   {
/* 2708 */     return this.useReadAheadInput.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseServerPreparedStmts()
/*      */   {
/* 2715 */     return this.detectServerPreparedStmts.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseSqlStateCodes()
/*      */   {
/* 2722 */     return this.useSqlStateCodes.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseSSL()
/*      */   {
/* 2729 */     return this.useSSL.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getUseStreamLengthsInPrepStmts()
/*      */   {
/* 2736 */     return this.useStreamLengthsInPrepStmts.getValueAsBoolean();
/*      */   }
/*      */   private BooleanConnectionProperty padCharsWithSpace;
/*      */   private BooleanConnectionProperty paranoid;
/*      */   private BooleanConnectionProperty pedantic;
/*      */   private BooleanConnectionProperty pinGlobalTxToPhysicalConnection;
/*      */   private BooleanConnectionProperty populateInsertRowWithDefaultValues;
/*      */   private IntegerConnectionProperty preparedStatementCacheSize;
/*      */   private IntegerConnectionProperty preparedStatementCacheSqlLimit;
/*      */   private StringConnectionProperty parseInfoCacheFactory;
/*      */   
/*      */   public boolean getUseTimezone()
/*      */   {
/* 2743 */     return this.useTimezone.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   private BooleanConnectionProperty processEscapeCodesForPrepStmts;
/*      */   private StringConnectionProperty profilerEventHandler;
/*      */   private StringConnectionProperty profileSql;
/*      */   private BooleanConnectionProperty profileSQL;
/*      */   private boolean profileSQLAsBoolean;
/*      */   private StringConnectionProperty propertiesTransform;
/*      */   private IntegerConnectionProperty queriesBeforeRetryMaster;
/*      */   private BooleanConnectionProperty queryTimeoutKillsConnection;
/*      */   private BooleanConnectionProperty reconnectAtTxEnd;
/*      */   private boolean reconnectTxAtEndAsBoolean;
/*      */   private BooleanConnectionProperty relaxAutoCommit;
/*      */   private IntegerConnectionProperty reportMetricsIntervalMillis;
/*      */   private BooleanConnectionProperty requireSSL;
/*      */   private StringConnectionProperty resourceId;
/*      */   
/*      */   public boolean getUseUltraDevWorkAround()
/*      */   {
/* 2750 */     return this.useUltraDevWorkAround.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   private IntegerConnectionProperty resultSetSizeThreshold;
/*      */   private BooleanConnectionProperty retainStatementAfterResultSetClose;
/*      */   private BooleanConnectionProperty rewriteBatchedStatements;
/*      */   private BooleanConnectionProperty rollbackOnPooledClose;
/*      */   private BooleanConnectionProperty roundRobinLoadBalance;
/*      */   private BooleanConnectionProperty runningCTS13;
/*      */   private IntegerConnectionProperty secondsBeforeRetryMaster;
/*      */   
/*      */   public boolean getUseUnbufferedInput()
/*      */   {
/* 2757 */     return this.useUnbufferedInput.getValueAsBoolean(); }
/*      */   
/*      */   private IntegerConnectionProperty selfDestructOnPingSecondsLifetime;
/*      */   private IntegerConnectionProperty selfDestructOnPingMaxOperations;
/*      */   private BooleanConnectionProperty replicationEnableJMX;
/*      */   private StringConnectionProperty serverTimezone;
/*      */   private StringConnectionProperty sessionVariables; private IntegerConnectionProperty slowQueryThresholdMillis; private LongConnectionProperty slowQueryThresholdNanos;
/* 2764 */   public boolean getUseUnicode() { return this.useUnicodeAsBoolean; }
/*      */   
/*      */   private StringConnectionProperty socketFactoryClassName;
/*      */   private IntegerConnectionProperty socketTimeout;
/*      */   private StringConnectionProperty statementInterceptors;
/*      */   private BooleanConnectionProperty strictFloatingPoint;
/*      */   private BooleanConnectionProperty strictUpdates; private BooleanConnectionProperty overrideSupportsIntegrityEnhancementFacility; private BooleanConnectionProperty tcpNoDelay;
/* 2771 */   public boolean getUseUsageAdvisor() { return this.useUsageAdvisorAsBoolean; }
/*      */   
/*      */   private BooleanConnectionProperty tcpKeepAlive;
/*      */   private IntegerConnectionProperty tcpRcvBuf;
/*      */   private IntegerConnectionProperty tcpSndBuf;
/*      */   private IntegerConnectionProperty tcpTrafficClass;
/*      */   private BooleanConnectionProperty tinyInt1isBit; protected BooleanConnectionProperty traceProtocol; private BooleanConnectionProperty treatUtilDateAsTimestamp; private BooleanConnectionProperty transformedBitIsBoolean; private BooleanConnectionProperty useBlobToStoreUTF8OutsideBMP; private StringConnectionProperty utf8OutsideBmpExcludedColumnNamePattern; private StringConnectionProperty utf8OutsideBmpIncludedColumnNamePattern; private BooleanConnectionProperty useCompression; private BooleanConnectionProperty useColumnNamesInFindColumn;
/* 2778 */   public boolean getYearIsDateType() { return this.yearIsDateType.getValueAsBoolean(); }
/*      */   
/*      */   private StringConnectionProperty useConfigs;
/*      */   private BooleanConnectionProperty useCursorFetch;
/*      */   private BooleanConnectionProperty useDynamicCharsetInfo;
/*      */   private BooleanConnectionProperty useDirectRowUnpack;
/*      */   private BooleanConnectionProperty useFastIntParsing; private BooleanConnectionProperty useFastDateParsing; private BooleanConnectionProperty useHostsInPrivileges; private BooleanConnectionProperty useInformationSchema; private BooleanConnectionProperty useJDBCCompliantTimezoneShift; private BooleanConnectionProperty useLocalSessionState; private BooleanConnectionProperty useLocalTransactionState;
/* 2785 */   public String getZeroDateTimeBehavior() { return this.zeroDateTimeBehavior.getValueAsString(); }
/*      */   
/*      */   private BooleanConnectionProperty useLegacyDatetimeCode;
/*      */   private BooleanConnectionProperty useNanosForElapsedTime;
/*      */   private BooleanConnectionProperty useOldAliasMetadataBehavior;
/*      */   private BooleanConnectionProperty useOldUTF8Behavior;
/*      */   private boolean useOldUTF8BehaviorAsBoolean;
/*      */   private BooleanConnectionProperty useOnlyServerErrorMessages;
/*      */   private BooleanConnectionProperty useReadAheadInput;
/*      */   private BooleanConnectionProperty useSqlStateCodes;
/*      */   private BooleanConnectionProperty useSSL;
/*      */   private BooleanConnectionProperty useSSPSCompatibleTimezoneShift;
/*      */   private BooleanConnectionProperty useStreamLengthsInPrepStmts;
/*      */   private BooleanConnectionProperty useTimezone; private BooleanConnectionProperty useUltraDevWorkAround;
/* 2799 */   protected void initializeFromRef(Reference ref) throws SQLException { int numPropertiesToSet = PROPERTY_LIST.size();
/*      */     
/* 2801 */     for (int i = 0; i < numPropertiesToSet; i++) {
/* 2802 */       Field propertyField = (Field)PROPERTY_LIST.get(i);
/*      */       
/*      */       try
/*      */       {
/* 2806 */         ConnectionProperty propToSet = (ConnectionProperty)propertyField.get(this);
/*      */         
/*      */ 
/* 2809 */         if (ref != null) {
/* 2810 */           propToSet.initializeFrom(ref, getExceptionInterceptor());
/*      */         }
/*      */       } catch (IllegalAccessException iae) {
/* 2813 */         throw SQLError.createSQLException("Internal properties failure", "S1000", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2818 */     postInitialization(); }
/*      */   
/*      */   private BooleanConnectionProperty useUnbufferedInput;
/*      */   private BooleanConnectionProperty useUnicode;
/*      */   private boolean useUnicodeAsBoolean;
/*      */   private BooleanConnectionProperty useUsageAdvisor;
/*      */   private boolean useUsageAdvisorAsBoolean;
/*      */   private BooleanConnectionProperty yearIsDateType;
/*      */   private StringConnectionProperty zeroDateTimeBehavior;
/*      */   private BooleanConnectionProperty useJvmCharsetConverters;
/*      */   private BooleanConnectionProperty useGmtMillisForDatetimes;
/*      */   private BooleanConnectionProperty dumpMetadataOnColumnNotFound;
/*      */   private StringConnectionProperty clientCertificateKeyStoreUrl; private StringConnectionProperty trustCertificateKeyStoreUrl; private StringConnectionProperty clientCertificateKeyStoreType;
/* 2831 */   protected void initializeProperties(Properties info) throws SQLException { if (info != null)
/*      */     {
/* 2833 */       String profileSqlLc = info.getProperty("profileSql");
/*      */       
/* 2835 */       if (profileSqlLc != null) {
/* 2836 */         info.put("profileSQL", profileSqlLc);
/*      */       }
/*      */       
/* 2839 */       Properties infoCopy = (Properties)info.clone();
/*      */       
/* 2841 */       infoCopy.remove("HOST");
/* 2842 */       infoCopy.remove("user");
/* 2843 */       infoCopy.remove("password");
/* 2844 */       infoCopy.remove("DBNAME");
/* 2845 */       infoCopy.remove("PORT");
/* 2846 */       infoCopy.remove("profileSql");
/*      */       
/* 2848 */       int numPropertiesToSet = PROPERTY_LIST.size();
/*      */       
/* 2850 */       for (int i = 0; i < numPropertiesToSet; i++) {
/* 2851 */         Field propertyField = (Field)PROPERTY_LIST.get(i);
/*      */         
/*      */         try
/*      */         {
/* 2855 */           ConnectionProperty propToSet = (ConnectionProperty)propertyField.get(this);
/*      */           
/*      */ 
/* 2858 */           propToSet.initializeFrom(infoCopy, getExceptionInterceptor());
/*      */         } catch (IllegalAccessException iae) {
/* 2860 */           throw SQLError.createSQLException(Messages.getString("ConnectionProperties.unableToInitDriverProperties") + iae.toString(), "S1000", getExceptionInterceptor());
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2867 */       postInitialization();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void postInitialization()
/*      */     throws SQLException
/*      */   {
/* 2874 */     if (this.profileSql.getValueAsObject() != null) {
/* 2875 */       this.profileSQL.initializeFrom(this.profileSql.getValueAsObject().toString(), getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 2879 */     this.reconnectTxAtEndAsBoolean = ((Boolean)this.reconnectAtTxEnd.getValueAsObject()).booleanValue();
/*      */     
/*      */ 
/*      */ 
/* 2883 */     if (getMaxRows() == 0)
/*      */     {
/*      */ 
/* 2886 */       this.maxRows.setValueAsObject(Integer.valueOf(-1));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2892 */     String testEncoding = getEncoding();
/*      */     
/* 2894 */     if (testEncoding != null)
/*      */     {
/*      */       try
/*      */       {
/* 2898 */         String testString = "abc";
/* 2899 */         StringUtils.getBytes(testString, testEncoding);
/*      */       } catch (UnsupportedEncodingException UE) {
/* 2901 */         throw SQLError.createSQLException(Messages.getString("ConnectionProperties.unsupportedCharacterEncoding", new Object[] { testEncoding }), "0S100", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2910 */     if (((Boolean)this.cacheResultSetMetadata.getValueAsObject()).booleanValue()) {
/*      */       try
/*      */       {
/* 2913 */         Class.forName("java.util.LinkedHashMap");
/*      */       } catch (ClassNotFoundException cnfe) {
/* 2915 */         this.cacheResultSetMetadata.setValue(false);
/*      */       }
/*      */     }
/*      */     
/* 2919 */     this.cacheResultSetMetaDataAsBoolean = this.cacheResultSetMetadata.getValueAsBoolean();
/*      */     
/* 2921 */     this.useUnicodeAsBoolean = this.useUnicode.getValueAsBoolean();
/* 2922 */     this.characterEncodingAsString = ((String)this.characterEncoding.getValueAsObject());
/*      */     
/* 2924 */     this.characterEncodingIsAliasForSjis = CharsetMapping.isAliasForSjis(this.characterEncodingAsString);
/* 2925 */     this.highAvailabilityAsBoolean = this.autoReconnect.getValueAsBoolean();
/* 2926 */     this.autoReconnectForPoolsAsBoolean = this.autoReconnectForPools.getValueAsBoolean();
/*      */     
/* 2928 */     this.maxRowsAsInt = ((Integer)this.maxRows.getValueAsObject()).intValue();
/*      */     
/* 2930 */     this.profileSQLAsBoolean = this.profileSQL.getValueAsBoolean();
/* 2931 */     this.useUsageAdvisorAsBoolean = this.useUsageAdvisor.getValueAsBoolean();
/*      */     
/* 2933 */     this.useOldUTF8BehaviorAsBoolean = this.useOldUTF8Behavior.getValueAsBoolean();
/*      */     
/* 2935 */     this.autoGenerateTestcaseScriptAsBoolean = this.autoGenerateTestcaseScript.getValueAsBoolean();
/*      */     
/* 2937 */     this.maintainTimeStatsAsBoolean = this.maintainTimeStats.getValueAsBoolean();
/*      */     
/* 2939 */     this.jdbcCompliantTruncationForReads = getJdbcCompliantTruncation();
/*      */     
/* 2941 */     if (getUseCursorFetch())
/*      */     {
/*      */ 
/* 2944 */       setDetectServerPreparedStmts(true);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAllowLoadLocalInfile(boolean property)
/*      */   {
/* 2952 */     this.allowLoadLocalInfile.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAllowMultiQueries(boolean property)
/*      */   {
/* 2959 */     this.allowMultiQueries.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAllowNanAndInf(boolean flag)
/*      */   {
/* 2966 */     this.allowNanAndInf.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAllowUrlInLocalInfile(boolean flag)
/*      */   {
/* 2973 */     this.allowUrlInLocalInfile.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAlwaysSendSetIsolation(boolean flag)
/*      */   {
/* 2980 */     this.alwaysSendSetIsolation.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAutoDeserialize(boolean flag)
/*      */   {
/* 2987 */     this.autoDeserialize.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAutoGenerateTestcaseScript(boolean flag)
/*      */   {
/* 2994 */     this.autoGenerateTestcaseScript.setValue(flag);
/* 2995 */     this.autoGenerateTestcaseScriptAsBoolean = this.autoGenerateTestcaseScript.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoReconnect(boolean flag)
/*      */   {
/* 3003 */     this.autoReconnect.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAutoReconnectForConnectionPools(boolean property)
/*      */   {
/* 3010 */     this.autoReconnectForPools.setValue(property);
/* 3011 */     this.autoReconnectForPoolsAsBoolean = this.autoReconnectForPools.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoReconnectForPools(boolean flag)
/*      */   {
/* 3019 */     this.autoReconnectForPools.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setBlobSendChunkSize(String value)
/*      */     throws SQLException
/*      */   {
/* 3026 */     this.blobSendChunkSize.setValue(value, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setCacheCallableStatements(boolean flag)
/*      */   {
/* 3033 */     this.cacheCallableStatements.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setCachePreparedStatements(boolean flag)
/*      */   {
/* 3040 */     this.cachePreparedStatements.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setCacheResultSetMetadata(boolean property)
/*      */   {
/* 3047 */     this.cacheResultSetMetadata.setValue(property);
/* 3048 */     this.cacheResultSetMetaDataAsBoolean = this.cacheResultSetMetadata.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCacheServerConfiguration(boolean flag)
/*      */   {
/* 3056 */     this.cacheServerConfiguration.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setCallableStatementCacheSize(int size)
/*      */     throws SQLException
/*      */   {
/* 3063 */     this.callableStatementCacheSize.setValue(size, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setCapitalizeDBMDTypes(boolean property)
/*      */   {
/* 3070 */     this.capitalizeTypeNames.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setCapitalizeTypeNames(boolean flag)
/*      */   {
/* 3077 */     this.capitalizeTypeNames.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setCharacterEncoding(String encoding)
/*      */   {
/* 3084 */     this.characterEncoding.setValue(encoding);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setCharacterSetResults(String characterSet)
/*      */   {
/* 3091 */     this.characterSetResults.setValue(characterSet);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setClobberStreamingResults(boolean flag)
/*      */   {
/* 3098 */     this.clobberStreamingResults.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setClobCharacterEncoding(String encoding)
/*      */   {
/* 3105 */     this.clobCharacterEncoding.setValue(encoding);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setConnectionCollation(String collation)
/*      */   {
/* 3112 */     this.connectionCollation.setValue(collation);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setConnectTimeout(int timeoutMs)
/*      */     throws SQLException
/*      */   {
/* 3119 */     this.connectTimeout.setValue(timeoutMs, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setContinueBatchOnError(boolean property)
/*      */   {
/* 3126 */     this.continueBatchOnError.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setCreateDatabaseIfNotExist(boolean flag)
/*      */   {
/* 3133 */     this.createDatabaseIfNotExist.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setDefaultFetchSize(int n)
/*      */     throws SQLException
/*      */   {
/* 3140 */     this.defaultFetchSize.setValue(n, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setDetectServerPreparedStmts(boolean property)
/*      */   {
/* 3147 */     this.detectServerPreparedStmts.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setDontTrackOpenResources(boolean flag)
/*      */   {
/* 3154 */     this.dontTrackOpenResources.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setDumpQueriesOnException(boolean flag)
/*      */   {
/* 3161 */     this.dumpQueriesOnException.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setDynamicCalendars(boolean flag)
/*      */   {
/* 3168 */     this.dynamicCalendars.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setElideSetAutoCommits(boolean flag)
/*      */   {
/* 3175 */     this.elideSetAutoCommits.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setEmptyStringsConvertToZero(boolean flag)
/*      */   {
/* 3182 */     this.emptyStringsConvertToZero.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setEmulateLocators(boolean property)
/*      */   {
/* 3189 */     this.emulateLocators.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setEmulateUnsupportedPstmts(boolean flag)
/*      */   {
/* 3196 */     this.emulateUnsupportedPstmts.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setEnablePacketDebug(boolean flag)
/*      */   {
/* 3203 */     this.enablePacketDebug.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setEncoding(String property)
/*      */   {
/* 3210 */     this.characterEncoding.setValue(property);
/* 3211 */     this.characterEncodingAsString = this.characterEncoding.getValueAsString();
/*      */     
/* 3213 */     this.characterEncodingIsAliasForSjis = CharsetMapping.isAliasForSjis(this.characterEncodingAsString);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setExplainSlowQueries(boolean flag)
/*      */   {
/* 3220 */     this.explainSlowQueries.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setFailOverReadOnly(boolean flag)
/*      */   {
/* 3227 */     this.failOverReadOnly.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setGatherPerformanceMetrics(boolean flag)
/*      */   {
/* 3234 */     this.gatherPerformanceMetrics.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setHighAvailability(boolean property)
/*      */   {
/* 3243 */     this.autoReconnect.setValue(property);
/* 3244 */     this.highAvailabilityAsBoolean = this.autoReconnect.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setHoldResultsOpenOverStatementClose(boolean flag)
/*      */   {
/* 3251 */     this.holdResultsOpenOverStatementClose.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setIgnoreNonTxTables(boolean property)
/*      */   {
/* 3258 */     this.ignoreNonTxTables.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setInitialTimeout(int property)
/*      */     throws SQLException
/*      */   {
/* 3265 */     this.initialTimeout.setValue(property, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setIsInteractiveClient(boolean property)
/*      */   {
/* 3272 */     this.isInteractiveClient.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setJdbcCompliantTruncation(boolean flag)
/*      */   {
/* 3279 */     this.jdbcCompliantTruncation.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setLocatorFetchBufferSize(String value)
/*      */     throws SQLException
/*      */   {
/* 3286 */     this.locatorFetchBufferSize.setValue(value, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLogger(String property)
/*      */   {
/* 3293 */     this.loggerClassName.setValueAsObject(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLoggerClassName(String className)
/*      */   {
/* 3300 */     this.loggerClassName.setValue(className);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLogSlowQueries(boolean flag)
/*      */   {
/* 3307 */     this.logSlowQueries.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setMaintainTimeStats(boolean flag)
/*      */   {
/* 3314 */     this.maintainTimeStats.setValue(flag);
/* 3315 */     this.maintainTimeStatsAsBoolean = this.maintainTimeStats.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setMaxQuerySizeToLog(int sizeInBytes)
/*      */     throws SQLException
/*      */   {
/* 3323 */     this.maxQuerySizeToLog.setValue(sizeInBytes, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */   public void setMaxReconnects(int property)
/*      */     throws SQLException
/*      */   {
/* 3330 */     this.maxReconnects.setValue(property, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */   public void setMaxRows(int property)
/*      */     throws SQLException
/*      */   {
/* 3337 */     this.maxRows.setValue(property, getExceptionInterceptor());
/* 3338 */     this.maxRowsAsInt = this.maxRows.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setMetadataCacheSize(int value)
/*      */     throws SQLException
/*      */   {
/* 3345 */     this.metadataCacheSize.setValue(value, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setNoDatetimeStringSync(boolean flag)
/*      */   {
/* 3352 */     this.noDatetimeStringSync.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setNullCatalogMeansCurrent(boolean value)
/*      */   {
/* 3359 */     this.nullCatalogMeansCurrent.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setNullNamePatternMatchesAll(boolean value)
/*      */   {
/* 3366 */     this.nullNamePatternMatchesAll.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setPacketDebugBufferSize(int size)
/*      */     throws SQLException
/*      */   {
/* 3373 */     this.packetDebugBufferSize.setValue(size, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setParanoid(boolean property)
/*      */   {
/* 3380 */     this.paranoid.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setPedantic(boolean property)
/*      */   {
/* 3387 */     this.pedantic.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setPreparedStatementCacheSize(int cacheSize)
/*      */     throws SQLException
/*      */   {
/* 3394 */     this.preparedStatementCacheSize.setValue(cacheSize, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */   public void setPreparedStatementCacheSqlLimit(int cacheSqlLimit)
/*      */     throws SQLException
/*      */   {
/* 3401 */     this.preparedStatementCacheSqlLimit.setValue(cacheSqlLimit, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setProfileSql(boolean property)
/*      */   {
/* 3408 */     this.profileSQL.setValue(property);
/* 3409 */     this.profileSQLAsBoolean = this.profileSQL.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setProfileSQL(boolean flag)
/*      */   {
/* 3416 */     this.profileSQL.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setPropertiesTransform(String value)
/*      */   {
/* 3423 */     this.propertiesTransform.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setQueriesBeforeRetryMaster(int property)
/*      */     throws SQLException
/*      */   {
/* 3430 */     this.queriesBeforeRetryMaster.setValue(property, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setReconnectAtTxEnd(boolean property)
/*      */   {
/* 3437 */     this.reconnectAtTxEnd.setValue(property);
/* 3438 */     this.reconnectTxAtEndAsBoolean = this.reconnectAtTxEnd.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRelaxAutoCommit(boolean property)
/*      */   {
/* 3446 */     this.relaxAutoCommit.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setReportMetricsIntervalMillis(int millis)
/*      */     throws SQLException
/*      */   {
/* 3453 */     this.reportMetricsIntervalMillis.setValue(millis, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setRequireSSL(boolean property)
/*      */   {
/* 3460 */     this.requireSSL.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setRetainStatementAfterResultSetClose(boolean flag)
/*      */   {
/* 3467 */     this.retainStatementAfterResultSetClose.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setRollbackOnPooledClose(boolean flag)
/*      */   {
/* 3474 */     this.rollbackOnPooledClose.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setRoundRobinLoadBalance(boolean flag)
/*      */   {
/* 3481 */     this.roundRobinLoadBalance.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setRunningCTS13(boolean flag)
/*      */   {
/* 3488 */     this.runningCTS13.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setSecondsBeforeRetryMaster(int property)
/*      */     throws SQLException
/*      */   {
/* 3495 */     this.secondsBeforeRetryMaster.setValue(property, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setServerTimezone(String property)
/*      */   {
/* 3502 */     this.serverTimezone.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setSessionVariables(String variables)
/*      */   {
/* 3509 */     this.sessionVariables.setValue(variables);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setSlowQueryThresholdMillis(int millis)
/*      */     throws SQLException
/*      */   {
/* 3516 */     this.slowQueryThresholdMillis.setValue(millis, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setSocketFactoryClassName(String property)
/*      */   {
/* 3523 */     this.socketFactoryClassName.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setSocketTimeout(int property)
/*      */     throws SQLException
/*      */   {
/* 3530 */     this.socketTimeout.setValue(property, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setStrictFloatingPoint(boolean property)
/*      */   {
/* 3537 */     this.strictFloatingPoint.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setStrictUpdates(boolean property)
/*      */   {
/* 3544 */     this.strictUpdates.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setTinyInt1isBit(boolean flag)
/*      */   {
/* 3551 */     this.tinyInt1isBit.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setTraceProtocol(boolean flag)
/*      */   {
/* 3558 */     this.traceProtocol.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setTransformedBitIsBoolean(boolean flag)
/*      */   {
/* 3565 */     this.transformedBitIsBoolean.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseCompression(boolean property)
/*      */   {
/* 3572 */     this.useCompression.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseFastIntParsing(boolean flag)
/*      */   {
/* 3579 */     this.useFastIntParsing.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseHostsInPrivileges(boolean property)
/*      */   {
/* 3586 */     this.useHostsInPrivileges.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseInformationSchema(boolean flag)
/*      */   {
/* 3593 */     this.useInformationSchema.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseLocalSessionState(boolean flag)
/*      */   {
/* 3600 */     this.useLocalSessionState.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseOldUTF8Behavior(boolean flag)
/*      */   {
/* 3607 */     this.useOldUTF8Behavior.setValue(flag);
/* 3608 */     this.useOldUTF8BehaviorAsBoolean = this.useOldUTF8Behavior.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseOnlyServerErrorMessages(boolean flag)
/*      */   {
/* 3616 */     this.useOnlyServerErrorMessages.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseReadAheadInput(boolean flag)
/*      */   {
/* 3623 */     this.useReadAheadInput.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseServerPreparedStmts(boolean flag)
/*      */   {
/* 3630 */     this.detectServerPreparedStmts.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseSqlStateCodes(boolean flag)
/*      */   {
/* 3637 */     this.useSqlStateCodes.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseSSL(boolean property)
/*      */   {
/* 3644 */     this.useSSL.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseStreamLengthsInPrepStmts(boolean property)
/*      */   {
/* 3651 */     this.useStreamLengthsInPrepStmts.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseTimezone(boolean property)
/*      */   {
/* 3658 */     this.useTimezone.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseUltraDevWorkAround(boolean property)
/*      */   {
/* 3665 */     this.useUltraDevWorkAround.setValue(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseUnbufferedInput(boolean flag)
/*      */   {
/* 3672 */     this.useUnbufferedInput.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseUnicode(boolean flag)
/*      */   {
/* 3679 */     this.useUnicode.setValue(flag);
/* 3680 */     this.useUnicodeAsBoolean = this.useUnicode.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseUsageAdvisor(boolean useUsageAdvisorFlag)
/*      */   {
/* 3687 */     this.useUsageAdvisor.setValue(useUsageAdvisorFlag);
/* 3688 */     this.useUsageAdvisorAsBoolean = this.useUsageAdvisor.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setYearIsDateType(boolean flag)
/*      */   {
/* 3696 */     this.yearIsDateType.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setZeroDateTimeBehavior(String behavior)
/*      */   {
/* 3703 */     this.zeroDateTimeBehavior.setValue(behavior);
/*      */   }
/*      */   
/*      */   protected void storeToRef(Reference ref) throws SQLException {
/* 3707 */     int numPropertiesToSet = PROPERTY_LIST.size();
/*      */     
/* 3709 */     for (int i = 0; i < numPropertiesToSet; i++) {
/* 3710 */       Field propertyField = (Field)PROPERTY_LIST.get(i);
/*      */       
/*      */       try
/*      */       {
/* 3714 */         ConnectionProperty propToStore = (ConnectionProperty)propertyField.get(this);
/*      */         
/*      */ 
/* 3717 */         if (ref != null) {
/* 3718 */           propToStore.storeTo(ref);
/*      */         }
/*      */       } catch (IllegalAccessException iae) {
/* 3721 */         throw SQLError.createSQLException(Messages.getString("ConnectionProperties.errorNotExpected"), getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean useUnbufferedInput()
/*      */   {
/* 3730 */     return this.useUnbufferedInput.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseCursorFetch()
/*      */   {
/* 3737 */     return this.useCursorFetch.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseCursorFetch(boolean flag)
/*      */   {
/* 3744 */     this.useCursorFetch.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getOverrideSupportsIntegrityEnhancementFacility()
/*      */   {
/* 3751 */     return this.overrideSupportsIntegrityEnhancementFacility.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setOverrideSupportsIntegrityEnhancementFacility(boolean flag)
/*      */   {
/* 3758 */     this.overrideSupportsIntegrityEnhancementFacility.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getNoTimezoneConversionForTimeType()
/*      */   {
/* 3765 */     return this.noTimezoneConversionForTimeType.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setNoTimezoneConversionForTimeType(boolean flag)
/*      */   {
/* 3772 */     this.noTimezoneConversionForTimeType.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseJDBCCompliantTimezoneShift()
/*      */   {
/* 3779 */     return this.useJDBCCompliantTimezoneShift.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseJDBCCompliantTimezoneShift(boolean flag)
/*      */   {
/* 3786 */     this.useJDBCCompliantTimezoneShift.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getAutoClosePStmtStreams()
/*      */   {
/* 3793 */     return this.autoClosePStmtStreams.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAutoClosePStmtStreams(boolean flag)
/*      */   {
/* 3800 */     this.autoClosePStmtStreams.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getProcessEscapeCodesForPrepStmts()
/*      */   {
/* 3807 */     return this.processEscapeCodesForPrepStmts.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setProcessEscapeCodesForPrepStmts(boolean flag)
/*      */   {
/* 3814 */     this.processEscapeCodesForPrepStmts.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseGmtMillisForDatetimes()
/*      */   {
/* 3821 */     return this.useGmtMillisForDatetimes.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseGmtMillisForDatetimes(boolean flag)
/*      */   {
/* 3828 */     this.useGmtMillisForDatetimes.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getDumpMetadataOnColumnNotFound()
/*      */   {
/* 3835 */     return this.dumpMetadataOnColumnNotFound.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setDumpMetadataOnColumnNotFound(boolean flag)
/*      */   {
/* 3842 */     this.dumpMetadataOnColumnNotFound.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getResourceId()
/*      */   {
/* 3849 */     return this.resourceId.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setResourceId(String resourceId)
/*      */   {
/* 3856 */     this.resourceId.setValue(resourceId);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getRewriteBatchedStatements()
/*      */   {
/* 3863 */     return this.rewriteBatchedStatements.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setRewriteBatchedStatements(boolean flag)
/*      */   {
/* 3870 */     this.rewriteBatchedStatements.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getJdbcCompliantTruncationForReads()
/*      */   {
/* 3877 */     return this.jdbcCompliantTruncationForReads;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setJdbcCompliantTruncationForReads(boolean jdbcCompliantTruncationForReads)
/*      */   {
/* 3885 */     this.jdbcCompliantTruncationForReads = jdbcCompliantTruncationForReads;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseJvmCharsetConverters()
/*      */   {
/* 3892 */     return this.useJvmCharsetConverters.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseJvmCharsetConverters(boolean flag)
/*      */   {
/* 3899 */     this.useJvmCharsetConverters.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getPinGlobalTxToPhysicalConnection()
/*      */   {
/* 3906 */     return this.pinGlobalTxToPhysicalConnection.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3913 */   public void setPinGlobalTxToPhysicalConnection(boolean flag) { this.pinGlobalTxToPhysicalConnection.setValue(flag); }
/*      */   
/*      */   private StringConnectionProperty clientCertificateKeyStorePassword;
/*      */   private StringConnectionProperty trustCertificateKeyStoreType;
/*      */   private StringConnectionProperty trustCertificateKeyStorePassword;
/*      */   private BooleanConnectionProperty verifyServerCertificate;
/*      */   private BooleanConnectionProperty useAffectedRows;
/*      */   private StringConnectionProperty passwordCharacterEncoding;
/*      */   private IntegerConnectionProperty maxAllowedPacket;
/*      */   private StringConnectionProperty authenticationPlugins;
/*      */   private StringConnectionProperty disabledAuthenticationPlugins;
/*      */   private StringConnectionProperty defaultAuthenticationPlugin;
/* 3925 */   private BooleanConnectionProperty disconnectOnExpiredPasswords; private BooleanConnectionProperty getProceduresReturnsFunctions; private BooleanConnectionProperty detectCustomCollations; public void setGatherPerfMetrics(boolean flag) { setGatherPerformanceMetrics(flag); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getGatherPerfMetrics()
/*      */   {
/* 3932 */     return getGatherPerformanceMetrics();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUltraDevHack(boolean flag)
/*      */   {
/* 3939 */     setUseUltraDevWorkAround(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUltraDevHack()
/*      */   {
/* 3946 */     return getUseUltraDevWorkAround();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setInteractiveClient(boolean property)
/*      */   {
/* 3953 */     setIsInteractiveClient(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setSocketFactory(String name)
/*      */   {
/* 3960 */     setSocketFactoryClassName(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getSocketFactory()
/*      */   {
/* 3967 */     return getSocketFactoryClassName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseServerPrepStmts(boolean flag)
/*      */   {
/* 3974 */     setUseServerPreparedStmts(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseServerPrepStmts()
/*      */   {
/* 3981 */     return getUseServerPreparedStmts();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setCacheCallableStmts(boolean flag)
/*      */   {
/* 3988 */     setCacheCallableStatements(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getCacheCallableStmts()
/*      */   {
/* 3995 */     return getCacheCallableStatements();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setCachePrepStmts(boolean flag)
/*      */   {
/* 4002 */     setCachePreparedStatements(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getCachePrepStmts()
/*      */   {
/* 4009 */     return getCachePreparedStatements();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setCallableStmtCacheSize(int cacheSize)
/*      */     throws SQLException
/*      */   {
/* 4016 */     setCallableStatementCacheSize(cacheSize);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getCallableStmtCacheSize()
/*      */   {
/* 4023 */     return getCallableStatementCacheSize();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setPrepStmtCacheSize(int cacheSize)
/*      */     throws SQLException
/*      */   {
/* 4030 */     setPreparedStatementCacheSize(cacheSize);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getPrepStmtCacheSize()
/*      */   {
/* 4037 */     return getPreparedStatementCacheSize();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setPrepStmtCacheSqlLimit(int sqlLimit)
/*      */     throws SQLException
/*      */   {
/* 4044 */     setPreparedStatementCacheSqlLimit(sqlLimit);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getPrepStmtCacheSqlLimit()
/*      */   {
/* 4051 */     return getPreparedStatementCacheSqlLimit();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getNoAccessToProcedureBodies()
/*      */   {
/* 4058 */     return this.noAccessToProcedureBodies.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setNoAccessToProcedureBodies(boolean flag)
/*      */   {
/* 4065 */     this.noAccessToProcedureBodies.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseOldAliasMetadataBehavior()
/*      */   {
/* 4072 */     return this.useOldAliasMetadataBehavior.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseOldAliasMetadataBehavior(boolean flag)
/*      */   {
/* 4079 */     this.useOldAliasMetadataBehavior.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getClientCertificateKeyStorePassword()
/*      */   {
/* 4086 */     return this.clientCertificateKeyStorePassword.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClientCertificateKeyStorePassword(String value)
/*      */   {
/* 4094 */     this.clientCertificateKeyStorePassword.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getClientCertificateKeyStoreType()
/*      */   {
/* 4101 */     return this.clientCertificateKeyStoreType.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClientCertificateKeyStoreType(String value)
/*      */   {
/* 4109 */     this.clientCertificateKeyStoreType.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getClientCertificateKeyStoreUrl()
/*      */   {
/* 4116 */     return this.clientCertificateKeyStoreUrl.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClientCertificateKeyStoreUrl(String value)
/*      */   {
/* 4124 */     this.clientCertificateKeyStoreUrl.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getTrustCertificateKeyStorePassword()
/*      */   {
/* 4131 */     return this.trustCertificateKeyStorePassword.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustCertificateKeyStorePassword(String value)
/*      */   {
/* 4139 */     this.trustCertificateKeyStorePassword.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getTrustCertificateKeyStoreType()
/*      */   {
/* 4146 */     return this.trustCertificateKeyStoreType.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustCertificateKeyStoreType(String value)
/*      */   {
/* 4154 */     this.trustCertificateKeyStoreType.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getTrustCertificateKeyStoreUrl()
/*      */   {
/* 4161 */     return this.trustCertificateKeyStoreUrl.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustCertificateKeyStoreUrl(String value)
/*      */   {
/* 4169 */     this.trustCertificateKeyStoreUrl.setValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseSSPSCompatibleTimezoneShift()
/*      */   {
/* 4176 */     return this.useSSPSCompatibleTimezoneShift.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseSSPSCompatibleTimezoneShift(boolean flag)
/*      */   {
/* 4183 */     this.useSSPSCompatibleTimezoneShift.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getTreatUtilDateAsTimestamp()
/*      */   {
/* 4190 */     return this.treatUtilDateAsTimestamp.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setTreatUtilDateAsTimestamp(boolean flag)
/*      */   {
/* 4197 */     this.treatUtilDateAsTimestamp.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseFastDateParsing()
/*      */   {
/* 4204 */     return this.useFastDateParsing.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseFastDateParsing(boolean flag)
/*      */   {
/* 4211 */     this.useFastDateParsing.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getLocalSocketAddress()
/*      */   {
/* 4218 */     return this.localSocketAddress.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLocalSocketAddress(String address)
/*      */   {
/* 4225 */     this.localSocketAddress.setValue(address);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseConfigs(String configs)
/*      */   {
/* 4232 */     this.useConfigs.setValue(configs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getUseConfigs()
/*      */   {
/* 4239 */     return this.useConfigs.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getGenerateSimpleParameterMetadata()
/*      */   {
/* 4247 */     return this.generateSimpleParameterMetadata.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setGenerateSimpleParameterMetadata(boolean flag)
/*      */   {
/* 4254 */     this.generateSimpleParameterMetadata.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getLogXaCommands()
/*      */   {
/* 4261 */     return this.logXaCommands.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLogXaCommands(boolean flag)
/*      */   {
/* 4268 */     this.logXaCommands.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getResultSetSizeThreshold()
/*      */   {
/* 4275 */     return this.resultSetSizeThreshold.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setResultSetSizeThreshold(int threshold)
/*      */     throws SQLException
/*      */   {
/* 4282 */     this.resultSetSizeThreshold.setValue(threshold, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getNetTimeoutForStreamingResults()
/*      */   {
/* 4289 */     return this.netTimeoutForStreamingResults.getValueAsInt();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setNetTimeoutForStreamingResults(int value)
/*      */     throws SQLException
/*      */   {
/* 4296 */     this.netTimeoutForStreamingResults.setValue(value, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getEnableQueryTimeouts()
/*      */   {
/* 4303 */     return this.enableQueryTimeouts.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setEnableQueryTimeouts(boolean flag)
/*      */   {
/* 4310 */     this.enableQueryTimeouts.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getPadCharsWithSpace()
/*      */   {
/* 4317 */     return this.padCharsWithSpace.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setPadCharsWithSpace(boolean flag)
/*      */   {
/* 4324 */     this.padCharsWithSpace.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getUseDynamicCharsetInfo()
/*      */   {
/* 4331 */     return this.useDynamicCharsetInfo.getValueAsBoolean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUseDynamicCharsetInfo(boolean flag)
/*      */   {
/* 4338 */     this.useDynamicCharsetInfo.setValue(flag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getClientInfoProvider()
/*      */   {
/* 4345 */     return this.clientInfoProvider.getValueAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setClientInfoProvider(String classname)
/*      */   {
/* 4352 */     this.clientInfoProvider.setValue(classname);
/*      */   }
/*      */   
/*      */   public boolean getPopulateInsertRowWithDefaultValues() {
/* 4356 */     return this.populateInsertRowWithDefaultValues.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setPopulateInsertRowWithDefaultValues(boolean flag) {
/* 4360 */     this.populateInsertRowWithDefaultValues.setValue(flag);
/*      */   }
/*      */   
/*      */   public String getLoadBalanceStrategy() {
/* 4364 */     return this.loadBalanceStrategy.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceStrategy(String strategy) {
/* 4368 */     this.loadBalanceStrategy.setValue(strategy);
/*      */   }
/*      */   
/*      */   public boolean getTcpNoDelay() {
/* 4372 */     return this.tcpNoDelay.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setTcpNoDelay(boolean flag) {
/* 4376 */     this.tcpNoDelay.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getTcpKeepAlive() {
/* 4380 */     return this.tcpKeepAlive.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setTcpKeepAlive(boolean flag) {
/* 4384 */     this.tcpKeepAlive.setValue(flag);
/*      */   }
/*      */   
/*      */   public int getTcpRcvBuf() {
/* 4388 */     return this.tcpRcvBuf.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setTcpRcvBuf(int bufSize) throws SQLException {
/* 4392 */     this.tcpRcvBuf.setValue(bufSize, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public int getTcpSndBuf() {
/* 4396 */     return this.tcpSndBuf.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setTcpSndBuf(int bufSize) throws SQLException {
/* 4400 */     this.tcpSndBuf.setValue(bufSize, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public int getTcpTrafficClass() {
/* 4404 */     return this.tcpTrafficClass.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setTcpTrafficClass(int classFlags) throws SQLException {
/* 4408 */     this.tcpTrafficClass.setValue(classFlags, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public boolean getUseNanosForElapsedTime() {
/* 4412 */     return this.useNanosForElapsedTime.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseNanosForElapsedTime(boolean flag) {
/* 4416 */     this.useNanosForElapsedTime.setValue(flag);
/*      */   }
/*      */   
/*      */   public long getSlowQueryThresholdNanos() {
/* 4420 */     return this.slowQueryThresholdNanos.getValueAsLong();
/*      */   }
/*      */   
/*      */   public void setSlowQueryThresholdNanos(long nanos) throws SQLException {
/* 4424 */     this.slowQueryThresholdNanos.setValue(nanos, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public String getStatementInterceptors() {
/* 4428 */     return this.statementInterceptors.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setStatementInterceptors(String value) {
/* 4432 */     this.statementInterceptors.setValue(value);
/*      */   }
/*      */   
/*      */   public boolean getUseDirectRowUnpack() {
/* 4436 */     return this.useDirectRowUnpack.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseDirectRowUnpack(boolean flag) {
/* 4440 */     this.useDirectRowUnpack.setValue(flag);
/*      */   }
/*      */   
/*      */   public String getLargeRowSizeThreshold() {
/* 4444 */     return this.largeRowSizeThreshold.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setLargeRowSizeThreshold(String value) throws SQLException {
/* 4448 */     this.largeRowSizeThreshold.setValue(value, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public boolean getUseBlobToStoreUTF8OutsideBMP() {
/* 4452 */     return this.useBlobToStoreUTF8OutsideBMP.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseBlobToStoreUTF8OutsideBMP(boolean flag) {
/* 4456 */     this.useBlobToStoreUTF8OutsideBMP.setValue(flag);
/*      */   }
/*      */   
/*      */   public String getUtf8OutsideBmpExcludedColumnNamePattern() {
/* 4460 */     return this.utf8OutsideBmpExcludedColumnNamePattern.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setUtf8OutsideBmpExcludedColumnNamePattern(String regexPattern) {
/* 4464 */     this.utf8OutsideBmpExcludedColumnNamePattern.setValue(regexPattern);
/*      */   }
/*      */   
/*      */   public String getUtf8OutsideBmpIncludedColumnNamePattern() {
/* 4468 */     return this.utf8OutsideBmpIncludedColumnNamePattern.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setUtf8OutsideBmpIncludedColumnNamePattern(String regexPattern) {
/* 4472 */     this.utf8OutsideBmpIncludedColumnNamePattern.setValue(regexPattern);
/*      */   }
/*      */   
/*      */   public boolean getIncludeInnodbStatusInDeadlockExceptions() {
/* 4476 */     return this.includeInnodbStatusInDeadlockExceptions.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setIncludeInnodbStatusInDeadlockExceptions(boolean flag) {
/* 4480 */     this.includeInnodbStatusInDeadlockExceptions.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getBlobsAreStrings() {
/* 4484 */     return this.blobsAreStrings.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setBlobsAreStrings(boolean flag) {
/* 4488 */     this.blobsAreStrings.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getFunctionsNeverReturnBlobs() {
/* 4492 */     return this.functionsNeverReturnBlobs.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setFunctionsNeverReturnBlobs(boolean flag) {
/* 4496 */     this.functionsNeverReturnBlobs.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getAutoSlowLog() {
/* 4500 */     return this.autoSlowLog.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setAutoSlowLog(boolean flag) {
/* 4504 */     this.autoSlowLog.setValue(flag);
/*      */   }
/*      */   
/*      */   public String getConnectionLifecycleInterceptors() {
/* 4508 */     return this.connectionLifecycleInterceptors.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setConnectionLifecycleInterceptors(String interceptors) {
/* 4512 */     this.connectionLifecycleInterceptors.setValue(interceptors);
/*      */   }
/*      */   
/*      */   public String getProfilerEventHandler() {
/* 4516 */     return this.profilerEventHandler.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setProfilerEventHandler(String handler) {
/* 4520 */     this.profilerEventHandler.setValue(handler);
/*      */   }
/*      */   
/*      */   public boolean getVerifyServerCertificate() {
/* 4524 */     return this.verifyServerCertificate.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setVerifyServerCertificate(boolean flag) {
/* 4528 */     this.verifyServerCertificate.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseLegacyDatetimeCode() {
/* 4532 */     return this.useLegacyDatetimeCode.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseLegacyDatetimeCode(boolean flag) {
/* 4536 */     this.useLegacyDatetimeCode.setValue(flag);
/*      */   }
/*      */   
/*      */   public int getSelfDestructOnPingSecondsLifetime() {
/* 4540 */     return this.selfDestructOnPingSecondsLifetime.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setSelfDestructOnPingSecondsLifetime(int seconds) throws SQLException {
/* 4544 */     this.selfDestructOnPingSecondsLifetime.setValue(seconds, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public int getSelfDestructOnPingMaxOperations() {
/* 4548 */     return this.selfDestructOnPingMaxOperations.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setSelfDestructOnPingMaxOperations(int maxOperations) throws SQLException {
/* 4552 */     this.selfDestructOnPingMaxOperations.setValue(maxOperations, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public boolean getUseColumnNamesInFindColumn() {
/* 4556 */     return this.useColumnNamesInFindColumn.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseColumnNamesInFindColumn(boolean flag) {
/* 4560 */     this.useColumnNamesInFindColumn.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseLocalTransactionState() {
/* 4564 */     return this.useLocalTransactionState.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setUseLocalTransactionState(boolean flag) {
/* 4568 */     this.useLocalTransactionState.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getCompensateOnDuplicateKeyUpdateCounts() {
/* 4572 */     return this.compensateOnDuplicateKeyUpdateCounts.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setCompensateOnDuplicateKeyUpdateCounts(boolean flag) {
/* 4576 */     this.compensateOnDuplicateKeyUpdateCounts.setValue(flag);
/*      */   }
/*      */   
/*      */   public int getLoadBalanceBlacklistTimeout() {
/* 4580 */     return this.loadBalanceBlacklistTimeout.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceBlacklistTimeout(int loadBalanceBlacklistTimeout) throws SQLException {
/* 4584 */     this.loadBalanceBlacklistTimeout.setValue(loadBalanceBlacklistTimeout, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public int getLoadBalancePingTimeout() {
/* 4588 */     return this.loadBalancePingTimeout.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setLoadBalancePingTimeout(int loadBalancePingTimeout) throws SQLException {
/* 4592 */     this.loadBalancePingTimeout.setValue(loadBalancePingTimeout, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public void setRetriesAllDown(int retriesAllDown) throws SQLException {
/* 4596 */     this.retriesAllDown.setValue(retriesAllDown, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public int getRetriesAllDown() {
/* 4600 */     return this.retriesAllDown.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setUseAffectedRows(boolean flag) {
/* 4604 */     this.useAffectedRows.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getUseAffectedRows() {
/* 4608 */     return this.useAffectedRows.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setPasswordCharacterEncoding(String characterSet) {
/* 4612 */     this.passwordCharacterEncoding.setValue(characterSet);
/*      */   }
/*      */   
/*      */   public String getPasswordCharacterEncoding() {
/* 4616 */     return this.passwordCharacterEncoding.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setExceptionInterceptors(String exceptionInterceptors) {
/* 4620 */     this.exceptionInterceptors.setValue(exceptionInterceptors);
/*      */   }
/*      */   
/*      */   public String getExceptionInterceptors() {
/* 4624 */     return this.exceptionInterceptors.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setMaxAllowedPacket(int max) throws SQLException {
/* 4628 */     this.maxAllowedPacket.setValue(max, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public int getMaxAllowedPacket() {
/* 4632 */     return this.maxAllowedPacket.getValueAsInt();
/*      */   }
/*      */   
/*      */   public boolean getQueryTimeoutKillsConnection() {
/* 4636 */     return this.queryTimeoutKillsConnection.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setQueryTimeoutKillsConnection(boolean queryTimeoutKillsConnection) {
/* 4640 */     this.queryTimeoutKillsConnection.setValue(queryTimeoutKillsConnection);
/*      */   }
/*      */   
/*      */   public boolean getLoadBalanceValidateConnectionOnSwapServer() {
/* 4644 */     return this.loadBalanceValidateConnectionOnSwapServer.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceValidateConnectionOnSwapServer(boolean loadBalanceValidateConnectionOnSwapServer)
/*      */   {
/* 4649 */     this.loadBalanceValidateConnectionOnSwapServer.setValue(loadBalanceValidateConnectionOnSwapServer);
/*      */   }
/*      */   
/*      */   public String getLoadBalanceConnectionGroup()
/*      */   {
/* 4654 */     return this.loadBalanceConnectionGroup.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceConnectionGroup(String loadBalanceConnectionGroup) {
/* 4658 */     this.loadBalanceConnectionGroup.setValue(loadBalanceConnectionGroup);
/*      */   }
/*      */   
/*      */   public String getLoadBalanceExceptionChecker() {
/* 4662 */     return this.loadBalanceExceptionChecker.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceExceptionChecker(String loadBalanceExceptionChecker) {
/* 4666 */     this.loadBalanceExceptionChecker.setValue(loadBalanceExceptionChecker);
/*      */   }
/*      */   
/*      */   public String getLoadBalanceSQLStateFailover() {
/* 4670 */     return this.loadBalanceSQLStateFailover.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceSQLStateFailover(String loadBalanceSQLStateFailover) {
/* 4674 */     this.loadBalanceSQLStateFailover.setValue(loadBalanceSQLStateFailover);
/*      */   }
/*      */   
/*      */   public String getLoadBalanceSQLExceptionSubclassFailover() {
/* 4678 */     return this.loadBalanceSQLExceptionSubclassFailover.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceSQLExceptionSubclassFailover(String loadBalanceSQLExceptionSubclassFailover) {
/* 4682 */     this.loadBalanceSQLExceptionSubclassFailover.setValue(loadBalanceSQLExceptionSubclassFailover);
/*      */   }
/*      */   
/*      */   public boolean getLoadBalanceEnableJMX() {
/* 4686 */     return this.loadBalanceEnableJMX.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceEnableJMX(boolean loadBalanceEnableJMX) {
/* 4690 */     this.loadBalanceEnableJMX.setValue(loadBalanceEnableJMX);
/*      */   }
/*      */   
/*      */   public void setLoadBalanceAutoCommitStatementThreshold(int loadBalanceAutoCommitStatementThreshold) throws SQLException {
/* 4694 */     this.loadBalanceAutoCommitStatementThreshold.setValue(loadBalanceAutoCommitStatementThreshold, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   public int getLoadBalanceAutoCommitStatementThreshold() {
/* 4698 */     return this.loadBalanceAutoCommitStatementThreshold.getValueAsInt();
/*      */   }
/*      */   
/*      */   public void setLoadBalanceAutoCommitStatementRegex(String loadBalanceAutoCommitStatementRegex) {
/* 4702 */     this.loadBalanceAutoCommitStatementRegex.setValue(loadBalanceAutoCommitStatementRegex);
/*      */   }
/*      */   
/*      */   public String getLoadBalanceAutoCommitStatementRegex() {
/* 4706 */     return this.loadBalanceAutoCommitStatementRegex.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setIncludeThreadDumpInDeadlockExceptions(boolean flag) {
/* 4710 */     this.includeThreadDumpInDeadlockExceptions.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getIncludeThreadDumpInDeadlockExceptions() {
/* 4714 */     return this.includeThreadDumpInDeadlockExceptions.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setIncludeThreadNamesAsStatementComment(boolean flag) {
/* 4718 */     this.includeThreadNamesAsStatementComment.setValue(flag);
/*      */   }
/*      */   
/*      */   public boolean getIncludeThreadNamesAsStatementComment() {
/* 4722 */     return this.includeThreadNamesAsStatementComment.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setAuthenticationPlugins(String authenticationPlugins) {
/* 4726 */     this.authenticationPlugins.setValue(authenticationPlugins);
/*      */   }
/*      */   
/*      */   public String getAuthenticationPlugins() {
/* 4730 */     return this.authenticationPlugins.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setDisabledAuthenticationPlugins(String disabledAuthenticationPlugins) {
/* 4734 */     this.disabledAuthenticationPlugins.setValue(disabledAuthenticationPlugins);
/*      */   }
/*      */   
/*      */   public String getDisabledAuthenticationPlugins() {
/* 4738 */     return this.disabledAuthenticationPlugins.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setDefaultAuthenticationPlugin(String defaultAuthenticationPlugin) {
/* 4742 */     this.defaultAuthenticationPlugin.setValue(defaultAuthenticationPlugin);
/*      */   }
/*      */   
/*      */   public String getDefaultAuthenticationPlugin()
/*      */   {
/* 4747 */     return this.defaultAuthenticationPlugin.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setParseInfoCacheFactory(String factoryClassname) {
/* 4751 */     this.parseInfoCacheFactory.setValue(factoryClassname);
/*      */   }
/*      */   
/*      */   public String getParseInfoCacheFactory() {
/* 4755 */     return this.parseInfoCacheFactory.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setServerConfigCacheFactory(String factoryClassname) {
/* 4759 */     this.serverConfigCacheFactory.setValue(factoryClassname);
/*      */   }
/*      */   
/*      */   public String getServerConfigCacheFactory() {
/* 4763 */     return this.serverConfigCacheFactory.getValueAsString();
/*      */   }
/*      */   
/*      */   public void setDisconnectOnExpiredPasswords(boolean disconnectOnExpiredPasswords) {
/* 4767 */     this.disconnectOnExpiredPasswords.setValue(disconnectOnExpiredPasswords);
/*      */   }
/*      */   
/*      */   public boolean getDisconnectOnExpiredPasswords() {
/* 4771 */     return this.disconnectOnExpiredPasswords.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public boolean getAllowMasterDownConnections() {
/* 4775 */     return this.allowMasterDownConnections.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setAllowMasterDownConnections(boolean connectIfMasterDown) {
/* 4779 */     this.allowMasterDownConnections.setValue(connectIfMasterDown);
/*      */   }
/*      */   
/*      */   public boolean getReplicationEnableJMX() {
/* 4783 */     return this.replicationEnableJMX.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setReplicationEnableJMX(boolean replicationEnableJMX) {
/* 4787 */     this.replicationEnableJMX.setValue(replicationEnableJMX);
/*      */   }
/*      */   
/*      */   public void setGetProceduresReturnsFunctions(boolean getProcedureReturnsFunctions)
/*      */   {
/* 4792 */     this.getProceduresReturnsFunctions.setValue(getProcedureReturnsFunctions);
/*      */   }
/*      */   
/*      */   public boolean getGetProceduresReturnsFunctions() {
/* 4796 */     return this.getProceduresReturnsFunctions.getValueAsBoolean();
/*      */   }
/*      */   
/*      */   public void setDetectCustomCollations(boolean detectCustomCollations) {
/* 4800 */     this.detectCustomCollations.setValue(detectCustomCollations);
/*      */   }
/*      */   
/*      */   public boolean getDetectCustomCollations() {
/* 4804 */     return this.detectCustomCollations.getValueAsBoolean();
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ConnectionPropertiesImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */