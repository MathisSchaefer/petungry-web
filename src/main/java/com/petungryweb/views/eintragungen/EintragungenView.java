package com.petungryweb.views.eintragungen;


import com.petungryweb.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.*;

import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@PageTitle("Gewichtseintragung")
@Route(value = "gewichtseintragung", layout = MainLayout.class)
public class EintragungenView extends Main{

    public EintragungenView() {

        addClassName("eintragungs-view");

        Board board = new Board();
        board.addRow(createHighlight("Aktuelles Gewicht", "5kg", 1.0), createHighlight("Monatliches Maximalgewicht", "6kg", -5.0),
                createHighlight("Monatliches Minimalgewicht", "4,5kg", 0.0));
        board.addRow(createGewichtseintragung());
        board.addRow(createChart());
        add(board);
    }

    private Component createGewichtseintragung() {
        NumberField kgField = new NumberField();
        kgField.setLabel("Gewicht");
        kgField.setValue(5.0);
        Div kgSuffix = new Div();
        kgSuffix.setText("kg");
        kgField.setSuffixComponent(kgSuffix);

        Button submitButton = new Button("Eintragen");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_CONTRAST);
        submitButton.addClickListener(clickEvent -> {
            writeWheigthInDB(kgField.getValue());
        });
        VerticalLayout layout = new VerticalLayout(kgField, submitButton);
        System.out.println(System.getenv("DB_Password"));
        return layout;
    }

    private void writeWheigthInDB(Double gewicht){
        String connectionUrl = "jdbc:mysql://127.0.0.1:3306;databaseName=petungry;user=petungry_user;password=Abde1245;";
        Date today = new Date();
        try (Connection connection = DriverManager.getConnection(connectionUrl);
            Statement statement = connection.createStatement()) {
            String insertSql = "INSERT INTO gewicht (pet_id, gewicht, date) VALUES (1, ?, current_date)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)){
                preparedStatement.setString(1, String.valueOf(gewicht));
                preparedStatement.executeUpdate();
            }
            System.out.println("Datensatz erfolgreich eingefügt!");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private Component createHighlight(String title, String value, Double percentage) {
        VaadinIcon icon = VaadinIcon.ARROW_UP;
        String prefix = "";
        String theme = "badge";

        if (percentage == 0) {
            prefix = "±";
        } else if (percentage > 0) {
            prefix = "+";
            theme += " success";
        } else if (percentage < 0) {
            icon = VaadinIcon.ARROW_DOWN;
            theme += " error";
        }

        H2 h2 = new H2(title);
        h2.addClassNames(FontWeight.NORMAL, Margin.NONE, TextColor.SECONDARY, FontSize.XSMALL);

        Span span = new Span(value);
        span.addClassNames(FontWeight.SEMIBOLD, FontSize.XXXLARGE);

        Icon i = icon.create();
        i.addClassNames(BoxSizing.BORDER, Padding.XSMALL);

        Span badge = new Span(i, new Span(prefix + percentage.toString()));
        badge.getElement().getThemeList().add(theme);

        VerticalLayout layout = new VerticalLayout(h2, span, badge);
        layout.addClassName(Padding.LARGE);
        layout.setPadding(false);
        layout.setSpacing(false);
        return layout;
    }

    private Component createViewFeedingsMonths() {
        // Header
        String[] monatsNamen = {"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
        Select year = new Select();
        Select month = new Select();
        month.setItems("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember");
        year.setItems("2024");
        year.setValue("2024");
        month.setValue(monatsNamen[Calendar.getInstance().get(Calendar.MONTH)-1]);
        year.setWidth("100px");

        HorizontalLayout header = createHeader("Fütterungen", "Monat");
        header.add(month);
        header.add(year);

        // Chart
        Chart chart = new Chart(ChartType.AREASPLINE);
        Configuration conf = chart.getConfiguration();
        conf.getChart().setStyledMode(true);

        XAxis xAxis = new XAxis();
        xAxis.setCategories("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24", "25", "26", "30", "31");
        conf.addxAxis(xAxis);

        conf.getyAxis().setTitle("Menge");

        PlotOptionsAreaspline plotOptions = new PlotOptionsAreaspline();
        plotOptions.setPointPlacement(PointPlacement.ON);
        plotOptions.setMarker(new Marker(false));
        conf.addPlotOptions(plotOptions);

        conf.addSeries(new ListSeries("Mathis", 0));
        conf.addSeries(new ListSeries("Lea", 0));
        conf.addSeries(new ListSeries("Birgit", 0));
        conf.addSeries(new ListSeries("Wilfried", 0));

        // Add it all together
        VerticalLayout viewEvents = new VerticalLayout(header, chart);
        viewEvents.addClassName(Padding.LARGE);
        viewEvents.setPadding(false);
        viewEvents.setSpacing(false);
        viewEvents.getElement().getThemeList().add("spacing-l");
        return viewEvents;
    }



    private HorizontalLayout createHeader(String title, String subtitle) {
        H2 h2 = new H2(title);
        h2.addClassNames(FontSize.XLARGE, Margin.NONE);

        Span span = new Span(subtitle);
        span.addClassNames(TextColor.SECONDARY, FontSize.XSMALL);

        VerticalLayout column = new VerticalLayout(h2, span);
        column.setPadding(false);
        column.setSpacing(false);

        HorizontalLayout header = new HorizontalLayout(column);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setSpacing(false);
        header.setWidthFull();
        return header;
    }

    private Component createChart(){
        Chart chart = new Chart(ChartType.SPLINE);
        Configuration conf = chart.getConfiguration();
        conf.getChart().setStyledMode(true);

        XAxis xAxis = new XAxis();
        xAxis.setCategories("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24", "25", "26", "30", "31");
        conf.addxAxis(xAxis);

        conf.getyAxis().setTitle("Gewicht in KG");

        PlotOptionsAreaspline plotOptions = new PlotOptionsAreaspline();
        plotOptions.setPointPlacement(PointPlacement.ON);
        plotOptions.setMarker(new Marker(false));
        conf.addPlotOptions(plotOptions);
        Series chartData = new ListSeries();

        conf.addSeries(new ListSeries("Pepe", 5,4.5,4,5,5.5,6,5.5,6,6.5,5,4.5,4,5,5,5.5,6,7,5,6,5,4.5,4,5,5,5,4,4,4.5,5,6,5.5,5));

        // Add it all together
        VerticalLayout viewEvents = new VerticalLayout(chart);
        viewEvents.addClassName(Padding.LARGE);
        viewEvents.setPadding(false);
        viewEvents.setSpacing(false);
        viewEvents.getElement().getThemeList().add("spacing-l");
        return viewEvents;
    }
}
