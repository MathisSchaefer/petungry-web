package com.petungryweb.views.dashboard;


import com.petungryweb.views.MainLayout;
import com.petungryweb.views.dashboard.Fedding.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import java.util.ArrayList;
import java.util.Calendar;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class DashboardView extends Main {

    private Grid<Fedding> grid = new Grid();
    private double futterMenge = 40;
    private double gesamtFutter = 500;

    private double futterBeutel = 66;
    private double futterBeutelMengeGesamt = 10000;
    private double futterBeutelMengeAktuell = 4000;

    private double futterAufWaage = 20;


    private Chart todayFeedingPieChart = new Chart(ChartType.PIE);

    private Configuration conf = todayFeedingPieChart.getConfiguration();


    private Board board = new Board();

    private Span spanPastFeed;
    private Span spanOpenFeed;
    private Span spanBag;

    private VerticalLayout layoutPastFeed;
    private VerticalLayout layoutOpenFeed;
    private VerticalLayout layoutBag;

    private int bisherFutter;
    private DataSeries seriesFeedings;
    private Configuration confChartMonths;


    private DataSeriesItem initialPastFeed = new DataSeriesItem("Bisher", bisherFutter);
    private DataSeriesItem initialFutureFeed = new DataSeriesItem("Zukünftig", 100-bisherFutter);

    ArrayList<Fedding> a = new ArrayList<Fedding>();

    public DashboardView() {
        addClassName("dashboard-view");
        a.add(new Fedding(Status.EXCELLENT, "Mathis", futterMenge));
        board.addRow(createFeed(), createHighlightPastFeed("Heutige Futtermenge", futterMenge + "g"),
                createHighlightOpenFeed("Offenes Futter",  (gesamtFutter - futterMenge) + "g"), createHighlightBag("Futterbeutel", String.valueOf(Math.round(futterBeutelMengeAktuell/futterBeutelMengeGesamt*100)) + "%"));
        board.addRow(createTodayFeedings(), createTodaysFeedingPlan());
        board.addRow(createViewFeedingsMonths());
        add(board);
    }

    private Component createHighlightPastFeed(String title, String value) {

        H2 h2 = new H2(title);
        h2.addClassNames(FontWeight.NORMAL, Margin.NONE, TextColor.SECONDARY, FontSize.XSMALL);

        spanPastFeed = new Span(value);
        spanPastFeed.addClassNames(FontWeight.SEMIBOLD, FontSize.XXXLARGE);


        layoutPastFeed = new VerticalLayout(h2, spanPastFeed);
        layoutPastFeed.addClassName(Padding.LARGE);
        layoutPastFeed.setPadding(false);
        layoutPastFeed.setSpacing(false);
        return layoutPastFeed;
    }    private Component createHighlightOpenFeed(String title, String value) {

        H2 h2 = new H2(title);
        h2.addClassNames(FontWeight.NORMAL, Margin.NONE, TextColor.SECONDARY, FontSize.XSMALL);

        spanOpenFeed = new Span(value);
        spanOpenFeed.addClassNames(FontWeight.SEMIBOLD, FontSize.XXXLARGE);


        layoutOpenFeed = new VerticalLayout(h2, spanOpenFeed);
        layoutOpenFeed.addClassName(Padding.LARGE);
        layoutOpenFeed.setPadding(false);
        layoutOpenFeed.setSpacing(false);
        return layoutOpenFeed;
    }
    private Component createHighlightBag(String title, String value) {

        H2 h2 = new H2(title);
        h2.addClassNames(FontWeight.NORMAL, Margin.NONE, TextColor.SECONDARY, FontSize.XSMALL);

        spanBag = new Span(value);
        spanBag.addClassNames(FontWeight.SEMIBOLD, FontSize.XXXLARGE);


        layoutBag = new VerticalLayout(h2, spanBag);
        layoutBag.addClassName(Padding.LARGE);
        layoutBag.setPadding(false);
        layoutBag.setSpacing(false);
        return layoutBag;
    }

    private Component createFeed() {

        H2 h2 = new H2("Futtermenge auf der Waage");
        h2.addClassNames(FontWeight.NORMAL, Margin.NONE, TextColor.SECONDARY, FontSize.XSMALL);
        Span span = new Span(futterAufWaage + "g");
        span.addClassNames(FontWeight.SEMIBOLD, FontSize.XXXLARGE);

        Button feed = new Button("Füttern");
        feed.addClickListener(clickListener -> {
            futterBeutelMengeAktuell -= futterAufWaage;
            futterMenge += futterAufWaage;
            DataSeries series = new DataSeries();
            series.add(new DataSeriesItem("Bisher", futterMenge));
            series.add(new DataSeriesItem("Zukünftig", gesamtFutter-futterMenge));
            seriesFeedings.clear();
            seriesFeedings.add(new DataSeriesItem("Bisher", bisherFutter));
            seriesFeedings.add(new DataSeriesItem("Zukünftig", gesamtFutter-bisherFutter));
            conf.setSeries(series);
            a.add(new Fedding(Status.EXCELLENT, MainLayout.getFuetterer(), futterAufWaage));
            grid.setItems(a);
            spanPastFeed.setText((int) futterMenge + "g");
            spanOpenFeed.setText((int) (gesamtFutter-futterMenge) + "g");
            spanBag.setText(Math.round(futterBeutelMengeAktuell/futterBeutelMengeGesamt*100)+"%");

            confChartMonths.addSeries(new ListSeries(MainLayout.getFuetterer(), futterAufWaage));
        });

        VerticalLayout layout = new VerticalLayout(h2, span, feed);
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
        confChartMonths = chart.getConfiguration();
        confChartMonths.getChart().setStyledMode(true);

        XAxis xAxis = new XAxis();
        xAxis.setCategories("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24", "25", "26", "30", "31");
        confChartMonths.addxAxis(xAxis);

        confChartMonths.getyAxis().setTitle("Menge");

        PlotOptionsAreaspline plotOptions = new PlotOptionsAreaspline();
        plotOptions.setPointPlacement(PointPlacement.ON);
        plotOptions.setMarker(new Marker(false));
        confChartMonths.addPlotOptions(plotOptions);

        confChartMonths.addSeries(new ListSeries("Mathis", 0));
        confChartMonths.addSeries(new ListSeries("Lea", 0));
        confChartMonths.addSeries(new ListSeries("Birgit", 0));
        confChartMonths.addSeries(new ListSeries("Wilfried", 0));

        // Add it all together
        VerticalLayout viewEvents = new VerticalLayout(header, chart);
        viewEvents.addClassName(Padding.LARGE);
        viewEvents.setPadding(false);
        viewEvents.setSpacing(false);
        viewEvents.getElement().getThemeList().add("spacing-l");
        return viewEvents;
    }
    public Component createTodayFeedings() {
        // Header
        HorizontalLayout header = createHeader("Heutige Fütterungen", "Menge");

        // Grid
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setAllRowsVisible(true);

        grid.addColumn(new ComponentRenderer<>(fedding -> {
            Span status = new Span();
            String statusText = getStatusDisplayName(fedding);
            status.getElement().setAttribute("aria-label", "Status: " + statusText);
            status.getElement().setAttribute("title", "Status: " + statusText);
            status.getElement().getThemeList().add(getStatusTheme(fedding));
            return status;
        })).setHeader("").setFlexGrow(0).setAutoWidth(true);
        grid.addColumn(Fedding::getFuetterer).setHeader("Fütterer").setFlexGrow(1);
        grid.addColumn(Fedding::getMenge).setHeader("Menge").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);

        grid.setItems(new Fedding(Status.EXCELLENT, "Mathis", 40));

        // Add it all together
        VerticalLayout FeedingLayout = new VerticalLayout(header, grid);
        FeedingLayout.addClassName(Padding.LARGE);
        FeedingLayout.setPadding(false);
        FeedingLayout.setSpacing(false);
        FeedingLayout.getElement().getThemeList().add("spacing-l");
        return FeedingLayout;
    }

    private Component createTodaysFeedingPlan() {
        HorizontalLayout header = createHeader("Heutige Futtermenge", "Bisherige Futtermenge / Zukünftige Futtermenge");

        // Chart
        conf.getChart().setStyledMode(true);
        todayFeedingPieChart.setThemeName("gradient");

        seriesFeedings = new DataSeries();
        bisherFutter = (int) (futterMenge/gesamtFutter*100);
        seriesFeedings.add(new DataSeriesItem("Bisher", bisherFutter));
        seriesFeedings.add(new DataSeriesItem("Zukünftig", 100-bisherFutter));
        conf.addSeries(seriesFeedings);

        // Add it all together
        VerticalLayout serviceHealth = new VerticalLayout(header, todayFeedingPieChart);
        serviceHealth.addClassName(Padding.LARGE);
        serviceHealth.setPadding(false);
        serviceHealth.setSpacing(false);
        serviceHealth.getElement().getThemeList().add("spacing-l");
        return serviceHealth;
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

    private String getStatusDisplayName(Fedding fedding) {
        Status status = fedding.getStatus();
        if (status == Status.OK) {
            return "Ok";
        } else if (status == Status.FAILING) {
            return "Failing";
        } else if (status == Status.EXCELLENT) {
            return "Excellent";
        } else {
            return status.toString();
        }
    }

    private String getStatusTheme(Fedding fedding) {
        Status status = fedding.getStatus();
        String theme = "badge primary small";
        if (status == Status.EXCELLENT) {
            theme += " success";
        } else if (status == Status.FAILING) {
            theme += " error";
        }
        return theme;
    }
}
