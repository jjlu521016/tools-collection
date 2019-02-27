package com.jason.toolscollection.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ESConfiguration implements FactoryBean<RestHighLevelClient>, InitializingBean, DisposableBean {

    @Value("${elasticsearch.cluster-nodes}")
    private String clusterNodes;

    private RestHighLevelClient restHighLevelClient;


    /**
     * 控制bean的实例化过程
     *
     * @return
     * @throws Exception
     */
    @Override
    public RestHighLevelClient getObject() throws Exception {
        return restHighLevelClient;
    }

    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        restHighLevelClient = buildClient();

    }

    @Override
    public void destroy() throws Exception {
        if (restHighLevelClient != null) {
            restHighLevelClient.close();
        }

    }

    private RestHighLevelClient buildClient() {
        try {
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(clusterNodes.split(":")[0], Integer.parseInt(clusterNodes.split(":")[1]), "http")));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return restHighLevelClient;
    }


}
