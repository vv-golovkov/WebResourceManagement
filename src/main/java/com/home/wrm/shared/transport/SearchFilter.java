package com.home.wrm.shared.transport;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum SearchFilter implements IsSerializable{
    ONLY_BY_NAME("Names only"),
    BY_BOTH_NAME_AND_LINK("Both, names and links"),
    ONLY_BY_LINK("Links only");
    
    private String value;
    private SearchFilter(String value) {
        this.value = value;
    }
    public String getFilterValue() {
        return value;
    }
    
    public static SearchFilter get(String value) {
        for (SearchFilter searchFilter : values()) {
            if (searchFilter.getFilterValue().equals(value)) {
                return searchFilter;
            }
        }
        return null;
    }
    
    public static String[] composeValues() {
        SearchFilter[] values = values();
        String[] array = new String[values.length];
        for (int index = 0; index < values.length; index++) {
            array[index] = values[index].getFilterValue();
        }
        return array;
    }
}
