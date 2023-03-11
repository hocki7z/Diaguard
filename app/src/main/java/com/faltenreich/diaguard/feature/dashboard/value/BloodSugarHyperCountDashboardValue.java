package com.faltenreich.diaguard.feature.dashboard.value;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;

import java.util.List;

class BloodSugarHyperCountDashboardValue implements DashboardValue {

    private final String key;
    private final String value;

    BloodSugarHyperCountDashboardValue(Context context, List<BloodSugar> bloodSugars) {
        key = context.getString(R.string.hyper);
        value = bloodSugars.size() > 0
            ? context.getString(R.string.dashboard_percentage, getPercentage(bloodSugars))
            : context.getString(R.string.placeholder);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    private float getPercentage(List<BloodSugar> bloodSugars) {
        return 100 * (float) getCount(bloodSugars) / (float) bloodSugars.size();
    }

    private int getCount(List<BloodSugar> bloodSugars) {
        float limit = PreferenceStore.getInstance().getLimitHyperglycemia();
        int hyperCount = 0;
        for (BloodSugar bloodSugar : bloodSugars) {
            float mgDl = bloodSugar.getMgDl();
            if (mgDl > limit) {
                hyperCount++;
            }
        }
        return hyperCount;
    }
}
