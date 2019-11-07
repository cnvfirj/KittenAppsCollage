package com.example.kittenappscollage.draw.addLyrs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.helpers.Massages;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class AddLyrInNet extends SelectedFragment {

    private final String HTTP = "http://";
    private final String HTTPS = "https://";

    private final String PNG = "png";
    private final String JPEG = "jpeg";
    private final String JPG = "jpg";

    private final String KEY_PATH_LINK = "path link";

    private SharedPreferences aPreferences;

    private ImageView aDoneLink;

    private ImageView aClearLink;

    private ImageView aExit;

    private EditText aEnterLink;

    private TextView aDisclaimer;

    private TextView aInstruction;


    @Override
    public void onResume() {
        super.onResume();
        aPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        aEnterLink.setText(aPreferences.getString(KEY_PATH_LINK,""));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_lir_in_net,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aDoneLink = view.findViewById(R.id.network_done_link);
        aDoneLink.setOnClickListener(this);
        aClearLink = view.findViewById(R.id.network_clear_link);
        aClearLink.setOnClickListener(this);
        aExit = view.findViewById(R.id.network_exit);
        aExit.setOnClickListener(this);
        aEnterLink = view.findViewById(R.id.network_enter_link);

    }

    @Override
    protected void readinessView(View v) {
        
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.network_done_link:
               doneLink(view);
               break;
           case R.id.network_clear_link:
               aEnterLink.setText("");
               break;
           case R.id.network_exit:
               selector = (SelectorFrameFragments) getParentFragment();
               selector.exitAll();
               break;
       }
    }

    private void doneLink(View view){
        String link = aEnterLink.getText().toString();
        if (!link.isEmpty()) {
            if(link.length()>8) {
                String http = link.substring(0, 7);
                String https = link.substring(0,8);
                if (http.equals(HTTP)||https.equals(HTTPS)) {
                    if(checkExp(link)) {
                        saveLink(link);
                        selector = (SelectorFrameFragments) getParentFragment();
                        assert selector != null;
                        selector.backInAddLyr(view, link);
                    }else Massages.SHOW_MASSAGE(getActivity(),"не правильная ссылка");
                } else {
                    Massages.SHOW_MASSAGE(getActivity(), "адрес должен начинаться на " + HTTP+" или "+HTTPS);
                }
            }else {
                Massages.SHOW_MASSAGE(getActivity(),"адрес содержит больше символов");
            }
        }else {
            Massages.SHOW_MASSAGE(getActivity(),getResources().getString(R.string.fill_form_enter));
        }
    }

    private boolean checkExp(String link){
        String[] s = link.split("[.]");
        String exp = s[s.length-1].toLowerCase();
        if(exp.equals(JPEG)||exp.equals(PNG)||exp.equals(JPG))return true;
        return false;
    }
    private void saveLink(String link){
        aPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor e = aPreferences.edit();
        e.putString(KEY_PATH_LINK,link);
        e.apply();
    }
}
