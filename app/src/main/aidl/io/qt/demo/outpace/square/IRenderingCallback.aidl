// Copyright (C) 2025 The Qt Company Ltd.
// SPDX-License-Identifier: LicenseRef-Qt-Commercial

package io.qt.demo.outpace.square;

import android.os.Bundle;

interface IRenderingCallback {
    void onPropertyChanged(String name, in Bundle value);
}
