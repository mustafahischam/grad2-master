package com.sasa.project.grad2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sasa.project.grad2.Firebase_DataObjects.Organization_DataObjects;

public class ViewHolder_OrganizationLayout extends RecyclerView.ViewHolder {

    public TextView textview;

    public ViewHolder_OrganizationLayout(View view) {
        super(view);

        textview = view.findViewById(R.id.organization_name);
    }

    public void model(Organization_DataObjects organizationDataObjects) {
        textview.setText(organizationDataObjects.name);
    }
}