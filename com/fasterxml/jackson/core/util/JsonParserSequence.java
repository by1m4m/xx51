/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParseException;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class JsonParserSequence
/*     */   extends JsonParserDelegate
/*     */ {
/*     */   protected final JsonParser[] _parsers;
/*     */   protected int _nextParser;
/*     */   
/*     */   protected JsonParserSequence(JsonParser[] parsers)
/*     */   {
/*  37 */     super(parsers[0]);
/*  38 */     this._parsers = parsers;
/*  39 */     this._nextParser = 1;
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
/*     */   public static JsonParserSequence createFlattened(JsonParser first, JsonParser second)
/*     */   {
/*  53 */     if ((!(first instanceof JsonParserSequence)) && (!(second instanceof JsonParserSequence)))
/*     */     {
/*  55 */       return new JsonParserSequence(new JsonParser[] { first, second });
/*     */     }
/*  57 */     ArrayList<JsonParser> p = new ArrayList();
/*  58 */     if ((first instanceof JsonParserSequence)) {
/*  59 */       ((JsonParserSequence)first).addFlattenedActiveParsers(p);
/*     */     } else {
/*  61 */       p.add(first);
/*     */     }
/*  63 */     if ((second instanceof JsonParserSequence)) {
/*  64 */       ((JsonParserSequence)second).addFlattenedActiveParsers(p);
/*     */     } else {
/*  66 */       p.add(second);
/*     */     }
/*  68 */     return new JsonParserSequence((JsonParser[])p.toArray(new JsonParser[p.size()]));
/*     */   }
/*     */   
/*     */ 
/*     */   protected void addFlattenedActiveParsers(List<JsonParser> result)
/*     */   {
/*  74 */     int i = this._nextParser - 1; for (int len = this._parsers.length; i < len; i++) {
/*  75 */       JsonParser p = this._parsers[i];
/*  76 */       if ((p instanceof JsonParserSequence)) {
/*  77 */         ((JsonParserSequence)p).addFlattenedActiveParsers(result);
/*     */       } else {
/*  79 */         result.add(p);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */     do
/*     */     {
/*  93 */       this.delegate.close(); } while (switchToNext());
/*     */   }
/*     */   
/*     */   public JsonToken nextToken()
/*     */     throws IOException, JsonParseException
/*     */   {
/*  99 */     JsonToken t = this.delegate.nextToken();
/* 100 */     if (t != null) return t;
/* 101 */     while (switchToNext()) {
/* 102 */       t = this.delegate.nextToken();
/* 103 */       if (t != null) return t;
/*     */     }
/* 105 */     return null;
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
/*     */ 
/*     */   public int containedParsersCount()
/*     */   {
/* 120 */     return this._parsers.length;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean switchToNext()
/*     */   {
/* 139 */     if (this._nextParser >= this._parsers.length) {
/* 140 */       return false;
/*     */     }
/* 142 */     this.delegate = this._parsers[(this._nextParser++)];
/* 143 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\util\JsonParserSequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */