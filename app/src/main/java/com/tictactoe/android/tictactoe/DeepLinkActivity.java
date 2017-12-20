package com.tictactoe.android.tictactoe;

import android.app.Activity;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;

@DeepLinkHandler({AppDeepLinkModule.class})
public class DeepLinkActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(
                new AppDeepLinkModuleLoader());
        deepLinkDelegate.dispatchFrom(this);
        finish();
    }
}

