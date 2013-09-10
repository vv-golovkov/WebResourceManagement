package com.home.wrm.client.view.widget;

import com.smartgwt.client.widgets.form.fields.TextItem;

public class WrmTextItem extends TextItem {
    
    public WrmTextItem(String name, String title) {
        super(name, title);
        
        init();
    }
    
    private void init() {
        setTitleStyle("wrmTitleStyleForTextItem");
        setTextBoxStyle("wrmTextBoxStyleForTextItem");
        setBrowserSpellCheck(false);
        setCanFocus(false);
    }
}
