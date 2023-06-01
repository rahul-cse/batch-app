package com.rahul.batchapp.config;

import com.rahul.batchapp.entity.EuropeanLeague;
import com.rahul.batchapp.repository.EuropeanLeagueRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class BatchConfig {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private EuropeanLeagueRepository europeanLeagueRepository;

    @Bean
    FlatFileItemReader<EuropeanLeague> reader() {
        FlatFileItemReader<EuropeanLeague> itemReader = new FlatFileItemReader<>();
        itemReader.setResource( new FileSystemResource("src/main/resources/european-leauge.csv"));
        itemReader.setName("CSV Reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<EuropeanLeague> lineMapper() {
        DefaultLineMapper<EuropeanLeague> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("ID","COUNTRY","CLUB");
        BeanWrapperFieldSetMapper<EuropeanLeague> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(EuropeanLeague.class);

        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        return lineMapper;
    }

    @Bean
    public EuropeanLeagueProcessor europeanLeagueProcessor() {
        return new EuropeanLeagueProcessor();
    }

    private RepositoryItemWriter writer() {
        RepositoryItemWriter<EuropeanLeague> itemWriter = new RepositoryItemWriter<EuropeanLeague>();
        itemWriter.setRepository(europeanLeagueRepository);
        itemWriter.setMethodName("save");
        return itemWriter;
    }

    @Bean
    public Step europeanLeagueProcessorStep () {
        return stepBuilderFactory.get("csvToDb-step").<EuropeanLeague,EuropeanLeague>chunk(10)
                .reader(reader())
                .processor(europeanLeagueProcessor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importEuropeanLeague")
                .flow(europeanLeagueProcessorStep())
                .end()
                .build();
    }
}
