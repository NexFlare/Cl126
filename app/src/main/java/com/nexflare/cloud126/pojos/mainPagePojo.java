package com.nexflare.cloud126.pojos;

import com.nexflare.cloud126.R;

/**
 * Created by 15103068 on 01-03-2017.
 */

public class mainPagePojo {
    String name;
    int icon;

    public mainPagePojo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        icon= R.drawable.notes;
        if(name.equals("Annapurna Menu"))
            icon=R.drawable.menu;
        else if(name.equals("Tutorials"))
            icon=R.drawable.tutorial;
        else if(name.equals("Previous Year Paper"))
            icon=R.drawable.papers;
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
