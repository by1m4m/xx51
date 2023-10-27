/*     */ package com.google.api.client.util;
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
/*     */ public class Base64
/*     */ {
/*     */   public static byte[] encodeBase64(byte[] binaryData)
/*     */   {
/*  42 */     return com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.encodeBase64(binaryData);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encodeBase64String(byte[] binaryData)
/*     */   {
/*  53 */     return com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.encodeBase64String(binaryData);
/*     */   }
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
/*     */   public static byte[] encodeBase64URLSafe(byte[] binaryData)
/*     */   {
/*  67 */     return com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.encodeBase64URLSafe(binaryData);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encodeBase64URLSafeString(byte[] binaryData)
/*     */   {
/*  79 */     return com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(binaryData);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] decodeBase64(byte[] base64Data)
/*     */   {
/*  90 */     return com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.decodeBase64(base64Data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] decodeBase64(String base64String)
/*     */   {
/* 101 */     return com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.decodeBase64(base64String);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Base64.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */