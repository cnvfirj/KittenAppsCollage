package com.example.kittenappscollage;

import java.util.ArrayList;
import java.util.HashMap;

public interface MainSwitching {
    void stepToEdit();
    void resultScan(HashMap<String, ArrayList<String>> imgs,HashMap<String, Long>mut,HashMap<String,String>folds);
}
