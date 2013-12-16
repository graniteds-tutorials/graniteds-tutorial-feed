package org.graniteds.tutorial.feed.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.granite.client.javafx.tide.JavaFXApplication;
import org.granite.client.messaging.Consumer;
import org.granite.client.messaging.TopicMessageListener;
import org.granite.client.messaging.events.TopicMessageEvent;
import org.granite.client.tide.Context;
import org.granite.client.tide.impl.SimpleContextManager;
import org.granite.client.tide.server.*;


public class FeedClient extends Application {

    /**
     * Main method which lauches the JavaFX application
     */
    public static void main(String[] args) {
        Application.launch(FeedClient.class, args);
    }

    final static Context context = new SimpleContextManager(new JavaFXApplication()).getContext(); // <1>

    @Override
    public void start(Stage stage) throws Exception {
        // tag::client-setup[]
        final ServerSession serverSession = context.set(
                new ServerSession("/feed", "localhost", 8080)); // <2>
        serverSession.addRemoteAliasPackage("org.graniteds.tutorial.feed.client"); // <3>
        serverSession.start(); // <4>

        final Consumer feedConsumer = serverSession.getConsumer("feedTopic", "NASDAQ"); // <5>
        // end::client-setup[]

        // tag::client-ui[]
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setMaxHeight(Double.MAX_VALUE);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(10);

        Label titleLabel = new Label("Stock Feed Example");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setStyle("-fx-padding: 10 10 10 10; -fx-background-color: #97b54b; -fx-text-fill: white");
        titleLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
        gridPane.add(titleLabel, 0, 0);

        final TilePane tilePane = new TilePane();
        tilePane.setOrientation(Orientation.HORIZONTAL);
        tilePane.setTileAlignment(Pos.CENTER);
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        gridPane.add(tilePane, 0, 1);

        Scene scene = new Scene(gridPane, 310, 280);
        stage.setTitle("GraniteDS Feed Tutorial");
        stage.setScene(scene);
        stage.show();
        // end::client-ui[]

        // tag::client-consume[]
        feedConsumer.addMessageListener(new TopicMessageListener() { // <1>
            @Override
            public void onMessage(final TopicMessageEvent event) { // <2>
                Platform.runLater(new Runnable() { // <3>
                    @Override
                    public void run() {
                        StockPrice[] stockPrices = (StockPrice[])event.getData();
                        if (tilePane.getChildren().size() == 0) {
                            for (int i = 0; i < stockPrices.length; i++) {
                                StockPriceView stockView = new StockPriceView(stockPrices[i].getName(), stockPrices[i].getPrice());
                                tilePane.getChildren().add(stockView);
                            }
                        }
                        for (int i = 0; i < stockPrices.length; i++)
                            ((StockPriceView)tilePane.getChildren().get(i)).updatePrice(stockPrices[i].getPrice());
                    }
                });
            }
        });

        feedConsumer.subscribe().get(); // <4>
        // end::client-consume[]
    }

    // tag::client-close[]
    @Override
    public void stop() throws Exception {
        context.byType(ServerSession.class).stop();

        super.stop();
    }
    // end::client-close[]
}
