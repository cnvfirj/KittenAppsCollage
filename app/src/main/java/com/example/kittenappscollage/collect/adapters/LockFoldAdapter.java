package com.example.kittenappscollage.collect.adapters;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.db.ActionsDataBasePerms;

import java.util.HashMap;

public class LockFoldAdapter extends LoadFoldAdapt {

    private HashMap<String,String>perms;

    public LockFoldAdapter(Context context) {
        super(context);
    }

    public void setPerms(HashMap<String,String>perms){
        this.perms = perms;
    }

    @Override
    public void onBindViewHolder(@NonNull FoldHolder holder, int position) {
        if(getFolds()[position].equals(RequestFolder.getFolderImages())) {
            holder.getLock().setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_item_unlok,null));
        }else {
            if(perms!=null){
                String p = perms.get(getFolds()[position]);
                if(p!=null&&!p.equals(ActionsDataBasePerms.NON_PERM)){
                    holder.getLock().setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_item_unlok,null));
                }else {
                    holder.getLock().setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_item_lock,null));
                }
            }
        }
        super.onBindViewHolder(holder, position);
    }
}
