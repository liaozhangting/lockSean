package com.lssm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch 配置类
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.lssm.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris:http://127.0.0.1:9200}")
    private String elasticsearchUri;

    @Value("${spring.elasticsearch.username:}")
    private String username;

    @Value("${spring.elasticsearch.password:}")
    private String password;

    @Override
    public ClientConfiguration clientConfiguration() {
        String host = elasticsearchUri.replace("http://", "").replace("https://", "");
        
        ClientConfiguration.MaybeSecureClientConfigurationBuilder builder = 
            ClientConfiguration.builder()
                .connectedTo(host);
        
        // 设置超时
        ClientConfiguration.TerminalClientConfigurationBuilder terminalBuilder = 
            builder.withConnectTimeout(java.time.Duration.ofSeconds(5))
                   .withSocketTimeout(java.time.Duration.ofSeconds(30));
        
        // 如果配置了用户名密码
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            return terminalBuilder.withBasicAuth(username, password).build();
        }
        
        return terminalBuilder.build();
    }
}
