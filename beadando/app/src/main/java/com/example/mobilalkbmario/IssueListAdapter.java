// com.example.mobilalkbmario.IssueListAdapter.java
package com.example.mobilalkbmario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class IssueListAdapter extends ArrayAdapter<Issue> {

    private Context mContext;
    private int mResource;

    public IssueListAdapter(Context context, int resource, List<Issue> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        TextView textViewIssueDescription = convertView.findViewById(R.id.textViewIssueDescription);
        TextView textViewReportedBy = convertView.findViewById(R.id.textViewReportedBy);

        Issue issue = getItem(position);

        if (issue != null) {
            textViewIssueDescription.setText(issue.getDescription());
            textViewReportedBy.setText(issue.getReportedBy());
        }

        return convertView;
    }
}
