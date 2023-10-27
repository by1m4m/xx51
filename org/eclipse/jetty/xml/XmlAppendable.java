/*     */ package org.eclipse.jetty.xml;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ import org.eclipse.jetty.util.StringUtil;
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
/*     */ public class XmlAppendable
/*     */ {
/*  32 */   private final String SPACES = "                                                                 ";
/*     */   private final Appendable _out;
/*     */   private final int _indent;
/*  35 */   private final Stack<String> _tags = new Stack();
/*  36 */   private String _space = "";
/*     */   
/*     */   public XmlAppendable(OutputStream out, String encoding) throws IOException
/*     */   {
/*  40 */     this(new OutputStreamWriter(out, encoding), encoding);
/*     */   }
/*     */   
/*     */   public XmlAppendable(Appendable out) throws IOException
/*     */   {
/*  45 */     this(out, 2);
/*     */   }
/*     */   
/*     */   public XmlAppendable(Appendable out, String encoding) throws IOException
/*     */   {
/*  50 */     this(out, 2, encoding);
/*     */   }
/*     */   
/*     */   public XmlAppendable(Appendable out, int indent) throws IOException
/*     */   {
/*  55 */     this(out, indent, "utf-8");
/*     */   }
/*     */   
/*     */   public XmlAppendable(Appendable out, int indent, String encoding) throws IOException
/*     */   {
/*  60 */     this._out = out;
/*  61 */     this._indent = indent;
/*  62 */     this._out.append("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n");
/*     */   }
/*     */   
/*     */   public XmlAppendable openTag(String tag, Map<String, String> attributes) throws IOException
/*     */   {
/*  67 */     this._out.append(this._space).append('<').append(tag);
/*  68 */     attributes(attributes);
/*     */     
/*  70 */     this._out.append(">\n");
/*  71 */     this._space += "                                                                 ".substring(0, this._indent);
/*  72 */     this._tags.push(tag);
/*  73 */     return this;
/*     */   }
/*     */   
/*     */   public XmlAppendable openTag(String tag) throws IOException
/*     */   {
/*  78 */     this._out.append(this._space).append('<').append(tag).append(">\n");
/*  79 */     this._space += "                                                                 ".substring(0, this._indent);
/*  80 */     this._tags.push(tag);
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public XmlAppendable content(String s) throws IOException
/*     */   {
/*  86 */     if (s != null) {
/*  87 */       this._out.append(StringUtil.sanitizeXmlString(s));
/*     */     }
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public XmlAppendable cdata(String s) throws IOException
/*     */   {
/*  94 */     this._out.append("<![CDATA[").append(s).append("]]>");
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public XmlAppendable tag(String tag) throws IOException
/*     */   {
/* 100 */     this._out.append(this._space).append('<').append(tag).append("/>\n");
/* 101 */     return this;
/*     */   }
/*     */   
/*     */   public XmlAppendable tag(String tag, Map<String, String> attributes) throws IOException
/*     */   {
/* 106 */     this._out.append(this._space).append('<').append(tag);
/* 107 */     attributes(attributes);
/* 108 */     this._out.append("/>\n");
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public XmlAppendable tag(String tag, String content) throws IOException
/*     */   {
/* 114 */     this._out.append(this._space).append('<').append(tag).append('>');
/* 115 */     content(content);
/* 116 */     this._out.append("</").append(tag).append(">\n");
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   public XmlAppendable tagCDATA(String tag, String data) throws IOException
/*     */   {
/* 122 */     this._out.append(this._space).append('<').append(tag).append('>');
/* 123 */     cdata(data);
/* 124 */     this._out.append("</").append(tag).append(">\n");
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public XmlAppendable tag(String tag, Map<String, String> attributes, String content) throws IOException
/*     */   {
/* 130 */     this._out.append(this._space).append('<').append(tag);
/* 131 */     attributes(attributes);
/* 132 */     this._out.append('>');
/* 133 */     content(content);
/* 134 */     this._out.append("</").append(tag).append(">\n");
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public XmlAppendable closeTag() throws IOException
/*     */   {
/* 140 */     if (this._tags.isEmpty())
/* 141 */       throw new IllegalStateException("Tags closed");
/* 142 */     String tag = (String)this._tags.pop();
/* 143 */     this._space = this._space.substring(0, this._space.length() - this._indent);
/* 144 */     this._out.append(this._space).append("</").append(tag).append(">\n");
/* 145 */     if ((this._tags.isEmpty()) && ((this._out instanceof Closeable)))
/* 146 */       ((Closeable)this._out).close();
/* 147 */     return this;
/*     */   }
/*     */   
/*     */   private void attributes(Map<String, String> attributes) throws IOException
/*     */   {
/* 152 */     for (String k : attributes.keySet())
/*     */     {
/* 154 */       String v = (String)attributes.get(k);
/* 155 */       this._out.append(' ').append(k).append("=\"");
/* 156 */       content(v);
/* 157 */       this._out.append('"');
/*     */     }
/*     */   }
/*     */   
/*     */   public void literal(String xml) throws IOException
/*     */   {
/* 163 */     this._out.append(xml);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\xml\XmlAppendable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */