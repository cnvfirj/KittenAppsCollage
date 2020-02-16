package com.example.kittenappscollage.collect.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.db.aller.ActionsContentPerms;

import java.util.HashMap;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class LockFoldAdapter extends LoadFoldAdapt {

    private HashMap<String,String>perms;

    private HashMap<String, Boolean>progress;

    public LockFoldAdapter(Context context) {
        super(context);
    }

    public void setPerms(HashMap<String,String>perms){
        this.perms = perms;
    }

    public void correctProgress(String key, boolean vis){
//        if(progress==null)progress = new HashMap<>();
//        progress.put(key, vis);
//        LYTE("correct "+vis);
    }

    @Override
    public void onBindViewHolder(@NonNull FoldHolder holder, int position) {
//
//        if(progress!=null&&progress.containsKey(getFolds()[position])){
//            boolean b = progress.get(getFolds()[position]);
//            if(b)holder.getProgress().setVisibility(View.VISIBLE);
//            else holder.getProgress().setVisibility(View.INVISIBLE);
//        }else {
//            LYTE("correct progress invis");
//            holder.getProgress().setVisibility(View.INVISIBLE);
//        }

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
