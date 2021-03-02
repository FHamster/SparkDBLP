package cn.jmu.spark_dblp.server;

import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class QueryConversionPipelineConfig {
    @Bean
    public QueryConversionPipeline defaultQueryConversionPipeline() {
        return QueryConversionPipeline.defaultPipeline();
    }

}

