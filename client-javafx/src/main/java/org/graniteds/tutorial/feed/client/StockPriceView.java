package org.graniteds.tutorial.feed.client;

import de.jensd.fx.fontawesome.AwesomeDude;
import de.jensd.fx.fontawesome.AwesomeIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
* Created by william on 25/11/13.
*/
public class StockPriceView extends GridPane {

    private final BigDecimal initialPrice;

    private final Label nameLabel;
    private final Label priceLabel;
    private final Label percentLabel;
    private final Label arrowLabel;

    public StockPriceView(String name, BigDecimal initialPrice) {
        this.initialPrice = initialPrice;
        setMinWidth(140);
        setMinHeight(90);
        setStyle("-fx-padding: 10; -fx-alignment: center; -fx-border-color: #97b54b; -fx-border-width: 1");
        setHgap(10);
        getColumnConstraints().addAll(new ColumnConstraints(60), new ColumnConstraints(40));

        nameLabel = new Label(name);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 28));
        nameLabel.setAlignment(Pos.CENTER);
        add(nameLabel, 0, 0, 2, 1);

        priceLabel = new Label("0.0");
        priceLabel.setMaxWidth(Double.MAX_VALUE);
        priceLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        priceLabel.setAlignment(Pos.CENTER);
        add(priceLabel, 0, 1);

        percentLabel = new Label(" 0.00%");
        percentLabel.setMaxWidth(Double.MAX_VALUE);
        percentLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        percentLabel.setAlignment(Pos.CENTER);
        add(percentLabel, 0, 2);

        AwesomeDude.createIconLabel(AwesomeIcon.ASTERISK);
        arrowLabel = new Label(AwesomeIcon.CHEVRON_SIGN_RIGHT.toString());
        arrowLabel.setMaxWidth(Double.MAX_VALUE);
        arrowLabel.getStyleClass().add("awesome");
        arrowLabel.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 24;");
        arrowLabel.setAlignment(Pos.CENTER);
        add(arrowLabel, 1, 1, 1, 2);
    }

    public void updatePrice(BigDecimal price) {
        NumberFormat pFormat = NumberFormat.getCurrencyInstance(Locale.US);
        pFormat.setMinimumFractionDigits(2);
        priceLabel.setText(pFormat.format(price));
        NumberFormat pcFormat = NumberFormat.getPercentInstance(Locale.US);
        ((DecimalFormat)pcFormat).setPositivePrefix("+");
        pcFormat.setMinimumFractionDigits(2);
        percentLabel.setText(pcFormat.format(price.subtract(initialPrice).divide(initialPrice, 4, RoundingMode.CEILING)));
        if (price.compareTo(initialPrice) > 0) {
            percentLabel.setStyle("-fx-alignment: center; -fx-text-fill: green");
            arrowLabel.setStyle("-fx-alignment: center; -fx-text-fill: green; -fx-font-family: FontAwesome; -fx-font-size: 24;");
            arrowLabel.setText(AwesomeIcon.CHEVRON_SIGN_UP.toString());
        }
        else if (price.compareTo(initialPrice) < 0) {
            percentLabel.setStyle("-fx-alignment: center; -fx-text-fill: red");
            arrowLabel.setStyle("-fx-alignment: center; -fx-text-fill: red; -fx-font-family: FontAwesome; -fx-font-size: 24;");
            arrowLabel.setText(AwesomeIcon.CHEVRON_SIGN_DOWN.toString());
        }
        else {
            percentLabel.setStyle("-fx-alignment: center; -fx-text-fill: black");
            arrowLabel.setStyle("-fx-alignment: center; -fx-text-fill: black; -fx-font-family: FontAwesome; -fx-font-size: 24;");
            arrowLabel.setText(AwesomeIcon.CHEVRON_SIGN_RIGHT.toString());
        }
    }
}
