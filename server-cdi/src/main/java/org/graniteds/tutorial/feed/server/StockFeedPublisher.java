package org.graniteds.tutorial.feed.server;

import java.math.BigDecimal;
import java.util.Random;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.granite.gravity.Gravity;
import org.graniteds.tutorial.feed.entities.StockPrice;

import flex.messaging.messages.AsyncMessage;

@ApplicationScoped @Singleton
public class StockFeedPublisher {

    private static final String[] STOCKS = { "AAPL", "GOOG", "MSFT", "AMZN" };

    // tag::inject-gravity[]
    @Inject
    private Gravity gravity;
    // end::inject-gravity[]

    private StockPrice[] stockPrices = new StockPrice[STOCKS.length];

    private Random random = new Random(System.currentTimeMillis());

    public StockFeedPublisher() {
        for (int i = 0; i < stockPrices.length; i++)
            stockPrices[i] = new StockPrice(STOCKS[i], new BigDecimal(50.0 + random.nextDouble()*100.0));
    }

    // tag::server-publish[]
    @Schedule(second="*/1", minute="*", hour="*", persistent=false)
    public void publish() {
        for (int i = 0; i < stockPrices.length; i++) {
            double inc = random.nextDouble()-0.5;
            stockPrices[i].setPrice(stockPrices[i].getPrice().add(new BigDecimal(inc)));
        }

        AsyncMessage message = new AsyncMessage(); // <1>
        message.setDestination("feedTopic");
        message.setHeader(AsyncMessage.SUBTOPIC_HEADER, "NASDAQ");
        message.setBody(stockPrices);
        gravity.publishMessage(message);
    }
    // end::server-publish[]
}
