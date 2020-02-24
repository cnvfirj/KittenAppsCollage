package com.example.kittenappscollage.collect.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;


public class LockFoldAdapter extends LoadFoldAdapt {

    public LockFoldAdapter(Context context) {
        super(context);
    }


    @Override
    public void onBindViewHolder(@NonNull FoldHolder holder, int position) {

//            if(getPerms()!=null&&position<getItems().length){
//                final String p = getItems()[position].permission;
//                if(p!=null&&!p.equals(ActionsContentPerms.NON_PERM)){
//                    holder.getLock().setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_item_unlok,null));
//                }else {
//                    holder.getLock().setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_item_lock,null));
//                }
//            }

        super.onBindViewHolder(holder, position);
    }
}
