/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ 
/*     */ public class HttpStatus
/*     */ {
/*     */   public static final int CONTINUE_100 = 100;
/*     */   
/*     */   public static final int SWITCHING_PROTOCOLS_101 = 101;
/*     */   
/*     */   public static final int PROCESSING_102 = 102;
/*     */   
/*     */   public static final int OK_200 = 200;
/*     */   
/*     */   public static final int CREATED_201 = 201;
/*     */   
/*     */   public static final int ACCEPTED_202 = 202;
/*     */   
/*     */   public static final int NON_AUTHORITATIVE_INFORMATION_203 = 203;
/*     */   
/*     */   public static final int NO_CONTENT_204 = 204;
/*     */   
/*     */   public static final int RESET_CONTENT_205 = 205;
/*     */   
/*     */   public static final int PARTIAL_CONTENT_206 = 206;
/*     */   
/*     */   public static final int MULTI_STATUS_207 = 207;
/*     */   
/*     */   public static final int MULTIPLE_CHOICES_300 = 300;
/*     */   
/*     */   public static final int MOVED_PERMANENTLY_301 = 301;
/*     */   
/*     */   public static final int MOVED_TEMPORARILY_302 = 302;
/*     */   
/*     */   public static final int FOUND_302 = 302;
/*     */   
/*     */   public static final int SEE_OTHER_303 = 303;
/*     */   
/*     */   public static final int NOT_MODIFIED_304 = 304;
/*     */   
/*     */   public static final int USE_PROXY_305 = 305;
/*     */   
/*     */   public static final int TEMPORARY_REDIRECT_307 = 307;
/*     */   
/*     */   public static final int PERMANENT_REDIRECT_308 = 308;
/*     */   
/*     */   public static final int BAD_REQUEST_400 = 400;
/*     */   
/*     */   public static final int UNAUTHORIZED_401 = 401;
/*     */   
/*     */   public static final int PAYMENT_REQUIRED_402 = 402;
/*     */   
/*     */   public static final int FORBIDDEN_403 = 403;
/*     */   
/*     */   public static final int NOT_FOUND_404 = 404;
/*     */   
/*     */   public static final int METHOD_NOT_ALLOWED_405 = 405;
/*     */   
/*     */   public static final int NOT_ACCEPTABLE_406 = 406;
/*     */   
/*     */   public static final int PROXY_AUTHENTICATION_REQUIRED_407 = 407;
/*     */   
/*     */   public static final int REQUEST_TIMEOUT_408 = 408;
/*     */   
/*     */   public static final int CONFLICT_409 = 409;
/*     */   public static final int GONE_410 = 410;
/*     */   public static final int LENGTH_REQUIRED_411 = 411;
/*     */   public static final int PRECONDITION_FAILED_412 = 412;
/*     */   @Deprecated
/*     */   public static final int REQUEST_ENTITY_TOO_LARGE_413 = 413;
/*     */   public static final int PAYLOAD_TOO_LARGE_413 = 413;
/*     */   @Deprecated
/*     */   public static final int REQUEST_URI_TOO_LONG_414 = 414;
/*     */   public static final int URI_TOO_LONG_414 = 414;
/*     */   public static final int UNSUPPORTED_MEDIA_TYPE_415 = 415;
/*     */   @Deprecated
/*     */   public static final int REQUESTED_RANGE_NOT_SATISFIABLE_416 = 416;
/*     */   public static final int RANGE_NOT_SATISFIABLE_416 = 416;
/*     */   public static final int EXPECTATION_FAILED_417 = 417;
/*     */   public static final int IM_A_TEAPOT_418 = 418;
/*     */   public static final int ENHANCE_YOUR_CALM_420 = 420;
/*     */   public static final int MISDIRECTED_REQUEST_421 = 421;
/*     */   public static final int UNPROCESSABLE_ENTITY_422 = 422;
/*     */   public static final int LOCKED_423 = 423;
/*     */   public static final int FAILED_DEPENDENCY_424 = 424;
/*     */   public static final int UPGRADE_REQUIRED_426 = 426;
/*     */   public static final int PRECONDITION_REQUIRED_428 = 428;
/*     */   public static final int TOO_MANY_REQUESTS_429 = 429;
/*     */   public static final int REQUEST_HEADER_FIELDS_TOO_LARGE_431 = 431;
/*     */   public static final int UNAVAILABLE_FOR_LEGAL_REASONS_451 = 451;
/*     */   public static final int INTERNAL_SERVER_ERROR_500 = 500;
/*     */   public static final int NOT_IMPLEMENTED_501 = 501;
/*     */   public static final int BAD_GATEWAY_502 = 502;
/*     */   public static final int SERVICE_UNAVAILABLE_503 = 503;
/*     */   public static final int GATEWAY_TIMEOUT_504 = 504;
/*     */   public static final int HTTP_VERSION_NOT_SUPPORTED_505 = 505;
/*     */   public static final int INSUFFICIENT_STORAGE_507 = 507;
/*     */   public static final int LOOP_DETECTED_508 = 508;
/*     */   public static final int NOT_EXTENDED_510 = 510;
/*     */   public static final int NETWORK_AUTHENTICATION_REQUIRED_511 = 511;
/*     */   public static final int MAX_CODE = 511;
/* 101 */   private static final Code[] codeMap = new Code['Ȁ'];
/*     */   
/*     */   static
/*     */   {
/* 105 */     for (Code code : Code.values())
/*     */     {
/* 107 */       codeMap[code._code] = code;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static enum Code
/*     */   {
/* 114 */     CONTINUE(100, "Continue"), 
/* 115 */     SWITCHING_PROTOCOLS(101, "Switching Protocols"), 
/* 116 */     PROCESSING(102, "Processing"), 
/*     */     
/*     */ 
/* 119 */     OK(200, "OK"), 
/* 120 */     CREATED(201, "Created"), 
/* 121 */     ACCEPTED(202, "Accepted"), 
/* 122 */     NON_AUTHORITATIVE_INFORMATION(203, "Non Authoritative Information"), 
/* 123 */     NO_CONTENT(204, "No Content"), 
/* 124 */     RESET_CONTENT(205, "Reset Content"), 
/* 125 */     PARTIAL_CONTENT(206, "Partial Content"), 
/* 126 */     MULTI_STATUS(207, "Multi-Status"), 
/*     */     
/* 128 */     MULTIPLE_CHOICES(300, "Multiple Choices"), 
/* 129 */     MOVED_PERMANENTLY(301, "Moved Permanently"), 
/* 130 */     MOVED_TEMPORARILY(302, "Moved Temporarily"), 
/* 131 */     FOUND(302, "Found"), 
/* 132 */     SEE_OTHER(303, "See Other"), 
/* 133 */     NOT_MODIFIED(304, "Not Modified"), 
/* 134 */     USE_PROXY(305, "Use Proxy"), 
/* 135 */     TEMPORARY_REDIRECT(307, "Temporary Redirect"), 
/* 136 */     PERMANET_REDIRECT(308, "Permanent Redirect"), 
/*     */     
/* 138 */     BAD_REQUEST(400, "Bad Request"), 
/* 139 */     UNAUTHORIZED(401, "Unauthorized"), 
/* 140 */     PAYMENT_REQUIRED(402, "Payment Required"), 
/* 141 */     FORBIDDEN(403, "Forbidden"), 
/* 142 */     NOT_FOUND(404, "Not Found"), 
/* 143 */     METHOD_NOT_ALLOWED(405, "Method Not Allowed"), 
/* 144 */     NOT_ACCEPTABLE(406, "Not Acceptable"), 
/* 145 */     PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"), 
/* 146 */     REQUEST_TIMEOUT(408, "Request Timeout"), 
/* 147 */     CONFLICT(409, "Conflict"), 
/* 148 */     GONE(410, "Gone"), 
/* 149 */     LENGTH_REQUIRED(411, "Length Required"), 
/* 150 */     PRECONDITION_FAILED(412, "Precondition Failed"), 
/* 151 */     PAYLOAD_TOO_LARGE(413, "Payload Too Large"), 
/* 152 */     URI_TOO_LONG(414, "URI Too Long"), 
/* 153 */     UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"), 
/* 154 */     RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable"), 
/* 155 */     EXPECTATION_FAILED(417, "Expectation Failed"), 
/* 156 */     IM_A_TEAPOT(418, "I'm a Teapot"), 
/* 157 */     ENHANCE_YOUR_CALM(420, "Enhance your Calm"), 
/* 158 */     MISDIRECTED_REQUEST(421, "Misdirected Request"), 
/* 159 */     UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"), 
/* 160 */     LOCKED(423, "Locked"), 
/* 161 */     FAILED_DEPENDENCY(424, "Failed Dependency"), 
/* 162 */     UPGRADE_REQUIRED(426, "Upgrade Required"), 
/* 163 */     PRECONDITION_REQUIRED(428, "Precondition Required"), 
/* 164 */     TOO_MANY_REQUESTS(429, "Too Many Requests"), 
/* 165 */     REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"), 
/* 166 */     UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable for Legal Reason"), 
/*     */     
/* 168 */     INTERNAL_SERVER_ERROR(500, "Server Error"), 
/* 169 */     NOT_IMPLEMENTED(501, "Not Implemented"), 
/* 170 */     BAD_GATEWAY(502, "Bad Gateway"), 
/* 171 */     SERVICE_UNAVAILABLE(503, "Service Unavailable"), 
/* 172 */     GATEWAY_TIMEOUT(504, "Gateway Timeout"), 
/* 173 */     HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"), 
/* 174 */     INSUFFICIENT_STORAGE(507, "Insufficient Storage"), 
/* 175 */     LOOP_DETECTED(508, "Loop Detected"), 
/* 176 */     NOT_EXTENDED(510, "Not Extended"), 
/* 177 */     NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");
/*     */     
/*     */ 
/*     */     private final int _code;
/*     */     
/*     */     private final String _message;
/*     */     
/*     */     private Code(int code, String message)
/*     */     {
/* 186 */       this._code = code;
/* 187 */       this._message = message;
/*     */     }
/*     */     
/*     */     public int getCode()
/*     */     {
/* 192 */       return this._code;
/*     */     }
/*     */     
/*     */     public String getMessage()
/*     */     {
/* 197 */       return this._message;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(int code)
/*     */     {
/* 203 */       return this._code == code;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 209 */       return String.format("[%03d %s]", new Object[] { Integer.valueOf(this._code), getMessage() });
/*     */     }
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
/*     */     public boolean isInformational()
/*     */     {
/* 224 */       return HttpStatus.isInformational(this._code);
/*     */     }
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
/*     */     public boolean isSuccess()
/*     */     {
/* 239 */       return HttpStatus.isSuccess(this._code);
/*     */     }
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
/*     */     public boolean isRedirection()
/*     */     {
/* 254 */       return HttpStatus.isRedirection(this._code);
/*     */     }
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
/*     */     public boolean isClientError()
/*     */     {
/* 269 */       return HttpStatus.isClientError(this._code);
/*     */     }
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
/*     */     public boolean isServerError()
/*     */     {
/* 284 */       return HttpStatus.isServerError(this._code);
/*     */     }
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
/*     */   public static Code getCode(int code)
/*     */   {
/* 298 */     if (code <= 511)
/*     */     {
/* 300 */       return codeMap[code];
/*     */     }
/* 302 */     return null;
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
/*     */   public static String getMessage(int code)
/*     */   {
/* 315 */     Code codeEnum = getCode(code);
/* 316 */     if (codeEnum != null)
/*     */     {
/* 318 */       return codeEnum.getMessage();
/*     */     }
/*     */     
/*     */ 
/* 322 */     return Integer.toString(code);
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
/*     */   public static boolean isInformational(int code)
/*     */   {
/* 339 */     return (100 <= code) && (code <= 199);
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
/*     */   public static boolean isSuccess(int code)
/*     */   {
/* 355 */     return (200 <= code) && (code <= 299);
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
/*     */   public static boolean isRedirection(int code)
/*     */   {
/* 371 */     return (300 <= code) && (code <= 399);
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
/*     */   public static boolean isClientError(int code)
/*     */   {
/* 387 */     return (400 <= code) && (code <= 499);
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
/*     */   public static boolean isServerError(int code)
/*     */   {
/* 403 */     return (500 <= code) && (code <= 599);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HttpStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */