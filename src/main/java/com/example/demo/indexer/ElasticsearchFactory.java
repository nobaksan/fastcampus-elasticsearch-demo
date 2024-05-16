package com.example.demo.indexer;

import javax.net.ssl.SSLContext;

import co.elastic.clients.transport.TransportUtils;
import lombok.RequiredArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Component
@RequiredArgsConstructor
public class ElasticsearchFactory {
    private final String serverUrl = "https://localhost:9200";
    //private final String apiKey = "YnN4QWdJOEJZTWpncWxRempKNVY6ai1UVzUzeDRTMkdKUVBjZkhiWmdSZw==";
    private final String apiKey = "YU9QM2dJOEJ4aDM0XzFPWklUN2s6cEpudUZHTGRUV0taY0xoZE13UV9hQQ==";

    ElasticsearchClient getElasticsearchClient() {
        RestClient restClient = getRestClient();
        return new ElasticsearchClient(new RestClientTransport(restClient, new JacksonJsonpMapper()));
    }

    public RestClient getRestClient() {
        //String fingerprint = "b14e85fff4cc6fecb35d1d58a3cce6d5d7a45d28a867485ab87869686c302961";	// 추가된 부분
        String fingerprint = "b1f0929db6432ba92cc91e377dc7aac6b8f21e5fd6dbbfb747acb35eaad4a00b";
        SSLContext sslContext = TransportUtils.sslContextFromCaFingerprint(fingerprint); // 추가된 부분
        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
       // credsProv.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "iRHN0_WxKtaeYDMk8lTi"));
        credsProv.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "klpJr9*HPsEVfAe-HF=R"));

        return RestClient
                .builder(HttpHost.create(serverUrl))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setSSLContext(sslContext) // 추가된 부분
                        .setDefaultCredentialsProvider(credsProv)
                )
                .build();
    }
}
