package com.home.wrm.client.view.main;

import java.util.ArrayList;
import java.util.List;

import com.home.wrm.client.presenter.BasePresenter;
import com.home.wrm.client.view.widget.WrmTextItem;
import com.home.wrm.shared.transport.SearchFilter;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Common class for default pages, except Pop up windows (views).
 * 
 * @param <T>
 *            - appropriate presenter type.
 */
public abstract class AbstractHeaderSupportView<T extends BasePresenter<?>> extends AbstractViewProvider<T> {
    private VLayout mainViewport;
    private VLayout mainLayout;
    private HLayout header;
    protected TextItem searchField;
    
    public AbstractHeaderSupportView() {
        /****** MAIN VIEWPORT ******/
        mainViewport = new VLayout(0);
        mainViewport.setWidth("100%");
        mainViewport.setHeight("100%");
        
        /********* HEADER ********/
        Label icon = new Label();
        icon.setIcon("play.png");
        icon.setAlign(Alignment.CENTER);
        icon.setIconSize(64);
        
        Label title = new Label("Web resource management");
        title.setWrap(false);
        title.setStyleName("titleStyle");
        
//        Button manageButton = new Button("Manage WR...");
//        manageButton.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                SC.ask("Open in new tab?", new BooleanCallback() {
//                    public void execute(Boolean yes) {
//                        if (yes != null) {
//                            if (yes.booleanValue()) {
//                                getPresenter().openInNewTab(getResourceSelectionLink());
//                            } else {
//                                History.newItem(Page.WR_SELECTION_PAGE.getToken());
//                            }
//                        }
//                    }
//                });
//            }
//        });
        
//        Button createButton = new Button("Create WR...");
//        createButton.setVisible(false);
//        createButton.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                History.newItem(Page.WR_PROCESSING_PAGE.getToken());
//            }
//        });
        
//        VLayout headerButtonLayout = new VLayout(0);
//        headerButtonLayout.setAlign(Alignment.CENTER);
//        headerButtonLayout.setMembers(manageButton, createButton);
//        headerButtonLayout.setWidth("12%");
        
        final Label exitLink = new Label("Exit");
        exitLink.setStyleName("clickable");
        exitLink.setAlign(Alignment.CENTER);
        
//      exitLink.setHeight(10);
//      exitLink.setWidth(90);
//      exitLink.setMargin(15);
//      exitLink.setTop("40px");
        
        exitLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                SC.ask("Close resource management application?", new BooleanCallback() {
                    public void execute(Boolean yes) {
                        if (yes != null && yes.booleanValue()) {
                            closeTab();
                        }
                    }
                });
            }
        });
        
        SelectItem searchFilterCombo = new SelectItem("searchComboFilter", "Search in ");
        searchFilterCombo.setValueMap(SearchFilter.composeValues());
//        searchFilterCombo.setValueMap("Names only", "Both, names and links", "Links only");
        searchFilterCombo.setDefaultToFirstOption(true);
        searchFilterCombo.setWidth("100%");
        searchFilterCombo.setTitleStyle("wrmTitleStyleForTextItem");
        searchFilterCombo.setTextBoxStyle("wrmTextBoxStyleForTextItem");
        searchFilterCombo.setSelectOnFocus(false);
        searchFilterCombo.setShowFocused(false);
        
        searchField = new WrmTextItem("searchitemname", "Search");
        searchField.setWidth("100%");
        DynamicForm searchForm = new DynamicForm();
        searchForm.setTitleSuffix("");
        searchForm.setTitleOrientation(TitleOrientation.TOP);
        searchForm.setCellPadding(0);
        searchForm.setStyleName("searchFieldStyle");
        searchForm.setNumCols(1);
        searchForm.setFields(searchFilterCombo, searchField);
        searchForm.setColWidths("100%");
        
//        searchField.setShowFocused(true);
//        searchField.setShowFocusedPickerIcon(true);
        
        icon.setWidth("80px");
        title.setWidth("*");
        searchForm.setWidth("300px");
        exitLink.setWidth("40px");
        
//        icon.setBorder("1px solid green");
//        title.setBorder("1px solid blue");
//        searchForm.setCellBorder(1);
//        exitLink.setBorder("1px solid yellow");
        
        header = new HLayout(0);
        header.setWidth("100%");
        header.setHeight(80);
        header.setBackgroundColor("#333333");
//        header.setStyleName("headerStyle");
//        header.setMembers(icon, title, searchForm, exitLink);
        header.setMembers(icon, title, searchForm);
        
        /****** MAIN LAYOUT ******/
        mainLayout = new VLayout(0);
        mainLayout.setPadding(15);
        mainLayout.setBackgroundColor("#777777");
//        mainLayout.setBackgroundColor("#EEEEEE");
    }

    protected void setFieldsAsReadonly(FormItem... items) {
       for (FormItem item : items) {
           item.setAttribute("readOnly", true);
           item.setTextBoxStyle("readonlyFieldStyle");
           item.setCanFocus(false);
       }
    }
    
//    private String getResourceSelectionLink() {
//        String link = "";
//        final String DEL = "#";
//        String currentLink = Window.Location.getHref();
//        if (currentLink.contains(DEL)) {
//            link = currentLink.replaceFirst(currentLink.substring(currentLink.indexOf(DEL) + 1), Page.WR_SELECTION_PAGE.getToken());
//        } else { //just TBS
//            link = currentLink + DEL + Page.WR_SELECTION_PAGE.getToken();
//        }
//        return link;
//    }
    
    protected native void closeTab() /*-{
		$wnd.closeTab();
	}-*/;

    protected void depriveHeaderPrivileges(ListGrid listGrid) {
        listGrid.setCanSort(false);
        listGrid.setCanGroupBy(false);
        listGrid.setCanPickFields(false);
        listGrid.setCanFreezeFields(false);
        listGrid.setCanResizeFields(true);
        listGrid.setCanAutoFitFields(false);
        listGrid.setCanReorderFields(false);
    }
    
    protected abstract void drawUIWithMembers(VLayout mainLayout);
    
    @Override
    protected void drawUI() {
        List<Canvas> members = new ArrayList<Canvas>();
        
        final Label separateLabel = createDefaultSeparateLabel("");
        members.add(header);
        members.add(separateLabel);
        
        drawUIWithMembers(mainLayout);
        members.add(mainLayout);
        mainViewport.setMembers(members.toArray(new Canvas[members.size()]));
        members.clear();
        if (mainViewport.isDrawn()) {
            mainViewport.redraw();
        } else mainViewport.draw();
    }
    
    protected Label createDefaultSeparateLabel(final String title) {
        final Label label = new Label(title);
        label.setStyleName("informationSeparatorStyle");
        label.setWidth("100%");
        label.setHeight("1em");
        return label;
    }
    
    protected HLayout getHeader() {
        return header;
    }
}
