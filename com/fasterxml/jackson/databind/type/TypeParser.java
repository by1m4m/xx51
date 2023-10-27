/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeParser
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final TypeFactory _factory;
/*     */   
/*     */   public TypeParser(TypeFactory f)
/*     */   {
/*  22 */     this._factory = f;
/*     */   }
/*     */   
/*     */   public JavaType parse(String canonical)
/*     */     throws IllegalArgumentException
/*     */   {
/*  28 */     canonical = canonical.trim();
/*  29 */     MyTokenizer tokens = new MyTokenizer(canonical);
/*  30 */     JavaType type = parseType(tokens);
/*     */     
/*  32 */     if (tokens.hasMoreTokens()) {
/*  33 */       throw _problem(tokens, "Unexpected tokens after complete type");
/*     */     }
/*  35 */     return type;
/*     */   }
/*     */   
/*     */   protected JavaType parseType(MyTokenizer tokens)
/*     */     throws IllegalArgumentException
/*     */   {
/*  41 */     if (!tokens.hasMoreTokens()) {
/*  42 */       throw _problem(tokens, "Unexpected end-of-string");
/*     */     }
/*  44 */     Class<?> base = findClass(tokens.nextToken(), tokens);
/*     */     
/*  46 */     if (tokens.hasMoreTokens()) {
/*  47 */       String token = tokens.nextToken();
/*  48 */       if ("<".equals(token)) {
/*  49 */         return this._factory._fromParameterizedClass(base, parseTypes(tokens));
/*     */       }
/*     */       
/*  52 */       tokens.pushBack(token);
/*     */     }
/*  54 */     return this._factory._fromClass(base, null);
/*     */   }
/*     */   
/*     */   protected List<JavaType> parseTypes(MyTokenizer tokens)
/*     */     throws IllegalArgumentException
/*     */   {
/*  60 */     ArrayList<JavaType> types = new ArrayList();
/*  61 */     while (tokens.hasMoreTokens()) {
/*  62 */       types.add(parseType(tokens));
/*  63 */       if (!tokens.hasMoreTokens()) break;
/*  64 */       String token = tokens.nextToken();
/*  65 */       if (">".equals(token)) return types;
/*  66 */       if (!",".equals(token)) {
/*  67 */         throw _problem(tokens, "Unexpected token '" + token + "', expected ',' or '>')");
/*     */       }
/*     */     }
/*  70 */     throw _problem(tokens, "Unexpected end-of-string");
/*     */   }
/*     */   
/*     */   protected Class<?> findClass(String className, MyTokenizer tokens)
/*     */   {
/*     */     try {
/*  76 */       return ClassUtil.findClass(className);
/*     */     } catch (Exception e) {
/*  78 */       if ((e instanceof RuntimeException)) {
/*  79 */         throw ((RuntimeException)e);
/*     */       }
/*  81 */       throw _problem(tokens, "Can not locate class '" + className + "', problem: " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   protected IllegalArgumentException _problem(MyTokenizer tokens, String msg)
/*     */   {
/*  87 */     return new IllegalArgumentException("Failed to parse type '" + tokens.getAllInput() + "' (remaining: '" + tokens.getRemainingInput() + "'): " + msg);
/*     */   }
/*     */   
/*     */ 
/*     */   static final class MyTokenizer
/*     */     extends StringTokenizer
/*     */   {
/*     */     protected final String _input;
/*     */     
/*     */     protected int _index;
/*     */     protected String _pushbackToken;
/*     */     
/*     */     public MyTokenizer(String str)
/*     */     {
/* 101 */       super("<,>", true);
/* 102 */       this._input = str;
/*     */     }
/*     */     
/*     */     public boolean hasMoreTokens()
/*     */     {
/* 107 */       return (this._pushbackToken != null) || (super.hasMoreTokens());
/*     */     }
/*     */     
/*     */     public String nextToken()
/*     */     {
/*     */       String token;
/* 113 */       if (this._pushbackToken != null) {
/* 114 */         String token = this._pushbackToken;
/* 115 */         this._pushbackToken = null;
/*     */       } else {
/* 117 */         token = super.nextToken();
/*     */       }
/* 119 */       this._index += token.length();
/* 120 */       return token;
/*     */     }
/*     */     
/*     */     public void pushBack(String token) {
/* 124 */       this._pushbackToken = token;
/* 125 */       this._index -= token.length();
/*     */     }
/*     */     
/* 128 */     public String getAllInput() { return this._input; }
/* 129 */     public String getUsedInput() { return this._input.substring(0, this._index); }
/* 130 */     public String getRemainingInput() { return this._input.substring(this._index); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\TypeParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */