package com.petungryweb.views.historie;

import com.petungryweb.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Arrays;
import java.util.List;

@PageTitle("Historie")
@Route(value = "feed", layout = MainLayout.class)
public class HistorieView extends Div implements AfterNavigationObserver {

    Grid<Person> grid = new Grid<>();

    public HistorieView() {
        addClassName("historie-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(person -> createCard(person));
        add(grid);
    }

    private HorizontalLayout createCard(Person person) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = new Image();
        image.setSrc(person.getImage());
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(person.getName());
        name.addClassName("name");
        Span date = new Span(person.getDate());
        date.addClassName("date");
        header.add(name, date);

        Span post = new Span(person.getPost());
        post.addClassName("post");

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Icon commentIcon = VaadinIcon.COMMENT.create();
        commentIcon.addClassName("icon");
        Span comments = new Span(person.getComments());
        comments.addClassName("comments");

        actions.add(commentIcon, comments);

        description.add(header, post, actions);
        card.add(image, description);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        String [] Mathis = new String[]{"profile-images/Mathis.jpg", "Mathis Schäfer"};
        String [] Lea = new String[]{"profile-images/Lea.jpg", "Lea Schäfer"};
        String [] Birgit = new String[]{"profile-images/Birgit.jpg", "Birgit Schäfer"};
        String [] Wilfried = new String[]{"profile-images/Wilfried.jpg", "Wilfried Schäfer"};

        // Set some data when this view is displayed.
        List<Person> persons = Arrays.asList( //
                createPerson(Mathis[0], Mathis[1], "May 8",
                        "Pepe hat 100g Futter bekommen",
                        "0", "0", "0"),
                createPerson(Birgit[0], Birgit[1], "May 8",
                        "Pepe hat 100g Futter bekommen",
                        "0", "0", "0")

        );

        grid.setItems(persons);
    }

    private static Person createPerson(String image, String name, String date, String post, String likes,
            String comments, String shares) {
        Person p = new Person();
        p.setImage(image);
        p.setName(name);
        p.setDate(date);
        p.setPost(post);
        p.setLikes(likes);
        p.setComments(comments);
        p.setShares(shares);

        return p;
    }

}
