package com.example.dotori;

public class Dictionary {

    private String id;
    private String List;
    private String Memo;
    private String Text1;
    private boolean Check;
    private boolean isSelected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText1() {
        return Text1;
    }

    public void setText1(String text1) {
        this.Text1 = text1;
    }

    public String getList() {
        return List;
    }

    public void setList(String list) {
        List = list;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public boolean getCheck() { return Check;}

    public void setCheck(Boolean check) { Check = check; }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Dictionary(String id, String list, String memo, boolean check, String text1) {
        this.id = id;
        List = list;
        Memo = memo;
        Check = check;
        Text1 = text1;
    }
    public Dictionary (String id, String list, String memo) {
        this.id = id;
        List = list;
        Memo = memo;
    }

}
