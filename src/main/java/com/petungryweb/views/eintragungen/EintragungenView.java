package com.petungryweb.views.eintragungen;

import com.petungryweb.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Gewichtseintragung")
@Route(value = "gewichtseintragung", layout = MainLayout.class)
@Uses(Icon.class)
public class EintragungenView extends Composite<VerticalLayout> {

    public EintragungenView() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
    }
}
