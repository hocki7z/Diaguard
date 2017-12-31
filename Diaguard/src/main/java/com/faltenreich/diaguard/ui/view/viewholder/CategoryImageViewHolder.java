package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryImage;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.ViewUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class CategoryImageViewHolder extends BaseViewHolder<ListItemCategoryImage> implements View.OnClickListener {

    @BindView(R.id.category_image) ImageView imageView;

    public CategoryImageViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
    }

    @Override
    public void bindData() {
        int categoryImageResourceId = PreferenceHelper.getInstance().getCategoryImageResourceId(getListItem().getCategory());
        if (categoryImageResourceId > 0) {
            Picasso.with(getContext()).load(categoryImageResourceId).into(imageView);
        }
    }

    @Override
    public void onClick(View view) {
        ViewUtils.showToast(getContext(), getListItem().getCategory().toLocalizedString(), Toast.LENGTH_SHORT);
    }
}
