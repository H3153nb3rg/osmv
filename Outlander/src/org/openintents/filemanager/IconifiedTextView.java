package org.openintents.filemanager;

/*
 * $Id: BulletedTextView.java 57 2007-11-21 18:31:52Z steven $ Copyright 2007
 * Steven Osborn Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * Dec 7, 2008: Peli: Use inflated layout.
 */

import org.outlander.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconifiedTextView extends LinearLayout {

    private final TextView mText;
    private final TextView mInfo;

    public IconifiedTextView(final Context context, final IconifiedText aIconifiedText) {
        super(context);

        // inflate rating
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.filelist_item, this, true);

        mText = (TextView) findViewById(R.id.text);
        mInfo = (TextView) findViewById(R.id.info);
    }

    public void setText(final String words) {
        mText.setText(words);
    }

    public void setInfo(final String info) {
        mInfo.setText(info);
    }

}