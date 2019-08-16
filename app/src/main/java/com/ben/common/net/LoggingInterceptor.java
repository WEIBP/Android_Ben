package com.ben.common.net;

import android.util.Log;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a {@linkplain
 * OkHttpClient#networkInterceptors() network interceptor}. <p> The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 */
public final class LoggingInterceptor implements Interceptor {

        private static final Charset UTF8 = Charset.forName("UTF-8");
        public static final String LINE_SEPARATOR = System.getProperty("line.separator");
        private static final String TAG = "HTTP";
        private static final int MAX_LENGTH = 3000;
        private static final String BYTE_BODY = "-byte body)";
        private static final String END = "--> END ";

        public enum Level {
                NONE, BASIC, HEADERS, BODY
        }


        private volatile Level level = Level.BODY;

        /** Change the level at which this interceptor logs. */
        public LoggingInterceptor setLevel(Level level) {
                if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
                this.level = level;
                return this;
        }


        @Override public Response intercept(Chain chain) throws IOException {
                StringBuilder stringBuffer = new StringBuilder();
                Request request = chain.request();
                if (level == Level.NONE) {
                        return chain.proceed(request);
                }

                boolean logBody = level == Level.BODY;
                boolean logHeaders = logBody || level == Level.HEADERS;

                RequestBody requestBody = request.body();
                boolean hasRequestBody = requestBody != null;

                Connection connection = chain.connection();
                Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
                String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
                if (!logHeaders && hasRequestBody) {
                        requestStartMessage += " (" + requestBody.contentLength() + BYTE_BODY;
                }
                stringBuffer.append(requestStartMessage);

                if (logHeaders) {
                        if (hasRequestBody) {
                                // Request body headers are only present when installed as a network interceptor. Force
                                // them to be included (when available) so there values are known.
                                if (requestBody.contentType() != null) {
                                        stringBuffer.append(LINE_SEPARATOR).append("Content-Type: " + requestBody.contentType());
                                }
                                if (requestBody.contentLength() != -1) {
                                        stringBuffer.append(LINE_SEPARATOR).append("Content-Length: " + requestBody.contentLength());
                                }
                        }

                        Headers headers = request.headers();
                        for (int i = 0, count = headers.size(); i < count; i++) {
                                String name = headers.name(i);
                                // Skip headers from the request body as they are explicitly logged above.
                                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                                        stringBuffer.append(LINE_SEPARATOR).append(name + ": " + headers.value(i));
                                }
                        }

                        if (!logBody || !hasRequestBody) {
                                stringBuffer.append(LINE_SEPARATOR).append(END + request.method());
                        } else if (bodyEncoded(request.headers())) {
                                stringBuffer.append(LINE_SEPARATOR).append(END + request.method() + " (encoded body omitted)");
                        } else {
                                Buffer buffer = new Buffer();
                                requestBody.writeTo(buffer);

                                Charset charset = UTF8;
                                MediaType contentType = requestBody.contentType();
                                if (contentType != null) {
                                        charset = contentType.charset(UTF8);
                                }

                                stringBuffer.append(LINE_SEPARATOR).append("");
                                if (isPlaintext(buffer)) {
                                        stringBuffer.append(LINE_SEPARATOR).append(buffer.readString(charset));
                                        stringBuffer.append(LINE_SEPARATOR).append(END + request.method()
                                                + " (" + requestBody.contentLength() + BYTE_BODY);
                                } else {
                                        stringBuffer.append(LINE_SEPARATOR).append(END + request.method() + " (binary "
                                                + requestBody.contentLength() + "-byte body omitted)");
                                }
                        }
                }

                long startNs = System.nanoTime();
                Response response;
                try {
                        response = chain.proceed(request);
                } catch (Exception e) {
                        stringBuffer.append(LINE_SEPARATOR).append("<-- HTTP FAILED: " + e);
                        throw e;
                }
                long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

                ResponseBody responseBody = response.body();
                long contentLength = responseBody.contentLength();
                String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
                stringBuffer.append(LINE_SEPARATOR).append("<-- " + response.code() + ' ' + response.message() + ' '
                        + response.request().url() + " (" + tookMs + "ms" + (!logHeaders ? ", "
                        + bodySize + " body" : "") + ')');

                if (logHeaders) {
                        Headers headers = response.headers();
                        for (int i = 0, count = headers.size(); i < count; i++) {
                                stringBuffer.append(LINE_SEPARATOR).append(headers.name(i) + ": " + headers.value(i));
                        }

                        if (!logBody || !HttpHeaders.hasBody(response)) {
                                stringBuffer.append(LINE_SEPARATOR).append("<-- END HTTP");
                        } else if (bodyEncoded(response.headers())) {
                                stringBuffer.append(LINE_SEPARATOR).append("<-- END HTTP (encoded body omitted)");
                        } else {
                                BufferedSource source = responseBody.source();
                                source.request(Long.MAX_VALUE); // Buffer the entire body.
                                Buffer buffer = source.buffer();

                                Charset charset = UTF8;
                                MediaType contentType = responseBody.contentType();
                                if (contentType != null) {
                                        try {
                                                charset = contentType.charset(UTF8);
                                        } catch (UnsupportedCharsetException e) {
                                                stringBuffer.append(LINE_SEPARATOR).append("");
                                                stringBuffer.append(LINE_SEPARATOR).append("Couldn't decode the response body; charset is likely malformed.");
                                                stringBuffer.append(LINE_SEPARATOR).append("<-- END HTTP");

                                                return response;
                                        }
                                }

                                if (!isPlaintext(buffer)) {
                                        
                                        stringBuffer.append(LINE_SEPARATOR).append("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                                        return response;
                                }

                                if (contentLength != 0) {
                                        stringBuffer.append(LINE_SEPARATOR).append(buffer.clone().readString(charset));
                                        if (buffer.clone().readString(charset).length() < MAX_LENGTH) {
                                                stringBuffer.append(LINE_SEPARATOR).append(printJson(buffer.clone().readString(charset)));
                                        }
                                }

                                stringBuffer.append(LINE_SEPARATOR).append("<-- END HTTP (" + buffer.size() + BYTE_BODY);
                        }
                }
                String[] lines = stringBuffer.toString().split(LINE_SEPARATOR);
                printLine(TAG, true);
                for (String line : lines) {
                        Log.d(TAG, "║ " + line);
                }
                printLine(TAG, false);
                return response;
        }

        /**
         * Returns true if the body in question probably contains human readable text. Uses a small sample
         * of code points to detect unicode control characters commonly used in binary file signatures.
         */
        static boolean isPlaintext(Buffer buffer) {
                try {
                        Buffer prefix = new Buffer();
                        long byteCount = buffer.size() < 64 ? buffer.size() : 64;
                        buffer.copyTo(prefix, 0, byteCount);
                        for (int i = 0; i < 16; i++) {
                                if (prefix.exhausted()) {
                                        break;
                                }
                                int codePoint = prefix.readUtf8CodePoint();
                                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                                        return false;
                                }
                        }
                        return true;
                } catch (EOFException e) {
                        return false; // Truncated UTF-8 sequence.
                }
        }

        private boolean bodyEncoded(Headers headers) {
                String contentEncoding = headers.get("Content-Encoding");
                return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
        }

        public void printLine(String tag, boolean isTop) {
                if (isTop) {
                        Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
                } else {
                        Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
                }
        }

        /**
         * 打印Json
         * @param msg
         * @return
         */
        public String printJson(String msg) {
                String message;
                try {
                        if (msg.startsWith("{")) {
                                JSONObject jsonObject = new JSONObject(msg);
                                message = jsonObject.toString(4);
                                //最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
                        } else if (msg.startsWith("[")) {
                                JSONArray jsonArray = new JSONArray(msg);
                                message = jsonArray.toString(4);
                        } else {
                                message = msg;
                        }
                } catch (JSONException e) {
                        message = msg;
                }
                return message;
        }
}
