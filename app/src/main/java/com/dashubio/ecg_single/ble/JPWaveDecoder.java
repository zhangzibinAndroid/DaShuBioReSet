package com.dashubio.ecg_single.ble;


public class JPWaveDecoder {
    private JPBleWaveCallback mUiCallback;
    private JPWaveFrame mFrame1 = new JPWaveFrame(); // 最新的帧
    private JPWaveFrame mFrame2 = new JPWaveFrame(); // 第二新的帧
    private JPWaveFrame mFrame3 = new JPWaveFrame(); // 第三新的真
    private JPWaveFrame mFrame4 = new JPWaveFrame(); // 第四新的真
    private int[] fillPoints = new int[]{255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255};

    private int mPreviousID = 0;
    private int mCurrentID = 0;

    /**
     * 构造函数
     */
    public JPWaveDecoder(JPBleWaveCallback callback) {
        mUiCallback = callback;
    }

    /**
     * 解码帧
     */
    public void decodeWaveFrame(final byte[] rawData) {
        if (null == rawData || 20 > rawData.length) {

            return;
        }

        float[] decodePoints = new float[20];

        // 记录当前帧
        mFrame1.setData(rawData);
        mCurrentID = mFrame1.getCountID();

        if (mCurrentID > mPreviousID && 5 > mCurrentID - mPreviousID) {
            // 存在丢帧
            // 祯间隔大于2
            if (mCurrentID > mPreviousID + 1) {
                for (int i = 0; i < 5; i++) {
                    decodePoints[i * 4] = mFrame4.getPoints()[i];
                    decodePoints[i * 4 + 1] = mFrame3.getPoints()[i + 5];
                    decodePoints[i * 4 + 2] = mFrame2.getPoints()[i + 10];
                    decodePoints[i * 4 + 3] = fillPoints[i + 15];
                }

                mFrame4.copy(mFrame3);
                mFrame3.copy(mFrame2);
                mFrame2.invalid();

//				JPLog.ERROR("Loss>2 decode ==> " + Arrays.toString(decodePoints));
                mUiCallback.uiDrawWavePoints(calcPoints(decodePoints));
            }

            // 祯间隔大于3
            if (mCurrentID > mPreviousID + 2) {
                for (int i = 0; i < 5; i++) {
                    decodePoints[i * 4] = mFrame4.getPoints()[i];
                    decodePoints[i * 4 + 1] = mFrame3.getPoints()[i + 5];
                    decodePoints[i * 4 + 2] = mFrame2.getPoints()[i + 10];
                    decodePoints[i * 4 + 3] = fillPoints[i + 15];
                }

                mFrame4.copy(mFrame3);
                mFrame3.copy(mFrame2);
                mFrame2.invalid();

//				JPLog.ERROR("Loss>3 decode ==> " + Arrays.toString(decodePoints));
                mUiCallback.uiDrawWavePoints(calcPoints(decodePoints));
            }

            // 祯间隔大于4
            if (mCurrentID > mPreviousID + 3) {
                for (int i = 0; i < 5; i++) {
                    decodePoints[i * 4] = mFrame4.getPoints()[i];
                    decodePoints[i * 4 + 1] = mFrame3.getPoints()[i + 5];
                    decodePoints[i * 4 + 2] = mFrame2.getPoints()[i + 10];
                    decodePoints[i * 4 + 3] = fillPoints[i + 15];
                }

                mFrame4.copy(mFrame3);
                mFrame3.copy(mFrame2);
                mFrame2.invalid();

//				JPLog.ERROR("Loss>4 decode ==> " + Arrays.toString(decodePoints));
                mUiCallback.uiDrawWavePoints(calcPoints(decodePoints));
            }
        }

        if (mCurrentID == mPreviousID - 1) {
            for (int i = 0; i < 5; i++) {
                decodePoints[i * 4 + 2] = mFrame1.getPoints()[i + 10];
            }

            for (int j = 0; j < 20; j++) {
                mFrame3.getPoints()[j] = mFrame1.getPoints()[j];
            }
        } else {
            // 正常情况解码
            for (int i = 0; i < 5; i++) {
                decodePoints[i * 4] = mFrame4.getPoints()[i];
                decodePoints[i * 4 + 1] = mFrame3.getPoints()[i + 5];
                decodePoints[i * 4 + 2] = mFrame2.getPoints()[i + 10];
                decodePoints[i * 4 + 3] = mFrame1.getPoints()[i + 15];
            }

            mFrame4.copy(mFrame3);
            mFrame3.copy(mFrame2);
            mFrame2.copy(mFrame1);

//			JPLog.ERROR("Normal decode ==> " + Arrays.toString(decodePoints));
            mUiCallback.uiDrawWavePoints(calcPoints(decodePoints));
//			JPLog.ERROR("Finish Normal decode draw......");
        }

        mPreviousID = mCurrentID;
    }

    public float[] calcPoints(final float[] points) {
        int MAX_VALUE = 255;
        int[] arrPointsTmp = new int[20];

        for (int i = 0; i < points.length; i++) {
            arrPointsTmp[i] = (int) points[i];
        }

        boolean bAllMaxArray = false;

        //从头删除无效点
        if (arrPointsTmp[0] == MAX_VALUE) {
            int nFindPos = 1;
            for (; nFindPos < arrPointsTmp.length; ++nFindPos) {
                if (arrPointsTmp[nFindPos] != MAX_VALUE) {
                    break;
                }
            }

            if (nFindPos != arrPointsTmp.length) {
                for (int i = 0; i < nFindPos; ++i) {
                    arrPointsTmp[i] = arrPointsTmp[nFindPos];
                }
            } else {
                bAllMaxArray = true;
            }
        }

        if (!bAllMaxArray && arrPointsTmp[arrPointsTmp.length - 1] == MAX_VALUE) {
            int nFindPos = arrPointsTmp.length - 2;
            for (; nFindPos >= 0; --nFindPos) {
                if (arrPointsTmp[nFindPos] != MAX_VALUE) {
                    break;
                }
            }

            for (int i = arrPointsTmp.length - 1; i > nFindPos; --i) {
                arrPointsTmp[i] = arrPointsTmp[nFindPos];
            }
        }

        if (!bAllMaxArray) {
            for (int i = 0; i < arrPointsTmp.length; ++i) {
                if (arrPointsTmp[i] == MAX_VALUE) {
                    int nBegin = i - 1;
                    int nEnd = i + 1;
                    while (arrPointsTmp[nBegin] == MAX_VALUE) {
                        --nBegin;
                    }

                    while (arrPointsTmp[nEnd] == MAX_VALUE) {
                        ++nEnd;
                    }

                    int nOffset = (arrPointsTmp[nEnd] - arrPointsTmp[nBegin]) / (nEnd - nBegin);
                    int nToValue = arrPointsTmp[nBegin] + nOffset;
                    for (int index = nBegin + 1; index < nEnd; ++index) {
                        arrPointsTmp[index] = nToValue;
                        nToValue += nOffset;
                    }
                }
            }
        }

        float[] arrPoints = new float[20];
        for (int i = 0, j = 0; i < arrPointsTmp.length; i += 1, ++j) {
            arrPoints[j] = arrPointsTmp[i];
        }

        return arrPoints;
    }
}