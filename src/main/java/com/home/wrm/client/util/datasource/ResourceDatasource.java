package com.home.wrm.client.util.datasource;

import com.home.wrm.shared.transport.Resource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ResourceDatasource extends CommonDatasource {
	private static final String DATASOURCE_ID = "RES_DSaa";
    private static ResourceDatasource INSTANCE = null;
    
	private static final String INST_ID_FIELD = "AAAAAAA";
	public static final String LINK_FIELD = "BBBBBBBBB";
	public static final String NAME_FIELD = "CCCCCCCCC";
    public static final String FULL_DIR_PATH_FIELD = "DDDDDDDD";
    public static final String SAVING_DATE_FIELD = "EEEEEEE";
    public static final String SAVING_TIME_FIELD = "FFFFFFFFFFFF";
    public static final String GO_BUTTON_FIELD = "GGGGGGGGG";
    public static final String REMOVE_FIELD = "HHHHHHHHHHHHHHHH";
    public static final String MOVE_FIELD = "JJJJJJJJJJJJ";
    
    private ResourceDatasource(final String dataSourceId) {
        setID(dataSourceId);
        
        DataSourceIntegerField primaryKeyField = getPrimaryKeyDatasourceField(INST_ID_FIELD);
        
        DataSourceTextField linkField = new DataSourceTextField(LINK_FIELD, "Link");
        DataSourceTextField nameField = new DataSourceTextField(NAME_FIELD, "Name");
        nameField.setCanEdit(true);
        DataSourceTextField savingDateField = new DataSourceTextField(SAVING_DATE_FIELD, "Saving date");
        DataSourceTextField savingTimeField = new DataSourceTextField(SAVING_TIME_FIELD, "Saving time");
        DataSourceTextField fullDirPath = new DataSourceTextField(FULL_DIR_PATH_FIELD, "Full path");//group by field
        
        DataSourceTextField goButtonField = new DataSourceTextField(GO_BUTTON_FIELD, "Info");
        
        DataSourceTextField removeButtonField = new DataSourceTextField(REMOVE_FIELD, "X");
        DataSourceTextField moveButtonField = new DataSourceTextField(MOVE_FIELD, "X");
        
        /*DataSourceImageField removeField = new DataSourceImageField(REMOVE_FIELD, "X");
        removeField.setImageWidth(16);
        removeField.setImageHeight(16);
        removeField.setImageURLPrefix("/images/");*/
//        
        removeButtonField.setAttribute("align", "center");
        moveButtonField.setAttribute("align", "center");
        goButtonField.setAttribute("align", "center");
        savingDateField.setAttribute("align", "center");
        savingTimeField.setAttribute("align", "center");
        
//        linkField.setAttribute(WIDTH, "70%");
        goButtonField.setAttribute(WIDTH, 55);
        savingDateField.setAttribute(WIDTH, 70);
        savingTimeField.setAttribute(WIDTH, 70);
        removeButtonField.setAttribute(WIDTH, 35);
        moveButtonField.setAttribute(WIDTH, 35);
        fullDirPath.setAttribute(WIDTH, 1);
        
//        linkField.setType(FieldType.LINK);
        
        setFields(primaryKeyField, removeButtonField, moveButtonField, linkField, nameField, savingDateField, savingTimeField, goButtonField, fullDirPath);
        setClientOnly(true);
    }
    
    public static ResourceDatasource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ResourceDatasource(SC.generateID());
        }
        return INSTANCE;
    }

	
	public static final class ResourceRecord extends ListGridRecord {
		private Resource resource;
		public ResourceRecord(Resource resource) {
			this.resource = resource;
			init();
		}
		public Resource asResource() {
			return resource;
		}
		private void init() {
			setAttribute(INST_ID_FIELD, resource.getId());
			setAttribute(LINK_FIELD, resource.getLink());
			setAttribute(NAME_FIELD, resource.getName());
			setAttribute(SAVING_DATE_FIELD, resource.getSavingDate());
			setAttribute(SAVING_TIME_FIELD, resource.getSavingTime());
			setAttribute(FULL_DIR_PATH_FIELD, resource.getFullDirPath());
		}
	}
}
