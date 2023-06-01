package com.rahul.batchapp.config;

import com.rahul.batchapp.entity.EuropeanLeague;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;

public class EuropeanLeagueProcessor implements ItemProcessor<EuropeanLeague, EuropeanLeague> {

    private int chunkNumber;

   /*
   @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext) {
        chunkNumber = chunkContext.getStepContext().getStepExecution().getExecutionContext().getInt("chunkNumber", 0) + 1;
        chunkContext.getStepContext().getStepExecution().getExecutionContext().putInt("chunkNumber", chunkNumber);
        System.out.println("chunk number:" + chunkNumber);
    }
    */

    @Override
    public EuropeanLeague process(EuropeanLeague item) {
        System.out.println(item);
        //System.out.println("Processing item in chunk number: " + chunkNumber);
        return item;
    }
}
