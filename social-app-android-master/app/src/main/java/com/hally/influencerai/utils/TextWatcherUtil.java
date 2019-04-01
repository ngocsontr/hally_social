/*
 *  Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hally.influencerai.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by HallyTran on 4/01/2019.
 * transon97uet@gmail.com
 */
public class TextWatcherUtil {
    public static TextWatcher getTextWatcher(EditText editText) {
        CustomTextWatcher tw = new CustomTextWatcher(editText);
        return tw;
    }

    static class CustomTextWatcher implements TextWatcher {

        private EditText editText;
        private String current = "";
        private String dateFormat = "YYYYMMDD";
        private Calendar cal = Calendar.getInstance();

        public CustomTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8) {
                    clean = clean + dateFormat.substring(clean.length());
                } else {
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int year = Integer.parseInt(clean.substring(0, 4));
                    int mon = Integer.parseInt(clean.substring(4, 6));
                    int day = Integer.parseInt(clean.substring(6, 8));

                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                    cal.set(Calendar.MONTH, mon - 1);
                    year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE))
                            ? cal.getActualMaximum(Calendar.DATE) : day;
                    clean = String.format("%02d%02d%02d", year, mon, day);
                }

                clean = String.format("%s-%s-%s",
                        clean.substring(0, 4),
                        clean.substring(4, 6),
                        clean.substring(6, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                editText.setText(current);
                editText.setSelection(sel < current.length() ? sel : current.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
