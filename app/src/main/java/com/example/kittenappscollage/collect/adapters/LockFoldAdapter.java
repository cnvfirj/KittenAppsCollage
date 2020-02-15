package com.example.kittenappscollage.collect.adapters;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.db.aller.ActionsContentPerms;

import java.util.HashMap;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class LockFoldAdapter extends LoadFoldAdapt {

    private HashMap<String,String>perms;

    public LockFoldAdapter(Context context) {
        super(context);
    }

    public void setPerms(HashMap<String,String>perms){
        this.perms = perms;
        LYTE("LockFoldAdapter set perms - "+perms.size());
    }

    @Override
    public void onBindViewHolder(@NonNull FoldHolder holder, int position) {

            if(perms!=null&&position<getFolds().length){
                String p = perms.get(getFolds()[position]);
                if(p!=null&&!p.equals(ActionsContentPerms.NON_PERM)){
                    holder.getLock().setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_item_unlok,null));
                }else {
                    holder.getLock().setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_item_lock,null));
                }
            }

        super.onBindViewHolder(holder, position);
    }
}
