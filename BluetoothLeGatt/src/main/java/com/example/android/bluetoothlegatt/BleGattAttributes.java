/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothlegatt;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class BleGattAttributes {

    //有人服务
//    public final static UUID USR_SERVICE = UUID.fromString("0003cdd0-0000-1000-8000-00805f9b0131");
//
//    public static final UUID USR_CHARACTERISTIC_NOTIFY = UUID.fromString("0003cdd1-0000-1000-8000-00805f9b0131");
//    public static UUID USR_CHARACTERISTIC_WRITE = UUID.fromString("0003cdd2-0000-1000-8000-00805f9b0131");

    public final static String USR_SERVICE = "0003cdd0-0000-1000-8000-00805f9b0131";

    public static final String USR_CHARACTERISTIC_NOTIFY = "0003cdd1-0000-1000-8000-00805f9b0131";
    public static String USR_CHARACTERISTIC_WRITE = "0003cdd2-0000-1000-8000-00805f9b0131";

}
