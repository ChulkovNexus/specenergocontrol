package com.specenergocontrol.http;

import android.content.Context;

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class CustomRestTamplate extends RestTemplate {
    private final HttpClient httpClient;
    private final HttpContext httpContext;
    private final StatefullHttpComponentsClientHttpRequestFactory statefullHttpComponentsClientHttpRequestFactory;

    private static final int TIMEOUT = 60000;

    public CustomRestTamplate(Context context) {
        super();
        HttpParams params = new BasicHttpParams();
        HttpClientParams.setRedirecting(params, false);
        httpClient = new DefaultHttpClient(params);
        httpContext = new BasicHttpContext();

        statefullHttpComponentsClientHttpRequestFactory = new
                StatefullHttpComponentsClientHttpRequestFactory(httpClient, httpContext);
        super.setRequestFactory(statefullHttpComponentsClientHttpRequestFactory);

        if (getRequestFactory() instanceof SimpleClientHttpRequestFactory) {
            ((SimpleClientHttpRequestFactory) getRequestFactory()).setConnectTimeout(TIMEOUT);

            ((SimpleClientHttpRequestFactory) getRequestFactory()).setReadTimeout(TIMEOUT);
        } else if (getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory) {
            ((HttpComponentsClientHttpRequestFactory) getRequestFactory()).setReadTimeout(TIMEOUT);
            ((HttpComponentsClientHttpRequestFactory) getRequestFactory()).setConnectTimeout(TIMEOUT);
        }
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }

    public StatefullHttpComponentsClientHttpRequestFactory getStatefulHttpClientRequestFactory() {
        return statefullHttpComponentsClientHttpRequestFactory;
    }

    public class StatefullHttpComponentsClientHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {
        private final HttpContext httpContext;

        public StatefullHttpComponentsClientHttpRequestFactory(HttpClient httpClient, HttpContext httpContext) {
            super(httpClient);
            this.httpContext = httpContext;
        }

        @Override
        protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
            return this.httpContext;
        }
    }
}
