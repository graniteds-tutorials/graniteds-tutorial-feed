package org.graniteds.tutorial.feed.server;

import flex.messaging.messages.AsyncMessage;
import org.granite.gravity.Gravity;
import org.graniteds.tutorial.feed.entities.StockPrice;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.Override;
import java.math.BigDecimal;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@ApplicationScoped
public class StockFeedPublisher {

    private static final String[] STOCKS = { "AAPL", "GOOG", "MSFT", "AMZN" };

    // tag::inject-gravity[]
    @Inject
    private Gravity gravity;
    // end::inject-gravity[]

    private StockPrice[] stockPrices = new StockPrice[STOCKS.length];

    private Random random = new Random(System.currentTimeMillis());

    private Timer timer = new Timer();

    public StockFeedPublisher() {
        System.out.println("Init publishing");
        for (int i = 0; i < stockPrices.length; i++)
            stockPrices[i] = new StockPrice(STOCKS[i], new BigDecimal(50.0 + random.nextDouble()*100.0));
    }

    // tag::server-publish[]
    @PostConstruct
    private void init() {
        System.out.println("Schedule publising");
        timer.scheduleAtFixedRate(publishTask, 1000L, 1000L);
    }

    private TimerTask publishTask = new TimerTask() {
        @Override
        public void run() {
            System.out.println("Publish message");
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
    };
    // end::server-publish[]
}
