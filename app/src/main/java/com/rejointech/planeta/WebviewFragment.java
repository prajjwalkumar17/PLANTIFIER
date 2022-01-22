package com.rejointech.planeta;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.rejointech.planeta.Container.HomeActivityContainer;
import com.rejointech.planeta.Utils.Constants;


public class WebviewFragment extends Fragment {
    WebView webView;
    Context thiscontext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_webview, container, false);
        initScreen();
        webView = root.findViewById(R.id.webview);

        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.WIKILINK, Context.MODE_PRIVATE);
        String wikilink = sharedPreferences.getString(Constants.prefwikireallinktoopen, "No data found!!!");

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new Callback());
        webView.loadUrl(wikilink);
        return root;
    }

    private void initScreen() {
        ((HomeActivityContainer) getActivity()).setToolbarInvisible();
        ((HomeActivityContainer) getActivity()).setDrawerLocked();
        ((HomeActivityContainer) getActivity()).setbotInvisible();
        ((HomeActivityContainer) getActivity()).setfabinvisible();
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }
}