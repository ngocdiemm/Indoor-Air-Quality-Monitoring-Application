package com.uit.myairquality.Model;

import com.uit.myairquality.Interfaces.APIInterface;
import com.uit.myairquality.Interfaces.CallMap;
import com.uit.myairquality.Interfaces.CallToken;
import com.uit.myairquality.Interfaces.CallWeather;

import java.io.IOException;
import java.security.cert.CertificateException;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;


import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
    public static String token = null;
    //private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLcHRXNWJCcTlsRGliY2s5NHI3TldHQVl0SHBrUFI3N1A4V0hMWDVIX1E0In0.eyJleHAiOjE2NjUwNDQ2MTAsImlhdCI6MTY2NTA0NDU1MCwiYXV0aF90aW1lIjoxNjY1MDM5NDIwLCJqdGkiOiJlZjE4OTc2Yi05YjVkLTQ2MzktYWM3Ny0wOGZhNTcxNzFkMWIiLCJpc3MiOiJodHRwczovLzEwMy4xMjYuMTYxLjE5OS9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOlsibWFzdGVyLXJlYWxtIiwiYWNjb3VudCJdLCJzdWIiOiJlMTA4ODg5ZS1kYTNlLTRjMzMtYThkNC1kOGY3ZDU4ZTQ2MDMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJvcGVucmVtb3RlIiwic2Vzc2lvbl9zdGF0ZSI6IjlkY2RjMjE2LTExYmUtNDM3NC1hMGJiLTVkNTFkMzQ5N2Q0NiIsImFjciI6IjAiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9sb2NhbGhvc3QiLCJodHRwczovLzEwMy4xMjYuMTYxLjE5OSJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiY3JlYXRlLXJlYWxtIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJvZmZsaW5lX2FjY2VzcyIsImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJvcGVucmVtb3RlIjp7InJvbGVzIjpbIndyaXRlOmxvZ3MiLCJ3cml0ZTphc3NldHMiLCJyZWFkIiwid3JpdGU6YWRtaW4iLCJyZWFkOmxvZ3MiLCJyZWFkOm1hcCIsInJlYWQ6YXNzZXRzIiwid3JpdGU6dXNlciIsInJlYWQ6dXNlcnMiLCJ3cml0ZTpydWxlcyIsInJlYWQ6cnVsZXMiLCJ3cml0ZTphdHRyaWJ1dGVzIiwid3JpdGUiLCJyZWFkOmFkbWluIl19LCJtYXN0ZXItcmVhbG0iOnsicm9sZXMiOlsidmlldy1yZWFsbSIsInZpZXctaWRlbnRpdHktcHJvdmlkZXJzIiwibWFuYWdlLWlkZW50aXR5LXByb3ZpZGVycyIsImltcGVyc29uYXRpb24iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidmlldy1hdXRob3JpemF0aW9uIiwicXVlcnktY2xpZW50cyIsInF1ZXJ5LXVzZXJzIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiLCJxdWVyeS1ncm91cHMiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJzaWQiOiI5ZGNkYzIxNi0xMWJlLTQzNzQtYTBiYi01ZDUxZDM0OTdkNDYiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJTeXN0ZW0gQWRtaW5pc3RyYXRvciIsInByZWZlcnJlZF91c2VybmFtZSI6ImFkbWluIiwiZ2l2ZW5fbmFtZSI6IlN5c3RlbSIsImZhbWlseV9uYW1lIjoiQWRtaW5pc3RyYXRvciJ9.JeZw1i979XIQ4dE_OFZUEHFX-kW2Z-LsHJ9eZmHJJhPP0cK94sCPwibPwMj9FUZffNrxgBe1h8Ry5365aIjhKnOpncDDC6C5Zz0c4xrVDeOMwM5AboJeYCmJ79oBmlkJ2KTBEugb-7hzhYtArZa1Num1R9klXlrCVzBrHaerJ8C1K5PJEVni0d5yd9W62pp5ennkNcApOMgJKDS9sjZ0tfdSn6HBjbypqK_4I6Wqwkg4uO51aLf0TtTwRAxhLN56_cCP79tJlDKt-py4XKPzJ_205CHTS-wnaRKZ17YqtKU4iG2VgZG_oIgHIffI_SujmqdfpgqBHbn0D3MjlP2t-Q";
    //private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLcHRXNWJCcTlsRGliY2s5NHI3TldHQVl0SHBrUFI3N1A4V0hMWDVIX1E0In0.eyJleHAiOjE2NjUxOTY4NDIsImlhdCI6MTY2NTExMDQ2NCwiYXV0aF90aW1lIjoxNjY1MTEwNDQyLCJqdGkiOiJlZDA2MzZjZC0xZDBhLTQ4MDgtYWQ4MC00ZWU1MzcxYTQ5NTIiLCJpc3MiOiJodHRwczovLzEwMy4xMjYuMTYxLjE5OS9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMTAxZGQ1MmMtMjNiYS00ZjM4LWExMjQtYjc4MGUxYjVhODFiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoib3BlbnJlbW90ZSIsInNlc3Npb25fc3RhdGUiOiJjNzkxMGYyZC1lNDFmLTQ0YmUtYTY2Zi01NzM3ZDkyOGUyMzIiLCJhY3IiOiIwIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vbG9jYWxob3N0IiwiaHR0cHM6Ly8xMDMuMTI2LjE2MS4xOTkiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDp1c2VycyIsInJlYWQ6bG9ncyIsInJlYWQ6bWFwIiwicmVhZDpydWxlcyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwic2lkIjoiYzc5MTBmMmQtZTQxZi00NGJlLWE2NmYtNTczN2Q5MjhlMjMyIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyMSJ9.cabnvk_IP7Gk3yWbcWlJp-VRiZXKZ0vcIXf_-7xh24AtBHFe-W44VT0k1sZ5k3r0Ze4JAnNSs3kQg4FrdbGbNl9UQOuqYB4LV5jicebevTxIhElPzVPGIhUOTG0-5czaqVwsqNSxw-aP9EHgvJiuB-PJJ8y6jrNwGid7GDgucdUpzpZWrTu75wp6pCDEuTVzbZ8XOl56WlpwhanFS62Q--RRORJMXWOXGuqVkzmQpT5jWt0gPZJYT7mXvCf8Ru6KJ-bXzFWMfDOLEo_eKhlSy-SffRy6VVfPvRNWr8jzOHA9UTkR7VgdQ_Adw-9Q0DfMg1aDAQNJQhy7qO-YsLRHmA";
    //private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLcHRXNWJCcTlsRGliY2s5NHI3TldHQVl0SHBrUFI3N1A4V0hMWDVIX1E0In0.eyJleHAiOjE2NjUyMDU5MzgsImlhdCI6MTY2NTExOTU2OCwiYXV0aF90aW1lIjoxNjY1MTE5NTM4LCJqdGkiOiJmYjZkMTYyOS1lODNlLTQyYzMtYTlhNi1kOGZiZDhlMDU2YzIiLCJpc3MiOiJodHRwczovLzEwMy4xMjYuMTYxLjE5OS9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMTAxZGQ1MmMtMjNiYS00ZjM4LWExMjQtYjc4MGUxYjVhODFiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoib3BlbnJlbW90ZSIsInNlc3Npb25fc3RhdGUiOiJiMTNkOTQwMS1hN2ViLTRkN2EtYjJjNy1hNGIxMmVkZTI3NDIiLCJhY3IiOiIwIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vbG9jYWxob3N0IiwiaHR0cHM6Ly8xMDMuMTI2LjE2MS4xOTkiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDp1c2VycyIsInJlYWQ6bG9ncyIsInJlYWQ6bWFwIiwicmVhZDpydWxlcyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwic2lkIjoiYjEzZDk0MDEtYTdlYi00ZDdhLWIyYzctYTRiMTJlZGUyNzQyIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyMSJ9.BY20wuEhwMPCaMT0YLGgq5wdg0WrWHXG6YioCZwmav1ns3cfTpYpc2JIYMUyaCnzLwlWXIv7O09UUMcVTVDw5fKrOQ2cLOAe4yHQGRCsHl6Xr9-KgvdWt-uCS602hZWWsneYn8WZHvXqx1zs8eEra1qAxouJnv4DIm6B72625PmC5d9c-UNfpnPDM7KFBeCdXG11AXa0WpnRmFgmp8n9B5zoQDpJkqRcntrewArlvEkiYxPxGOLRfe1BNtnQTG17HVYn35YvwLiCiZ_JJ1kiUR7Q_Sp0nZhQOhxf26iEAx5LzrhNE8NhtT_zenNGh8NI7a1tcd-rBF-oxmd2-xSekA";
    //private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLcHRXNWJCcTlsRGliY2s5NHI3TldHQVl0SHBrUFI3N1A4V0hMWDVIX1E0In0.eyJleHAiOjE2NjUyMDc4MzMsImlhdCI6MTY2NTEyMTQ0OCwiYXV0aF90aW1lIjoxNjY1MTIxNDMzLCJqdGkiOiIxZjc3OTFjMy00OGJmLTQ4NGUtODI3MS1kOGY0NWNiNTcyMzkiLCJpc3MiOiJodHRwczovLzEwMy4xMjYuMTYxLjE5OS9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMTAxZGQ1MmMtMjNiYS00ZjM4LWExMjQtYjc4MGUxYjVhODFiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoib3BlbnJlbW90ZSIsInNlc3Npb25fc3RhdGUiOiI5MjYyNGM1Yi1iNjM3LTRjNDItOWVhYS02MzhkNmRkZDJiNjQiLCJhY3IiOiIwIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vbG9jYWxob3N0IiwiaHR0cHM6Ly8xMDMuMTI2LjE2MS4xOTkiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDp1c2VycyIsInJlYWQ6bG9ncyIsInJlYWQ6bWFwIiwicmVhZDpydWxlcyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwic2lkIjoiOTI2MjRjNWItYjYzNy00YzQyLTllYWEtNjM4ZDZkZGQyYjY0IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyMSJ9.U0pMuxOkHW8pZKBVmTlQeIzGq-WtQoHzxCIgf2-gmB8vsAFCZlajaBvO8jJpupmS7NzegIFI6TcfAXkXFZXxPIJAmPb0a4RtFr-pthoOtERpct5TjxZvrEfJjNcC1J_EM1aumUJvOUZE7goVl4qG4UCw08Su0wIcmztWthLQu3CTgNuO5XwBqORJwGqIZAdSLyRSjhx4970nU4C_z-DRxVMtv1jlDfUyHeIHUA6c9gEiEY5PpbR6bnHDslb9Ta3MUOzFEdMd_PcXZtNxsJFKaeD_YKp-n9dmIQ45DY5-JKa6Mefo92NuAO_X9Jv5SA3FB5hj4dW1ODxjbnCqR3ZqTg";
    //private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLcHRXNWJCcTlsRGliY2s5NHI3TldHQVl0SHBrUFI3N1A4V0hMWDVIX1E0In0.eyJleHAiOjE2NjUyMDg5NjQsImlhdCI6MTY2NTEyMjU3OSwiYXV0aF90aW1lIjoxNjY1MTIyNTY0LCJqdGkiOiJkNzdiNmE4Ny0yN2ViLTRiYWItYjVlNC03OGY1ZjYzYTQ4NzciLCJpc3MiOiJodHRwczovLzEwMy4xMjYuMTYxLjE5OS9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMTAxZGQ1MmMtMjNiYS00ZjM4LWExMjQtYjc4MGUxYjVhODFiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoib3BlbnJlbW90ZSIsInNlc3Npb25fc3RhdGUiOiI4N2RiYWQ0Yy1lNzU0LTQ3NzktYTY1OC0yNDc0YTcyYThjNzQiLCJhY3IiOiIwIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vbG9jYWxob3N0IiwiaHR0cHM6Ly8xMDMuMTI2LjE2MS4xOTkiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDp1c2VycyIsInJlYWQ6bG9ncyIsInJlYWQ6bWFwIiwicmVhZDpydWxlcyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwic2lkIjoiODdkYmFkNGMtZTc1NC00Nzc5LWE2NTgtMjQ3NGE3MmE4Yzc0IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyMSJ9.jr33vmRATXCtvQ6VGLxk_Y3XTRMzfpierqSSEvsOM0kKaZP0IIAlxlvWRnjj2W2iVMJqkKX-7vY6-GqYGoXbqKwXcsdziHQMG3aTK-t-WaiE-fh_FU3Ge5z1A-9dXkLAS_gMjeVxCQZt58vycrBzDcJUSceiSaAsIpDt_DzuVWGJMGFd2-26k2SJbWN-EijQy8f--UH7p_9tZxPytFnVKxRy_QRV6cZ7bYDkDaZkccifVfZnOnwB1OZm52IlDR2YMESg9uGH6HJeGSXJ9tZ6ob5Ta5Bs1Q7PKDqs6MKyi7x3dFGYG0jbZ3KMjz1MEtYUHcVSzSuADm1gJHT592gTNw";
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //Log
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);

            //Bear token
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                }
            });


            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static CallMap CallMap() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(CallMap.class);
    }
    public static CallToken CallToken() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(CallToken.class);
    }
    public static CallWeather CallWeather() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(CallWeather.class);
    }
    public static Retrofit getClient() {

        //HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

       /* OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();*
        */

        OkHttpClient client = getUnsafeOkHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/token/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}

