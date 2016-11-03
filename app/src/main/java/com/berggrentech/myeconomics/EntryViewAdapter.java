package com.berggrentech.myeconomics;

import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by Simon Berggren for assignment 1 in the course Development of Mobile Devices.
 */
class EntryViewAdapter extends EntryViewAnimated.AnimatedExpandableListAdapter {
    private ArrayList<Entry> mEntries;
    private EntriesFragment mParent;
    private LayoutInflater mInflater;

    EntryViewAdapter(EntriesFragment _Parent, ArrayList<Entry> _Entries) {
        super();
        mEntries = _Entries;
        mParent = _Parent;
        mInflater = (LayoutInflater) mParent.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.list_entry, parent, false);

        Entry entry = mEntries.get(groupPosition);

        // get resources
        TextView title = (TextView) view.findViewById(R.id.entry_title);
        ImageView category_image = (ImageView) view.findViewById(R.id.entry_category_image);
        TextView date = (TextView) view.findViewById(R.id.entry_date);
        ImageView type_image = (ImageView) view.findViewById(R.id.entry_type_image);
        TextView category_text = (TextView) view.findViewById(R.id.entry_category_text);
        TextView sum = (TextView) view.findViewById(R.id.entry_sum_text);

        // set resources
        title.setText(entry.getTitle());
        category_image.setImageResource(entry.getCategoryImageID());
        date.setText(Utils.dateToString(entry.getDate()));
        type_image.setImageResource(entry.getTypeImageID());
        category_text.setText(entry.getCategory());
        sum.setText(String.valueOf(entry.getSum()));

        return view;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public View getRealChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.list_entry_child, parent, false);

        Button btnEdit = (Button) view.findViewById(R.id.btnEdit);
        Button btnDelete = (Button) view.findViewById(R.id.btnDelete);

        // edit entry button opens up entry fragment dialog for editing
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntryFragment entFrag = new EntryFragment();
                entFrag.setArguments(Utils.EntryAsArgs(mEntries.get(groupPosition)));
                entFrag.show(mParent.getActivity().getFragmentManager(), "entryFragment");
                entFrag.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mParent.updateList();
                    }
                });
            }
        });

        // delete entry from database and listview after closing child
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EntryViewAnimated entries = (EntryViewAnimated) mParent.getActivity().findViewById(R.id.entry_listview);

                // wait until after animation
                entries.collapseGroupWithAnimation(groupPosition);
                entries.postOnAnimationDelayed(new TimerTask() {
                    @Override
                    public void run() {
                    MainActivity.DBM.removeEntry(mEntries.get(groupPosition).getID());
                    mEntries.remove(groupPosition);
                    }
                }, 300);
            }
        });

        return view;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public int getGroupCount() {
        return mEntries.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mEntries.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}