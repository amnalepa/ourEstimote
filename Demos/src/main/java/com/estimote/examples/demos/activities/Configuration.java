package com.estimote.examples.demos.activities;

import android.net.Uri;

import com.nuance.speechkit.PcmFormat;

/**
 * All Nuance Developers configuration parameters can be set here.
 *
 * Copyright (c) 2015 Nuance Communications. All rights reserved.
 */
public class Configuration {

    public static final String APP_KEY = "157b13ac49f03f100b48ff18ce3381058c98cc1ea4bfef81dc3c766ef643ff0cbe13b80703f7170e3f17260e4aa713d640dff4e49fe3667b1fdce91620e922de";
    public static final String APP_ID = "NMDPTRIAL_analepa_uwaterloo_ca20160220194906";
    public static final String SERVER_HOST = "sslsandbox.nmdp.nuancemobility.net";
    public static final String SERVER_PORT = "443";
    public static final Uri SERVER_URI = Uri.parse("nmsps://" + APP_ID + "@" + SERVER_HOST + ":" + SERVER_PORT);
    public static final PcmFormat PCM_FORMAT = new PcmFormat(PcmFormat.SampleFormat.SignedLinear16, 16000, 1);
}

