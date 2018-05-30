package com.wms.services.warehouse.datastructures;

public class InspectFinishArgs {
    private boolean allFinish = false;
    private int inspectionNoteId = -1;
    private boolean qualified = true;
    private int personId = -1;
    private InspectFinishItem[] inspectFinishItems = new InspectFinishItem[]{};

    public boolean isAllFinish() {
        return allFinish;
    }

    public void setAllFinish(boolean allFinish) {
        this.allFinish = allFinish;
    }

    public boolean isQualified() {
        return qualified;
    }

    public void setQualified(boolean qualified) {
        this.qualified = qualified;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getInspectionNoteId() {
        return inspectionNoteId;
    }

    public void setInspectionNoteId(int inspectionNoteId) {
        this.inspectionNoteId = inspectionNoteId;
    }


    public InspectFinishItem[] getInspectFinishItems() {
        return inspectFinishItems;
    }

    public void setInspectFinishItems(InspectFinishItem[] inspectFinishItems) {
        this.inspectFinishItems = inspectFinishItems;
    }

}
