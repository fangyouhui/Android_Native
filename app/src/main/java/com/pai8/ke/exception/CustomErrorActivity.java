/*
 * Copyright 2014-2017 Eduard Ereza Martínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pai8.ke.exception;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;

import android.os.Bundle; 
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * 
 * @author Created by zzf
 * @time  20:45
 * Description：
 */
public class CustomErrorActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_custom_error;
    }

    @Override
    public void initView() {
        TextView errorDetailsText = findViewById(R.id.error_details);
        errorDetailsText.setText(CustomActivityOnCrash.getStackTraceFromIntent(getIntent()));

        Button restartButton = findViewById(R.id.restart_button);

        final CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());

        if (config == null) {
            //This should never happen - Just finish the activity to avoid a recursive crash.
            finish();
            return;
        }

        if (config.isShowRestartButton() && config.getRestartActivityClass() != null) {
            restartButton.setText(getString(R.string.customactivityoncrash_error_activity_restart_app));
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.restartApplication(CustomErrorActivity.this, config);
                }
            });
        } else {
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.closeApplication(CustomErrorActivity.this, config);
                }
            });
        }
    }
}
