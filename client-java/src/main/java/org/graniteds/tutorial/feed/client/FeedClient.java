package org.graniteds.tutorial.feed.client;

import org.granite.client.messaging.Consumer;
import org.granite.client.messaging.TopicMessageListener;
import org.granite.client.messaging.events.TopicMessageEvent;
import org.granite.client.tide.Context;
import org.granite.client.tide.impl.SimpleContextManager;
import org.granite.client.tide.server.ServerSession;

import java.text.NumberFormat;
import java.util.Locale;


public class FeedClient {

    public static void main(String[] args) throws Exception {
        // tag::client-setup[]
        Context context = new SimpleContextManager().getContext(); // <1>
        final ServerSession serverSession = context.set(
                new ServerSession("/feed", "localhost", 8080)); // <2>
        serverSession.addRemoteAliasPackage("org.graniteds.tutorial.feed.client"); // <3>
        serverSession.start(); //  <4>

        Consumer feedConsumer = serverSession.getConsumer("feedTopic", "NASDAQ"); // <5>
        // end::client-setup[]

        final NumberFormat pFormat = NumberFormat.getCurrencyInstance(Locale.US);
        pFormat.setMinimumFractionDigits(2);

        // tag::client-consumer[]
        feedConsumer.addMessageListener(new TopicMessageListener() {
            @Override
            public void onMessage(TopicMessageEvent event) { // <1>
                StockPrice[] stockPrices = (StockPrice[])event.getData();
                System.out.println("------------------");
                for (int i = 0; i < stockPrices.length; i++) {
                    System.out.println(stockPrices[i].getName() + ": " + pFormat.format(stockPrices[i].getPrice()));
                }
            }
        });

        feedConsumer.subscribe().get(); // <2>
        // end::client-consumer[]
    }
}
