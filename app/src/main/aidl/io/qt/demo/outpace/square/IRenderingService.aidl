// Copyright (C) 2025 The Qt Company Ltd.
// SPDX-License-Identifier: LicenseRef-Qt-Commercial

package io.qt.demo.outpace.square;

import android.view.Surface;
import android.view.MotionEvent;
import android.os.Bundle;
import io.qt.demo.outpace.square.IRenderingCallback;

//! [aidl-definition]
interface IRenderingService {

    void setSurface(in Surface surface, String itemId);
    void unsetSurface(String itemId);
    void motionEvent(in MotionEvent event, String itemId);

    void setProperty(String name, in Bundle value);
    Bundle getProperty(String name);
    void registerCallback(IRenderingCallback callback);
    void unregisterCallback(IRenderingCallback callback);
}
//! [aidl-definition]
