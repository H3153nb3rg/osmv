package org.openintents.filemanager;

/*
 * Copyright 2007 Steven Osborn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import android.graphics.drawable.Drawable;

/** @author Steven Osborn - http://steven.bitsetters.com */
public class IconifiedText implements Comparable<IconifiedText> {

    private String   mText       = "";
    private String   mInfo       = "";
    private Drawable mIcon;
    private boolean  mSelectable = true;

    public IconifiedText(final String text, final String info,
            final Drawable bullet) {
        mIcon = bullet;
        mText = text;
        mInfo = info;
    }

    public boolean isSelectable() {
        return mSelectable;
    }

    public void setSelectable(final boolean selectable) {
        mSelectable = selectable;
    }

    public String getText() {
        return mText;
    }

    public void setText(final String text) {
        mText = text;
    }

    public String getInfo() {
        return mInfo;
    }

    public void setInfo(final String info) {
        mInfo = info;
    }

    public void setIcon(final Drawable icon) {
        mIcon = icon;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    /** Make IconifiedText comparable by its name */

    @Override
    public int compareTo(final IconifiedText other) {
        if (mText != null) {
            return mText.compareTo(other.getText());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
